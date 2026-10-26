package edu.dlsu.csnetwk.server;

/**
 * Guillarte, Dana Louise A.
 * So, Chrysille Grace L.
 */
import java.net.*;
import java.io.*;
import edu.dlsu.csnetwk.common.ProtocolConfig;

public class ClientHandler
{
	private final Socket serverEndpoint;

	public ClientHandler(Socket serverEndpoint)
	{
		this.serverEndpoint = serverEndpoint;
	}

	public void handle()
	{
		try
		{
			System.out.println("Server: New client connected: " + serverEndpoint.getRemoteSocketAddress());

			DataInputStream disReader = new DataInputStream(serverEndpoint.getInputStream());
			System.out.println(disReader.readUTF());

			DataOutputStream dosWriter = new DataOutputStream(serverEndpoint.getOutputStream());
			dosWriter.writeUTF("Server: Hello World!");

			// Resolve file location (prioritize data/server/, fallback to working directory)
			File textFile = new File(ProtocolConfig.SERVER_DATA_PATH);
			if (!textFile.exists())
			{
				textFile = new File(ProtocolConfig.SERVER_FILE);
			}

			if (textFile.exists())
			{
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
				System.out.println("Server: File " + ProtocolConfig.SERVER_FILE + " does not exist. No file sent.");
				dosWriter.writeInt(ProtocolConfig.FILE_NOT_FOUND_CODE);
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
		}
	}
}
