package training.weather;

import java.time.Instant;
import org.json.JSONArray;
import org.junit.Assert;
import org.junit.Test;

public class WeatherForecastTest {
	@Test
	public void getCityWeatherByName_NotNull_ShouldPass() {
		WeatherForecast weatherForecast = new WeatherForecast();
		String weatherStatus = weatherForecast.getCityWeatherByName("Barcelona", Instant.now());
		Assert.assertNotNull("CITY STATUS IS NULL", weatherStatus);
	}
	
	@Test
	public void getCityWeatherByName_NotInRangeDate_ShouldFail() {
		WeatherForecast weatherForecast = new WeatherForecast();
		
        Instant myDate = Instant.parse("2025-12-12T10:15:30.00Z");

		String WeatherStatus = weatherForecast.getCityWeatherByName("Barcelona", myDate);
		Assert.assertNotNull("DATE NOT IN RANGE", WeatherStatus);
	}
	
	@Test
	public void getCityWeatherByName_NullDateTime_ShouldPass() {
		WeatherForecast weatherForecast = new WeatherForecast();
		String WeatherStatus = weatherForecast.getCityWeatherByName("Barcelona", null);
		Assert.assertNotNull("DATE TIME IS NULL", WeatherStatus);
	}
	
	@Test
	public void getCityIDByName_IDEqual_ShouldPass() {
		WeatherForecast weatherForecast = new WeatherForecast();
		String cityID = weatherForecast.getCityIDByName("Barcelona");
		Assert.assertEquals("CITY ID NOT EQUAL","753692", cityID);
	}
	
	@Test
	public void getCityIDByName_NullCityID_ShouldFail() {
		WeatherForecast weatherForecast = new WeatherForecast();
		String cityID = weatherForecast.getCityIDByName("Palma");
		Assert.assertNotNull("CITY ID IS NULL", cityID);
	}
	
	@Test
	public void getCityDataByCityID_NotNullArray_ShouldPass() {
		WeatherForecast weatherForecast = new WeatherForecast();
		JSONArray cityDataArray = weatherForecast.getCityDataByCityID("753692");
		Assert.assertNotNull("CITY DATA IS NULL", cityDataArray);
	}
	
	@Test
	public void getCityDataByCityID_ThrowException_ShouldFail() {
		WeatherForecast weatherForecast = new WeatherForecast();
		JSONArray cityDataArray = weatherForecast.getCityDataByCityID("hola");
		System.out.println(cityDataArray);
		Assert.assertNotNull("CITY DATA IS NULL", cityDataArray);
	}

}