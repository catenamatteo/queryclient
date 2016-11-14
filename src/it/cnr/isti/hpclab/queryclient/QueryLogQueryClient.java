package it.cnr.isti.hpclab.queryclient;


import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;

/**
 * 
 * It sends queries using interarrival timings.
 * A query log file is considered, line by line.
 * Each line has to contain query_id, query_time and query,
 * tab separated.
 * query_time is the interval from the origin in ms.
 * 
 * @author Matteo Catena
 *
 */
public class QueryLogQueryClient extends QueryClient{
	
	public QueryLogQueryClient(String hostName, int portNumber, String queryFile) {

		super(hostName, portNumber, queryFile);
	}

	protected void send(BufferedReader queryReader) throws InterruptedException, IOException {
		
		String queryLine = null;
		int previous = 0;
		int millisecs = 0;
		while ((queryLine = queryReader.readLine()) != null) {
			
			String fields[] = queryLine.split("\t");
			millisecs = Integer.parseInt(fields[0]);
			String queryToSend = String.join("\t", Arrays.copyOfRange(fields, 1, fields.length));
			System.err.println("Sending: "+ queryToSend + " after ms"+(millisecs-previous));
			toTerrier.println(queryToSend);
			Thread.sleep(millisecs - previous);
			previous = millisecs;			
		}
	}

	public static void main(String args[]) {

		if (args.length != 3) {
			System.err
					.println("Usage: java QueryLogQueryClient <host name> <port number> <query file>");
			System.exit(1);
		}

		String hostName = args[0];
		int portNumber = Integer.parseInt(args[1]);
		String queryFile = args[2];
		QueryLogQueryClient qs = new QueryLogQueryClient(hostName, portNumber, queryFile);
		qs.doWork();
	}
}
