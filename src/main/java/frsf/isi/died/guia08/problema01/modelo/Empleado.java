package frsf.isi.died.guia08.problema01.modelo;

import java.text.DateFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Formatter;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collector;

import frsf.isi.died.guia08.problema01.excepciones.ExcepcionAsig;
import frsf.isi.died.guia08.problema01.excepciones.ExcepcionAsignaTarea;
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

	public Empleado(String nombre, Integer cuil, Tipo tipo, double costo) {
		this.nombre = nombre;
		this.cuil = cuil;
		this.tipo = tipo;
		this.costoHora = costo;
		this.tareasAsignadas = new ArrayList<Tarea>();
		configurar();	
		}
	
	public Tipo getTipo() {
		return this.tipo;
	}
	
	public List<Tarea> getTareas(){
		return tareasAsignadas;
	}
	
	
	public Integer getCuil() {
		return this.cuil;
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
		if(t.getFechaFin() == null) {
			return t.getDuracionEstimada() * this.costoHora;
		}
		else return calculoPagoPorTarea.apply(t);
	}
		
	public Boolean asignarTarea(Tarea t) throws ExcepcionAsig, ExcepcionAsignaTarea {
	if(puedeAsignarTarea.test(t)) {
			this.tareasAsignadas.add(t);
			t.asignarEmpleado(this);
	return true;
	}
	else throw new ExcepcionAsig();
	}
	
	public void comenzar(Integer idTarea) throws ExcepcionNoExisteTarea{
		Supplier<ExcepcionNoExisteTarea> S = () -> {return new ExcepcionNoExisteTarea();}; 
		Optional<Tarea> T = (this.tareasAsignadas.stream()
				.filter(t -> t.getId() == idTarea)
				.findFirst());
		Tarea tarea = T.orElseThrow(S);
		tarea.setFechaInicio(LocalDateTime.now());
	}
	
	public void finalizar(Integer idTarea) throws ExcepcionNoExisteTarea{
		Supplier<ExcepcionNoExisteTarea> S = () -> {return new ExcepcionNoExisteTarea();}; 
		Optional<Tarea> T = (this.tareasAsignadas.stream()
				.filter(t -> t.getId() == idTarea)
				.findFirst());
		Tarea tarea = T.orElseThrow(S);
		tarea.setFechaFin(LocalDateTime.now());
	}

	public void comenzar(Integer idTarea,String fecha) throws ExcepcionNoExisteTarea{
	String formato = "dd-MM-yyyy HH:mm";
	DateTimeFormatter df = DateTimeFormatter.ofPattern(formato);
	LocalDateTime fechaInicio = LocalDateTime.parse(fecha, df);
		Supplier<ExcepcionNoExisteTarea> S = () -> {return new ExcepcionNoExisteTarea();}; 
		Optional<Tarea> T = (this.tareasAsignadas.stream()
				.filter(t -> t.getId() == idTarea)
				.findFirst());
		Tarea tarea = T.orElseThrow(S);
		tarea.setFechaInicio(fechaInicio);
	}
	
	public void finalizar(Integer idTarea,String fecha) throws ExcepcionNoExisteTarea{
		String formato = "dd-MM-yyyy HH:mm";
		DateTimeFormatter df = DateTimeFormatter.ofPattern(formato);
		LocalDateTime fechaFin = LocalDateTime.parse(fecha, df);
		Supplier<ExcepcionNoExisteTarea> S = () -> {return new ExcepcionNoExisteTarea();}; 
		Optional<Tarea> T = (this.tareasAsignadas.stream()
				.filter(t -> t.getId() == idTarea)
				.findFirst());
		Tarea tarea = T.orElseThrow(S);
		tarea.setFechaFin(fechaFin);
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
	@Override
	public String toString() {	
	return this.nombre;
	}
	
	public void configurar() {
		switch(tipo){
		case CONTRATADO:
			this.puedeAsignarTarea = t -> ((int) this.tareasAsignadas.stream()
				.filter(t1 -> t.getFechaFin() == null)
				.count() <= 5);
			this.calculoPagoPorTarea = t -> {if(finalizaAntes(t))
				return calcularCosto(t)*1.2;
				else if(finalizaConDemora(t))
				return calcularCosto(t)*0.75;
				else return calcularCosto(t);};
			break;
		case EFECTIVO:
			this.puedeAsignarTarea = t -> ( (int) this.tareasAsignadas.stream()
				.filter(t1 -> t.getFechaFin() == null)
				.mapToInt(t1 -> t.getDuracionEstimada())
				.sum() <= 15);
			this.calculoPagoPorTarea = t -> {if(finalizaAntes(t))
				return calcularCosto(t)*1.3;
				else return calcularCosto(t);};
			break;
	}
}
	}
