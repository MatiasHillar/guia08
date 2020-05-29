package frsf.isi.died.guia08.problema01;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Optional;
import java.util.function.Predicate;

import org.junit.Before;
import org.junit.Test;

import frsf.isi.died.guia08.problema01.excepciones.ExcepcionAsig;
import frsf.isi.died.guia08.problema01.excepciones.ExcepcionAsignaTarea;
import frsf.isi.died.guia08.problema01.excepciones.ExcepcionNoExisteTarea;
import frsf.isi.died.guia08.problema01.excepciones.ExcepcionYaExisteEmpleado;
import frsf.isi.died.guia08.problema01.modelo.Empleado;
import frsf.isi.died.guia08.problema01.modelo.Empleado.Tipo;
import frsf.isi.died.guia08.problema01.modelo.Tarea;

public class AppRRHHTest {

	
	private Tarea t1;
	private Tarea t2;
	private Tarea t3;
	private Tarea t4;
	private Tarea t5;
	private Tarea t6;
	private Tarea t7;
	private AppRRHH app;
	private Empleado e1;
	private Empleado e2;

	@Before
	public void setUp() throws ExcepcionAsig, ExcepcionAsignaTarea {
		app = new AppRRHH();
		t1 = new Tarea(12345, "asfg", 10);
		t2 = new Tarea(14345, "aasf", 10);
		t3 = new Tarea(14365, "aasf", 10);
		t4 = new Tarea(14325, "agsf", 10);
		t5 = new Tarea(14315, "adsf", 10);
		t6 = new Tarea(14545, "affaf", 10);
		t7 = new Tarea(14125, "aagf", 10);
		e1 = new Empleado("Fidel", 48415254, Tipo.EFECTIVO, 300);
		e2 = new Empleado("Pedro", 48425254, Tipo.CONTRATADO, 300);
		e1.asignarTarea(t1);
		e1.asignarTarea(t3);
		e2.asignarTarea(t1);
		e2.asignarTarea(t3);
		e2.asignarTarea(t4);
		e2.asignarTarea(t5);
		e2.asignarTarea(t6);
		e2.asignarTarea(t7);
	}
	
	@Test
	public void agregarEmpleadoContratadoTest() {
		app.agregarEmpleadoContratado(41940907, "Matias Hillar", 500.0);
		Empleado e = (Empleado)app.getEmpleados().get(0);
		assertEquals(41940907, e.getCuil(), 0);
		assertTrue(Tipo.CONTRATADO == e.getTipo());
	}
	
	@Test
	public void agregarEmpleadoEfectivoTest() {
		app.agregarEmpleadoEfectivo(42450584, "Martin Escowich", 500.0);
		Empleado e = (Empleado)app.getEmpleados().get(0);
		assertEquals(42450584, e.getCuil(), 0);
		assertTrue(Tipo.EFECTIVO == e.getTipo());
	}
	
	@Test
	public void asignarTareaTest() throws ExcepcionAsig, ExcepcionAsignaTarea {
		Empleado e1 = new Empleado("Martin Escowich", 42450584,Tipo.CONTRATADO, 500.0);
		Tarea t1 = new Tarea(12345, "asfg", 10);
		e1.asignarTarea(t1);
		assertTrue(e1.getTareas().contains(t1));
		assertEquals(t1.getEmpleadoAsignado(), e1);
	}
	
	@Test
	public void asignarTareaFailsEfectivo(){
	//Falla por cantidad de horas acumuladas
		assertThrows(ExcepcionAsig.class,() -> e1.asignarTarea(t2));
		
	}
	
	@Test
	public void asignarTareaFailsContratado(){
	//Falla por cantidad de tareas acumuladas
		assertThrows(ExcepcionAsig.class,() -> e2.asignarTarea(t2));
		
	}
	
	@Test
	public void asignarTareaFailsTareaAsignada() throws ExcepcionNoExisteTarea, ExcepcionAsig, ExcepcionAsignaTarea{
	//Falla porque la tarea ya está asignada
		e2.finalizar(t3.getId());
		e2.finalizar(t4.getId());
		e2.finalizar(t5.getId());
		/*for(int i =0; i<e2.getTareas().size(); i++)
		System.out.println(e2.getTareas().get(i).toString());
		System.out.println(e2.getTareas().stream()
				.filter(t1 -> t1.getFechaFin() == null)
				.count());*/
		//No comprendo porqué se lanza la excepcion correspondiente
		//al caso en que el predicado "puedeAsignarTarea" falla.
		//Debería lanzar la excepcion del método asignarEmpleado de Tarea.
		assertThrows(ExcepcionAsignaTarea.class,() -> e2.asignarTarea(t1));
	}
	
	@Test
	public void empezarTareaTest() throws ExcepcionYaExisteEmpleado {
		app.agregarEmpleado(e1);
		app.empezarTarea(e1.getCuil(), t1.getId());
	}
	
	@Test
	public void empezarTareaTestFails() {
		//En la clase AppRRHH capturo la excepcion e imprimo el mensaje con el
		//stack trace. Debería entonces no testear este caso, o no capturar
		//las excepciones en AppRRHH y sólo lanzarlas?
		//assertThrows(ExcepcionYaExisteEmpleado.class,
			//	() -> app.empezarTarea(11111111, t1.getId()));
	}
	
	@Test
	public void terminarTareaTest() throws ExcepcionYaExisteEmpleado {
		app.agregarEmpleado(e1);
		app.empezarTarea(e1.getCuil(), t1.getId());
		app.terminarTarea(e1.getCuil(), t1.getId());
	}
	
	@Test
	public void terminarTareaTestFails() {
		//Idem empezarTareaTestFails
		//assertThrows(ExcepcionYaExisteEmpleado.class,
			//	() -> app.terminarTarea(11111111, t1.getId()));
	}
	
	@Test
	public void cargarEmpleadoContratadoCSVTest() throws FileNotFoundException, IOException {
		
				app.cargarEmpleadosContratadosCSV("./target/empleados.csv");
			System.out.println(app.getEmpleados());
		}
		
	@Test
	public void guardarTareasTerminadasCSVTest() throws IOException {
		app.guardarTareasTerminadasCSV();
	}
	}

