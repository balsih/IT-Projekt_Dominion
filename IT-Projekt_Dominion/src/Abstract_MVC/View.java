

/**
 * @author Renate
 * @version 1.0
 * @created 31-Okt-2017 17:06:35
 */
public abstract class View {

	private Model model;
	protected Scene scene;
	protected Stage stage;

	public View(){

	}

	public void finalize() throws Throwable {

	}
	/**
	 * 
	 * @param stage
	 * @param model
	 */
	protected int View(Stage stage, Model model){
		return 0;
	}

	protected abstract Scene create_GUI();

	public Stage getStage(){
		return null;
	}

	public start(){

	}

	public stop(){

	}
}//end View