package framework;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.BasicWindow;
import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.CheckBoxList;
import com.googlecode.lanterna.gui2.GridLayout;
import com.googlecode.lanterna.gui2.MultiWindowTextGUI;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.WindowBasedTextGUI;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

public class Menu {
	private String rutaArchivo;
	private Map<Integer, Action> acciones;

	public Menu(String rutaArchivo) {
		this.rutaArchivo = Objects.requireNonNull(rutaArchivo);
		this.acciones = new HashMap<Integer, Action>();
	}

	public void iniciar() {

		if (rutaArchivo.endsWith(".json"))
			this.cargarAccionesJson();
		if (rutaArchivo.endsWith(".properties"))
			this.cargarAccionesProperties();
		this.menuGrafico();
	}

	private void menuGrafico() {
		try {
			Terminal terminal = new DefaultTerminalFactory().createTerminal();
			TerminalScreen screen = new TerminalScreen(terminal);
			screen.startScreen();
			WindowBasedTextGUI textGUI = new MultiWindowTextGUI(screen);
			BasicWindow window = new BasicWindow("Bienvenido, estas son sus opciones:");
			TerminalSize size = new TerminalSize(50, 14);
			CheckBoxList<String> checkBoxList = new CheckBoxList<String>(size);
			Panel panel = new Panel(new GridLayout(2));

			for (Map.Entry<Integer, Action> entry : acciones.entrySet()) {
				checkBoxList.addItem(entry.getKey() + ". " + entry.getValue().nombreItemMenu() + " ("
						+ entry.getValue().descripcionItemMenu() + ")");
			}

			panel.addComponent(checkBoxList);
			panel.addComponent(new Button("Ejecutar Actions", new Runnable() {
				@Override
				public void run() {
					for (String var : checkBoxList.getCheckedItems()) {
						Integer val = Integer.parseInt(var.substring(0, 1));
						acciones.get(val).ejecutar();
					}
				}
			}));
			window.setComponent(panel);
			textGUI.addWindow(window);
			textGUI.waitForWindowToClose(window);

			screen.stopScreen();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void cargarAccionesProperties() {
		Properties prop = new Properties();

		try (InputStream configFile = getClass().getResourceAsStream(rutaArchivo);) {

			prop.load(configFile);
			String clase = prop.getProperty("acciones");

			String[] clases = clase.split(";");

			for (int i = 0; i < clases.length; i++) {
				System.out.println(clases[i]);
				Action accionTemp = (Action) Class.forName(clases[i]).getDeclaredConstructor().newInstance();
				this.acciones.put((i + 1), accionTemp);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void cargarAccionesJson() {
		ObjectMapper objectMapper = new ObjectMapper();
		Integer i = 0;
		System.out.println(rutaArchivo);
		try {

			InputStream file = getClass().getResourceAsStream(rutaArchivo);
			JsonNode rootNode = objectMapper.readTree(file);
			JsonNode accionesNode = rootNode.get("acciones");
			JsonNode cantHilos = rootNode.get("max-threads");
			System.out.println(cantHilos);

			for (JsonNode accionNode : accionesNode) {
				i++;
				System.out.println(accionNode.asText());
				Action accionTemp = (Action) Class.forName(accionNode.asText()).getDeclaredConstructor().newInstance();
				this.acciones.put((i), accionTemp);

			}
		} catch (Exception e) {
		}

	}

}
