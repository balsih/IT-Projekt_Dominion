package Client_Login_VC;

import Abstract_MVC.View;
import Client_GameApp_MVC.GameApp_Model;
import Client_Services.ServiceLocator;
import Client_Services.Translator;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * View class for Login screen 
 * Defines controls and elements of this GUI, aligns and styles them.
 * 
 * @author Rene Schwab
 */
public class Login_View extends View<GameApp_Model> {

	private ServiceLocator sl;
	
	// controls, accessed by controller 
	protected Label ipLbl;
	protected Label portLbl;
	protected TextField ipText;
	protected TextField portText;
	protected Button connectBtn;
	
	protected Label nameLbl;
	protected TextField nameText;
	
	protected Label passwordLbl;
	protected PasswordField passwordText;
	protected Button loginBtn;
	
	protected Button createNewPlayerBtn;
	protected Button quitBtn;
	
	protected Alert loginAlert; 


	public Login_View(Stage stage, GameApp_Model model){
		super(stage, model);
	}

	@Override
	protected Scene create_GUI(){
		
		sl = ServiceLocator.getServiceLocator();
		Translator t = sl.getTranslator();
		
		// layouts
		GridPane root = new GridPane();
		root.setId("root");
		VBox centerBox = new VBox();
		centerBox.setId("centerBox");
		VBox ipBox = new VBox();
		ipBox.setId("ipBox");
		VBox nameBox = new VBox();
		nameBox.setId("nameBox");
		VBox passwordBox = new VBox();
		passwordBox.setId("passwordBox");
		
		VBox structureBox = new VBox();
		structureBox.setId("structureBox");
		
		
		// header with Dominion and Keep typing logo
		Label logoLbl = new Label();
		logoLbl.setId("logoLbl");
		logoLbl.setPrefSize(410.0, 150.0);
		logoLbl.setMinSize(380.0, 0.0);
		logoLbl.setAlignment(Pos.CENTER);

		Image image = new Image(getClass().getResourceAsStream("Logo.png"));
		ImageView imageView = new ImageView(image);
		logoLbl.setGraphic(imageView);
		imageView.setScaleX(1.1);
		imageView.setScaleY(1.1);

		Label keepTypingLbl = new Label();
		keepTypingLbl.setId("keepTypingLbl");
		keepTypingLbl.setPrefSize(40.0, 20.0);
		keepTypingLbl.setMinSize(40.0, 20.0);
		keepTypingLbl.setAlignment(Pos.CENTER_LEFT);

		Image image2 = new Image(getClass().getResourceAsStream("KeepTyping.png"));
		ImageView imageView2 = new ImageView(image2);
		keepTypingLbl.setGraphic(imageView2);

		Label space = new Label();
		HBox typingBox = new HBox();
		typingBox.setId("typingBox");
		typingBox.getChildren().addAll(space, keepTypingLbl);

		
		// Label loginLbl
		Label loginLbl = new Label(t.getString("login.loginLbl"));
		loginLbl.setId("loginLbl");
		
		// IP and port TextFields and connectBtn
		ipLbl = new Label(t.getString("login.ipLbl"));
		ipLbl.setId("ipLbl");
		portLbl = new Label(t.getString("login.portLbl"));
		portLbl.setId("portLbl");
		HBox ipAndPortLblBox = new HBox(ipLbl, portLbl);
		ipAndPortLblBox.setId("ipAndPortLblBox");
		
		ipText = new TextField();
		ipText.setId("ipText");
		ipText.setPrefSize(140.0, 30.0);
		portText = new TextField();
		portText.setId("portText");
		portText.setPrefSize(60.0, 30.0);
		
		connectBtn = new Button(t.getString("login.connectBtn"));
		connectBtn.setId("connectBtn");	
		connectBtn.setPrefSize(130.0, 30.0);
		connectBtn.setDisable(true);
		
		HBox ipAndConnectBox = new HBox(ipText, portText, connectBtn);
		ipAndConnectBox.setId("ipAndConnectBox");
		ipBox.getChildren().addAll(ipAndPortLblBox, ipAndConnectBox);
		
		// Name and password elements and loginBtn
		nameLbl = new Label(t.getString("login.nameLbl"));
		nameLbl.setId("nameLbl");
		nameText = new TextField();
		nameText.setId("nameText");
		nameText.setPrefSize(220.0, 30.0);
		nameText.setMaxSize(220.0, 30.0);
		nameBox.getChildren().addAll(nameLbl, nameText);
		
		passwordLbl = new Label(t.getString("login.passwordLbl"));
		passwordLbl.setId("passwordLbl");
		passwordText = new PasswordField();
		passwordText.setId("passwordText");
		passwordText.setPrefSize(220.0, 30.0);
		loginBtn = new Button(t.getString("login.loginBtn"));
		loginBtn.setId("loginBtn");
		loginBtn.setPrefSize(130.0, 30.0);
		loginBtn.setDisable(true);
		
		HBox pwLoginBox = new HBox(passwordText, loginBtn);
		pwLoginBox.setId("pwLoginBox");
		passwordBox.getChildren().addAll(passwordLbl, pwLoginBox);
		
		// warning message, if connect or login fails
		loginAlert = new Alert(AlertType.WARNING);
		loginAlert.setTitle(t.getString("login.connectAlert"));

		
		// buttons
		createNewPlayerBtn = new Button(t.getString("login.createNewPlayerBtn"));
		createNewPlayerBtn.setId("createNewPlayerBtn");
		createNewPlayerBtn.setPrefSize(182, 30);
		quitBtn = new Button(t.getString("login.quitBtn"));
		quitBtn.setId("quitBtn");
		quitBtn.setPrefSize(168.0, 30.0);
		HBox buttonBox = new HBox(createNewPlayerBtn, quitBtn);
		buttonBox.setId("buttonBox");
		
		
		// VBox for layout and spacing 
		VBox ipNamePasswordBox = new VBox();
		ipNamePasswordBox.setId("ipNamePasswordBox");
		ipNamePasswordBox.getChildren().addAll(ipBox, nameBox, passwordBox);
		
		// Layout and size configurations 
		root.setPrefSize(1280,720);
		root.setAlignment(Pos.CENTER);
		centerBox.getChildren().addAll(loginLbl, ipNamePasswordBox, buttonBox);
		
		structureBox.getChildren().addAll(logoLbl, typingBox, centerBox);
		root.getChildren().add(structureBox);
		
		Scene scene = new Scene(root);	
		scene.getStylesheets().add(getClass().getResource("Login.css").toExternalForm());
		this.stage.setScene(scene);
		stage.setFullScreen(true); // set Full Screen
		stage.setFullScreenExitHint(""); // set full screen message -> shows nothing
		
		loginAlert.initOwner(stage); // focus stays on full screen when alert message appears
		
		return scene;
	}
	
	//public void start() {
	
}//end Login_View