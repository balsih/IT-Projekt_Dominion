

/**
 * @author Lukas
 * @version 1.0
 * @created 31-Okt-2017 17:09:36
 */
public class ServiceLocator {

	private DB_Connector connection;
	private ServiceLocator ServiceLocator;
	public DB_Connector m_DB_Connector;



	public void finalize() throws Throwable {

	}
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
	public setDB_Connector(DB_Connector connector){

	}
}//end ServiceLocator