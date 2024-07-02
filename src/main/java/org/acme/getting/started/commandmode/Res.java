package org.acme.getting.started.commandmode;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.Data;

@Data
public class Res {
			
	private Map<String, List<String>> headers;
	
	private JsonNode body;
	
	private Integer statuscode;

}
