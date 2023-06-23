package utilizacion;

import framework.Action;

public class ActionUno implements Action {

	@Override
	public void ejecutar() {
		System.out.println("Accion uno");
	}

	@Override
	public String nombreItemMenu() {
		// TODO Auto-generated method stub
		return "Accion Uno";
	}

	@Override
	public String descripcionItemMenu() {
		// TODO Auto-generated method stub
		return "La descripcion de la accion uno";
	}

}
