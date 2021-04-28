package de.tum.in.ase.eist.collision;

import de.tum.in.ase.eist.car.Car;

import java.util.Random;

public class VirusCollision extends Collision {

    public VirusCollision(Car car1, Car car2) {
        super(car1, car2);
    }

    @Override
    public Car evaluate() {
        Random random = new Random();
        int randomInt;

        if (this.car1.isInfected()) {
            if (!this.car2.isInfected()) {
                if (!this.car2.isWearingMask())
                    this.car2.setInfected(true);
                else {
                    randomInt = random.nextInt(100);
                    if (randomInt < 69) {
                        this.car2.setInfected(true);
                        this.car2.setViralLoad(this.car2.getViralLoad() + Math.abs(11 - this.car1.getSpeed() - this.car2.getSpeed()));
                        return this.car1;
                    } else {
                        return this.car2;
                    }
                }
            }
        } else if (this.car2.isInfected()) {
            if (!this.car1.isInfected()) {
                if (!this.car1.isWearingMask())
                    this.car1.setInfected(true);
                else {
                    randomInt = random.nextInt(100);
                    if (randomInt < 69) {
                        this.car1.setInfected(true);
                        this.car1.setViralLoad(this.car1.getViralLoad() + Math.abs(11 - this.car1.getSpeed() - this.car2.getSpeed()));
                        return this.car2;
                    } else {
                        return this.car1;
                    }
                }
            }
        }

        return null;

    }
}
