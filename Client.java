import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.ServerSocket;

public class Client implements Runnable {
    private static final String version = "P2P-CI/1.0";
	static int serverPort = 7734;
	static int portNumber = 5677;
	private Socket socket;
	ServerSocket serverSock;
	Methods m;

    public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Enter server's IP address: ");
		String serverIP = br.readLine().trim();
		Methods m = new Methods(serverIP, serverPort, portNumber);
		int rfcNum;
		String title;

		while (true) {
			System.out.println("Select an option:");
			System.out.println("1 - ADD");
			System.out.println("2 - LIST");
			System.out.println("3 - LOOKUP");
			// System.out.println("4 - EXIT");

			int userChoice = Integer.parseInt(br.readLine().trim());
			
			switch(userChoice){
				case 1: // Add RFC
					System.out.println("Enter RFC number: ");
					rfcNum = Integer.parseInt(br.readLine().trim());
					System.out.println("Enter RFC title: ");
					title = br.readLine().trim();
					m.addRFC(rfcNum, title);
					break;
				case 2: // List RFCs
					m.listRFC();
					break;
				case 3: // Lookup item
					System.out.println("Enter RFC number: ");
					rfcNum = Integer.parseInt(br.readLine().trim());
					System.out.println("Enter RFC title: ");
					title = br.readLine().trim();
					m.lookUpRFC(rfcNum, title);
					break;
				// case 4: // Exit
				// 	System.exit(0);
				default:
					continue;
			}
        }
	}

	@Override
	public void run() {
		try {
			serverSock = new ServerSocket(portNumber);
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		while (true) {
			try {
				socket = serverSock.accept();
				new Thread(this).start();
			} catch (IOException e) {
				try {
					socket.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String request; // change name
			String outputString;
			
			try {
				DataInputStream in = new DataInputStream(socket.getInputStream());
				DataOutputStream out = new DataOutputStream(socket.getOutputStream());
				request = in.readUTF();
				String req[] = request.split("\n");

				if(req[0].equals("GET")) {
					String rfcNumber = req[1];
					String fileName = rfcNumber + ".txt";
					File file = new File(fileName);
					if (!file.exists()) {
						outputString = version + " 404 Not Found";	
						out.writeUTF(outputString);
						out.close();
						in.close();
					}
					String response = version + " 200 OK \n"
						+ "Date: " + new java.util.Date() +"\n"
						+ "OS: " + System.getProperty("os.name") + " " + System.getProperty("os.version") + "\n"
						+ "Last-Modified: " + file.lastModified() + "\n"
						+ "Content-Length: " + file.length() + "\n"
						+ "Content-Type: Text/Text" + "\n";
					out.writeUTF(response);

					FileInputStream fileInput = new FileInputStream(fileName);

					in.close();
					out.close();
					socket.close();
					fileInput.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}