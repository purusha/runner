package org.acme.getting.started.commandmode;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import jakarta.enterprise.inject.Produces;

public class Config {

	@Produces
	public ObjectMapper objectMapper() {
		return new ObjectMapper()
			.enable(SerializationFeature.INDENT_OUTPUT);
	}
}
