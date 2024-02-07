import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

public class Server implements Runnable {

    private static final String version = "P2P-CI/1.0";
    private static int portNumber = 7734;
	public ServerSocket serverSocket;
    public RFCLinkedList rList;
    public PeerLinkedList pList;

    public Server(int portNum) throws IOException {
		serverSocket = new ServerSocket(portNum);
		System.out.println("Starting server");
      	System.out.println("Host: "+ InetAddress.getLocalHost().getHostAddress());
        System.out.println("Port: " + serverSocket.getLocalPort());
		System.out.println("OS: " + System.getProperty("os.name"));
		System.out.println("Version: " + version);
        rList = new RFCLinkedList();
        pList = new PeerLinkedList();
		new Thread(this).start();
	}

    public static void main(String[] args) throws IOException {
        new Server(portNumber);
    }

    @Override
    public void run() {
        Socket socket = null;
        String hostName = null;
	    String clientIP = null;
        int clientPort = 0;
        
        
        try {
            socket = serverSocket.accept();
            String ip = socket.getRemoteSocketAddress().toString();
            String request;
            int rfcNumber;
            String title;
            DataInputStream input = new DataInputStream(socket.getInputStream());
            DataOutputStream output = new DataOutputStream(socket.getOutputStream());
            
            new Thread(this).start();
            
            clientPort = socket.getPort();
	        clientIP = socket.getInetAddress().toString();
        
            while (true) { 
                try {
                    request = input.readUTF();

                    // Split the string into an List to get the information easier
                    ArrayList<String> array = new ArrayList<>(Arrays.asList(request.split(" |\n")));
                    int index = array.indexOf("PORT:") + 1;
                    int port = Integer.parseInt(array.get(index));

                    hostName = ip.substring(1, ip.indexOf(":"));
                    String outputString;
                    switch(array.get(0)){
                        case "ADD":{

                            // Make sure peer isn't added more than once
                            Peer peer = new Peer(hostName, clientIP, clientPort);
                            if (pList.size() == 0) {
                                pList.addAtHead(peer);
                            }
                            if (!pList.findPeer(peer)) {
                                pList.addAtHead(peer);
                            }
                            
                            int idx = array.indexOf("TITLE:") + 1;
                            title = array.get(idx);
                            idx = array.indexOf("RFC") + 1;
                            rfcNumber = Integer.parseInt(array.get(idx));
                            
                            // If the list is empty, add it.
                            // Otherwise, check if it is in the list and add it if it doesn't exist.
                            if (rList.size() == 0) {
                                RFC r = new RFC(rfcNumber, title, hostName);
                                rList.addAtHead(r);
                            } else {
                                for (int i = 0; i < rList.size(); i++) {
                                    if (i == rList.size() - 1 && rList.get(i).hostName != hostName && rList.get(i).rfcNumber != rfcNumber) {
                                        RFC r = new RFC(rfcNumber, title, hostName);
                                        rList.addAtHead(r);
                                        break;
                                    }
                                }
                            }

                            outputString = version + " 200 OK\nRFC " 
                                + rfcNumber + " " 
                                + title + " " 
                                + hostName + " " 
                                + port;
                            output.writeUTF(outputString);
                            break;
                        }
                        case "LIST":{
                            outputString = version + " 200 OK\n";

                            for (int i = 0; i < rList.size(); i++) {
                                outputString = outputString + "\nRFC " 
                                    + rList.get(i).rfcNumber + " " 
                                    + rList.get(i).title + " " 
                                    + rList.get(i).hostName;
                            }

                            output.writeUTF(outputString);
                            break;
                        }
                        case "LOOKUP":{
                            int idx = array.indexOf("TITLE:") + 1;
                            title = array.get(idx);
                            idx = array.indexOf("RFC") + 1;
                            rfcNumber = Integer.parseInt(array.get(idx));
                            boolean exists = true;

                            if (rList.size() == 0) {
                                outputString = version + " 404 Not Found";
                                output.writeUTF(outputString);
                                break;
                            }

                            
                            // Check if the RFC is in the list, send an error if it doesn't exist.
                            for (int i = 0; i < rList.size(); i++) {
                                if (rList.size() == 1) {
                                    if (rList.get(0).getRFCNumber() == rfcNumber && rList.get(0).getTitle().equals(title)) {
                                        exists = true;
                                        break;
                                    }
                                }
                                if (i == rList.size() - 1 && ((rList.get(i).getRFCNumber() != rfcNumber &&  !rList.get(i).getTitle().equals(title))
                                    || (rList.get(i).getRFCNumber() != rfcNumber &&  rList.get(i).getTitle().equals(title))) 
                                    || (rList.get(i).getRFCNumber() == rfcNumber &&  !rList.get(i).getTitle().equals(title))) {
                                    outputString = version + " 404 Not Found";
                                    output.writeUTF(outputString);
                                    exists = false;
                                    break;
                                }
                            }
                            
                            // If the RFC exists, check if any peers have it.
                            // if (exists) {
                            //     for (int i = 0; i < pList.size(); i++) {
                            //         if (i == pList.size() - 1 && !pList.get(i).getHostName().equals(hostName)){
                            //             outputString = version + " 404 Not Found";
                            //             output.writeUTF(outputString);
                            //             System.out.println("sfads");
                            //             exists = false;
                            //         }
                            //     }
                            // }

                            if (exists) {
                                outputString = version + " 200 OK\nRFC " 
                                + rfcNumber + " " 
                                + title + " " 
                                + hostName + " " 
                                + port;
                                output.writeUTF(outputString);
                            }

                            break;
                        }
                        case "default":{
                            break;
                        }
                    }
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            pList.deletePeer(hostName);
            rList.deleteRFC(hostName);
            try {
                socket.close();
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
    }
}