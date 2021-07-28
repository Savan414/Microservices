package model;
import java.math.BigInteger;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Scanner;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Engine {
	public static final double DEGREES_TO_RADIANS = Math.PI / 180.0;
	static final int DISTANCE = 12742; //in KM
	static final String GEOCODE_URL = "https://maps.googleapis.com/maps/api/geocode/";
	static final String RIDE_URL = "https://maps.googleapis.com/maps/api/distancematrix/";
	static final String API_KEY = "AIzaSyDhZOqiEirzeveI1iNojGnSg9nXAK5SZtQ";

	final double DRONE_SPEED = 150; //in KM/HR
	final long MIN_PER_HOUR = 60; //No. of mins in an Hour
	final double TRAFFIC_COST_PER_MIN = 0.5;
	final int HTTPSuccessCodeRange = 299;
	
	private static Engine instance = null;

	private Engine() {
	}

	public synchronized static Engine getInstance() {
		if (instance == null)
			instance = new Engine();
		return instance;
	}

	public String doPrime(String lowerBound, String upperBound) throws Exception {
		try {
			if (!(lowerBound.matches("(\\d+)") && upperBound.matches("(\\d+)"))) {
				throw new Exception("Invalid Entries!");
				
			} else {
				BigInteger lower = new BigInteger(lowerBound);
				BigInteger upper = new BigInteger(upperBound);
				BigInteger prime = lower.nextProbablePrime();

				if (prime.compareTo(upper) == -1 || prime.compareTo(upper) == 0) {

					return prime.toString();
				} else {
					throw new Exception("No primes in range!");

				}
			}

		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}

	}

	public int doGps(String lat1, String lon1, String lat2, String lon2) throws Exception {
		try 
		{
			double t1 = Double.parseDouble(lat1) * DEGREES_TO_RADIANS;
			double t2 = Double.parseDouble(lat2) * DEGREES_TO_RADIANS;
			double n1 = Double.parseDouble(lon1) * DEGREES_TO_RADIANS;
			double n2 = Double.parseDouble(lon2) * DEGREES_TO_RADIANS;

			// Y = cos(t1) * cos(t2)
			double y = Math.cos(t1) * Math.cos(t2);

			// X = sin^2((t2-t1)/2) + Y * sin^2((n2-n1)/2)
			double sint = Math.sin((t2 - t1) / 2);
			double sinn = Math.sin((n2 - n1) / 2);
			double x = Math.pow(sint, 2) + (y * Math.pow(sinn, 2));

			// Distance = 12742 * atan2[sqrt(x), sqrt(1 - x)]
			int distance = (int) (DISTANCE * Math.atan2(Math.sqrt(x), Math.sqrt(1 - x)));

			return distance;
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}

	}
	
	public double doDrone(String address1, String address2) throws Exception
	{
		String[] add1 = this.droneHelper(address1);
		String[] add2 = this.droneHelper(address2);
		
		int distance = this.doGps(add1[0], add1[1], add2[0], add2[1]);
		//System.out.println(distance);
		double result =   (distance / DRONE_SPEED) * MIN_PER_HOUR;
		return result;
	}
	
	private String[] droneHelper(String address) throws Exception 
	{
		try 
		{
			address.trim();
			String add = address.replace(" ", "+");

			URL url = new URL(GEOCODE_URL + "json?address=" + add + "&key=" + API_KEY);
			
			System.out.println(url);
			
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			
			if (connection.getResponseCode()>HTTPSuccessCodeRange)
			{
				throw new ConnectException ("fail to get coordinate of address " + address);
			}
			connection.connect();

			int responsecode = connection.getResponseCode();
			String result = "";
			
			if(responsecode != 200)
			{
				throw new RuntimeException("HttpResponseCode: " + responsecode);
			}	
			else
			{
				Scanner sc = new Scanner(url.openStream());
				while(sc.hasNext())
				{
					result += sc.nextLine();				
				}	
				sc.close();
			}
			connection.disconnect();
			//Parse the JSON data present in the string format
			
			//Type caste the parsed json data in json object
			JsonObject jsonobj = JsonParser.parseString(result).getAsJsonObject(); 
			//Store the JSON object in JSON array as objects (For level 1 array element i.e Results)
			JsonArray results = jsonobj.get("results").getAsJsonArray(); 
			
			JsonObject coordinates = results.get(0).getAsJsonObject().get("geometry").getAsJsonObject().get("location").getAsJsonObject();
			
			String lat = coordinates.get("lat").getAsString();
			String lng = coordinates.get("lng").getAsString();
			
			String[] output = {lat, lng};
			return output;
			
		} 
		catch (Exception e) 
		{
			throw new Exception(e.getMessage());
		}

	}
	
	public double doRide(String from, String dest) throws Exception 
	{
		try
		{
			String add1 = from.trim().replace(" ", "+");
			String add2 = dest.trim().replace(" ", "+");
			
			URL url = new URL(RIDE_URL + "json?origins=\"" + add1 + "\"&destinations=\"" + add2 + "\"&departure_time=now&key="+ API_KEY);	
		//	System.out.println(url);
			
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			
			if(connection.getResponseCode()>HTTPSuccessCodeRange)
			{
				throw new ConnectException("Fail to get distance matrix");
			}
			connection.connect();

			int responsecode = connection.getResponseCode();
			String result = "";
			
			if(responsecode != 200)
			{
				throw new RuntimeException("HttpResponseCode: " + responsecode);
			}	
			else
			{
				Scanner sc = new Scanner(url.openStream());
				while(sc.hasNext())
				{
					result += sc.nextLine();				
				}	
				sc.close();
			}
			connection.disconnect();
			//Parse the JSON data present in the string format
			JsonParser parse = new JsonParser();
			//Type caste the parsed json data in json object
			JsonObject jsonobj = parse.parse(result).getAsJsonObject(); 
			//Store the JSON object in JSON array as objects (For level 1 array element i.e Results)
			JsonArray rows = jsonobj.get("rows").getAsJsonArray(); 
	
			
			JsonArray travel_time = rows.get(0).getAsJsonObject().get("elements").getAsJsonArray();
			JsonObject traffic = travel_time.get(0).getAsJsonObject().get("duration_in_traffic").getAsJsonObject();
		
		
			String text = traffic.get("text").getAsString();
			String[] parts = text.split(" ");
			int time = Integer.parseInt(parts[0]);
			
			double ride_cost = time * TRAFFIC_COST_PER_MIN;
			//System.out.println(ride_cost);
			return ride_cost;
			
			
		}
		catch(Exception e)
		{
			throw new Exception(e.getMessage());
		}
			
	}
	public List<StudentBean> doSis(String prefix, String minGpa, String sortBy) throws Exception
	{
		try
		{
			return (StudentDAO.query(prefix, minGpa, sortBy));			
		}
		catch (Exception e)
		{
			throw new Exception(e);
		}
	}

	public static void main(String[] args) throws Exception 
	{
		
		Engine engine = Engine.getInstance();
	
		engine.doRide("4700 keele", "57 gunton");

	}
}
