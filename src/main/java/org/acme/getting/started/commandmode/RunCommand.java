package org.acme.getting.started.commandmode;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command
public class RunCommand implements Runnable {

	@Option(names = { "-f", "--file" }, description = "execution file", required = true)
	String file;
	
	private final ReqBuilder builder;
	
	public RunCommand(ReqBuilder builder) {
		this.builder = builder;
	}

	@Override
	public void run() {
		try (Stream<String> stream = Files.lines(Paths.get(file))) {	
			
			stream.forEach(line -> builder.append(line));
			builder.append(ReqBuilder.PREFIX);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}