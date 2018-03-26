package Client_CreatePlayer_VC;

import Abstract_MVC.View;
import Client_GameApp_MVC.GameApp_Model;
import Client_Services.ServiceLocator;
import Client_Services.Translator;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


/**
 * View class for CreatePlayer screen 
 * Defines controls and elements of this GUI, aligns and styles them.
 * 
 * @author Rene Schwab
 */
public class CreatePlayer_View extends View<GameApp_Model> {

	private ServiceLocator sl; 
	
	// controls, accessed by controller 
	protected Label nameLbl;
	protected TextField nameText;
		
	protected Label passwordLbl;
	protected TextField pWtextField;
	protected PasswordField passwordText;

	protected Button saveBtn;
	protected Button backBtn;
	
	protected Alert saveAlert;
	

	public CreatePlayer_View(Stage stage, GameApp_Model model){
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
		
		
		// Label createNewPlayerLbl
		Label createNewPlayerLbl = new Label(t.getString("cnp.createNewPlayerLbl"));
		createNewPlayerLbl.setId("createNewPlayerLbl");
		
		// Name and password elements and unmaskPw CheckBox
		nameLbl = new Label(t.getString("cnp.nameLbl"));
		nameLbl.setId("nameLbl");
		nameText = new TextField();
		nameText.setId("nameText");
		nameBox.getChildren().addAll(nameLbl, nameText);
		
		passwordLbl = new Label(t.getString("cnp.passwordLbl"));
		passwordLbl.setId("passwordLbl");
		
		// Text field to show password as unmasked
		pWtextField = new TextField();
		pWtextField.setId("textField");
		// Set initial state
		pWtextField.setManaged(false);
		pWtextField.setVisible(false);

		// Actual password field
		passwordText = new PasswordField();
		passwordText.setId("passwordText");

		CheckBox unmaskPw = new CheckBox();
		unmaskPw.setId("unmaskPw");

		// Bind pWtextField and passwordText together. Switch  the visibility 
		// when CheckBox gets clicked and display only one component at a time.
		pWtextField.managedProperty().bind(unmaskPw.selectedProperty());
		pWtextField.visibleProperty().bind(unmaskPw.selectedProperty());

		passwordText.managedProperty().bind(unmaskPw.selectedProperty().not());
		passwordText.visibleProperty().bind(unmaskPw.selectedProperty().not());

		// Bind the textField and passwordField text values together. 
		pWtextField.textProperty().bindBidirectional(passwordText.textProperty());
		
		Label showPwLbl = new Label(t.getString("cnp.showPwLbl"));
		showPwLbl.setId("showPwLbl");

		HBox unmaskPwBox = new HBox(unmaskPw, showPwLbl);
		unmaskPwBox.setId("unmaskPwBox");
		unmaskPwBox.setAlignment(Pos.CENTER_LEFT);

		passwordBox.getChildren().addAll(passwordLbl, passwordText, pWtextField);
		
		VBox pwFieldAndUnmaskBox = new VBox(passwordBox,unmaskPwBox);
		pwFieldAndUnmaskBox.setId("pwFieldAndUnmaskBox");
		
		
		// buttons
		saveBtn = new Button(t.getString("cnp.saveBtn"));
		saveBtn.setId("saveBtn");
		saveBtn.setPrefSize(130.0, 30.0);
		saveBtn.setDisable(true);
		backBtn = new Button(t.getString("cnp.backBtn"));
		backBtn.setId("backBtn");	
		backBtn.setPrefSize(130.0, 30.0);
		HBox buttonBox = new HBox(saveBtn, backBtn);
		buttonBox.setId("buttonBox");
		
		// Warning message, if login fails
		saveAlert = new Alert(AlertType.WARNING);
		saveAlert.setTitle(t.getString("cnp.saveAlert"));
		
		// VBox for layout and spacing 
		VBox namePasswordBox = new VBox();
		namePasswordBox.setId("namePasswordLanguageBox");
		namePasswordBox.getChildren().addAll(nameBox, pwFieldAndUnmaskBox);
		
		
		// Layout and size configurations 
		root.setPrefSize(1280,720);
		root.setAlignment(Pos.CENTER);
		centerBox.getChildren().addAll(createNewPlayerLbl, namePasswordBox, buttonBox);
	
	    structureBox.getChildren().addAll(logoLbl, typingBox, centerBox);
		root.getChildren().add(structureBox);

		Scene scene = new Scene(root);	
		scene.getStylesheets().add(getClass().getResource("CreatePlayer.css").toExternalForm());
		this.stage.setScene(scene);
		stage.setFullScreen(true); // set Full Screen
		stage.setFullScreenExitHint(""); // set full screen message -> shows nothing
		
		saveAlert.initOwner(stage); // focus stays on full screen when alert message appears
		
		return scene;
	}
	
	
	public void start() {
		this.stage.show();
	}
	
}//end CreatePlayer_View