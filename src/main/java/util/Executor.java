package util;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

public class Executor {
	private ArrayList<String> currentOutput = new ArrayList<String>();
	public void executeCommand(String command) {
		command = "cmd /c "+ command;
	    try {
	        returnOutput(command);
	        Process process = Runtime.getRuntime().exec(command);
	        logOutput(process.getInputStream(), "");
	        logOutput(process.getErrorStream(), "Error: ");
	        process.waitFor();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}

	public void logOutput(InputStream inputStream, String prefix) {
	    new Thread(() -> {
	        Scanner scanner = new Scanner(inputStream, "UTF-8");
	        while (scanner.hasNextLine()) {
	            synchronized (this) {
	                returnOutput(prefix + scanner.nextLine());
	            }
	        }
	        scanner.close();
	    }).start();
	}

	public synchronized void returnOutput(String message) {
	
		
	
	    currentOutput.add(message);
	}
	
	public ArrayList<String> getOutput() {
		return currentOutput;
	}
}
