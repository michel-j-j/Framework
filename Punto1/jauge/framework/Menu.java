package framework;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.Scanner;

public class Menu {
	private String rutaArchivo;
	private Map<Integer, Action> acciones;

	public Menu(String rutaArchivo) {
		this.rutaArchivo = Objects.requireNonNull(rutaArchivo);
		this.acciones = new HashMap<Integer, Action>();
	}

	public void iniciar() {

		this.cargarAcciones();
		this.mostrarMenu();
		this.leerValor();
	}

	private void leerValor() {
		Scanner scanner = new Scanner(System.in);
		Integer opcion = 1;
		leer(opcion, scanner);
	}

	private void leer(Integer opcion, Scanner scanner) {
		while (true) {
			System.out.print("\nIngrese su opción: _\r\n");
			opcion = scanner.nextInt();
			if (opcion == 0) {
				System.out.println("Fin. \n");
				break;
			}
			try {
				this.acciones.get(opcion).ejecutar();
				this.mostrarMenu();
			} catch (Exception e) {
				System.out.println("Seleccione una opción válida.");
			}
		}
		scanner.close();
	}

	private void mostrarMenu() {
		System.out.println("Bienvenido, estas son sus opciones:\n");
		System.out.println("0. Salir");
		for (Map.Entry<Integer, Action> entry : acciones.entrySet()) {

			System.out.println(entry.getKey() + ". " + entry.getValue().nombreItemMenu() + " ("
					+ entry.getValue().descripcionItemMenu() + ")");
		}
	}

	private void cargarAcciones() {
		Properties prop = new Properties();

		try (InputStream configFile = getClass().getResourceAsStream(this.rutaArchivo);) {

			prop.load(configFile);
			String clase = prop.getProperty("acciones");

			String[] clases = clase.split(";");

			for (int i = 0; i < clases.length; i++) {
				Action accionTemp = (Action) Class.forName(clases[i]).getDeclaredConstructor().newInstance();
				this.acciones.put((i + 1), accionTemp);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
