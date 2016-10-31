package pl.zablocki.core.roadnetwork;

import lombok.Getter;
import lombok.ToString;

@ToString
public class Position {

	@Getter
	private Road currentRoad;
	@Getter
	private int distance;

	public Position() {
		this.currentRoad = null;
		this.distance = 0;
	}
}