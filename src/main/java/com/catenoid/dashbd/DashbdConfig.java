package com.catenoid.dashbd;

public class DashbdConfig {
	
	private static final String PROGRAM_NAME = "DashBD";
	private static final double PROGRAM_VERSION = 1.0; 
	
	public static String getVersion() {
		return String.format("%s %d", PROGRAM_NAME, PROGRAM_VERSION);
	}
}
