package Server_MVC;

import Abstract_MVC.View;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

/**
 * @author Bodo Gruetter
 *
 * The Server_View creates and shows the GUI to interact with the server.
 * 
 * Adapted from:
 * Prof. Bradley Richards, Package: ch.fhnw.richards.lecture11_chatLab.v3_server, Class: View
 */
public class Server_View extends View<Server_Model>{

	protected Label lblPort;
	protected TextField txtPort;
	protected Button btnStart;
	protected Button btnStop;
	protected Pane spacer;
	protected Stage stage;
	
	public Server_View(Stage stage, Server_Model model, TextArea txtLog){
	
		super(stage, model);
		this.stage = stage;
		
		this.lblPort = new Label("Port");
		this.lblPort.getStyleClass().add("label");
		this.lblPort.setMinSize(Button.USE_PREF_SIZE, Button.USE_PREF_SIZE);
		
		this.txtPort = new TextField();
		this.txtPort.getStyleClass().add("textfield");
		this.txtPort.setMinWidth(60);
		this.txtPort.setPrefWidth(60);
		
		this.btnStart = new Button("Start");
		this.btnStart.getStyleClass().add("button");
		this.btnStart.setMinSize(Button.USE_PREF_SIZE, Button.USE_PREF_SIZE);
		this.btnStart.setDisable(true);
		
		this.btnStop = new Button("Stop");
		this.btnStop.getStyleClass().add("button");
		this.btnStop.setMinSize(Button.USE_PREF_SIZE, Button.USE_PREF_SIZE);
		this.btnStop.setDisable(true);
		
		
		BorderPane root = new BorderPane();
		root.getStyleClass().add("borderpane");
		
		this.spacer = new Pane();
		spacer.setMinSize(10, 1);
		
		HBox boxTop = new HBox(lblPort, txtPort, spacer, btnStart, btnStop);
		boxTop.getStyleClass().add("hbox");
		HBox.setHgrow(spacer, Priority.ALWAYS);
		
		root.setTop(boxTop);
		root.setCenter(txtLog);
		txtLog.setEditable(false);;
		
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("Server.css").toExternalForm());
		stage.setScene(scene);
	}

	public void start(){
		stage.show();
	}

	@Override
	protected Scene create_GUI() {
		// nothing to do here
		return null;
	}
}//end Server_View