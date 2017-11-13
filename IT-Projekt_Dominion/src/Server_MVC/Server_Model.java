package Server_MVC;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

import Abstract_MVC.Model;
import Server_GameLogic.ServerThreadForClient;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * @author Bodo
 * @version 1.0
 * @created 31-Okt-2017 17:09:18
 */
public class Server_Model extends Model {

	private int port;
	private ServerSocket listener;
	private final Logger logger = Logger.getLogger("");
	
	//Clients?
	protected ObservableList<Client> clients = FXCollections.observableArrayList();
	
	String info;
	private volatile boolean stop = false;
	
	
	public Server_Model(){
		super();
	}

	//starts the server with the entered port
	public void startServerSocket(int port) throws IOException{
		this.info = "Start Server";
		logger.info(info);
		System.out.println(port);
		try{
			this.listener = new ServerSocket(port);
			//Accept connections in separate thread
			Runnable r = new Runnable(){
				@Override
				public void run() {
					while(!stop){
						try{
							Socket socket = listener.accept();
							//Client Object?
							Client client = new Client(socket);
							clients.add(client);
						} catch (Exception e){
							logger.info(e.toString());
						}
					}
				}
				
			};
			//start the thread
			Thread t = new Thread(r, "ServerSocket");
			t.start();
		}catch (IOException e) {
			this.logger.info(e.toString());
		}
	}
	
	public void stopServer(){
		//Clients?
		this.info = "Stop all clients";
		this.logger.info(info);
		
		this.info = "Stop Server";
		this.logger.info(info);
		this.stop = true;
		if(this.listener != null){
			try{
				this.listener.close();
			} catch (IOException e){
				//Nothing to do here
			}
		}
	}
	
	/*checks if the port value is valid
	 *Adapted from: 
	 * Prof. Bradley Richards, Package: ch.fhnw.richards.lecture02.email_validator, Class: EmailValidator_Model
	 * */
	protected boolean isValidPortNumber(String newValue) {
		boolean valid = true;

		try {
			int value = Integer.parseInt(newValue);
			if (value < 1 || value > 65535)
				valid = false;
		} catch (NumberFormatException e) {
			valid = false;
		}

		return valid;
	}
}//end Server_Model