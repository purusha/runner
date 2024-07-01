package org.acme.getting.started.commandmode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import io.quarkus.vertx.ConsumeEvent;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.core.eventbus.EventBus;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class Storage {
	
	@Inject
    EventBus bus; 
	
	private final List<Req> requests = new ArrayList<>();
	
	private final Map<Long, UUID> responses = new HashMap<>();
	
	public void add(Req req) {
		System.out.println("enqueue: " + req);
		requests.add(req);
		
		bus.<Long>requestAndAwait("run", req.getIdentifier());
	}
	
	@ConsumeEvent("run")          
	public Uni<Long> consume(Long id) {
	    final Req request = requests.stream()
    		.filter(req -> req.getIdentifier() == id)
    		.findFirst()
    		.get();
	    
	    try {
	    	Long sleep = (long) (1000 * (new Random().nextInt(10)));
	    	System.out.println("sleep for " + sleep + " msec for " + request);
	    	
			Thread.sleep(sleep);
		} catch (Exception e) {
			e.printStackTrace();
		}
	    
	    responses.put(id, UUID.randomUUID());
	    System.out.println("dequeue: " + id);
	    
	    return Uni.createFrom().item(id);
	}

}
