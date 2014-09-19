package it.polito.ai.polibox.dao;

import it.polito.ai.polibox.entity.Condivisione;
import it.polito.ai.polibox.entity.Utente;
import it.polito.ai.polibox.persistence.HibernateUtil;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class CondivisioneDAOImpl implements CondivisioneDAO {

	@Override
	public void addCondivisione(Condivisione condivisione) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction tx = (Transaction) session.beginTransaction();
		session.save(condivisione);
		tx.commit();
	}

	@Override
	public List<Condivisione> getCondivisioni(Long id) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Query query = session.createQuery("from Condivisione where userId=:userId");
		query.setLong("userId", id);
		return query.list();
	}

	@Override
	public Condivisione getCondivisione(Long id) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction tx = (Transaction) session.beginTransaction();
		Query query = session.createQuery("from Condivisione where id = :id");
		query.setLong("id", id);
		List<Condivisione> condivisioni = new ArrayList<Condivisione>();
		condivisioni = query.list();
		tx.commit();
		if (condivisioni.size() > 0) {
			return condivisioni.get(0);
		}
		return null;
	}

	@Override
	public void updateCondivisione(Condivisione condivisione) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction tx = (Transaction) session.beginTransaction();
		session.update(condivisione);
		tx.commit();
	}

	@Override
	public void deleteCondivisione(Condivisione condivisione) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction tx = (Transaction) session.beginTransaction();
		session.delete(condivisione);
		tx.commit();
	}

	@Override
	public Condivisione getCondivisione(Long owner_id, Long u_id, String path) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction tx = (Transaction) session.beginTransaction();
		Query query = session.createQuery("from Condivisione where ownerId = :ownerId AND userId = :userId AND dirPath = :dirPath");
		query.setLong("ownerId", owner_id);
		query.setLong("userId", u_id);
		query.setString("dirPath", path);
		List<Condivisione> condivisioni = new ArrayList<Condivisione>();
		condivisioni = query.list();
		tx.commit();
		if (condivisioni.size() > 0) {
			return condivisioni.get(0);
		}
		return null;
	}

	@Override
	public Condivisione getCondivisioneWithoutTrans(Long id) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Query query = session.createQuery("from Condivisione where id = :id");
		query.setLong("id", id);
		List<Condivisione> condivisioni = new ArrayList<Condivisione>();
		condivisioni = query.list();
		if (condivisioni.size() > 0) {
			return condivisioni.get(0);
		}
		return null;
	}

	@Override
	public List<Condivisione> getCondivisioniOwner(Long id) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Query query = session.createQuery("from Condivisione where ownerId=:ownerId");
		query.setLong("ownerId", id);
		return query.list();
	}
	
	@Override
	public List<Condivisione> getActiveCondivisioni(String resource){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction tx = (Transaction) session.beginTransaction();
		Query query = session.createQuery("from Condivisione where dirPath = :dirPath AND state = 1");
		query.setString("dirPath", resource);
		List<Condivisione> condivisioni = query.list();
		tx.commit();
		return condivisioni;
		
	}
	
	@Override
	public List<Condivisione> getActiveCondivisioniWithoutTrans(String resource){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Query query = session.createQuery("from Condivisione where dirPath = :dirPath AND state = 1");
		query.setString("dirPath", resource);
		List<Condivisione> condivisioni = query.list();
		return condivisioni;
		
	}

	@Override
	public List<Condivisione> getCondivisioniWithTransaction(Long id) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = (Transaction) session.beginTransaction();
		Query query = session.createQuery("from Condivisione where userId=:userId");
		query.setLong("userId", id);
		tx.commit();
		return query.list();
	}

}
