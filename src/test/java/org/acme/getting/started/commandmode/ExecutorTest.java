package org.acme.getting.started.commandmode;

import static org.mockito.Mockito.*;
import org.eclipse.microprofile.context.ManagedExecutor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.fasterxml.jackson.databind.ObjectMapper;


@ExtendWith(MockitoExtension.class)
public class ExecutorTest {

	ManagedExecutor executor = ManagedExecutor.builder().build();

	@Mock
	ReportWriter writer;

	@Mock
	ObjectMapper mapper;

	@Mock
	HttpCaller caller;

	Executor executorService;

	private Req req;
	private Res res;

	@BeforeEach
	public void setUp() {
		req = new Req();
		res = new Res();

		executorService = new Executor(executor, writer, mapper, caller);
	}

	@Test
	public void testHandle() throws Exception {
		Long identifier = 1L;

		when(caller.consume(identifier, req)).thenReturn(res);
		when(mapper.writeValueAsString(req)).thenReturn("reqJson");
		when(mapper.writeValueAsString(res)).thenReturn("resJson");

		executorService.handle(identifier, req);

		verify(caller, times(1)).consume(identifier, req);
		verify(writer, times(1)).append(anyList());
	}
}