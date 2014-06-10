package it.polito.ai.polibox.dao;

import it.polito.ai.polibox.entity.Dispositivo;

import java.util.List;

public interface DispositivoDAO {
	
	public void addDispositivo(Dispositivo dispositivo);
	public List<Dispositivo> getDispositivi();
	public Dispositivo getDispositivo(Long id);
	public void updateDispositivo(Dispositivo dispositivo);
	public void deleteDispositivo(Dispositivo dispositivo);
	
}
