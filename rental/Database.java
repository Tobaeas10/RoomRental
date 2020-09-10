package rental;

import java.io.Serializable;
import java.util.ArrayList;

public class Database implements Serializable {
	
	private static final long serialVersionUID = 8466054410968617118L;
	public ArrayList<Building> buildings = new ArrayList<Building>();
	public ArrayList<Booking> bookings = new ArrayList<Booking>();

	
	public void setBuildings(ArrayList<Building> buildings) {
		this.buildings = buildings;
		for(Building b : buildings) {
			b.calculateNumberOfRooms();
		}
	}

	public ArrayList<Building> getBuildings() {
		return buildings;
	}
}
