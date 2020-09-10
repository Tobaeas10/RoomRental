package rental;

import java.util.ArrayList;

public class Building {
	public ArrayList<Floor> floors = new ArrayList<>();
	public String name = "";
	public int vacantRooms, totalRooms;
    
    public Building(String name) {
        this.name = name;
    }
    
	public void calculateNumberOfRooms() {
		vacantRooms = 0;
		totalRooms = 0;
		for(Floor f : floors) {
			totalRooms += f.rooms.size();
			vacantRooms += f.vacantRooms;
		}
	}
    
	public void setFloors(ArrayList<Floor> floors) {
		this.floors = floors;
		calculateNumberOfRooms();
	}
	
    public void addFloor(Floor floor) {
    	floor.building = this;
    	vacantRooms += floor.vacantRooms;
    	totalRooms += floor.rooms.size();
    	floors.add(floor);
    }
    
    public void removeFloor(Floor floor) {
    	floor.building = null;
    	vacantRooms -= floor.vacantRooms;
    	totalRooms -= floor.rooms.size();
    	floors.remove(floor);
    }
    
    @Override
    public String toString() {
		if(!name.trim().equalsIgnoreCase("")) {
			return name + " " + vacantRooms;
		} else {
			return "Unnamed Building";
		}
    }
}