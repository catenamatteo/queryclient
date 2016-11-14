package it.cnr.isti.hpclab.queryclient;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * 
 * It sends queries at a constant rate. Query file is considered line by line.
 * Each line has to contain query_id and query, tab separated.
 * 
 * @author Matteo Catena
 *
 */
public class ConstantRateQueryClient extends QueryClient {

	private long toSleepMillis;
	private int toSleepNanos;

	public ConstantRateQueryClient(String hostName, int portNumber,
			String queryFile, int sendRate) {

		super(hostName, portNumber, queryFile);

		double r = 1000.0 / sendRate;
		toSleepMillis = (long) r;
		double fPart = r - toSleepMillis;
		toSleepNanos = (int) (fPart * 1000000);
	}

	public static void main(String args[]) {

		if (args.length != 4) {
			System.err
					.println("Usage: java QueryClient <host name> <port number> <query file> <send rate>");
			System.exit(1);
		}

		String hostName = args[0];
		int portNumber = Integer.parseInt(args[1]);
		String queryFile = args[2];
		int sendRate = Integer.parseInt(args[3]);
		ConstantRateQueryClient qs = new ConstantRateQueryClient(hostName,
				portNumber, queryFile, sendRate);
		qs.doWork();
	}

	@Override
	protected void send(BufferedReader queryReader)
			throws InterruptedException, IOException {

		String queryLine = null;
		
		while ((queryLine = queryReader.readLine()) != null) {

			System.err.println("Sending: " + queryLine);
			toTerrier.println(queryLine);

			Thread.sleep(toSleepMillis, toSleepNanos);
		}
	}
}
