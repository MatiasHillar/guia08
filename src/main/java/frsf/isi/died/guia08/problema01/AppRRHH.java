package frsf.isi.died.guia08.problema01;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

import frsf.isi.died.guia08.problema01.excepciones.ExcepcionAsig;
import frsf.isi.died.guia08.problema01.excepciones.ExcepcionAsignaTarea;
import frsf.isi.died.guia08.problema01.excepciones.ExcepcionNoExisteEmpleado;
import frsf.isi.died.guia08.problema01.excepciones.ExcepcionNoExisteTarea;
import frsf.isi.died.guia08.problema01.modelo.Empleado;
import frsf.isi.died.guia08.problema01.modelo.Empleado.Tipo;
import frsf.isi.died.guia08.problema01.modelo.Tarea;

public class AppRRHH {

	private List<Empleado> empleados;
	
	public AppRRHH() {
		this.empleados = new ArrayList<Empleado>();
	}
	
	public List<Empleado> getEmpleados() {
		return empleados;
	}
	
	public void agregarEmpleado(Empleado e) throws ExcepcionYaExisteEmpleado {
		if(this.empleados.contains(e)) throw new ExcepcionYaExisteEmpleado();
		else this.empleados.add(e);
	}
	
	public void agregarEmpleadoContratado(Integer cuil,String nombre,Double costoHora) {
		Empleado e = new Empleado(nombre, cuil, Tipo.CONTRATADO, costoHora);
		this.empleados.add(e);
	}
	
	public void agregarEmpleadoEfectivo(Integer cuil,String nombre,Double costoHora) {
		Empleado e = new Empleado(nombre, cuil, Tipo.EFECTIVO, costoHora);
		this.empleados.add(e);	
	}
	
	public void asignarTarea(Integer cuil,Integer idTarea,String descripcion,Integer duracionEstimada) {
		try {
		Optional<Empleado> e = this.buscarEmpleado(p -> p.getCuil() == cuil);
		Empleado emp = e.orElseThrow(() -> {return new ExcepcionNoExisteEmpleado();});
		Tarea t = new Tarea(idTarea, descripcion, duracionEstimada);
		emp.asignarTarea(t);
		}
		catch(ExcepcionNoExisteEmpleado | ExcepcionAsig | ExcepcionAsignaTarea e1) {
			System.out.println(e1.getMessage());
			e1.printStackTrace();
		}
	}
	
	public void empezarTarea(Integer cuil,Integer idTarea) {
		try {
			Optional<Empleado> e = this.buscarEmpleado(p -> p.getCuil() == cuil);
			Empleado emp = e.orElseThrow(() -> {return new ExcepcionNoExisteEmpleado();});
			emp.comenzar(idTarea);
			}
			catch(ExcepcionNoExisteEmpleado | ExcepcionNoExisteTarea e1) {
				System.out.println(e1.getMessage());
				e1.printStackTrace();
			}
	}
	
	public void terminarTarea(Integer cuil,Integer idTarea) {
		try {
			Optional<Empleado> e = this.buscarEmpleado(p -> p.getCuil() == cuil);
			Empleado emp = e.orElseThrow(() -> {return new ExcepcionNoExisteEmpleado();});
			emp.finalizar(idTarea);
			}
			catch(ExcepcionNoExisteEmpleado | ExcepcionNoExisteTarea e1) {
				System.out.println(e1.getMessage());
				e1.printStackTrace();
			}	
	}

	public void cargarEmpleadosContratadosCSV(String nombreArchivo) {
		// leer datos del archivo
		// por cada fila invocar a agregarEmpleadoContratado
	}

	public void cargarEmpleadosEfectivosCSV(String nombreArchivo) {
		// leer datos del archivo
		// por cada fila invocar a agregarEmpleadoContratado		
	}

	public void cargarTareasCSV(String nombreArchivo) {
		// leer datos del archivo
		// cada fila del archivo tendrá:
		// cuil del empleado asignado, numero de la taera, descripcion y duración estimada en horas.
	}
	
	private void guardarTareasTerminadasCSV() {
		// guarda una lista con los datos de la tarea que fueron terminadas
		// y todavía no fueron facturadas
		// y el nombre y cuil del empleado que la finalizó en formato CSV 
	}
	
	private Optional<Empleado> buscarEmpleado(Predicate<Empleado> p){
		return this.empleados.stream().filter(p).findFirst();
	}

	public Double facturar() {
		this.guardarTareasTerminadasCSV();
		return this.empleados.stream()				
				.mapToDouble(e -> e.salario())
				.sum();
	}
}
