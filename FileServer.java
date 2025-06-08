/**
 * Guillarte, Dana Louise A.
 * So, Chrysille Grace L.
 */
import java.net.*;
import java.io.*;

public class FileServer
{
	public static void main(String[] args)
	{
		int nPort = Integer.parseInt(args[0]);
		System.out.println("Server: Listening on port " + args[0] + "...");
		ServerSocket serverSocket;
		Socket serverEndpoint;

		try 
		{
			serverSocket = new ServerSocket(nPort);
			serverEndpoint = serverSocket.accept();
			
			System.out.println("Server: New client connected: " + serverEndpoint.getRemoteSocketAddress());
			
			DataInputStream disReader = new DataInputStream(serverEndpoint.getInputStream());
			System.out.println(disReader.readUTF());
			
			DataOutputStream dosWriter = new DataOutputStream(serverEndpoint.getOutputStream());
			dosWriter.writeUTF("Server: Hello World!");

			File textFile = new File("Download.txt");

			// Check if the file exists before attempting to send it
            if(textFile.exists()){

				// Read file contents into a byte array
                byte[] fileData = new byte[(int) textFile.length()];
                FileInputStream fis = new FileInputStream(textFile);
                BufferedInputStream bis = new BufferedInputStream(fis);
                bis.read(fileData, 0, fileData.length);

				System.out.println("Server: Sending file \"" + textFile.getName() + "\" (" + fileData.length + " bytes)");

				// Send the size of the file
                dosWriter.writeInt(fileData.length);
                dosWriter.flush();

				// Send the actual file data
                dosWriter.write(fileData, 0, fileData.length);
                dosWriter.flush();

				// Close the file input stream
                bis.close();
            } else {
				// Inform if the file does not exist
                System.out.println("Server: File Download.txt does not exist. No file sent.");
            }

			// Close the socket connections
			serverSocket.close();
			serverEndpoint.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			System.out.println("Server: Connection is terminated.");
		}
	}
}