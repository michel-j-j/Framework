package utilizacion;

import framework.Action;

public class ActionDos implements Action {

	@Override
	public void ejecutar() {
		System.out.println("Accion dos");
	}

	@Override
	public String nombreItemMenu() {
		// TODO Auto-generated method stub
		return "Accion Dos";
	}

	@Override
	public String descripcionItemMenu() {
		// TODO Auto-generated method stub
		return "La descripcion de la accion dos";
	}

}
