package it.polito.ai.polibox.dao;

import it.polito.ai.polibox.entity.Utente;
import it.polito.ai.polibox.persistence.HibernateUtil;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class UtenteDAOImpl implements UtenteDAO {
	
	@Override
	public void addUtente(Utente utente) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.save(utente);
	}

	@Override
	public List<Utente> getUtenti() {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Query query = session.createQuery("from Utente");
		return query.list();
	}

	@Override
	public void deleteUtente(Utente utente) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.delete(utente);
	}

	@Override
	public Utente getUtente(String email, String password) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Query query = session.createQuery("from Utente where email = :email and password = :password");
		query.setString("email", email);
		query.setString("password", password);
		List<Utente> utenti = new ArrayList<Utente>();
		utenti = query.list();
		if (utenti.size() > 0) {
			return utenti.get(0);
		}
		return null;
	}

	@Override
	public Utente getUtente(Long id) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction tx = (Transaction) session.beginTransaction();
		Query query = session.createQuery("from Utente where id = :id");
		query.setLong("id", id);
		List<Utente> utenti = new ArrayList<Utente>();
		utenti = query.list();
		tx.commit();
		if (utenti.size() > 0) {
			return utenti.get(0);
		}
		return null;
	}

	@Override
	public void updateUtente(Utente utente) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.update(utente);
	}

	@Override
	public Utente getUtente(String email) {
		System.out.println("getUtente: "+email);
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction tx = (Transaction) session.beginTransaction();
		Query query = session.createQuery("from Utente where email = :email");
		query.setString("email", email);
		List<Utente> utenti = new ArrayList<Utente>();
		utenti = query.list();
		tx.commit();
		if (utenti.size() > 0) {
			return utenti.get(0);
		}
		return null;
	}

	@Override
	public Utente getUtenteWithoutTrans(Long id) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Query query = session.createQuery("from Utente where id = :id");
		query.setLong("id", id);
		List<Utente> utenti = new ArrayList<Utente>();
		utenti = query.list();
		if (utenti.size() > 0) {
			return utenti.get(0);
		}
		return null;
	}
}
