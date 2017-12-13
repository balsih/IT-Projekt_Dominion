package Client_CreatePlayer_VC;

import java.net.URL;

import Abstract_MVC.View;
import Client_GameApp_MVC.GameApp_Model;
import Client_Services.ServiceLocator;
import Client_Services.Translator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;


/**
 * @author Rene
 * @version 1.0
 * @created 31-Okt-2017 17:03:48
 */
public class CreatePlayer_View extends View<GameApp_Model> {

	private ServiceLocator sl; 
	
	// controls -> accessed by controller 
	
	protected Label logoLbl;
	protected Label keepTypingLbl;
	
	protected Label createNewPlayerLbl;
		
	protected Label nameLbl;
	protected TextField nameText;
		
	protected Label passwordLbl;
	protected PasswordField passwordText;
	
	protected Label languageSelectLbl;
	
	protected Button saveBtn;
	protected Button backBtn;
	
	protected Alert saveAlert;
	

	/**
	 * 
	 * @param model
	 */
	public CreatePlayer_View(Stage stage, GameApp_Model model){
		super(stage, model);
		//stage.initStyle(StageStyle.TRANSPARENT);
	}

	@Override
	protected Scene create_GUI(){
		
		sl = ServiceLocator.getServiceLocator();
		//sl.setTranslator(new Translator("en"));
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
		
		VBox languageBox = new VBox();
		languageBox.setId("languageBox");
		
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
		// keepTypingLbl.setAlignment(Pos.CENTER_RIGHT);
		keepTypingLbl.setAlignment(Pos.CENTER_LEFT);

		Image image2 = new Image(getClass().getResourceAsStream("KeepTyping.png"));
		ImageView imageView2 = new ImageView(image2);
		keepTypingLbl.setGraphic(imageView2);

		Label space = new Label();
		HBox typingBox = new HBox();
		typingBox.setId("typingBox");
		typingBox.getChildren().addAll(space, keepTypingLbl);
		
		/////////
		
		
		createNewPlayerLbl = new Label(t.getString("cnp.createNewPlayerLbl"));
		createNewPlayerLbl.setId("createNewPlayerLbl");
		
		nameLbl = new Label(t.getString("cnp.nameLbl"));
		nameLbl.setId("nameLbl");
		nameText = new TextField();
		nameText.setId("nameText");
		nameBox.getChildren().addAll(nameLbl, nameText);
		
		passwordLbl = new Label(t.getString("cnp.passwordLbl"));
		passwordLbl.setId("passwordLbl");
		passwordText = new PasswordField();
		passwordText.setId("passwordText");
		passwordBox.getChildren().addAll(passwordLbl, passwordText);
		
		
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
		
		// warning message, if login fails
		saveAlert = new Alert(AlertType.WARNING);
		saveAlert.setTitle(t.getString("cnp.saveAlert"));
		//saveAlert.setHeaderText(t.getString("isUsed"));
		// loginAlert.setContentText("do this or that");
		
		// VBox for layout and spacing 
		VBox namePasswordLanguageBox = new VBox();
		namePasswordLanguageBox.setId("namePasswordLanguageBox");
		namePasswordLanguageBox.getChildren().addAll(nameBox, passwordBox, languageBox);
		
		// layout and size configurations 
		root.setPrefSize(1280,720);
		root.setAlignment(Pos.CENTER);
		centerBox.getChildren().addAll(createNewPlayerLbl, namePasswordLanguageBox, buttonBox);
		// centerBox.getChildren().addAll(createNewPlayer, nameBox, passwordBox, buttonBox); // -> ohne Sprachauswahl 
		
		
		//////////////////
		// text field to show password as unmasked
	    TextField textField = new TextField();
	    // Set initial state
	    textField.setManaged(false);
	    textField.setVisible(false);

	    // Actual password field
	    PasswordField passwordField = new PasswordField();

	    CheckBox checkBox = new CheckBox("Show/Hide password");

	    // Bind properties. Toggle textField and passwordField
	    // visibility and managability properties mutually when checkbox's state is changed.
	    // Because we want to display only one component (textField or passwordField)
	    // on the scene at a time.
	    textField.managedProperty().bind(checkBox.selectedProperty());
	    textField.visibleProperty().bind(checkBox.selectedProperty());

	    passwordField.managedProperty().bind(checkBox.selectedProperty().not());
	    passwordField.visibleProperty().bind(checkBox.selectedProperty().not());

	    // Bind the textField and passwordField text values bidirectionally.
	    textField.textProperty().bindBidirectional(passwordField.textProperty());
	    
	    //////////////////////////
		
		
		
		
		//structureBox.getChildren().addAll(logoLbl, typingBox, centerBox, passwordField, textField, checkBox);
	    
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