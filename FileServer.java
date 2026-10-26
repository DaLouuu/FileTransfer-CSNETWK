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
		if (args.length < 1)
		{
			System.out.println("Usage: java FileServer <port>");
			return;
		}

		int nPort;
		try
		{
			nPort = Integer.parseInt(args[0]);
		}
		catch (NumberFormatException e)
		{
			System.err.println("Error: Port must be a valid integer.");
			return;
		}

		System.out.println("Server: Listening on port " + nPort + "...");
		ServerSocket serverSocket = null;
		Socket serverEndpoint = null;

		try 
		{
			serverSocket = new ServerSocket(nPort);
			serverEndpoint = serverSocket.accept();
			
			System.out.println("Server: New client connected: " + serverEndpoint.getRemoteSocketAddress());
			
			DataInputStream disReader = new DataInputStream(serverEndpoint.getInputStream());
			System.out.println(disReader.readUTF());
			
			DataOutputStream dosWriter = new DataOutputStream(serverEndpoint.getOutputStream());
			dosWriter.writeUTF("Server: Hello World!");

			File textFile = new File("data/server/Download.txt");
			if (!textFile.exists())
			{
				textFile = new File("Download.txt");
			}

			// Check if the file exists before attempting to send it
			if (textFile.exists())
			{
				// Read file contents into a byte array
				byte[] fileData = new byte[(int) textFile.length()];
				try (FileInputStream fis = new FileInputStream(textFile);
				     BufferedInputStream bis = new BufferedInputStream(fis))
				{
					int bytesRead = 0;
					while (bytesRead < fileData.length)
					{
						int read = bis.read(fileData, bytesRead, fileData.length - bytesRead);
						if (read == -1)
						{
							break;
						}
						bytesRead += read;
					}
				}

				System.out.println("Server: Sending file \"" + textFile.getName() + "\" (" + fileData.length + " bytes)");

				// Send the size of the file
				dosWriter.writeInt(fileData.length);
				dosWriter.flush();

				// Send the actual file data
				dosWriter.write(fileData, 0, fileData.length);
				dosWriter.flush();
			}
			else
			{
				// Inform if the file does not exist
				System.out.println("Server: File Download.txt does not exist. No file sent.");
				dosWriter.writeInt(-1);
				dosWriter.flush();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (serverEndpoint != null && !serverEndpoint.isClosed())
			{
				try
				{
					serverEndpoint.close();
				}
				catch (IOException e)
				{
					// Ignore exception on close
				}
			}
			if (serverSocket != null && !serverSocket.isClosed())
			{
				try
				{
					serverSocket.close();
				}
				catch (IOException e)
				{
					// Ignore exception on close
				}
			}
			System.out.println("Server: Connection is terminated.");
		}
	}
}