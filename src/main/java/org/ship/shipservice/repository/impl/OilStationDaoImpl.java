package org.ship.shipservice.repository.impl;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

public class OilStationDaoImpl {
	public void query(){
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("entityManagerFactory");
        EntityManager em = emf.createEntityManager();   
        String sql = "SELECT t.name FROM t_user t";   
        Query query =  em.createNamedQuery(sql);
        query.getResultList();
        em.close();
	}
}
