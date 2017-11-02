package Server_Services;



/**
 * @author Bodo
 * @version 1.0
 * @created 31-Okt-2017 17:09:36
 */
public class ServiceLocator {

	private DB_Connector connection;
	private static ServiceLocator ServiceLocator;


	private ServiceLocator(){

	}

	public DB_Connector getDB_Connector(){
		return null;
	}

	public static ServiceLocator getServiceLocator(){
		return null;
	}

	/**
	 * 
	 * @param connector
	 */
	public void setDB_Connector(DB_Connector connector){

	}
}//end ServiceLocator