package model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.paint.Color;

public class Track {
	
	public enum Behavior {
		TRACK,
		GROUND,
		RELATIVE,
		ABSOLUTE
	};
	
	
	private File trackFile;
	private String title;
	private Integer altitude;
	private Color lineColor;
	private Integer lineWidth;
	private Color polyColor;
	private Boolean extrude;
	private Behavior behavior;
	private List<Waypoint> waypoints;
	
	public Track(File trackFile) {
		this.trackFile = trackFile;
		this.title = "GPX Track";
		this.altitude = 10;
		this.lineColor = Color.rgb(232, 232, 19);
		this.lineWidth = 5;
		this.polyColor = Color.rgb(232, 232, 19, 0.5);
		this.extrude = false;
		this.behavior = Behavior.RELATIVE; 
		this.waypoints = new ArrayList<Waypoint>();
	}
	
	private String kmlColor(String jfxColor) {
		String color = "";
		color += jfxColor.substring(8,10);
		color += jfxColor.substring(6,8);
		color += jfxColor.substring(4,6);
		color += jfxColor.substring(2,4);
		return color;
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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getAltitude() {
		return altitude;
	}

	public void setAltitude(Integer altitude) {
		this.altitude = altitude;
	}

	public Color getLineColor() {
		return lineColor;
	}
	
	public String getLineKmlColor() {
		return kmlColor(this.lineColor.toString());
	}

	public void setLineColor(Color lineColor) {
		this.lineColor = lineColor;
	}

	public Color getPolyColor() {
		return polyColor;
	}
	
	public String getPolyKmlColor() {
		return kmlColor(this.polyColor.toString());
	}

	public void setPolyColor(Color polyColor) {
		this.polyColor = polyColor;
	}

	public Boolean getExtrude() {
		return extrude;
	}

	public void setExtrude(Boolean extrude) {
		this.extrude = extrude;
	}

	public Behavior getBehavior() {
		return behavior;
	}

	public void setBehavior(Behavior behavior) {
		this.behavior = behavior;
	}

	public Integer getLineWidth() {
		return lineWidth;
	}

	public void setLineWidth(Integer lineWidth) {
		this.lineWidth = lineWidth;
	}
	
}
