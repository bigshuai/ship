package org.ship.shipservice.service.favorite;

import java.math.BigInteger;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.ship.shipservice.entity.Favorite;
import org.ship.shipservice.repository.FavoriteDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
@Component
@Transactional
public class FavoriteService {
	@PersistenceContext  
	private EntityManager em; 
	private FavoriteDao favoriteDao;

	public Favorite save(Favorite favorite) {
		return favoriteDao.save(favorite);
	}

	public Page<Favorite> findFavorite(Integer userId, Integer type,
			PageRequest pageRequest) {
		return favoriteDao.findByUserIdAndType(userId, type, pageRequest);
	}
	public BigInteger findFavorite(Integer userId,Integer type, long faId) {
		String sql = "select count(1) from t_favorite where ";
		if(type==1){
			sql += " oil_id="+faId;
		}else {
			sql += " info_id="+faId;
		}
		sql +=" and user_id=" +userId ;
		Query q = em.createNativeQuery(sql);
		return (BigInteger)q.getSingleResult();
	}
	public FavoriteDao getFavoriteDao() {
		return favoriteDao;
	}
	@Autowired
	public void setFavoriteDao(FavoriteDao favoriteDao) {
		this.favoriteDao = favoriteDao;
	}

}
