package frsf.isi.died.guia08.problema01.modelo;

import java.time.LocalDateTime;

import frsf.isi.died.guia08.problema01.excepciones.ExcepcionAsig;
import frsf.isi.died.guia08.problema01.excepciones.ExcepcionAsignaTarea;

public class Tarea {

	private Integer id;
	private String descripcion;
	private Integer duracionEstimada;
	private Empleado empleadoAsignado;
	private LocalDateTime fechaInicio;
	private LocalDateTime fechaFin;
	private Boolean facturada;
	
	public Tarea(Integer id, String descripcion, Integer duracion) {		
	this.id = id;
	this.descripcion = descripcion;
	this.duracionEstimada = duracion;
	this.facturada = false;
	}
	
	public void asignarEmpleado(Empleado e) throws ExcepcionAsignaTarea, ExcepcionAsig{
		if(this.empleadoAsignado != null && this.fechaFin != null)
			throw new ExcepcionAsignaTarea();
		else this.setEmpleadoAsignado(e);
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Integer getDuracionEstimada() {
		return duracionEstimada;
	}

	public void setDuracionEstimada(Integer duracionEstimada) {
		this.duracionEstimada = duracionEstimada;
	}

	public LocalDateTime getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(LocalDateTime fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public LocalDateTime getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(LocalDateTime fechaFin) {
		this.fechaFin = fechaFin;
	}

	public Boolean getFacturada() {
		return facturada;
	}

	public void setFacturada(Boolean facturada) {
		this.facturada = facturada;
	}

	public Empleado getEmpleadoAsignado() {
		return empleadoAsignado;
	}
	
	public void setEmpleadoAsignado(Empleado e) {
		this.empleadoAsignado = e;
	}
	
	@Override
	public String toString() {
		if(this.fechaFin == null) return "null";
		return this.getFechaFin().toString();
	}
	
	
}
