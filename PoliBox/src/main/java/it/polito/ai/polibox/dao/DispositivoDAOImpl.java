package it.polito.ai.polibox.dao;

import it.polito.ai.polibox.entity.Dispositivo;
import it.polito.ai.polibox.entity.Utente;
import it.polito.ai.polibox.persistence.HibernateUtil;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

public class DispositivoDAOImpl implements DispositivoDAO {

	@Override
	public void addDispositivo(Dispositivo dispositivo) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.save(dispositivo);
	}

	@Override
	public List<Dispositivo> getDispositivi() {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Query query = session.createQuery("from Dispositivo");
		return query.list();
	}

	@Override
	public Dispositivo getDispositivo(Long id) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Query query = session.createQuery("from Dispositivo where id = :id");
		query.setLong("id", id);
		List<Dispositivo> dispositivi = new ArrayList<Dispositivo>();
		dispositivi = query.list();
		if (dispositivi.size() > 0) {
			return dispositivi.get(0);
		}
		return null;
	}

	@Override
	public void updateDispositivo(Dispositivo dispositivo) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.update(dispositivo);
	}

	@Override
	public void deleteDispositivo(Dispositivo dispositivo) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.delete(dispositivo);
	}

	
	
}
