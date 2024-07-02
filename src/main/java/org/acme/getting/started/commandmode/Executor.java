package org.acme.getting.started.commandmode;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

import org.eclipse.microprofile.context.ManagedExecutor;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class Executor {
	
	@Inject
	ManagedExecutor executor;
	
	@Inject
	ReportWriter writer;
	
	@Inject
	ObjectMapper mapper;
	
	private final List<Req> requests = new ArrayList<>();
	
	public void handle(Long identifier, Req req) {
		System.out.println("enqueue: " + req);
		requests.add(req);
		
		CompletableFuture
			.supplyAsync(
				() -> consume(identifier, req), executor
			)
			.thenAccept(res -> {
				writer.append(buildReport(identifier, req, res));
			});
	}

	private List<String> buildReport(Long identifier, Req req, Res res) {
		final List<String> report = new ArrayList<>();
		
		try {
			System.out.println("handled: " + identifier + " [" + Thread.currentThread() + "] ==> " + res);
			
			report.add(ReqBuilder.PREFIX);
			report.add("request");
			report.add(mapper.writeValueAsString(req));
			report.add("response");
			report.add(mapper.writeValueAsString(res));
			report.add(ReqBuilder.PREFIX);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return report;
	}
	
	private Res consume(Long identifier, Req req) {	    
	    try {
	    	Long sleep = (long) (1000 * (new Random().nextInt(10)));
	    	System.out.println("sleep for " + sleep + " msec for " + identifier);
	    	
			Thread.sleep(sleep);
		} catch (Exception e) {
			e.printStackTrace();
		}
	    
	    return new Res();
	}

}
