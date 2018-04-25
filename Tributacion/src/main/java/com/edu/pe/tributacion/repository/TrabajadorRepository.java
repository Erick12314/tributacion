package com.edu.pe.tributacion.repository;

import java.util.List;

import org.springframework.data.repository.Repository;
import com.edu.pe.tributacion.model.Trabajador;

public interface TrabajadorRepository 
	extends Repository<Trabajador, Integer>{
	
	void save(Trabajador model);
	
	List<Trabajador> findAll();
	
	//void update(Trabajador model);
	
	Trabajador findById(int id);

	void delete(Trabajador model);
	
}
