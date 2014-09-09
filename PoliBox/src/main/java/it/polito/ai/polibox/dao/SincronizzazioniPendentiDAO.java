package it.polito.ai.polibox.dao;

import java.util.List;

import it.polito.ai.polibox.entity.SincronizzazioniPendenti;

public interface SincronizzazioniPendentiDAO {
	public void addSincronizzazioniPendenti(SincronizzazioniPendenti sinc);
	public void deleteSincronizzazioniPendenti(SincronizzazioniPendenti sinc);
	public List<SincronizzazioniPendenti> getSincronizzazioniPendenti(Long id);
}
