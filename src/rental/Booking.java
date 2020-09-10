package rental;

import java.util.ArrayList;
import java.util.Date;

public class Booking {

	public ArrayList<Room> rooms = new ArrayList<Room>();
	public Date from = new Date();
	public Date until = new Date();
    String tenantName;
    
    public Booking(ArrayList<Room> rooms, Date from, Date until, String tenantName) {
    	this.rooms = rooms;
    	this.from = from;
    	this.until = until;
    	this.tenantName = tenantName;
    	
    	for(Room r : rooms) {
    		r.book(this);
    	}
    }
}
