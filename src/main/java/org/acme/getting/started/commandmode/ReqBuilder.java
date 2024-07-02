package org.acme.getting.started.commandmode;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

import static org.apache.commons.lang3.StringUtils.*;

import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import jakarta.validation.Validator;

@Dependent
public class ReqBuilder {
	
	public static final String PREFIX = "---";
	private static final String HEADERS = "HEADERS:";
	private static final String BODY = "BODY:";
	private static final String METHOD = "METHOD:";
	private static final String URL = "URL:";
	private static final String _HEADERS = "_HEADERS:";
	private static final String _BODY = "_BODY:";
	private static final String _STAUSCODE = "_STAUSCODE:";
	
    private AtomicLong sequence = new AtomicLong(0);
    
    private Req request;	
	
	@Inject
	Executor executor;
	
	@Inject
    Validator validator;
	
	public void append(String line) {
		if (startsWith(line, PREFIX)) {
			
			if (Objects.nonNull(request)) {
				
				if (isValid(request)) {
					executor.handle(sequence.incrementAndGet(), request);	
				} else {
					System.out.println("cannot execute actions because is not valid: " + request);
				}				
				
			}			
			
			request = new Req();			
			
		} else if (startsWith(line, HEADERS)) {
			request.setHeaders(trim(substringAfter(line, HEADERS)));
			
		} else if (startsWith(line, BODY)) {
			request.setBody(trim(substringAfter(line, BODY)));
			
		} else if (startsWith(line, METHOD)) {
			request.setMethod(trim(substringAfter(line, METHOD)));
			
		} else if (startsWith(line, URL)) {
			request.setUrl(trim(substringAfter(line, URL)));
			
		} else if (startsWith(line, _HEADERS)) {
			request.setExpectedHeaders(trim(substringAfter(line, _HEADERS)));
			
		} else if (startsWith(line, _BODY)) {
			request.setExpectedBody(trim(substringAfter(line, _BODY)));
			
		} else if (startsWith(line, _STAUSCODE)) {
			request.setExpectedStatuscode(trim(substringAfter(line, _STAUSCODE)));
			
		} else {
			
			throw new UnsupportedOperationException("cannot parse: " + line);
		}
	}

	private boolean isValid(Req request) {
		return validator.validate(request).isEmpty();
	}

}
