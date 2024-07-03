package org.acme.getting.started.commandmode;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpRequest.Builder;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
public class HttpCaller {
	
	private HttpClient httpClient;

	@Inject
	ObjectMapper mapper;
	
	public HttpCaller() {
		httpClient = HttpClient.newBuilder().sslContext(Config.sslContext).build();
	}
	
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
		
		try {
			
			final HttpResponse<String> response = httpClient.send(builder.build(), BodyHandlers.ofString());
			
			res.setBody(mapper.readTree(response.body()));
			res.setStatuscode(response.statusCode());
			res.setHeaders(response.headers().map());
			
		} catch (Exception e) {
			System.out.println("error calling [" + identifier + "]: " + e.getMessage());
		}
		
		return res;
	}
}
