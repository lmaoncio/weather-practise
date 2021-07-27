package training.weather;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.javanet.NetHttpTransport;
import training.interfaces.IWeatherService;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class WeatherForecast implements IWeatherService {

	public static final String DATE_FORMAT = "yyyy-MM-dd";
	public static final Date DATE_RANGE = new Date(new Date().getTime() + (1000 * 60 * 60 * 24 * 6));
	public static final String REQUEST_CITY_ID_URL = "https://www.metaweather.com/api/location/search/?query=";
	public static final String REQUEST_CITY_WEATHER_URL = "https://www.metaweather.com/api/location/";
	public static final String JSON_WEATHER_NODE = "consolidated_weather";
	public static final String JSON_CITY_ID_NODE = "woeid";
	public static final String JSON_APPLICABLE_DATE_NODE = "applicable_date";
	public static final String JSON_WEATHER_STATE_NODE = "weather_state_name";

	HttpRequestFactory requestFactory = new NetHttpTransport().createRequestFactory();

	@Override
	public String getCityWeatherByName(String city, Date dateTime) {
		String cityStatus = null;

		if (dateTime == null) {
			dateTime = new Date();
		}
		if (dateTime.before(DATE_RANGE)) {
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
		
		cityData = buildRequest(requestFactory, REQUEST_CITY_ID_URL, cityName);

		try {
			cityDataArray = new JSONArray(cityData);
			cityID = cityDataArray.getJSONObject(0).get(JSON_CITY_ID_NODE).toString();
		} catch (JSONException e) {
			System.out.println("ERROR BUILDING JSON ARRAY: " + e.getMessage());
		}
		return cityID;
	}

	@Override
	public JSONArray getCityDataByCityID(String cityID) {
		String cityData = null;
		JSONArray cityDataArray = null;
		
		cityData = buildRequest(requestFactory, REQUEST_CITY_WEATHER_URL, cityID);
		try {
			cityDataArray = new JSONObject(cityData).getJSONArray(JSON_WEATHER_NODE);
		} catch (JSONException e) {
			System.out.println("ERROR BUILDING JSON ARRAY: " + e.getMessage());
		}
		return cityDataArray;
	}

	@Override
	public String extractCityStatusFromCityData(JSONArray cityData, Date dateTime) {
		for (int i = 0; i < cityData.length(); i++) {
			if (new SimpleDateFormat(DATE_FORMAT).format(dateTime)
				.equals(cityData.getJSONObject(i).get(JSON_APPLICABLE_DATE_NODE).toString())) {
				return cityData.getJSONObject(i).get(JSON_WEATHER_STATE_NODE).toString();
			}
		}
		return null;
	}
	
	private String buildRequest(HttpRequestFactory requestFactory, String URL, String data) {
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
