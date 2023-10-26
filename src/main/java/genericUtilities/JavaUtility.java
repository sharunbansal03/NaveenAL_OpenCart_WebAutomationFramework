package genericUtilities;

import java.util.Date;
import java.util.Random;

/**
 * This class contains generic methods related to Java
 * 
 * @author sharu
 *
 */
public class JavaUtility {

	/**
	 * This method will generate and return random number between 0-9999
	 * 
	 * @return
	 */
	public int generateRandomNumber() {
		Random ran = new Random();
		return ran.nextInt(10000);
	}

	/**
	 * This method will return system data and time
	 * 
	 * @return
	 */
	public String getSystemDateAndTime() {
		Date dt = new Date();
		return dt.toString();
	}

	/**
	 * This method will return system date in format Date_Month_Year_Time
	 * 
	 * @return
	 */
	public String getSystemDataAndTimeInFormat() {
		Date dt = new Date();
		String month = dt.toString().split("\\s")[1];
		String date = dt.toString().split("\\s")[2];
		String year = dt.toString().split("\\s")[5];
		String time = dt.toString().split("\\s")[3].replace(":", "-");

		return date + "_" + month + "_" + year + "_" + time;
	}
}
