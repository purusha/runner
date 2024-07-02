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
	
	public void handle(Long identifier, Req req) {
		System.out.println("handle: " + req);
		
		async(identifier, req, true);
	}
	
	public void skip(Long identifier, Req req) {
		System.out.println("skip: " + req);
		
		async(identifier, req, false);
	}

	private void async(Long identifier, Req req, boolean isValid) {
		CompletableFuture
			.supplyAsync(
				() -> isValid ? consume(identifier, req) : buildNotValid(identifier), 
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
			report.add("Req: " + mapper.writeValueAsString(req));
			report.add("Res: " + mapper.writeValueAsString(res));
			report.add(ReqBuilder.PREFIX);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return report;
	}
	
	private Res buildNotValid(Long identifier) {	    
		final Res res = new Res();
	    res.setBody(String.valueOf(identifier) + " must not be executed because the req is not correct");
	    
	    return res;		
	}
	
	private Res consume(Long identifier, Req req) {	    
	    try {
	    	Long sleep = (long) (1000 * (new Random().nextInt(10)));
	    	System.out.println("sleep for " + sleep + " msec for " + identifier);
	    	
			Thread.sleep(sleep);
		} catch (Exception e) {
			e.printStackTrace();
		}
	    
	    final Res res = new Res();
	    res.setBody(String.valueOf(identifier));
	    
	    return res;
	}

}
