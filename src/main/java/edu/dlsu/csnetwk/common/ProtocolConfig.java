package edu.dlsu.csnetwk.common;

/**
 * Guillarte, Dana Louise A.
 * So, Chrysille Grace L.
 * 
 * Shared network configuration and protocol constants for CSNETWK Lab Activity 3.
 */
public final class ProtocolConfig
{
	public static final int DEFAULT_PORT = 4000;
	public static final String DEFAULT_HOST = "127.0.0.1";
	public static final String SERVER_FILE = "Download.txt";
	public static final String SERVER_DATA_PATH = "data/server/Download.txt";
	public static final String CLIENT_FILE = "Received.txt";
	public static final String CLIENT_DATA_DIR = "data/client";
	public static final int FILE_NOT_FOUND_CODE = -1;

	private ProtocolConfig()
	{
		// Prevent instantiation
	}
}
