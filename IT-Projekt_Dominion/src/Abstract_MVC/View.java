package Abstract_MVC;

import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Abstract super class for all views. 
 * 
 * @author Brad Richards, Copyright 2015, FHNW
 * , adapted by Rene Schwab
 * 
 */
public abstract class View<M> {
    protected Stage stage;
    protected Scene scene;
    protected M model;
    
    /**
     * Set any options for the stage in the subclass constructor
     * 
     * @param stage
     * @param model
     */
    protected View(Stage stage, M model) {
        this.stage = stage;
        this.model = model;
        
        Scene scene = create_GUI(); // Create all controls within "root"
        stage.setScene(scene);
    }

    protected abstract Scene create_GUI();

    /**
     * Display the view
     */
    public void start() {
        stage.show();
    }
    
    /**
     * Hide the view
     */
    public void stop() {
        stage.hide();
    }
    
    /**
     * Getter for the stage, so that the controller can access window events
     */
    public Stage getStage() {
        return stage;
    }
}
//end View