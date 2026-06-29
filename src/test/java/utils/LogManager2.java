package utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import io.restassured.config.LogConfig;
import io.restassured.config.RestAssuredConfig;
import java.io.PrintStream;
import java.io.FileOutputStream;

public class LogManager2 {

	private static final String LOG_FILE_PATH = "logs/execution.log";
	private static final String HEADERS_LOG_PATH = "logs/executionHeaders.log";

	public static void initLogs() {
		// Initializes and wipes both files cleanly
		clearAndInitFile(LOG_FILE_PATH, "=== Test Execution Started ===");
		clearAndInitFile(HEADERS_LOG_PATH, "=== API Traffic Log Started ===");
	}

	private static void clearAndInitFile(String filePath, String headerMessage) {
		try {
			File logFile = new File(filePath);
			if (logFile.getParentFile() != null) {
				logFile.getParentFile().mkdirs();
			}
			try (FileWriter fw = new FileWriter(logFile, false)) { // false overwrites old logs
				fw.write(headerMessage + " at: " + getTimestamp() + "\n\n");
			}
		} catch (IOException e) {
			System.err.println("Could not initialize log file [" + filePath + "]: " + e.getMessage());
		}
	}

	public static void log(String message) {
		try (FileWriter fw = new FileWriter(LOG_FILE_PATH, true); PrintWriter pw = new PrintWriter(fw)) {
			pw.println("[" + getTimestamp() + "] " + message);
		} catch (IOException e) {
			System.err.println("Failed to write to log file: " + e.getMessage());
		}
	}

	public static RestAssuredConfig getRestAssuredHeaderLogConfig() {
		try {
			// Appends Rest Assured technical logs directly into executionHeaders.log
			PrintStream fileOut = new PrintStream(new FileOutputStream(HEADERS_LOG_PATH, true));
			return RestAssuredConfig.config().logConfig(LogConfig.logConfig().defaultStream(fileOut));
		} catch (Exception e) {
			System.err.println("Failed to setup RestAssured log redirection: " + e.getMessage());
			return RestAssuredConfig.config();
		}
	}

	private static String getTimestamp() {
		return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
	}

	// 🟢 NEW: Add this method to your LogManager class
	public static void writeToHeadersLog(String logMessage) {
		try (FileWriter fw = new FileWriter(HEADERS_LOG_PATH, true); PrintWriter pw = new PrintWriter(fw)) {
			pw.println(logMessage);
		} catch (IOException e) {
			System.err.println("Failed to write to executionHeaders log file: " + e.getMessage());
		}
	}
}