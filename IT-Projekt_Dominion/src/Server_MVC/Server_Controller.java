package Server_MVC;

import Abstract_MVC.Controller;

/**
 * @author Lukas
 * @version 1.0
 * @created 31-Okt-2017 17:09:04
 */
public class Server_Controller extends Controller<Server_Model, Server_View> {

	/**
	 * 
	 * @param model
	 * @param view
	 */
	public Server_Controller(Server_Model model, Server_View view){
		super(model, view);
	}
}//end Server_Controller