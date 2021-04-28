package de.tum.in.ase.eist.car;

import de.tum.in.ase.eist.Dimension2D;

public class FastCar extends Car {

	private static final String FAST_CAR_IMAGE_FILE = "FastCar.gif";

	private static final int MIN_SPEED_FAST_CAR = 2;
	private static final int MAX_SPEED_FAST_CAR = 10;

	public FastCar(Dimension2D gameBoardSize) {
		super(gameBoardSize);
		setMinSpeed(MIN_SPEED_FAST_CAR);
		setMaxSpeed(MAX_SPEED_FAST_CAR);
		setRandomSpeed();
		setIconLocation(FAST_CAR_IMAGE_FILE);
	}

	@Override
	protected void refreshIconInfection(int infectionLevel) {
		if (infectionLevel > 200000) {

		} else if (infectionLevel > 400000) {

		} else if (infectionLevel > 600000) {

		} else if (infectionLevel > 800000) {

		} else if (infectionLevel > 1000000) {

		} else if (infectionLevel > 1200000) {

		} else if (infectionLevel > 1400000) {

		} else if (infectionLevel > 1600000) {

		} else if (infectionLevel > 1800000) {

		}
	}

}
