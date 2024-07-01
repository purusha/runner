package org.acme.getting.started.commandmode;

import java.util.Objects;

import org.apache.commons.lang3.StringUtils;

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
	
	Req request;
	
	@Inject
	ReqExecutors executors;
	
	@Inject
    Validator validator;
	
	public void append(String line) {
		if (StringUtils.startsWith(line, PREFIX)) {
			
			if (Objects.nonNull(request)) {
				
				if (isValid(request)) {
					executors.append(request);	
				} else {
					System.out.println("cannot execute actions because is not valid: " + request);
				}				
				
			}			
			
			request = new Req();			
			
		} else if (StringUtils.startsWith(line, HEADERS)) {
			request.setHeaders(StringUtils.substringAfter(line, HEADERS));
			
		} else if (StringUtils.startsWith(line, BODY)) {
			request.setBody(StringUtils.substringAfter(line, BODY));
			
		} else if (StringUtils.startsWith(line, METHOD)) {
			request.setMethod(StringUtils.substringAfter(line, METHOD));
			
		} else if (StringUtils.startsWith(line, URL)) {
			request.setUrl(StringUtils.substringAfter(line, URL));
			
		} else if (StringUtils.startsWith(line, _HEADERS)) {
			request.setExpectedHeaders(StringUtils.substringAfter(line, _HEADERS));
			
		} else if (StringUtils.startsWith(line, _BODY)) {
			request.setExpectedBody(StringUtils.substringAfter(line, _BODY));
			
		} else if (StringUtils.startsWith(line, _STAUSCODE)) {
			request.setExpectedStatuscode(StringUtils.substringAfter(line, _STAUSCODE));
			
		} else {
			
			throw new UnsupportedOperationException("cannot parse: " + line);
		}
	}

	private boolean isValid(Req request) {
		return validator.validate(request).isEmpty();
	}

}
