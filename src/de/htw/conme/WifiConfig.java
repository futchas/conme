/**
 * 
 */
package de.htw.conme;

import android.net.wifi.WifiConfiguration;

/**
 * @author Iyad Al-Sahwi
 *
 */
public class WifiConfig extends WifiConfiguration{
	
//	public static String networkSSID = "Blablub";
//	public static String key = "Badabum123";
	
//	public WifiConfig(boolean isAccessPoint){
//		
////		wifiConfig.hiddenSSID = true;
//		this.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
//		if(isAccessPoint) { //AP don't need quotation marks for the SSID or network key
//			this.SSID = networkSSID;
//			this.preSharedKey = key;
//		} else { //On the client quotation marks for the SSID and key are necessary
//			String quotation = "\"";
//			this.SSID = quotation + networkSSID + quotation;
//			this.preSharedKey = quotation + key + quotation;
//		}
//			
////		wifiConfig.preSharedKey = "x0UsFP83UOFtrfWYzWJ6P9Gt3Qb7UTCYrJn1j14wCT1vTxto8ygRFMq5QlRGk43";
//		
////		this.status = WifiConfiguration.Status.ENABLED;
////		this.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
////		this.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
////		this.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
////		this.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
////		this.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
////		this.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
//	}
	
	public WifiConfig(String ssid, String key, boolean isAccessPoint){
		
//		wifiConfig.hiddenSSID = true;
		this.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
		if(isAccessPoint) { //AP don't need quotation marks for the SSID or network key
			this.SSID = ssid;
			this.preSharedKey = key;
		} else { //On the client quotation marks for the SSID and key are necessary
			String quotation = "\"";
			this.SSID = quotation + ssid + quotation;
			this.preSharedKey = quotation + key + quotation;
		}
			
	}
}
