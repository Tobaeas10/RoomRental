package rental;

import java.util.ArrayList;

public class Floor {
    public ArrayList<Room> rooms = new ArrayList<>();
    public String name;
    public Building building;
    public int vacantRooms = 0; //totalRooms = rooms.size()
    
    public Floor(Building building, String name) {
    	this.name = name;
    	this.building = building;
    }
    
    public void addRoom(Room room) {
    	room.floor = this;
    	rooms.add(room);
    	if(room.getVacant()) vacantRooms ++;
    }
    
    public void removeRoom(Room room) {
    	room.floor = null;
    	rooms.remove(room);
    	if(room.getVacant()) vacantRooms --;
    }
    
    @Override
    public String toString() {
		if(!name.trim().equalsIgnoreCase("")) {
			return name + " " + vacantRooms;
		} else {
			return "Unnamed Floor";
		}
    }
}