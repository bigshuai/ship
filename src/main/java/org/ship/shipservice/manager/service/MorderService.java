package org.ship.shipservice.manager.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.ship.shipservice.domain.OrderBean;
import org.ship.shipservice.domain.ResultList;
import org.ship.shipservice.entity.Information;
import org.ship.shipservice.manager.dao.MorderDao;
import org.ship.shipservice.manager.param.OrderParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Component
@Transactional
public class MorderService {
	@Autowired
	private MorderDao morderDao;
	
	 @PersistenceContext  
	 private EntityManager em; 
	
	public ResultList getMakeOrderList(Integer page, Integer pageSize){
		List<OrderBean> result = new ArrayList<OrderBean>();
		ResultList rl = new ResultList();
		int start = (page - 1) * pageSize;
		List<Object[]> list = morderDao.queryMakeOrderList(start, pageSize);
		int total = morderDao.queryMakeOrderTotal();
		
		//o.id,o.product_name,o.num,o.status,o.order_no,o.book_time,o.book_addr,o.create_time,u.phone,u.user_name,u.ship_name
		for(int i = 0 ; (list != null && i < list.size()); i++){
			Object[] o = list.get(i);
			OrderBean bean = new OrderBean();
			bean.setId(Long.valueOf(o[0] + ""));
			bean.setProductName(o[1] + "");
			bean.setNum(Integer.parseInt(o[2] + ""));
			bean.setStatus(Integer.valueOf(o[3] + ""));
			bean.setOrderNo(o[4] + "");
			bean.setBookTime(o[5] + "");
			bean.setBookAddr(o[6] + "");
			bean.setCreateTime(o[7] + "");
			bean.setPhone(o[8] + "");
			bean.setUserName(o[9] + "");
			bean.setShipName(o[10] + "");
			result.add(bean);
		}
		rl.setDataList(result);
		rl.setPage(page);
		rl.setTotal(total);
		return rl;
	}
	
	public ResultList getOrderList(OrderParam param){
		ResultList rl = new ResultList();
		StringBuffer whereParam = new StringBuffer();
		int start = (param.getPage() - 1) * param.getPageSize();
		if(!StringUtils.isEmpty(param.getStatus())){
			whereParam.append(" and o.status="+param.getStatus());
		}
		
		if(!StringUtils.isEmpty(param.getType())){
			whereParam.append(" and o.type="+param.getType());
		}
		
		if(!StringUtils.isEmpty(param.getOrderNo())){
			whereParam.append(" and o.order_no="+param.getOrderNo());
		}
		
		if(!StringUtils.isEmpty(param.startTime)){
			whereParam.append(" and o.order_no >="+param.startTime);
		}
		
		if(!StringUtils.isEmpty(param.getEndTime())){
			whereParam.append(" and o.order_no<"+param.getEndTime());
		}
		
		String sql = "select o.id,o.product_name,o.num,o.status,o.order_no,o.book_time,o.book_addr,o.create_time,u.phone,u.user_name,u.ship_name "
				+ "from t_order o, t_user u, t_oil_station s where o.user_id=u.id and o.os_id=s.id "+whereParam.toString()+" "
				+ "order by o.create_time desc limit "+start+","+param.getPageSize()+"";
		Query q = em.createQuery(sql);
		List<Object[]> infoList = q.getResultList();
		return rl;
	}
}
