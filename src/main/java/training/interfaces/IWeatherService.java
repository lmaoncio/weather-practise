package training.interfaces;

import java.time.Instant;
import java.util.Date;

import org.json.JSONArray;

public interface IWeatherService {
	
	String getCityWeatherByName(String city, Instant dateTime);
	
	String getCityIDByName(String cityName);
	
	JSONArray getCityDataByCityID(String cityID);
	
}
