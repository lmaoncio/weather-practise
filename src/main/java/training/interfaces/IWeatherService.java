package training.interfaces;

import java.util.Date;

import org.json.JSONArray;

public interface IWeatherService {
	
	String getCityWeatherByName(String city, Date dateTime);
	
	String getCityIDByName(String cityName);
	
	JSONArray getCityDataByCityID(String cityID);
	
}
