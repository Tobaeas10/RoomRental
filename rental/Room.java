package rental;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Room {
    public String name;
    private Booking booking;
    public Floor floor;
    private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("dd.MM.yyyy");
    
    public Room(Floor floor, String name) {
    	this.name = name;
    	this.floor = floor;
    }
    
    public boolean getVacant() {
    	if(booking == null || getTenantName().equalsIgnoreCase("-")) {
    		return true;
    	} else {
    		return false;
    	}
    }
    
    public void book(Booking booking) {
    	this.booking = booking;
    	floor.vacantRooms --;
    }
    
    public void unbook() {
    	this.booking = null;
    	floor.vacantRooms ++;
    }
    
    @Override
    public String toString() {
		if(!name.trim().equalsIgnoreCase("")) {
			return name;
		} else {
			return "Unnamed Room";
		}
    }
    
    //Getters and Setters for TableView
    public String getName() {
    	return name;
    }
    public void setName(String name) {
    	this.name = name;
    }
    
    public String getTenantName() {
    	if(booking == null || booking.tenantName == null) {
    		return "";
    	} else {
    		return booking.tenantName;
    	}
    }
    public void setTenantName(String name) {
    	if(booking != null) booking.tenantName = name;
    }
    
    public String getFrom() {
    	if(booking == null) {
    		return "";
    	} else {
    		return DATE_FORMATTER.format(booking.from);
    	}
    }
    public void setFrom(Date date) {
    	if(booking != null) booking.from = date;
    }
    
    public String getUntil() {
    	if(booking == null) {
    		return "";
    	} else {
    		return DATE_FORMATTER.format(booking.until);
    	}
    }
    public void setUntil(Date date) {
    	if(booking != null) booking.until = date;
    }
    
    public String getFloor() {
    	return floor.name;
    }
    public void setFloor(String floor) {
    	this.floor.name = floor;
    }
    
    public String getBuilding() {
    	return floor.building.name;
    }
    public void setBuilding(String building) {
    	this.floor.building.name = building;
    }
}