/**
 * 
 */
package de.htw.conme;


/**
 * @author Iyad Al-Sahwi
 *
 */
public class ConMeUtils {

	public static final String NETWORK_BRANDING = "ConMe||"; 
	public static final int PORT = 3333;
	
	public static boolean isConMeNetwork(String ssid) {
		if(ssid.startsWith(NETWORK_BRANDING) && ssid.length() == 15)
			return true;
		return false;
	}

}
