package model;

public class Waypoint {
	
	private String lat;
	private String lon;
	
	public Waypoint(String lat, String lon) {
		this.lat = lat;
		this.lon = lon;
	}

	public String getLat() {
		return lat;
	}
	
	public String getLon() {
		return lon;
	}
	
	public void setLat(String lat) {
		this.lat = lat;
	}
	
	public void setLon(String lon) {
		this.lon = lon;
	}
}
