package org.acme.getting.started.commandmode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.eclipse.microprofile.context.ManagedExecutor;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class Storage {
	
	@Inject
	ManagedExecutor executor;
	
	private final List<Req> requests = new ArrayList<>();
	
	private final Map<Long, UUID> responses = new HashMap<>();
	
	public void add(Req req) {
		System.out.println("enqueue: " + req);
		requests.add(req);
		
		executor.submit(() -> consume(req.getIdentifier()));
	}
	
	private void consume(Long id) {
	    final Req request = requests.stream()
    		.filter(req -> req.getIdentifier() == id)
    		.findFirst()
    		.get();
	    
	    try {
	    	Long sleep = (long) (1000 * (new Random().nextInt(10)));
	    	System.out.println("sleep for " + sleep + " msec for " + request.getIdentifier());
	    	
			Thread.sleep(sleep);
		} catch (Exception e) {
			e.printStackTrace();
		}
	    
	    responses.put(id, UUID.randomUUID());
	    System.out.println("dequeue: " + id + " [" + Thread.currentThread() + "]");
	}

}
