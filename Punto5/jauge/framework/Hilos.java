package framework;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class Hilos implements Runnable {

	private Action action;

	public Hilos(Action action) {
		this.action = Objects.requireNonNull(action);
	}

	@Override
	public void run() {
		this.action.ejecutar();
		try {
			TimeUnit.MILLISECONDS.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
