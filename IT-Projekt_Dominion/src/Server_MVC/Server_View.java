package Server_MVC;

import Abstract_MVC.View;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

/**
 * @author Bodo Grütter
 * @version 1.0
 * @created 31-Okt-2017 17:09:20
 * 
 * Adapted from:
 * https://stackoverflow.com/questions/39214586/how-to-align-a-button-right-in-javafx
 */
public class Server_View extends View<Server_Model>{

	private TextArea txtLog;

	protected Label lblPort;
	protected TextField txtPort;
	protected Button btnStart;
	protected Pane spacer;
	protected Stage stage;
	
	public Server_View(Stage stage, Server_Model model, TextArea txtLog){
	
		super(stage, model);
		this.stage = stage;
		this.txtLog = txtLog;
		
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
		
		
		BorderPane root = new BorderPane();
		root.getStyleClass().add("borderpane");
		
		this.spacer = new Pane();
		spacer.setMinSize(10, 1);
		
		HBox boxTop = new HBox(lblPort, txtPort, spacer, btnStart);
		boxTop.getStyleClass().add("hbox");
		boxTop.setHgrow(spacer, Priority.ALWAYS);
		
		root.setTop(boxTop);
		root.setCenter(txtLog);
		
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("Server.css").toExternalForm());
		stage.setScene(scene);
	}

	public void start(){
		stage.show();
	}

	@Override
	protected Scene create_GUI() {
		// TODO Auto-generated method stub
		return null;
	}
	
}//end Server_View