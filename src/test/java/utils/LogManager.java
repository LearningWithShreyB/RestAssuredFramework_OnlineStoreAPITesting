package utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LogManager {

	private static final String LOG_FILE_PATH = "logs/execution.log";

	// This method cleans and sets up the fresh file before any test runs
	public static void initLogs() {
		try {
			File logFile = new File(LOG_FILE_PATH);
			if (logFile.getParentFile() != null) {
				logFile.getParentFile().mkdirs(); // Safely makes the logs/ folder if missing
			}
			FileWriter fw = new FileWriter(logFile, false); // 'false' overwrites old logs
			fw.write("=== Test Execution Started at: " + getTimestamp() + " ===\n\n");
			fw.close();
		} catch (IOException e) {
			System.err.println("Could not initialize log file: " + e.getMessage());
		}
	}

	// Call this anywhere to print runtime details into the file
	public static void log(String message) {
		try (FileWriter fw = new FileWriter(LOG_FILE_PATH, true); PrintWriter pw = new PrintWriter(fw)) {
			pw.println("[" + getTimestamp() + "] " + message);
		} catch (IOException e) {
			System.err.println("Failed to write to log file: " + e.getMessage());
		}
	}

	private static String getTimestamp() {
		return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
	}
}