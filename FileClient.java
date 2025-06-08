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
		String sServerAddress = args[0];
		int nPort = Integer.parseInt(args[1]);
		
		try
		{
			System.out.println("Client: Connecting to server at localhost/127.0.0.1:4000");

			Socket clientEndpoint = new Socket(sServerAddress, nPort);
			
			System.out.println("Client: Connected to server at" + clientEndpoint.getRemoteSocketAddress());
			
			DataOutputStream dosWriter = new DataOutputStream(clientEndpoint.getOutputStream());
			dosWriter.writeUTF("Client: Hello from client" + clientEndpoint.getLocalSocketAddress());
			
			DataInputStream disReader = new DataInputStream(clientEndpoint.getInputStream());
			System.out.println(disReader.readUTF());

			// Receive file size
			int fileSize = disReader.readInt();

			// Prepare a byte array to store incoming file data
            byte[] fileData = new byte[fileSize];

			// Read the entire file into the byte array
            disReader.readFully(fileData);

			// Define name for the new file to save received data
			String newFileName = "Received.txt";

			// Write the received file data to a local file
            FileOutputStream fos = new FileOutputStream(newFileName);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            bos.write(fileData);

			// Close output stream and socket
			bos.close();
			clientEndpoint.close();
			
			System.out.println("Client: Downloaded file \"" + newFileName + "\"");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			System.out.println("Client: Connection is terminated.");
		}
	}
}