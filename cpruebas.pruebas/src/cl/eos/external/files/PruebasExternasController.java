package cl.eos.external.files;

import cl.eos.imp.controller.AController;

public class PruebasExternasController extends AController {

	public PruebasExternasController() {
//		Font.loadFont(getClass().getResourceAsStream("/resources/fonts/marck.ttf"), 14);
		model = new PruebasExternasModel();
	}
	
	@Override
	public void initialize() {
		
	}

}
