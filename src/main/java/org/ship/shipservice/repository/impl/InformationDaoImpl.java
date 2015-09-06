package org.ship.shipservice.repository.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.ship.shipservice.entity.Information;


public class InformationDaoImpl  {
	 @PersistenceContext  
	 private EntityManager em;  
	 public List<Information> findInfoByParam(String param){
		 String sql = "select info from Information info where " +param;
		 Query q = em.createQuery(sql);
//		 q.setFirstResult(0);  
//	     q.setMaxResults(2);    
		 List<Information> infoList =(List<Information>) q.getResultList();
		 return infoList;
	 }
}
