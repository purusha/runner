package org.acme.getting.started.commandmode;

import java.util.ArrayList;
import java.util.List;

import jakarta.enterprise.context.Dependent;

@Dependent
public class ReqExecutors {
	
	private final List<Req> requests = new ArrayList<>();
	
	public void append(Req req) {
		System.out.println("enqueue: " + req);
		requests.add(req);
	}

}
