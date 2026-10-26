package edu.dlsu.csnetwk.client;

/**
 * Guillarte, Dana Louise A.
 * So, Chrysille Grace L.
 */
import java.net.*;
import java.io.*;
import edu.dlsu.csnetwk.common.ProtocolConfig;

public class FileClient
{
	public static void main(String[] args)
	{
		String sServerAddress = ProtocolConfig.DEFAULT_HOST;
		int nPort = ProtocolConfig.DEFAULT_PORT;

		if (args.length >= 2)
		{
			sServerAddress = args[0];
			try
			{
				nPort = Integer.parseInt(args[1]);
			}
			catch (NumberFormatException e)
			{
				System.err.println("Error: Port must be a valid integer.");
				return;
			}
		}
		else if (args.length == 1)
		{
			sServerAddress = args[0];
		}
		else
		{
			System.out.println("Usage: java edu.dlsu.csnetwk.client.FileClient <server-ip> <port>");
			System.out.println("Defaulting to " + sServerAddress + ":" + nPort + "...");
		}

		Socket clientEndpoint = null;
		try
		{
			String connectTarget = (sServerAddress.equals("127.0.0.1") || sServerAddress.equalsIgnoreCase("localhost"))
					? "localhost/127.0.0.1:" + nPort
					: sServerAddress + ":" + nPort;
			System.out.println("Client: Connecting to server at " + connectTarget);

			clientEndpoint = new Socket(sServerAddress, nPort);
			
			System.out.println("Client: Connected to server at" + clientEndpoint.getRemoteSocketAddress());
			
			DataOutputStream dosWriter = new DataOutputStream(clientEndpoint.getOutputStream());
			dosWriter.writeUTF("Client: Hello from client" + clientEndpoint.getLocalSocketAddress());
			
			DataInputStream disReader = new DataInputStream(clientEndpoint.getInputStream());
			System.out.println(disReader.readUTF());

			// Receive file size
			int fileSize = disReader.readInt();
			if (fileSize < 0)
			{
				System.out.println("Client: Server reported that Download.txt does not exist.");
				return;
			}

			// Prepare a byte array to store incoming file data
			byte[] fileData = new byte[fileSize];

			// Read the entire file into the byte array
			disReader.readFully(fileData);

			// Define name and target path for the received file
			String fileName = ProtocolConfig.CLIENT_FILE;
			File clientDir = new File(ProtocolConfig.CLIENT_DATA_DIR);
			if (!clientDir.exists())
			{
				clientDir.mkdirs();
			}

			// Save to data/client/Received.txt
			File clientOutputFile = new File(clientDir, fileName);
			try (FileOutputStream fos = new FileOutputStream(clientOutputFile);
			     BufferedOutputStream bos = new BufferedOutputStream(fos))
			{
				bos.write(fileData);
				bos.flush();
			}

			// Also save to root Received.txt for direct compatibility
			File rootOutputFile = new File(fileName);
			try (FileOutputStream fos = new FileOutputStream(rootOutputFile);
			     BufferedOutputStream bos = new BufferedOutputStream(fos))
			{
				bos.write(fileData);
				bos.flush();
			}

			System.out.println("Client: Downloaded file \"" + fileName + "\"");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (clientEndpoint != null && !clientEndpoint.isClosed())
			{
				try
				{
					clientEndpoint.close();
				}
				catch (IOException e)
				{
					// Ignore exception on close
				}
			}
			System.out.println("Client: Connection is terminated.");
		}
	}
}
