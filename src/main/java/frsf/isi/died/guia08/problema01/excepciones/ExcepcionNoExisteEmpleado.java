package frsf.isi.died.guia08.problema01.excepciones;

public class ExcepcionNoExisteEmpleado extends Exception{

	public ExcepcionNoExisteEmpleado() {
		super("No existe un empleado con tal numero de cuil");
	}
	
}
