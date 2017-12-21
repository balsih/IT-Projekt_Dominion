package Abstract_MVC;

/**
 * Abstract super class for all controllers. 
 * 
 * @author Brad Richards, Copyright 2015, FHNW
 * , adapted by Rene Schwab
 * 
 */
public abstract class Controller<M, V> {
    protected M model;
    protected V view;
    
    protected Controller(M model, V view) {
        this.model = model;
        this.view = view;
    }
}//end Controller