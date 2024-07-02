package org.acme.getting.started.commandmode;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpRequest.Builder;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

import jakarta.inject.Singleton;

@Singleton
public class HttpCaller {
	
	private HttpClient httpClient;

	public HttpCaller() {
		httpClient = HttpClient.newBuilder().build();
	}

//	public Res consume(Long identifier, Req req) {	    
//	    try {
//	    	Long sleep = (long) (1000 * (new Random().nextInt(10)));
//	    	System.out.println("sleep for " + sleep + " msec for " + identifier);
//	    	
//			Thread.sleep(sleep);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	    
//	    final Res res = new Res();
//	    res.setBody(String.valueOf(identifier));
//	    
//	    return res;
//	}
	
	public Res consume(Long identifier, Req req) {	 
		final Res res = new Res();
		final Builder builder = HttpRequest.newBuilder(URI.create(req.getUrl()));
		
		switch(req.getMethod()) {
			case "GET": {
				builder.GET();
			}break;
			case "POST": {
				builder.POST(BodyPublishers.ofString(req.getBody()));
			}break;
			case "PUT": {
				builder.PUT(BodyPublishers.ofString(req.getBody()));
			}break;
			case "DELETE": {
				builder.DELETE();
			}break;
		}
		
		final HttpRequest httpRequest = builder.build();
		
		try {
			final HttpResponse<String> response = httpClient.send(httpRequest, BodyHandlers.ofString());
			
			res.setBody(response.body());
			res.setStatuscode(response.statusCode());
			res.setHeaders(response.headers().map());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return res;
	}
}
