/**
 * Guillarte, Dana Louise A.
 * So, Chrysille Grace L.
 */
import java.net.*;
import java.io.*;

public class FileClient
{
	public static void main(String[] args)
	{
		if (args.length < 2)
		{
			System.out.println("Usage: java FileClient <server-ip> <port>");
			return;
		}

		String sServerAddress = args[0];
		int nPort;
		try
		{
			nPort = Integer.parseInt(args[1]);
		}
		catch (NumberFormatException e)
		{
			System.err.println("Error: Port must be a valid integer.");
			return;
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

			// Define name for the new file to save received data
			String newFileName = "Received.txt";

			// Write the received file data to a local file
			try (FileOutputStream fos = new FileOutputStream(newFileName);
			     BufferedOutputStream bos = new BufferedOutputStream(fos))
			{
				bos.write(fileData);
				bos.flush();
			}
			
			System.out.println("Client: Downloaded file \"" + newFileName + "\"");
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