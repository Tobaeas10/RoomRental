package gui;

import javafx.stage.WindowEvent;

public class Controller {
	
	Gui gui;
	
	public Controller(Gui gui) {
		this.gui = gui;
	}

	public void exitProgram(WindowEvent e) {
		e.consume();
		gui.window.close();
		System.exit(0);
	}
}
