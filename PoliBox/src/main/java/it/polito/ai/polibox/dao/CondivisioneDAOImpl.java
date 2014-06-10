package it.polito.ai.polibox.dao;

import it.polito.ai.polibox.entity.Condivisione;
import it.polito.ai.polibox.entity.Utente;
import it.polito.ai.polibox.persistence.HibernateUtil;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

public class CondivisioneDAOImpl implements CondivisioneDAO {

	@Override
	public void addCondivisione(Condivisione condivisione) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.save(condivisione);
	}

	@Override
	public List<Condivisione> getCondivisioni() {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Query query = session.createQuery("from Condivisione");
		return query.list();
	}

	@Override
	public Condivisione getCondivisione(Long id) {
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
	public void updateCondivisione(Condivisione condivisione) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.update(condivisione);
	}

	@Override
	public void deleteCondivisione(Condivisione condivisione) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.delete(condivisione);
	}

}
