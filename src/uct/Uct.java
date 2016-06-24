package uct;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Uct {
	public static void main(String[] args) throws Exception {
		// connection setup  copy pasta from http://archive.oreilly.com/pub/h/1966
        // The server to connect to and our details.
        String server = "127.0.0.1";
        String nick = "CamelCase";
        String login = "HaHaHa";

        // The channel which the bot will join.
        String channel = "#irchacks";
        
        // Connect directly to the IRC server.
        Socket socket = new Socket(server, 6667);
        BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(socket.getOutputStream( )));
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(socket.getInputStream( )));
        
        // Log on to the server.
        writer.write("NICK " + nick + "\r\n");
        writer.write("USER " + login + " 8 * : Java IRC Hacks Bot\r\n");
        writer.flush( );
        
        // Read lines from the server until it tells us we have connected.
        String line = null;

       
        while ((line = reader.readLine( )) != null) {
            if ((line.indexOf("You have logged in")) >= 0) {
                // We are now logged in.
                break;
            }
            else if (line.indexOf("433") >= 0) {
                System.out.println("Nickname is already in use.");
                return;
            }
        }
        
        // Join the channel.
        writer.write("JOIN " + channel + "\r\n");
        writer.flush( );
        
        // Keep reading lines from the server.
        Monitor listener = new Monitor(reader, writer);
        String room = null;
        try{
        	new SimpleGUI();
        	Scanner in = new Scanner(System.in);
        	while(listener.isAlive()){
        		//TODO look for input from user
        		String input = in.nextLine();
        		//have a command
        		if ( input.startsWith("/")){
        			writer.write(input.substring(1) + "\r\n");
        			writer.flush( );
        		}       		
        		else{
        			writer.write("/PRIVMSG "+ ':' + input);
        		}
        		
        		
        		//System.out.println("In main() you typed" + input);
        		//Thread.sleep(60000);
        	}
        }
        catch(Exception e ){
        	//TODO shit happened bail out
        }
    }

}

// should be monitoring the connection for messages from server
class Monitor extends Thread{
	private BufferedReader reader;
	private BufferedWriter writer;
	
	//constructor goes here
	Monitor(BufferedReader reader, BufferedWriter writer){
		super("Listening to server");
		this.reader = reader;
		this.writer = writer;
		start();
	}
	
	private ArrayList<String> parseData(String name){
		ArrayList<String> rValue = new ArrayList<String>();

	    String re1="((?:[a-z][a-z0-9_]*))";	// Variable Name 1
	    String re2="(!)";	// Any Single Character 1
	    String re3="((?:[a-z][a-z0-9_]*))";	// Variable Name 2
	    String re4="(@)";	// Any Single Character 2
	    String re5="((?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?))(?![\\d])";	// IPv4 IP Address 1
	    String re6="( )";	// Any Single Character 3
	    String re7="(.*)";	// non-greedy match on filler

	    Pattern p = Pattern.compile(re1+re2+re3+re4+re5+re6+re7,Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
	    Matcher m = p.matcher(name);
	    if (m.find()){
	        rValue.add(m.group(1)); // NICKNAME 
	        rValue.add(m.group(3)); // USERNAME
	        rValue.add(m.group(7)); // COMMAND
	        
	    }
		return rValue;
		
	}
	
	
	public void run(){
		try{
			// keep looping looking for input
			String line = null;
			while ((line = reader.readLine( )) != null) {
	            if (line.startsWith("PING")) {
	                // We must respond to PINGs to avoid being disconnected.
	                writer.write("PONG " + line.substring(5) + "\r\n");
	                //writer.write("PRIVMSG " + channel + " :I got pinged!\r\n");
	                writer.flush( );
	            }
	            else{
	                // Print the raw line received by the bot.
	            	ArrayList<String> msgData = parseData(line);
	            	if (msgData.isEmpty()){
	            		System.out.println(line);
	            		continue;
	            	}
	                System.out.println('<' + msgData.get(0) + "> : " + msgData.get(2) );
	            }
	        }
		}
		catch(Exception e){
			//TODO some shit happened bail out
		}
	}
}


class SimpleGUI extends Frame implements ActionListener{
	//objects in GUI need to be declared here
	private TextArea userList;
	private TextArea messageList;
	private TextArea infoList;
	private TextField userInput;
	
	
	
	
	public SimpleGUI(){
		//TODO implement constructor
		//TODO make something with .addActionListener(this)
		//will be object to create ActionEvent i.e. call actionPreformed()
		setLayout(new FlowLayout());
		
		userList = new TextArea();
		userList.setText("test");
		add(userList);
		setVisible(true);
	}
	
	// methods here
	
	// main
//	public static void main(String args[]){
//		//create instance of GUI
//		new SimpleGUI();
//	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO what happens when user interacts
		
	}
}