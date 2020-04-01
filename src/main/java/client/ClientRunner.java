package client;

import java.util.ArrayList;

import org.jibble.pircbot.User;

public class ClientRunner {

	public static String getHostName() {
		if (System.getProperty("os.name").startsWith("Windows")) {
		    
		    return System.getenv("COMPUTERNAME");
		} else {
		  
		    String hostname = System.getenv("HOSTNAME");
		    if (hostname != null) {
		       return hostname;
		    } else {
		    	return "UnknownHost";
		    }
		}
		
	}
	
	
	public static void main(String[] args) throws Exception {

		String channel = "#r3xyz12399";
		Client client = new Client("DefaultBot"+ (int)Math.random() + (int)Math.random());

		client.setVerbose(true);

		client.connect("fiery.ca.us.swiftirc.net");

		client.joinChannel(channel);

		ArrayList<String> users = new ArrayList<String>();
		
		
		User[] current = client.getUsers(channel).clone();
		
		String hostname = System.getProperty("user.name");
		int count = 0;
		for(User i: current) {
			if(hostname.equals(i.getNick())) {
				count++;
				hostname+=count +"";
			}
		}
	
		
		client.changeNick(hostname);
		
		
	}

}
