package com.survivingwithandroid.weatherapp;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.survivingwithandroid.weatherapp.model.Location;
import com.survivingwithandroid.weatherapp.model.Weather;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

import android.view.View;
import android.widget.Toast;
import android.widget.Button;

public class MainActivity extends Activity {

	
	private TextView cityText;
	private TextView condDescr;
	private TextView temp;
	private TextView press;
	private TextView windSpeed;
	private TextView windDeg;
	private TextView astronomy;
	
	private TextView hum;
	private ImageView imgView;

	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);


		String city = "(none)";
		cityText = (TextView) findViewById(R.id.cityText);
		condDescr = (TextView) findViewById(R.id.condDescr);
		temp = (TextView) findViewById(R.id.temp);
		hum = (TextView) findViewById(R.id.hum);
		press = (TextView) findViewById(R.id.press);
		windSpeed = (TextView) findViewById(R.id.windSpeed);
		windDeg = (TextView) findViewById(R.id.windDeg);
		imgView = (ImageView) findViewById(R.id.condIcon);
		astronomy = (TextView) findViewById(R.id.astronomyLab);


		Button simpleButton1;
		simpleButton1 = (Button) findViewById(R.id.simpleButton1);//get id of button 1

		simpleButton1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				//Toast.makeText(getApplicationContext(), "Simple Button 1", Toast.LENGTH_LONG).show();//display the text of button1
				JSONWeatherTask task = new JSONWeatherTask();
				task.execute(new String[]{"cityplaceholder"}); //cityplaceholder needs to be changed to a real city
			}
		});


		JSONWeatherTask task = new JSONWeatherTask();
		task.execute(new String[]{city});
	}

	@Override
	protected void onResume(){
		super.onResume();
		cityText.setText("Resumed");
        String city = "(none)";
        JSONWeatherTask task = new JSONWeatherTask();
        task.execute(new String[]{city});
	}




	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	
	private class JSONWeatherTask extends AsyncTask<String, Void, Weather> {

		@Override
		protected Weather doInBackground(String... params) {
			Weather weather = new Weather();
			String data = "";
			try {
				data = ( (new WeatherHttpClient()).getWeatherData(params[0]));
			}
			catch (Exception e) {
				e.printStackTrace();
			}

			Log.d("NickTag", "data= " + data);

			if (data != null){

				try {
					weather = JSONWeatherParser.getWeather(data);

					// Let's retrieve the icon
					//weather.iconData = ( (new WeatherHttpClient()).getImage(weather.currentCondition.getIcon()));

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}



			return weather;
		
	}
		
		
		
		
	@Override
		protected void onPostExecute(Weather weather) {			
			super.onPostExecute(weather);
			/*
			if (weather.iconData != null && weather.iconData.length > 0) {
				Bitmap img = BitmapFactory.decodeByteArray(weather.iconData, 0, weather.iconData.length); 
				imgView.setImageBitmap(img);
			}
			*/
			if (weather.location != null){


			//cityText.setText(weather.location.getCity() + "," + weather.location.getCountry());
		    //cityText.setText(weather.location.getCity() );
			String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
			cityText.setText("Miller Place  "  + currentDateTimeString);
			condDescr.setText(weather.currentCondition.getCondition() + "(" + weather.currentCondition.getDescr() + ")");
			//temp.setText("" + Math.round((weather.temperature.getTemp() - 273.15)) + "�C");
			temp.setText("" + Math.round((weather.temperature.getTemp() - 273.15) * 9/5 + 32) + "F");
			hum.setText("" + Math.round(weather.currentCondition.getHumidity()) + "%");
			press.setText("" + weather.currentCondition.getPressure() + " hPa");
			//windSpeed.setText("" + weather.wind.getSpeed() + " mps");
			windSpeed.setText("" + Math.round(weather.wind.getSpeed() * 2.236936) + " mph, ");
			windDeg.setText("" + Math.round(weather.wind.getDeg()) + " deg");
			//astronomy.setText(weather.currentCondition.getWeatherId() + " Excellent");
		    Integer weatheconditionid = weather.currentCondition.getWeatherId();
		if (weatheconditionid == 800)  {
			if (Math.round(weather.currentCondition.getHumidity()) <= 30) {
				astronomy.setText(" Excellent");
			}
			else {
				astronomy.setText(" Good (humid)");
			}
		} //Clear
			else if (weatheconditionid == 801) {astronomy.setText(" Mediocre");} //Clouds 11-25%
			else {astronomy.setText(" Forget it");} //Clouds, rain, fog etc
			}
			else{
				cityText.setText("Not Available  ");
				astronomy.setText("unknown");
				condDescr.setText("");
				temp.setText("");
				hum.setText("");
				windSpeed.setText("");
				windDeg.setText("");
			}
				
		}



	}



}
