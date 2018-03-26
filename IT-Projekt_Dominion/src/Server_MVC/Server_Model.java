package Server_MVC;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

import Abstract_MVC.Model;
import Server_GameLogic.ServerThreadForClient;

/**
 * The server model allows to start the server and to open a thread for
 * the clients.
 *         
 * @author Bodo Gruetter
 * source: Prof. Bradley Richards
 */
public class Server_Model extends Model {

	private ServerSocket listener;
	private final Logger logger = Logger.getLogger("");

	String info;
	private volatile boolean stop = false;

	Runnable r;

	public Server_Model() {
		super();
	}

	/**
	 * 
	 * Starts the Server in a thread and starts a new Thread if server is down.
	 * 
	 * @author Bodo Gruetter
	 * source: Prof. Bradley Richards
	 * 
	 * @param port
	 * @throws IOException
	 */
	public void startServerSocket(int port) throws IOException {
		this.info = "Start Server";
		logger.info(info);
		try {
			this.listener = new ServerSocket(port);
			// Accept connections in separate thread
			this.r = new Runnable() {
				@Override
				public void run() {
					while (!stop) {
						Socket socket = null;
						try {
							// start the thread
							socket = listener.accept();
							ServerThreadForClient client = ServerThreadForClient.getServerThreadForClient(socket);
							if (client != null) {
								new Thread(client).start();
							} else {
								logger.severe("Thread was null");
							}
						} catch (Exception e) {
							logger.severe("Exception in startServerSocket: " + e.toString());
						}
					}
				}

			};
			Thread t = new Thread(r, "ServerSocket");
			t.start();
		} catch (IOException e) {
			this.logger.info(e.toString());
		}
	}

	/**
	 * stops the server.
	 * 
	 * @author Bodo Gruetter
	 * source: Prof. Bradley Richards
	 */
	public void stopServer() {
		this.info = "Stop Server";
		this.logger.info(info);
		this.stop = true;
		if (this.listener != null) {
			try {
				this.listener.close();
			} catch (IOException e) {
				// Nothing to do here
			}
		}
	}

	/**
	 * checks if the port value is valid
	 * 
	 * @author Bodo Gruetter
	 * source: Prof. Bradley Richards
	 * 
	 * @param newValue
	 * , the new input value that will be checked
	 * @return true or false depending if new input is valid or not
	 */
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
}// end Server_Model