package edu.mayo.cts2.framework.plugin.service.exist.valuesetResolution;

public class JUnitAnnoyance
{
	private static boolean hasRun = false;
	
	public static boolean hasRun()
	{
		boolean temp = hasRun;
		hasRun = true;
		return temp;
	}
	public static void reset()
	{
		hasRun = false;
	}
	
}
