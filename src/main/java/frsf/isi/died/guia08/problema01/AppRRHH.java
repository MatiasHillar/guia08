package frsf.isi.died.guia08.problema01;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;

import frsf.isi.died.guia08.problema01.excepciones.ExcepcionAsig;
import frsf.isi.died.guia08.problema01.excepciones.ExcepcionAsignaTarea;
import frsf.isi.died.guia08.problema01.excepciones.ExcepcionNoExisteEmpleado;
import frsf.isi.died.guia08.problema01.excepciones.ExcepcionNoExisteTarea;
import frsf.isi.died.guia08.problema01.excepciones.ExcepcionYaExisteEmpleado;
import frsf.isi.died.guia08.problema01.modelo.Empleado;
import frsf.isi.died.guia08.problema01.modelo.Empleado.Tipo;
import frsf.isi.died.guia08.problema01.modelo.Tarea;

public class AppRRHH {

	public void main() {
		try {
			guardarTareasTerminadasCSV();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private List<Empleado> empleados;
	private Supplier<ExcepcionNoExisteEmpleado> s = 
			() -> {return new ExcepcionNoExisteEmpleado();}; 
	
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

	public void cargarEmpleadosContratadosCSV(String nombreArchivo) throws FileNotFoundException, IOException {
		try(Reader fileReader = new FileReader(nombreArchivo)){
			try(BufferedReader in = new BufferedReader(fileReader)){
				String linea = null;
				while((linea = in.readLine()) != null) {
					String[] fila = linea.split(";");
					agregarEmpleadoContratado(Integer.valueOf(fila[0]), 
							fila[1], Double.valueOf(fila[2]));
				}
				System.out.println(empleados);
			}
		}
	}

	public void cargarEmpleadosEfectivosCSV(String nombreArchivo) throws FileNotFoundException, IOException {
		try(Reader fileReader = new FileReader(nombreArchivo)){
			try(BufferedReader in = new BufferedReader(fileReader)){
				String linea = null;
				while((linea = in.readLine()) != null) {
					String[] fila = linea.split(";");
					agregarEmpleadoEfectivo(Integer.valueOf(fila[0]), 
							fila[1], Double.valueOf(fila[2]));
				}
			}
		}		
	}

	public void cargarTareasCSV(String nombreArchivo) throws FileNotFoundException, IOException, 
	ExcepcionNoExisteEmpleado,NumberFormatException,
	ExcepcionAsig, ExcepcionAsignaTarea {
		try(Reader fileReader = new FileReader(nombreArchivo)){
			try(BufferedReader in = new BufferedReader(fileReader)){
				String linea = null;
				while((linea = in.readLine()) != null) {
					String[] fila = linea.split(";");
					Integer cuil = Integer.valueOf(fila[3]);
					System.out.println(cuil);
					Optional<Empleado> e = buscarEmpleado(p -> 
					cuil.equals(p.getCuil()));
					Empleado emp = e.orElseThrow(s);
					emp.asignarTarea(new Tarea(Integer.valueOf(fila[0]), 
							fila[1], Integer.valueOf(fila[2])))
;				}
			}
		}	
	}
	
	public void guardarTareasTerminadasCSV() throws IOException {
	try(Writer fileWriter = new FileWriter("./target/tareasTerminadas.csv", true)){
		try(BufferedWriter out = new BufferedWriter(fileWriter)){
		for(Empleado e: this.empleados) {
			for(Tarea t: e.getTareas()) {
				if(t.getFechaFin() == null && t.getFacturada() == false)
				out.write(t.asCsv()+System.getProperty("line.separator"));
						}
					}
				}
			}
		}
	
	private Optional<Empleado> buscarEmpleado(Predicate<Empleado> p){
		return this.empleados.stream().filter(p).findFirst();
	}

	public Double facturar() throws IOException {
		this.guardarTareasTerminadasCSV();
		return this.empleados.stream()				
				.mapToDouble(e -> e.salario())
				.sum();
	}
}
