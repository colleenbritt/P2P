/**
 * Custom list to avoid getting ConcurrentModificationException.
 */
public class PeerLinkedList {
    private int size;
    private Node head;
    private Node tail;
	
    private class Node {
        Peer peer;
        Node next;

        public Node(Peer peer) {
            this.peer = peer;
        }

        public Node(Peer peer, Node next) {
            this.peer = peer;
            this.next = next;
        }
    }
    
    public PeerLinkedList() {
        size = 0;
        head = null;
        tail = null;
    }
    
    private Node getNode(int index) {
        Node p = head;
        for (int i = 0; i < index; ++i) 
            p = p.next;
        return p;
    }
    
    public Peer get(int index) {
        if (index < 0 || index >= size) 
            return null;
        return getNode(index).peer;
    }
    
    public void addAtHead(Peer peer) {
        Node node = new Node(peer, head);
        if (size == 0) {
            head = node;
            tail = node;
        } else 
            head = node;
        ++size;
    }
    
    public void deleteAtIndex(int index) {
        if (index < 0 || index >= size) 
            return;
        if (size == 1) {
            head = null;
            tail = null;
        }
        else if (index == 0) {
            head = head.next;
        }
        else {
           Node p = getNode(index - 1);
           p.next = p.next.next;
           if (index == size-1) tail = p; 
        }
        --size;
    }

    public void deletePeer(String hostName) {
        Node temp = head;
        Node previous = head;

        while (temp.next != null) {
            if (temp.peer.getHostName().equals(hostName)) {
                previous.next = temp.next;
                temp = temp.next;
            } else {
                previous =  temp;
                temp = temp.next;
            }
        }
    }

    public boolean findPeer(Peer peer) {
        Node temp = head;
        Node previous = head;

        while (temp.next != null) {
            if (temp.peer.clientIP.equals(peer.clientIP)) {
                return true;
            } else {
                previous =  temp;
                temp = temp.next;
            }
        }
        return false;
    }

    public int size() {
        return size;
    }
}
