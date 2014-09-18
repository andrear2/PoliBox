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
	public List<SincronizzazioniPendenti> getSincronizzazioniPendenti(
			Long userId, Long dispId) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Query query = session.createQuery("from SincronizzazioniPendenti where userId=:userId and dispId=:dispId");
		query.setLong("userId", userId);
		query.setLong("dispId", dispId);
		return query.list();
	}

}
