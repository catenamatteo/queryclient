package it.cnr.isti.hpclab.queryclient;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * 
 * 
 * @author Matteo Catena
 *
 */
public abstract class QueryClient {

	protected String hostName;
	protected int portNumber;

	protected String queryFile;

	protected BufferedReader queryReader;
	protected PrintWriter toTerrier;
	protected BufferedReader fromTerrier;

	public QueryClient(String hostName, int portNumber, String queryFile) 
	{

		this.hostName = hostName;
		this.portNumber = portNumber;
		this.queryFile = queryFile;
	}

	public void doWork() {

		try {

			Socket socket = null;
			while (true) {

				try {
					
					socket = new Socket(hostName, portNumber);
					break;
					
				} catch (ConnectException ce) {

					System.err
							.println("Connect failed, waiting and trying again");
					try {
						Thread.sleep(2000);// 2 seconds
					} catch (InterruptedException ie) {
						ie.printStackTrace();
					}
				}
			}
			toTerrier = new PrintWriter(socket.getOutputStream(), true);
			fromTerrier = new BufferedReader(
	                new InputStreamReader(socket.getInputStream()));

			queryReader = new BufferedReader(new FileReader(queryFile));

			send(queryReader);
			
			queryReader.close();

//			System.err.println("Waiting query server to close...");
//			toTerrier.println("<DONE?>"); //SEND <DONE?>
//			while (!"<DONE!>".equals(fromTerrier.readLine())); //WAIT TO RECEIVE <DONE!>
			toTerrier.close();
			fromTerrier.close();
			socket.close();
			System.err.println("Bye!");
			
		} catch (UnknownHostException e) {

			e.printStackTrace();
			System.exit(1);

		} catch (IOException e) {

			e.printStackTrace();
			System.exit(1);

		} catch (InterruptedException e) {

			e.printStackTrace();
			System.exit(1);

		}
	}

	protected abstract void send(BufferedReader queryReader)  throws InterruptedException, IOException ;

//	public static void main(String args[]) {
//
//		if (args.length != 4) {
//			System.err
//					.println("Usage: java QueryClient <host name> <port number> <query file> <send rate>");
//			System.exit(1);
//		}
//
//		String hostName = args[0];
//		int portNumber = Integer.parseInt(args[1]);
//		String queryFile = args[2];
//		int sendRate = Integer.parseInt(args[3]);
//		QueryClient qs = new QueryClient(hostName, portNumber, queryFile,
//				sendRate);
//		qs.doWork();
//	}
}
