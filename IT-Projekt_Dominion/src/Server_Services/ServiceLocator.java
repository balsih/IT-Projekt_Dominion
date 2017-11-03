package Server_Services;



/**
 * @author Bodo
 * @version 1.0
 * @created 31-Okt-2017 17:09:36
 */
public class ServiceLocator {

	private DB_Connector connector;
	private static ServiceLocator serviceLocator;


	public static void init(){
		if(serviceLocator == null)
			serviceLocator = new ServiceLocator();
	}

	public static DB_Connector getDB_Connector(){
		return serviceLocator.connector;
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