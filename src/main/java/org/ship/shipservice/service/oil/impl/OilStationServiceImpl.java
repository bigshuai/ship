package org.ship.shipservice.service.oil.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.ship.shipservice.domain.OilBean;
import org.ship.shipservice.domain.OilStationBean;
import org.ship.shipservice.domain.ResultList;
import org.ship.shipservice.entity.City;
import org.ship.shipservice.entity.Oil;
import org.ship.shipservice.entity.OilStation;
import org.ship.shipservice.repository.OilStationDao;
import org.ship.shipservice.service.oil.OilStationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class OilStationServiceImpl implements OilStationService {
	
	@Autowired
	private OilStationDao oilStationDao;
	
	public ResultList queryOilList(String param, int page, int pageSize){
		ResultList r = new ResultList();
		int start = (page - 1)*pageSize;
		List<Object[]> list = new ArrayList<Object[]>();
		Pattern pattern = Pattern.compile("[0-9]*"); 
		if(pattern.matcher(param).matches()){
			list = oilStationDao.findByCityId(Integer.parseInt(param), start, pageSize);
			r.setTotal(oilStationDao.findCountByCityId(Integer.parseInt(param)));
		}else{
			list = oilStationDao.findByOilName(param, start, pageSize);
			r.setTotal(oilStationDao.findCountByOilName(param));
		}
		
		List<OilStationBean> result = new ArrayList<OilStationBean>();
		for(Object[] o : list){
			OilStationBean os = new OilStationBean();
			os.setId(Integer.valueOf(o[0]+""));
			os.setName(o[1]+"");
			os.setCredit(Float.valueOf(o[2]+""));
			os.setCouponFlag(Integer.valueOf(o[3]+""));
			os.setAppraiseNum(Integer.valueOf(o[4]+""));
			os.setStatus(Integer.valueOf(o[5]+""));
			os.setDerate(o[6]+"");
			os.setPicUrl(o[7]+"");
			os.setLatitude(o[8]+"");
			os.setLongitude(o[9]+"");
			result.add(os);
		}
		r.setDataList(result);
		r.setPage(page);
		return r;
	}
	public OilStation queryOilStation(String name){
		return oilStationDao.findByName(name);
	}
	public ResultList queryCityList(){
		ResultList r = new ResultList();
		List<City> list = oilStationDao.queryCityList();
		r.setDataList(list);
		return r;
	}
	
	@Override
	public OilStationBean queryDetail(Long osId) {
		List<Object[]> obs = oilStationDao.queryDetailById(osId);
		OilStationBean os = new OilStationBean();
		//t.id,t.name,t.desc,t.address,t.credit,t.quality,t.service,num,t.coupon_flag, derate
		//��ȡ����վ������Ϣ
		List<Object[]> cs = oilStationDao.quertCreditForOsId(osId);
		Float credit = 5.0f;
		Float quality = 5.0f;
		Float service = 5.0f;
		if(cs != null && cs.size() > 0){
			Object[] c = cs.get(0);
			credit = c[0]==null?5.0f:Float.valueOf(c[0]+"");
			service = c[1]==null?5.0f:Float.valueOf(c[1]+"");
			quality = c[2]==null?5.0f:Float.valueOf(c[2]+"");
		}
		Object[] o = obs.get(0);
		os.setId(Integer.valueOf(o[0]+""));
		os.setName(o[1]+"");
		os.setDesc(o[2]+"");
		os.setAddress(o[3]+"");
		os.setPhone(o[4]+"");
		os.setCredit(credit);
		os.setQuality(quality);
		os.setService(service);
		os.setCouponFlag(Integer.valueOf(o[8]+""));
		os.setAppraiseNum(Integer.valueOf(o[9]+""));
		os.setStatus(Integer.valueOf(o[10]+""));
		os.setDerate(o[11]+"");
		os.setPicUrl(o[12]+"");
		os.setLatitude(o[13]+"");
		os.setLongitude(o[14]+"");
		List<Oil> oils = oilStationDao.queryOils(os.getId());
		for(Oil oil : oils){
			OilBean ob = new OilBean();
			ob.setId(oil.getId());
			ob.setName(oil.getName());
			ob.setPrice(oil.getPrice());
			ob.setUnit(oil.getUnit());
			os.addOil(ob);
		}
		return os;
	}

	public OilStationDao getOilStationDao() {
		return oilStationDao;
	}

	public void setOilStationDao(OilStationDao oilStationDao) {
		this.oilStationDao = oilStationDao;
	}
}
