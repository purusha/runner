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
	
	@Inject
	ManagedExecutor executor;
	
	@Inject
	ReportWriter writer;
	
	@Inject
	ObjectMapper mapper;
	
	@Inject
	HttpCaller caller;
	
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
			report.add("Req: " + mapper.writeValueAsString(req));
			report.add("Res: " + mapper.writeValueAsString(res));
			report.add(ReqBuilder.PREFIX);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return report;
	}
	
	private Res notValidRes(Long identifier) {	    
		final Res res = new Res();
	    res.setBody(String.valueOf(identifier) + " must not be executed because request is not correct");
	    
	    return res;		
	}
	
}
