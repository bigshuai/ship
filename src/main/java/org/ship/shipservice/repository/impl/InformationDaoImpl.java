package org.ship.shipservice.repository.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.ship.shipservice.entity.Information;


public class InformationDaoImpl  {
	 @PersistenceContext  
	 private EntityManager em;  
	 public List<Information> findInfoByParam(String param,Integer page,Integer pageSize){
		 String sql = "select info from Information info where " +param;
		 Query q = em.createQuery(sql);
		 q.setFirstResult((page-1)*pageSize);
		 q.setMaxResults(pageSize);
		 List<Information> infoList =(List<Information>) q.getResultList();
		 return infoList;
	 }
}
