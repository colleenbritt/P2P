public class RFC {
    public int rfcNumber;
    public String title;
    public String hostName;
        
    public RFC(int rfcNumber, String title, String hostName){	
        this.rfcNumber = rfcNumber;
        this.title = title;
        this.hostName = hostName;		
    }

    public int getRFCNumber() {
        return rfcNumber;
    }

    public void setRFCNumber(int rfcNumber) {
        this.rfcNumber = rfcNumber;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }
}
