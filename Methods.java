import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class Methods {
    Socket socket = null;
	DataInputStream input = null;
	DataOutputStream output = null;
	String version = "P2P-CI/1.0";
	int portNumber;
	String serverIP;
	String reply;
	String outputString;
	
    public Methods(String serverIP, int serverPort, int portNumber) throws IOException{
		this.socket = new Socket(serverIP, serverPort);
		System.out.println("Starting the client.");
        System.out.println("Host: " + InetAddress.getLocalHost().getHostAddress());
        System.out.println("Port: " + this.socket.getLocalPort() + "\n");
		this.input = new DataInputStream(this.socket.getInputStream());
		this.output = new DataOutputStream(this.socket.getOutputStream());
		this.portNumber = portNumber;
		this.serverIP = serverIP;
	}

    public void addRFC(int rfcNumber, String title) throws IOException{
		outputString = "ADD RFC " 
			+ rfcNumber 
			+ " " + version 
			+ "\nHOST: " + InetAddress.getByName(serverIP).getHostName()
			+ "\nPORT: " +  portNumber 
			+ "\nTITLE: " + title;
		output.writeUTF(outputString);
		reply = input.readUTF();
		System.out.println("\nReply from the server:\n" + reply + "\n");
	}
	
	public void listRFC() throws IOException{
		outputString = "LIST ALL RFC " 
			+ version 
			+ "\nHOST: " + InetAddress.getByName(serverIP).getHostName() 
			+ "\nPORT: " + portNumber;
		output.writeUTF(outputString);
		reply = input.readUTF();
		System.out.println("\nReply from server:\n" + reply + "\n");
	}
	
	public void lookUpRFC(int rfcNumber, String title) throws IOException{
		outputString = "LOOKUP RFC " 
			+ rfcNumber + " " 
			+ version 
			+ "\nHOST: " + InetAddress.getByName(serverIP).getHostName() 
			+ "\nPORT: " + portNumber 
			+ "\nTITLE: " + title;
		output.writeUTF(outputString);
		reply = input.readUTF();
		System.out.println("\nReply from server:\n" + reply + "\n");
	}
}