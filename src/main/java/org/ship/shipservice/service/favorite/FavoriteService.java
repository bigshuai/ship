package org.ship.shipservice.service.favorite;

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

	private FavoriteDao favoriteDao;

	public Favorite save(Favorite favorite) {
		return favoriteDao.save(favorite);
	}

	public Page<Favorite> findFavorite(Integer userId, Integer type,
			PageRequest pageRequest) {
		return favoriteDao.findByUserIdAndType(userId, type, pageRequest);
	}

	public FavoriteDao getFavoriteDao() {
		return favoriteDao;
	}
	@Autowired
	public void setFavoriteDao(FavoriteDao favoriteDao) {
		this.favoriteDao = favoriteDao;
	}

}
