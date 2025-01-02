package org.acme.getting.started.commandmode;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.eclipse.microprofile.context.ManagedExecutor;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class Executor {
	
	private final ManagedExecutor executor;
	private final ReportWriter writer;
	private final ObjectMapper mapper;
	private final HttpCaller caller;

	@Inject
	public Executor(ManagedExecutor executor, ReportWriter writer, ObjectMapper mapper, HttpCaller caller) {
		this.executor = executor;
		this.writer = writer;
		this.mapper = mapper;
		this.caller = caller;
	}
	
	public void handle(Long identifier, Req req) {
		System.out.println("handle [" + identifier + "]");
		
		async(identifier, req, true);
	}
	
	public void skip(Long identifier, Req req) {
		System.out.println("skip [" + identifier + "]");
		
		async(identifier, req, false);
	}

	/**
	 * Executes an asynchronous operation based on the validity of the request.
	 * 
	 * @param identifier The unique identifier for the request.
	 * @param req The request object to be processed.
	 * @param isValid A boolean flag indicating whether the request is valid.
	 */
	private void async(Long identifier, Req req, boolean isValid) {
		CompletableFuture
			.supplyAsync(
				() -> isValid ? caller.consume(identifier, req) : notValidRes(identifier), 
				executor
			)
			.thenAccept(res -> {
				writer.append(report(identifier, req, res));
			});
	}
	
	private List<String> report(Long identifier, Req req, Res res) {
		final List<String> report = new ArrayList<>();
		
		try {
			report.add(ReqBuilder.PREFIX);
			report.add("Req[" + String.valueOf(identifier) + "]: " + mapper.writeValueAsString(req));
			report.add("Res[" + String.valueOf(identifier) + "]: " + mapper.writeValueAsString(res));
			report.add(ReqBuilder.PREFIX);
		} catch (Exception e) {
			System.out.println("error report [" + identifier + "]: " + e.getMessage());
		}
		
		return report;
	}
	
	private Res notValidRes(Long identifier) {	    
		final Res res = new Res();
	    //res.setBody("not valid");
	    
	    return res;		
	}
	
}
