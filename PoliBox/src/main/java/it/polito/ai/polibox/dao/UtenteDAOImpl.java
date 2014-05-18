package it.polito.ai.polibox.dao;

import it.polito.ai.polibox.entity.Utente;
import it.polito.ai.polibox.persistence.HibernateUtil;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

public class UtenteDAOImpl implements UtenteDAO {
	
	@Override
	public void addUtente(Utente utente) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.save(utente);
	}

	@Override
	public List<Utente> getUtenti() {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Query query = session.createQuery("from Utenti");
		return query.list();
	}

	@Override
	public void deleteUtente(Utente utente) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.delete(utente);
	}

}
