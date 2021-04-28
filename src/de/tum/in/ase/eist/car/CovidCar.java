package de.tum.in.ase.eist.car;

import de.tum.in.ase.eist.Dimension2D;

public class CovidCar extends Car {

    //The coronavirus.png has been excessively squished to counteract the prebuilt algorithm
    private static String COVID_CAR_IMAGE_FILE = "coronavirus.png";

    private static final int MIN_SPEED_COVID_CAR = 6;
    private static final int MAX_SPEED_COVID_CAR = 9;

    /**
     * Constructor, taking the maximum coordinates of the game board. Each car gets
     * a random X and Y coordinate, a random direction and a random speed.
     * <p>
     * The position of the car cannot be larger then the dimensions of the game
     * board.
     *
     * @param gameBoardSize dimensions of the game board
     */

    public CovidCar(Dimension2D gameBoardSize) {
        super(gameBoardSize);
        setMinSpeed(MIN_SPEED_COVID_CAR);
        setMaxSpeed(MAX_SPEED_COVID_CAR);
        setRandomSpeed();
        setIconLocation(COVID_CAR_IMAGE_FILE);

        super.setInfected(true);
        super.setWearingMask(false);
        super.setViralLoad(690420);
    }

    @Override
    public void drive(Dimension2D gameBoardSize) {
        super.drive(gameBoardSize);
    }

    @Override
    protected void refreshIconInfection(int infectionLevel) {
        if (infectionLevel < 800000 && infectionLevel >= 600000) {
            COVID_CAR_IMAGE_FILE = "coronavirus_80.png";
        } else if (infectionLevel < 600000 && infectionLevel >= 400000) {
            COVID_CAR_IMAGE_FILE = "coronavirus_60.png";
        } else if (infectionLevel < 400000 && infectionLevel >= 200000) {
            COVID_CAR_IMAGE_FILE = "coronavirus_40.png";
        } else if (infectionLevel < 200000 ) {
            COVID_CAR_IMAGE_FILE = "coronavirus_20.png";
        } else {
            COVID_CAR_IMAGE_FILE = "coronavirus.png";
        }

        setIconLocation(COVID_CAR_IMAGE_FILE);
    }
}
