package no.hvl.dat110.aciotdevice.client;

import java.io.IOException;

import com.google.gson.Gson;

import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RestClient {
	
	public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
	private static String logpath = "/accessdevice/log";
	private static String codepath = "/accessdevice/code";
	private OkHttpClient client;
	private HttpUrl baseUrl;

	public RestClient() {
		client = new OkHttpClient();
		baseUrl=new HttpUrl.Builder()
			.scheme(Configuration.proxy)
			.host(Configuration.host)
			.port(Configuration.port)
			.build();
	}

	public void doPostAccessEntry(String message) {

		// TODO: implement a HTTP POST on the service to post the message
		Gson gson = new Gson();
		AccessMessage msg = new AccessMessage(message);
		HttpUrl url = baseUrl.resolve(logpath);
		
		RequestBody body = RequestBody.create(JSON, gson.toJson(msg));
		Request request = new Request.Builder().url(url).put(body).build();
		
		System.out.println(request.toString());

		try (Response response = client.newCall(request).execute()) {
			System.out.println(response.body().string());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	
	public AccessCode doGetAccessCode() {
		
		// TODO: implement a HTTP GET on the service to get current access code
		Gson gson = new Gson();
		AccessCode code = null;
		HttpUrl url = baseUrl.resolve(codepath);
		
		Request request = new Request.Builder()
				.url(url)
				.get()
				.build();
		System.out.println(request.toString());
	
		try (Response response = client.newCall(request).execute()) {
			if(response.isSuccessful()) {
				code = gson.fromJson(response.body().string(), AccessCode.class);
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		return code;
	}
}
