package frsf.isi.died.guia08.problema01.modelo;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import junit.runner.Version;	

import frsf.isi.died.guia08.problema01.excepciones.ExcepcionAsig;
import frsf.isi.died.guia08.problema01.excepciones.ExcepcionAsignaTarea;
import frsf.isi.died.guia08.problema01.excepciones.ExcepcionNoExisteTarea;
import frsf.isi.died.guia08.problema01.modelo.Empleado.Tipo;


public class EmpleadoTest {

	private Empleado e1;
	private Tarea t1;
	private Tarea t2;
	private Tarea t3;
	private Tarea t4;
	private Tarea t5;
	private Tarea t6;
	
	@Before
	public void setUp() throws ExcepcionAsig, ExcepcionAsignaTarea {
		e1 = new Empleado("Matías Hillar", 41994097, Tipo.CONTRATADO, 500);
		t1 = new Tarea(12345, "Guía08 DIED", 8);
		t2 = new Tarea(23456, "TP GD E1", 12);
		t3 = new Tarea(34567, "TP Comunicaciones", 12);
		t4 = new Tarea(45678, "TP DDS", 4);
		t5 = new Tarea(54862, "TP Economia", 5);
		t6 = new Tarea(14852, "Guia Superior", 2);
		
		e1.asignarTarea(t1);
		e1.asignarTarea(t2);
		e1.asignarTarea(t4);
		e1.asignarTarea(t5);
	}
	
	@Test
	public void testSalario() throws ExcepcionAsig{
		double salario = e1.salario();
		assertEquals(14500, salario, 0);
	}

	@Test
	public void testCostoTarea() {
		double costo = e1.costoTarea(t1);
		assertEquals(4000, costo, 0);
	}

	@Test
	public void testAsignarTarea() throws ExcepcionAsig, ExcepcionAsignaTarea {
		e1.asignarTarea(t3);
	}
	
	@Test
	public void testAsignarTareaFails() {
		assertThrows(ExcepcionAsig.class, () -> e1.asignarTarea(t1));
	}

	@Test
	public void testComenzarInteger() throws ExcepcionNoExisteTarea {
		e1.comenzar(t2.getId());
		LocalDateTime f = t2.getFechaInicio().truncatedTo(ChronoUnit.SECONDS);
		assertEquals(f, LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
	}

	@Test
	public void testFinalizarInteger() throws ExcepcionNoExisteTarea {
		e1.finalizar(t4.getId());
		LocalDateTime f = t4.getFechaFin().truncatedTo(ChronoUnit.SECONDS);
		assertEquals(f, LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
	}

	@Test
	public void testComenzarIntegerString() throws ExcepcionNoExisteTarea {
		LocalDateTime f = LocalDateTime.of(2020, 5, 27, 8, 30);
		e1.comenzar(t1.getId(), "27-05-2020 08:30");
		assertEquals(f,t1.getFechaInicio());
		
	}

	@Test
	public void testFinalizarIntegerString() throws ExcepcionNoExisteTarea {
		LocalDateTime f = LocalDateTime.of(2020, 5, 28, 8, 30);
		e1.finalizar(t5.getId(), "28-05-2020 08:30");
		assertEquals(f,t5.getFechaFin());
	}

}
