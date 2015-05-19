package com.example.practicaltest02;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

//import org.w3c.dom.Document;


//import android.renderscript.Element;
//import ro.pub.cs.systems.pdsd.practicaltest02.general.Constants;
//import ro.pub.cs.systems.pdsd.practicaltest02.general.Utilities;
//import ro.pub.cs.systems.pdsd.practicaltest02.model.WeatherForecastInformation;
import android.util.Log;

public class CommunicationThread extends Thread {
	
	private ServerThread serverThread;
	private Socket       socket;
	
	public CommunicationThread(ServerThread serverThread, Socket socket) {
		this.serverThread = serverThread;
		this.socket       = socket;
	}
	
	@Override
	public void run() {
		if (socket != null) {
			try {
				BufferedReader bufferedReader = Utilities.getReader(socket);
				PrintWriter    printWriter    = Utilities.getWriter(socket);
				if (bufferedReader != null && printWriter != null) {
					//Log.i(Constants.TAG, "[COMMUNICATION THREAD] Waiting for parameters from client (city / information type)!");
					
					String response_string = bufferedReader.readLine();
					
					//String informationType = bufferedReader.readLine();
					
					String data = serverThread.getData();
					
					//WeatherForecastInformation weatherForecastInformation = null;
					String answer = null;
					
					if (response_string != null && !response_string.isEmpty() ) {
						if (data == response_string) {
							//Log.i(Constants.TAG, "[COMMUNICATION THREAD] Getting the information from the cache...");
							//weatherForecastInformation = data.get(city);
							answer  = data;
							
						} else {
							//Log.i(Constants.TAG, "[COMMUNICATION THREAD] Getting the information from the webservice...");
							HttpClient httpClient = new DefaultHttpClient();
							HttpPost httpPost = new HttpPost("hhtp://autocomplete.wunderground.com/aq?query=" + response_string);
							
							List<NameValuePair> params = new ArrayList<NameValuePair>();        
							params.add(new BasicNameValuePair("querry", response_string));
							
							UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(params, HTTP.UTF_8);
							httpPost.setEntity(urlEncodedFormEntity);
							ResponseHandler<String> responseHandler = new BasicResponseHandler();
							String pageSourceCode = httpClient.execute(httpPost, responseHandler);
							
							if (pageSourceCode != null) {
								
								Document document = Jsoup.parse(pageSourceCode);
								Element element = document.child(0);
								Elements scripts = element.getElementsByTag("scipt");
								for (Element script: scripts) {
									
									String scriptData = script.data();
									
									if (scriptData.contains("name")) {
										int position = scriptData.indexOf("name") + "name".length();
										scriptData = scriptData.substring(position);
										
										JSONObject content = new JSONObject(scriptData);
										
										JSONObject currentObservation = content.getJSONObject("current_observation");
										
										//String temperature = currentObservation.getString(Constants.TEMPERATURE);
										//String windSpeed = currentObservation.getString(Constants.WIND_SPEED);
										//String condition = currentObservation.getString(Constants.CONDITION);
										//String pressure = currentObservation.getString(Constants.PRESSURE);
										//String humidity = currentObservation.getString(Constants.HUMIDITY);
										answer = currentObservation.getString("name");
										
										

										serverThread.setData(answer);
										
										break;
									}
								}
							} else {
								//Log.e(Constants.TAG, "[COMMUNICATION THREAD] Error getting the information from the webservice!");
							}
						}
						
						if (answer != null) {
							String result = null;
							result = answer;
							
							printWriter.println(result);
							printWriter.flush();
						} else {
							//Log.e(Constants.TAG, "[COMMUNICATION THREAD] Weather Forecast information is null!");
						}
						
					} else {
						//Log.e(Constants.TAG, "[COMMUNICATION THREAD] Error receiving parameters from client (city / information type)!");
					}
				} else {
					//Log.e(Constants.TAG, "[COMMUNICATION THREAD] BufferedReader / PrintWriter are null!");
				}
				socket.close();
			} catch (IOException ioException) {
				//Log.e(Constants.TAG, "[COMMUNICATION THREAD] An exception has occurred: " + ioException.getMessage());
				//if (Constants.DEBUG) {
				//	ioException.printStackTrace();
				//}
			} catch (JSONException jsonException) {
								
			}
		} else {
			
		}
	}

}
