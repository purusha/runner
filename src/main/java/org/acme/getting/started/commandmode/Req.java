package org.acme.getting.started.commandmode;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class Req {
		
	//XXX formato accettabile: content-type=application/json, vary=Host, vary=origin
	private String headers;
	
	//XXX obbligatorio se method == POST or PUT
	private String body;
	
	//XXX valori accettabili ... GET, POST, PUT and DELETE (upper o lowercase)
	@NotBlank
	private String method;
	
	//XXX deve sempre iniziare con http:// or https://
	@NotBlank
	private String url;
	
	//XXX formato accettabile: content-type=application/json, vary=Host, vary=origin
	private String expectedHeaders;
	
	//se json ... deve essere confrontato come JsonNode
	private String expectedBody;
	
	//XXX valori accettabili: https://en.wikipedia.org/wiki/List_of_HTTP_status_codes
	private String expectedStatuscode;

}
