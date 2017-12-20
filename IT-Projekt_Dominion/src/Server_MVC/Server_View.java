package Server_MVC;

import java.net.Inet4Address;
import java.net.UnknownHostException;

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
 * The Server_View creates and shows the GUI to interact with the server.
 * 
 * @author Bodo Gruetter 
 * source: Prof. Bradley Richards
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
		
		try {
			this.stage.setTitle("LocalHost: "+Inet4Address.getLocalHost().getHostAddress());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
		this.lblPort = new Label("Port");
		this.lblPort.getStyleClass().add("label");
		this.lblPort.setMinSize(Button.USE_PREF_SIZE, Button.USE_PREF_SIZE);
		
		this.txtPort = new TextField("8080");
		this.txtPort.getStyleClass().add("textfield");
		this.txtPort.setMinWidth(60);
		this.txtPort.setPrefWidth(60);
		
		this.btnStart = new Button("Start");
		this.btnStart.getStyleClass().add("button");
		this.btnStart.setMinSize(Button.USE_PREF_SIZE, Button.USE_PREF_SIZE);
		
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
		txtLog.setPrefSize(800, 600);;
		
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("Server.css").toExternalForm());
		stage.setScene(scene);
	}

	/**
	 * Shows the stage.
	 * 
	 * @author Bodo Gruetter
	 */
	public void start(){
		stage.show();
	}

	@Override
	protected Scene create_GUI() {
		// nothing to do here
		return null;
	}
}//end Server_View