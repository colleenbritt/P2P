/**
 * Custom list to avoid getting ConcurrentModificationException.
 */
public class RFCLinkedList {
    private int size;
    private Node head;
    private Node tail;
	
    private class Node {
        RFC rfc;
        Node next;

        public Node(RFC rfc) {
            this.rfc = rfc;
        }

        public Node(RFC rfc, Node next) {
            this.rfc = rfc;
            this.next = next;
        }
    }
    
    public RFCLinkedList() {
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
    
    public RFC get(int index) {
        if (index < 0 || index >= size) 
            return null;
        return getNode(index).rfc;
    }
    
    public void addAtHead(RFC rfc) {
        Node node = new Node(rfc, head);
        if (size == 0) {
            head = node;
            tail = node;
        } else 
            head = node;
        ++size;
    }
    
    public void deleteAtIndex(int index) {
        if (index < 0 || index >= size) return;
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

    public void deleteRFC(String hostName) {
        Node temp = head;
        Node previous = head;

        while (temp.next != null) {
            if (temp.rfc.getHostName().equals(hostName)) {
                previous.next = temp.next;
                temp = temp.next;
            } else {
                previous =  temp;
                temp = temp.next;
            }
        }
    }

    public int size() {
        return size;
    }
}
