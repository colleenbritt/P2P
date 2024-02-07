public class Peer {
    String hostName;
	String clientIP;
    int clientPort;
    
	public Peer(String hostName, String clientIP, int clientPort){		
		this.hostName = hostName;
        this.clientIP = clientIP;
        this.clientPort = clientPort;
	}
    
    public String getHostName() {
        return this.hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getClientIP() {
        return this.clientIP;
    }

    public void setClientIP(String clientIP) {
        this.clientIP = clientIP;
    }

    public int getClientPort() {
        return this.clientPort;
    }

    public void setClientPort(int clientPort) {
        this.clientPort = clientPort;
    }
}
