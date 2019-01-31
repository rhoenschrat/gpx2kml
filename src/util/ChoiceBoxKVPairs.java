package util;

import model.Track;

public class ChoiceBoxKVPairs {
	private Track.Behavior key;
	private String value;
	
	public ChoiceBoxKVPairs(Track.Behavior key, String value) {
		this.key = key;
		this.value = value;		
	}
	
	public Track.Behavior getKey() {
		return this.key;
	}
	
	public String toString() {
		return this.value;
	}

}
