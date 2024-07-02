package org.acme.getting.started.commandmode;

import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class Res {
			
	private Map<String, List<String>> headers;
	
	private String body;
	
	private int statuscode;

}
