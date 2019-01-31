package model;

public class Waypoint {
	
	private String lat;
	private String lon;
	private String ele;
		
	public Waypoint(String lat, String lon, String ele) {
		this.lat = lat;
		this.lon = lon;
	}

	public Waypoint(String lat, String lon) {
		this(lat, lon, "0");
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

	public String getEle() {
		return ele;
	}

	public void setEle(String ele) {
		this.ele = ele;
	}
}
