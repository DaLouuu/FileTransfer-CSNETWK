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


            if(textFile.exists()){
                byte[] fileData = new byte[(int) textFile.length()];

                FileInputStream fis = new FileInputStream(textFile);
                BufferedInputStream bis = new BufferedInputStream(fis);
                bis.read(fileData, 0, fileData.length);

				System.out.println("Server: Sending file \"" + textFile.getName() + "\" (" + fileData.length + " bytes)");

                dosWriter.writeInt(fileData.length);
                dosWriter.flush();


                dosWriter.write(fileData, 0, fileData.length);
                dosWriter.flush();

                bis.close();
            } else {
                System.out.println("Server: File Download.txt does not exist. No file sent.");
            }

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