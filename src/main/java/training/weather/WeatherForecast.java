package training.weather;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.javanet.NetHttpTransport;
import training.interfaces.IWeatherService;
import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;


import org.json.JSONArray;
import org.json.JSONObject;

public class WeatherForecast implements IWeatherService {
	public static final String DATE_FORMAT = "yyyy-MM-dd";
	public static final Long LIMIT_DATE = 7L;
	public static final String REQUEST_CITY_ID_URL = "https://www.metaweather.com/api/location/search/?query=";
	public static final String REQUEST_CITY_WEATHER_URL = "https://www.metaweather.com/api/location/";
	public static final String JSON_WEATHER_NODE = "consolidated_weather";
	public static final String JSON_CITY_ID_NODE = "woeid";
	public static final String JSON_APPLICABLE_DATE_NODE = "applicable_date";
	public static final String JSON_WEATHER_STATE_NODE = "weather_state_name";

	private HttpRequestFactory requestFactory = new NetHttpTransport().createRequestFactory();

	/**
	* Returns city weather status by date and name of https://www.metaweather.com/api/
	* @param  cityName city of an available city
	* @param  dateTime available date of API
	* @return the forecast that day in that city
	*/
	
	@Override
	public String getCityWeatherByName(String cityName, Instant dateTime) {
		String cityStatus = null;

		Instant limitDate = Instant.now().plus(LIMIT_DATE, ChronoUnit.DAYS);
		
		if (dateTime == null) {
			dateTime = Instant.now();
		}
		if (dateTime.isBefore(limitDate)) {
			String cityID = getCityIDByName(cityName);
			JSONArray cityData = getCityDataByCityID(cityID);

			cityStatus = extractCityStatusFromCityData(cityData, dateTime);
			
			System.out.println(cityStatus);
		}
		return cityStatus;
		
	}

	/**
	* gets the cityID from the IP searching by the name
	* @param  cityName city of an available city
	* @return the ID of the city
	*/
	@Override
	public String getCityIDByName(String cityName) {
		String cityID = null;
		String cityData = null;
		JSONArray cityDataArray = null;

		cityData = executeRequest(requestFactory, REQUEST_CITY_ID_URL, cityName);

		if (cityData == null || cityData.length() < 3) {
			return null;
		}
		
		cityDataArray = new JSONArray(cityData);
		cityID = cityDataArray.getJSONObject(0).get(JSON_CITY_ID_NODE).toString();
	
		return cityID;
	}

	/**
	* gets the city weather data by the city ID in the API
	* @param  cityID city ID of the city in the API
	* @return A JSONArray with all the data of that city that date
	*/
	@Override
	public JSONArray getCityDataByCityID(String cityID) {
		String cityData = null;
		JSONArray cityDataArray = null;

		cityData = executeRequest(requestFactory, REQUEST_CITY_WEATHER_URL, cityID);
		
		if (cityData == null) {
			return null;
		}
		
		cityDataArray = new JSONObject(cityData).getJSONArray(JSON_WEATHER_NODE);

		return cityDataArray;
	}

	/**
	* extracts the weather status and returns the response to the client
	* @param  cityData a JSONArray with all the data of that city that date
	* @param  dateTime date to query
	* @return returns the city status
	*/
	@SuppressWarnings("unchecked")
	private String extractCityStatusFromCityData(JSONArray cityData, Instant dateTime) {
	    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(DATE_FORMAT).withZone(ZoneId.systemDefault());

	    String formatedDate = dateFormatter.format(new Date().toInstant());

	    // I've changed the way the program iterates the list 
	    // Now it's an Stream who filters by a condition. Then casts the response and looks for the key value status
	    return cityData.toList().stream().filter(data -> {			
			return formatedDate
					.equals(((Map<String, String>)data).get(JSON_APPLICABLE_DATE_NODE));
		}).map(map -> { 
			Map<String,String> castedMap = (Map<String,String>) map;
			
			String weatherStatus = castedMap.get(JSON_WEATHER_STATE_NODE);
			return weatherStatus;
		}).findAny().orElse(null).toString();
	}

	/**
	* builds requests and parses responses
	* @param  requestFactory the request factory
	* @param  URL requested API URL
	* @param  data the short-link to request methods at the API
	* @return returns a data response
	*/
	private String executeRequest(HttpRequestFactory requestFactory, String URL, String data) {
		HttpRequest request = null;
		String result = null;
		
		try {
			request = requestFactory.buildGetRequest(new GenericUrl(URL + data));
			result = request.execute().parseAsString();
		} catch (IOException e) {
			System.out.println("ERROR EXECUTING REQUEST: " + e.getMessage());
		}
		return result;
	}
}
