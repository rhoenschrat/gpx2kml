package model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Track {
	
	private File trackFile;
	private List<Waypoint> waypoints;
	
	public Track(File trackFile) {
		this.trackFile = trackFile;
		this.waypoints = new ArrayList<Waypoint>();
	}
	
	public File getFile() {
		return this.trackFile;
	}
	
	public List<Waypoint> getWaypoints() {
		return this.waypoints;
	}
	
	public void setFile(File trackFile) {
		this.trackFile = trackFile;
	}
	
	public void setWaypoints(List<Waypoint> waypoints) {
		this.waypoints = waypoints;
	}
	
}
