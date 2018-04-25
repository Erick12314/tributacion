package com.edu.pe.tributacion.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class Trabajador {
	
	@Id
	@GeneratedValue
	private int id;
	
	@NotNull
	@Size(min=7,message="Ingrese los 7 Dígitos de su DNI")
	private String dni;
	
	@NotNull
	@Size(min=2, max=30, message="Ingrese nuevamente su nombre")
	private String nombre;
	
	@NotNull
	@Min(1)
	private double salario;
	
	@NotNull
	private String relacion_laboral;
	
	@NotNull
	@Min(850)
	private long uit;
	
	private double total_neto;
	private double total_impuesto;
	private double total_bruto;
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getDni() {
		return dni;
	}
	public void setDni(String dni) {
		this.dni = dni;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public double getSalario() {
		return salario;
	}
	public void setSalario(double salario) {
		this.salario = salario;
	}
	public String getRelacion_laboral() {
		return relacion_laboral;
	}
	public void setRelacion_laboral(String relacion_laboral) {
		this.relacion_laboral = relacion_laboral;
	}
	public double getTotal_neto() {
		return total_neto;
	}
	public void setTotal_neto(double total_neto) {
		this.total_neto = total_neto;
	}
	public double getTotal_impuesto() {
		return total_impuesto;
	}
	public void setTotal_impuesto(double total_impuesto) {
		this.total_impuesto = total_impuesto;
	}
	public double getTotal_bruto() {
		return total_bruto;
	}
	public void setTotal_bruto(double total_bruto) {
		this.total_bruto = total_bruto;
	}
	public long getUit() {
		return uit;
	}
	public void setUit(long uit) {
		this.uit = uit;
	}
}
