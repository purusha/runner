package org.acme.getting.started.commandmode;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class Req {
	
	//rappresenta l'identificativo della singola richiesta nel file
	private final Long identifier;
	
	private String headers;
	
	//XXX obbligatorio se method == POST or PUT
	private String body;
	
	//XXX valori accettabili ... GET, POST, PUT and DELETE
	@NotBlank
	private String method;
	
	@NotBlank
	private String url;
	
	private String expectedHeaders;
	
	private String expectedBody;
	
	private String expectedStatuscode;

}
