package it.polito.ai.polibox.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import it.polito.ai.polibox.entity.SincronizzazioniPendenti;
import it.polito.ai.polibox.persistence.HibernateUtil;

public class SincronizzazioniPendentiDAOImpl implements
		SincronizzazioniPendentiDAO {

	@Override
	public void addSincronizzazioniPendenti(SincronizzazioniPendenti sinc) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.save(sinc);
		
	}

	@Override
	public void deleteSincronizzazioniPendenti(SincronizzazioniPendenti sinc) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.delete(sinc);
		
	}

	@Override
	public List<SincronizzazioniPendenti> getSincronizzazioniPendenti(Long id) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Query query = session.createQuery("from SincronizzazioniPendenti where uId=:uId");
		query.setLong("uId", id);
		return query.list();
	}

}
