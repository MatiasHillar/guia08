package frsf.isi.died.guia08.problema01;

import java.io.IOException;

import frsf.isi.died.guia08.problema01.excepciones.ExcepcionAsig;
import frsf.isi.died.guia08.problema01.excepciones.ExcepcionAsignaTarea;
import frsf.isi.died.guia08.problema01.excepciones.ExcepcionNoExisteEmpleado;

public class app {
private static AppRRHH app;
	public static void main(String[] args) {
app = new AppRRHH();
		try {
			app.cargarEmpleadosContratadosCSV("./target/empleados.csv");
			app.cargarTareasCSV("./target/tareas.csv");
			app.guardarTareasTerminadasCSV();
		} catch (IOException | NumberFormatException | ExcepcionNoExisteEmpleado | ExcepcionAsig | ExcepcionAsignaTarea e) {
			e.getMessage();
			e.printStackTrace();
		}
	}

}
