package genericUtilities;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertiesFileUtility {

	public String readFromPropertiesFile(String key) throws IOException {
		FileInputStream fis = new FileInputStream(IConstantsUtility.propertiesFilePath);
		Properties prop = new Properties();
		prop.load(fis);
		return prop.getProperty(key);
	}
}
