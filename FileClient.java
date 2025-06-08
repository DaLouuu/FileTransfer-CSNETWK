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
			Socket clientEndpoint = new Socket(sServerAddress, nPort);
			
			System.out.println("Client: Connected to server at" + clientEndpoint.getRemoteSocketAddress());
			
			DataOutputStream dosWriter = new DataOutputStream(clientEndpoint.getOutputStream());
			dosWriter.writeUTF("Client: Hello from client" + clientEndpoint.getLocalSocketAddress());
			
			DataInputStream disReader = new DataInputStream(clientEndpoint.getInputStream());
			System.out.println(disReader.readUTF());

			boolean isFileComing = disReader.readBoolean();

			if (isFileComing) {
			
			String receivedFileName = disReader.readUTF();

			int fileSize = disReader.readInt();
            byte[] fileData = new byte[fileSize];
            disReader.read(fileData);

            FileOutputStream fos = new FileOutputStream("Received.txt");
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            bos.write(fileData);

			bos.close();
			clientEndpoint.close();
			System.out.println("Client: Downloaded file \"" + receivedFileName + "\"");
            } else {
                System.out.println("Client: No file sent by server.");
            }
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