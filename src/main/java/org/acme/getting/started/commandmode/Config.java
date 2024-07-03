package org.acme.getting.started.commandmode;

import java.net.Socket;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509ExtendedTrustManager;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import jakarta.enterprise.inject.Produces;

public class Config {
	
	//To ignore both certificate path and hostname verifications
	private final static X509ExtendedTrustManager trustManager = new X509ExtendedTrustManager() {
	    @Override
	    public X509Certificate[] getAcceptedIssuers() {
	        return new X509Certificate[]{};
	    }

	    @Override
	    public void checkClientTrusted(X509Certificate[] chain, String authType) {
	    }

	    @Override
	    public void checkServerTrusted(X509Certificate[] chain, String authType) {
	    }

	    @Override
	    public void checkClientTrusted(X509Certificate[] chain, String authType, Socket socket) {
	    }

	    @Override
	    public void checkServerTrusted(X509Certificate[] chain, String authType, Socket socket) {
	    }

	    @Override
	    public void checkClientTrusted(X509Certificate[] chain, String authType, SSLEngine engine) {
	    }

	    @Override
	    public void checkServerTrusted(X509Certificate[] chain, String authType, SSLEngine engine) {
	    }
	};	

	//XXX sarebbe bello esportarlo con un metodo @Produces ... ma purtroppo non funziona
	public static SSLContext sslContext = null;
	
	static {
		try {
			sslContext = SSLContext.getInstance("TLS");			
			sslContext.init(null, new TrustManager[]{trustManager}, new SecureRandom());
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
	@Produces
	public ObjectMapper objectMapper() {
		return new ObjectMapper()
			.enable(SerializationFeature.INDENT_OUTPUT);
	}
	
}
