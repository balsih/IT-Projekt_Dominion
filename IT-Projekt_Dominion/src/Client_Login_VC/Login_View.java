package Client_Login_VC;

import java.net.URL;

import Abstract_MVC.View;
import Client_GameApp_MVC.GameApp_Model;
import Client_Services.ServiceLocator;
import Client_Services.Translator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * @author Rene
 * @version 1.0
 * @created 31-Okt-2017 17:04:53
 */
public class Login_View extends View<GameApp_Model> {

	//private ServiceLocator sl = ServiceLocator.getServiceLocator();
	private ServiceLocator sl;
	
	// controls -> accessed by controller 
	
	protected Label logoLbl;
	protected Label keepTypingLbl;
	
	protected Label loginLbl;
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


	/**
	 * 
	 * @param model
	 */
	public Login_View(Stage stage, GameApp_Model model){
		super(stage, model);
		//stage.initStyle(StageStyle.TRANSPARENT);
		
	}

	@Override
	protected Scene create_GUI(){
		
		//sl.setTranslator(new Translator("en"));
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
		
		
		// labels and text fields
		logoLbl = new Label();
		logoLbl.setId("logoLbl");
		logoLbl.setPrefSize(410.0, 150.0);
		logoLbl.setMinSize(380.0, 0.0);
		logoLbl.setAlignment(Pos.CENTER);
	
		Image image = new Image(getClass().getResourceAsStream("Logo.png"));
		ImageView imageView = new ImageView(image);
		logoLbl.setGraphic(imageView);

		imageView.setScaleX(1.1);
		imageView.setScaleY(1.1);

		// Keep Typing Label
		keepTypingLbl = new Label();
		keepTypingLbl.setId("keepTypingLbl");
		keepTypingLbl.setPrefSize(40.0, 20.0);
		keepTypingLbl.setMinSize(40.0, 20.0);
		//keepTypingLbl.setAlignment(Pos.CENTER_RIGHT);
		keepTypingLbl.setAlignment(Pos.CENTER_LEFT);
	
		Image image2 = new Image(getClass().getResourceAsStream("KeepTyping.png"));
		ImageView imageView2 = new ImageView(image2);
		keepTypingLbl.setGraphic(imageView2);
		
		Label space = new Label();
		HBox typingBox = new HBox();
		typingBox.setId("typingBox");
		typingBox.getChildren().addAll(space, keepTypingLbl);

//		imageView2.setScaleX(1.1);
//		imageView2.setScaleY(1.1);
		
		loginLbl = new Label(t.getString("login.loginLbl"));
		loginLbl.setId("loginLbl");
		
		ipLbl = new Label(t.getString("login.ipLbl"));
		ipLbl.setId("ipLbl");
		portLbl = new Label(t.getString("login.portLbl"));
		portLbl.setId("portLbl");
		HBox ipAndPortLblBox = new HBox(ipLbl, portLbl);
		ipAndPortLblBox.setId("ipAndPortLblBox");
		
		ipText = new TextField();
		ipText.setId("ipText");
		ipText.setText("127.0.0.1"); // IP nur zu Testzwecken
		//ipText.setPrefSize(220.0, 30.0);
		ipText.setPrefSize(150.0, 30.0);
		portText = new TextField();
		portText.setId("portText");
		//portText.setText("8080"); // Port nur zu Testzwecken
		portText.setPrefSize(60.0, 30.0);
		
		connectBtn = new Button(t.getString("login.connectBtn"));
		connectBtn.setId("connectBtn");	
		connectBtn.setDisable(true);
		
		HBox ipAndConnectBox = new HBox(ipText, portText, connectBtn);
		ipAndConnectBox.setId("ipAndConnectBox");
		ipBox.getChildren().addAll(ipAndPortLblBox, ipAndConnectBox);
		
		
		nameLbl = new Label(t.getString("login.nameLbl"));
		nameLbl.setId("nameLbl");
		nameText = new TextField();
		nameText.setId("nameText");
		//nameText.setDisable(true); -> disable before connecting 
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
		quitBtn = new Button(t.getString("login.quitBtn"));
		quitBtn.setId("quitBtn");
		
		HBox buttonBox = new HBox(createNewPlayerBtn, quitBtn);
		buttonBox.setId("buttonBox");
	
		
		// VBox for layout and spacing 
		VBox ipNamePasswordBox = new VBox();
		ipNamePasswordBox.setId("ipNamePasswordBox");
		ipNamePasswordBox.getChildren().addAll(ipBox, nameBox, passwordBox);
		
		// layout and size configurations 
		root.setPrefSize(1280,720);
		root.setAlignment(Pos.CENTER);
		centerBox.getChildren().addAll(loginLbl, ipNamePasswordBox, buttonBox);
		// centerBox.getChildren().addAll(createNewPlayer, nameBox, passwordBox, buttonBox); // -> ohne Sprachauswahl 
		
		structureBox.getChildren().addAll(logoLbl, typingBox, centerBox);
		
		//root.getChildren().add(centerBox);
		root.getChildren().add(structureBox);
		
		
		Scene scene = new Scene(root);	
		scene.getStylesheets().add(getClass().getResource("Login.css").toExternalForm());
		this.stage.setScene(scene);
		stage.setFullScreen(true); // set Full Screen
		stage.setFullScreenExitHint(""); // set full screen message -> shows nothing
		
		loginAlert.initOwner(stage); // focus stays on full screen when alert message appears
		
		
//		// Set stage boundaries to visible bounds of the main screen
//		Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
//		stage.setX(primaryScreenBounds.getMinX());
//		stage.setY(primaryScreenBounds.getMinY());
//		stage.setWidth(primaryScreenBounds.getWidth());
//		stage.setHeight(primaryScreenBounds.getHeight());
		
		return scene;
	}
	
	//public void start() {
	
}//end Login_View