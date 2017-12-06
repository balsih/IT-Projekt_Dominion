package Server_MVC;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

import Abstract_MVC.Model;
import Server_GameLogic.ServerThreadForClient;

/**
 * @author Bodo Gruetter
 * 
 * The server model allows to start the server and to open a thread for the clients.
 * 
 * Adapted from:
 * Prof. Bradley Richards, Package: ch.fhnw.richards.lecture11_chatLab.v3_server, Class: Model
 */
public class Server_Model extends Model {

	private ServerSocket listener;
	private final Logger logger = Logger.getLogger("");
	
	String info;
	private volatile boolean stop = false;
	
	
	public Server_Model(){
		super();
	}

	/**
	 * @author Bodo Gruetter
	 * 
	 * @param port
	 * @throws IOException
	 */
	public void startServerSocket(int port) throws IOException{
		this.info = "Start Server";
		logger.info(info);
		try{
			this.listener = new ServerSocket(port);
			//Accept connections in separate thread
			Runnable r = new Runnable(){
				@Override
				public void run() {
					while(!stop){
						try{
							Socket socket = listener.accept();
							//start the thread
							ServerThreadForClient client = ServerThreadForClient.getServerThreadForClient(socket);
							new Thread(client).start();
						} catch (Exception e){
							logger.info(e.toString());
						}
					}
				}
				
			};
			Thread t = new Thread(r, "ServerSocket");
			t.start();
		}catch (IOException e) {
			this.logger.info(e.toString());
		}
	}
	
	public void stopServer(){		
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