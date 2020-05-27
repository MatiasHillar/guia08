package frsf.isi.died.guia08.problema01.modelo;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import frsf.isi.died.guia08.problema01.excepciones.ExcepcionAsig;
import frsf.isi.died.guia08.problema01.excepciones.ExcepcionAsignaTarea;
import frsf.isi.died.guia08.problema01.excepciones.ExcepcionNoExisteTarea;
import frsf.isi.died.guia08.problema01.modelo.Empleado.Tipo;

public class TareaTest {

	private Tarea t1;
	private Tarea t2;
	private Empleado e1;
	private Empleado e2;
	
	@Before
	public void setUp() throws ExcepcionAsignaTarea, ExcepcionNoExisteTarea, ExcepcionAsig {
		t1 = new Tarea(12345, "Modulo1 trabajoxx", 6);
		t2 = new Tarea(58422, "Modulo2 trabajoxx", 8);
		e1 = new Empleado("Juan Perez",45824156,Tipo.CONTRATADO,275);
		e2 = new Empleado("Marcos Perez",45848396,Tipo.CONTRATADO,350);
		e2.asignarTarea(t2);
		e2.comenzar(t2.getId(), "24-05-2020 08:30");
		e2.finalizar(t2.getId(), "26-05-2020 08:30");
	}
	
	@Test
	public void asignarEmpleadoTest() throws ExcepcionAsignaTarea, ExcepcionAsig {
		e1.asignarTarea(t1);
	}
	
//El método asignarTarea de Empleado llama al metodo asignar empleado de Tarea,
//la excepcion "ExcepcionAsignaTarea" es lanzada por este último.
	@Test
	public void asignarEmpleadoFailTest() {
		assertThrows(ExcepcionAsignaTarea.class, () -> e1.asignarTarea(t2));
	}
}
