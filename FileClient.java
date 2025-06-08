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

			int fileSize = disReader.readInt();
            byte[] fileData = new byte[fileSize];
            disReader.readFully(fileData);

			String newFileName = "Received.txt";
            FileOutputStream fos = new FileOutputStream(newFileName);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            bos.write(fileData);

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