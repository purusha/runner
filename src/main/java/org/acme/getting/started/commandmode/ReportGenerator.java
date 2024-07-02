package org.acme.getting.started.commandmode;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.inject.Singleton;

@Singleton
public class ReportGenerator {

	private Path path;

	public ReportGenerator() {
		try {
			final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			final File file = new File(dateFormat.format(new Date()) + ".txt");
			file.createNewFile();
			
			path = file.toPath();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public synchronized void append(List<String> report) {
		try {
			Files.write(
				path, 
				report.stream().collect(Collectors.joining("\n")).getBytes(), 
				StandardOpenOption.APPEND
			);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
