package genericUtilities;

import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * This class is used to define generic methods to read configurations from given config.json file
 * from json file
 * 
 * @author sharu
 *
 */
public class TestConfigUtility {


	public TestConfigUtility() {
	}
	
	/**
	 * This method will return complete Test Configuration json object
	 * 
	 * @param jsonFilePath
	 * @param objectKey
	 * @return
	 */
	public JSONObject getTestConfiguration(String jsonFilePath) {
		JSONParser parser = new JSONParser();
		JSONObject config = null;
		try {
			config = (JSONObject) parser.parse(new FileReader(jsonFilePath));
		} catch (IOException | ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return config;
	}

	/**
	 * This method will determine the "driver" inside test config object
	 * @param jsonFilePath
	 * @param objectKey
	 * @return
	 */
	public String determineLocalOrRemoteDriver(JSONObject config) {
		return config.get("driver").toString();
	}
	
	/**
	 * This method will return Configuration Object for given "Environment"
	 * @param configuration
	 * @param environment
	 * @return
	 */
	public JSONObject getEnvironmentConfiguration(JSONObject configuration, String environment) {
		JSONObject envs = (JSONObject) configuration.get("environments");
		return (JSONObject) envs.get(environment);
	}

	public String getBrowser(JSONObject configuration, String environment) {
		return getEnvCapabilities(configuration, environment).get("browserName").toString();
	}
	
	public String getBrowserVersion(JSONObject configuration, String environment) {
		return getEnvCapabilities(configuration, environment).get("browserVersion").toString();
	}
	
	public String getPlatform(JSONObject configuration, String environment) {
		return getEnvCapabilities(configuration, environment).get("platformName").toString();
	}

	public JSONObject getEnvCapabilities(JSONObject configuration, String environment) {
		JSONObject environmentConfig = getEnvironmentConfiguration(configuration,environment);
		return (JSONObject) environmentConfig.get("capabilities");
	}

	public String getRemoteHubURL(JSONObject configuration) {
		System.out.println(configuration.get("remote-hub-url"));
		return configuration.get("remote-hub-url").toString();
	}

	public JSONObject getSauceLabsCapabilities(JSONObject configuration) {
		return (JSONObject) configuration.get("capabilities");
	}
	
	public JSONObject getBrowserStackGeneralCapabilities(JSONObject configuration) {
		return (JSONObject) configuration.get("capabilities");
	}

	 public String getBrowserStackServer(JSONObject config)
	    {
	        String browserStackServer = (String) config.get("server");
	        return browserStackServer;
	    }

	    public String getBrowserStackUserName(JSONObject config)
	    {
	        String username = (String) config.get("userName");
	        return username;
	    }

	    public String getBrowserStackKey(JSONObject config)
	    {
	        String key = (String) config.get("accessKey");
	        return key;
	    }

}
