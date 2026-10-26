package edu.dlsu.csnetwk.server;

/**
 * Guillarte, Dana Louise A.
 * So, Chrysille Grace L.
 */
import java.net.*;
import java.io.*;
import edu.dlsu.csnetwk.common.ProtocolConfig;

public class FileServer
{
	public static void main(String[] args)
	{
		int nPort = ProtocolConfig.DEFAULT_PORT;
		if (args.length >= 1)
		{
			try
			{
				nPort = Integer.parseInt(args[0]);
			}
			catch (NumberFormatException e)
			{
				System.err.println("Error: Port must be a valid integer.");
				return;
			}
		}
		else
		{
			System.out.println("Usage: java edu.dlsu.csnetwk.server.FileServer <port>");
			System.out.println("Defaulting to port " + nPort + "...");
		}

		System.out.println("Server: Listening on port " + nPort + "...");
		ServerSocket serverSocket = null;

		try 
		{
			serverSocket = new ServerSocket(nPort);
			Socket serverEndpoint = serverSocket.accept();
			
			ClientHandler handler = new ClientHandler(serverEndpoint);
			handler.handle();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
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
