package training.weather;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.javanet.NetHttpTransport;
import training.interfaces.IWeatherService;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Locale;

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

	@Override
	public String getCityWeatherByName(String city, Instant dateTime) {
		String cityStatus = null;

		Instant limitDate = Instant.now().plus(LIMIT_DATE, ChronoUnit.DAYS);
		
		if (dateTime == null) {
			dateTime = Instant.now();
		}
		if (dateTime.isBefore(limitDate)) {
			String cityID = getCityIDByName(city);
			JSONArray cityData = getCityDataByCityID(cityID);

			cityStatus = extractCityStatusFromCityData(cityData, dateTime);
		}
		return cityStatus;
		
	}

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

	private String extractCityStatusFromCityData(JSONArray cityData, Instant dateTime) {
		String result = null;
		
	    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(DATE_FORMAT).withZone(ZoneId.systemDefault());

	    String formatedDate = dateFormatter.format(new Date().toInstant());
		
		
		for (int i = 0; i < cityData.length(); i++) {
			if (formatedDate.equals(cityData.getJSONObject(i).get(JSON_APPLICABLE_DATE_NODE).toString())) {
				result = cityData.getJSONObject(i).get(JSON_WEATHER_STATE_NODE).toString();
			}
		}
		
		return result;
	}

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
