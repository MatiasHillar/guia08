package frsf.isi.died.guia08.problema01.modelo;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import frsf.isi.died.guia08.problema01.excepciones.ExcepcionAsig;
import frsf.isi.died.guia08.problema01.excepciones.ExcepcionNoExisteTarea;

public class Empleado {

	public enum Tipo { CONTRATADO,EFECTIVO}; 
	
	private Integer cuil;
	private String nombre;
	private Tipo tipo;
	private Double costoHora;
	private List<Tarea> tareasAsignadas;
	
	private Function<Tarea, Double> calculoPagoPorTarea;		
	private Predicate<Tarea> puedeAsignarTarea;

	public Empleado(String nombre, Integer cuil) {
		this.nombre = nombre;
		this.cuil = cuil;
		//implementar toda la logica en el predicate
		this.puedeAsignarTarea = t -> ( (t.getEmpleadoAsignado() != null && 
				LocalDateTime.now().isBefore(t.getFechaFin()) &&
				
				((t.getEmpleadoAsignado().tipo == Tipo.CONTRATADO &&
						this.tareasAsignadas.stream()
						.filter(t1 -> t.getFechaFin().isAfter(LocalDateTime.now()))
						.count() < 5) 
				||
			
				(t.getEmpleadoAsignado().tipo == Tipo.EFECTIVO &&
					this.tareasAsignadas.stream()
					.filter(t1 -> t.getFechaFin().isAfter(LocalDateTime.now()))
					.mapToInt(t1 -> t.getDuracionEstimada())
					.sum() < 15))
				)
				);
		
		this.calculoPagoPorTarea = t -> { switch(this.tipo) {
		case CONTRATADO: 
			if(finalizaAntes(t)) return calcularCosto(t)*1.2;
		else if(finalizaConDemora(t)) return calcularCosto(t)*0.75;
		else return calcularCosto(t);
		case EFECTIVO:
			if(finalizaAntes(t)) return calcularCosto(t)*1.3;
			else return calcularCosto(t);
		default: return 0.0;
				}
	};
	}
	

	public Tipo getTipo() {
		return this.tipo;
	}
	
	public void setTipo(Tipo tipo) {
		this.tipo = tipo;
	}


	public void setCostoHora(Double costoHora) {
		this.costoHora = costoHora;
	}


	public void setTareasAsignadas(List<Tarea> tareasAsignadas) {
		this.tareasAsignadas = tareasAsignadas;
	}




	public Double salario() {
		return this.tareasAsignadas.stream()
				.filter(t -> t.getFacturada() == false)
				.peek(t -> t.setFacturada(true))
				.mapToDouble(t -> this.costoTarea(t))
				.sum();

	}
	
	/**
	 * Si la tarea ya fue terminada nos indica cuaal es el monto según el algoritmo de calculoPagoPorTarea
	 * Si la tarea no fue terminada simplemente calcula el costo en base a lo estimado.
	 * @param t
	 * @return
	 */
	public Double costoTarea(Tarea t) {
		if(LocalDateTime.now().isBefore(t.getFechaFin())) {
			return t.getDuracionEstimada() * this.costoHora;
		}
		else return calculoPagoPorTarea.apply(t);
	}
		
	public Boolean asignarTarea(Tarea t) throws ExcepcionAsig {
		//Si la tarea ya está asignada o finalizada
		if(!puedeAsignarTarea.test(t))
			throw new ExcepcionAsig();
		
		Predicate<Tarea> cond = t1 -> t.getFechaFin().isAfter(LocalDateTime.now());
		switch(this.tipo) {
		case CONTRATADO:	
			if (this.tareasAsignadas.stream()
			.filter(cond)
			.count() > 5) return false;
			else {
				this.tareasAsignadas.add(t);
				return true;
			}
		
		case EFECTIVO:
			if(this.tareasAsignadas.stream()
				.filter(cond)
				.mapToInt(t1 -> t1.getDuracionEstimada())
				.sum() > 15) return false;
			else {
				this.tareasAsignadas.add(t);
				return true;
			}
	
		default: return false;
		}
		
	}
	
	public void comenzar(Integer idTarea) throws Exception{
		Supplier<Exception> S = () -> {return new ExcepcionNoExisteTarea();}; 
		Optional<Tarea> T = (this.tareasAsignadas.stream()
				.filter(t -> t.getId() == idTarea)
				.findFirst());
		Tarea tarea = T.orElseThrow(S);
		tarea.setFechaInicio(LocalDateTime.now());
	}
	
	public void finalizar(Integer idTarea) {
		// busca la tarea en la lista de tareas asignadas 
		// si la tarea no existe lanza una excepción
		// si la tarea existe indica como fecha de finalizacion la fecha y hora actual
	}

	public void comenzar(Integer idTarea,String fecha) {
		// busca la tarea en la lista de tareas asignadas 
		// si la tarea no existe lanza una excepción
		// si la tarea existe indica como fecha de finalizacion la fecha y hora actual
	}
	
	public void finalizar(Integer idTarea,String fecha) {
		// busca la tarea en la lista de tareas asignadas 
		// si la tarea no existe lanza una excepción
		// si la tarea existe indica como fecha de finalizacion la fecha y hora actual
	}
	public double calcularCosto(Tarea t) {
		return t.getDuracionEstimada()*this.costoHora;
	}
	public Boolean finalizaAntes(Tarea t) {
		int duracion = (int) Duration.between(t.getFechaFin(), t.getFechaInicio()).toDays()*4;
		if(duracion < t.getDuracionEstimada()) return true;
		else return false;
	}
	public Boolean finalizaConDemora(Tarea t) {
		int duracion = (int) Duration.between(t.getFechaFin(), t.getFechaInicio()).toDays()*4;
		if((duracion - t.getDuracionEstimada()) >= 8) return true;
		else return false;
	}
}
