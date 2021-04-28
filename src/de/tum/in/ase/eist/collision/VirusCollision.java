package de.tum.in.ase.eist.collision;

import de.tum.in.ase.eist.car.Car;
import de.tum.in.ase.eist.car.CovidCar;

import java.util.Random;

public class VirusCollision extends Collision {

    public VirusCollision(Car car1, Car car2) {
        super(car1, car2);
    }

    @Override
    public Car evaluate() {

        int randomInt;
        if (this.car1 instanceof CovidCar) {
            if (this.car2.isWearingMask()) {
                randomInt = new Random().nextInt(100) + 30;
                if (randomInt < 69) {
                    this.car2.setViralLoad((int) ((this.car2.getViralLoad() + this.car1.getViralLoad()) * (randomInt / 100.0)));
                    this.car1.setViralLoad((int) (this.car1.getViralLoad() * 1.1));
                    return this.car1;
                } else {
                    return this.car2;
                }
            } else {
                this.car2.setViralLoad(this.car2.getViralLoad() + this.car1.getViralLoad());
                this.car1.setViralLoad((int) (this.car1.getViralLoad() * 1.1));
                return this.car1;
            }
        } else {
            if (this.car1.isWearingMask()) {
                randomInt = new Random().nextInt(100) + 30;
                if (randomInt < 69) {
                    this.car1.setViralLoad((int) ((this.car1.getViralLoad() + this.car2.getViralLoad()) * (randomInt / 100.0)));
                    this.car2.setViralLoad((int) (this.car2.getViralLoad() * 1.1));
                    return this.car2;
                } else {
                    return this.car1;
                }
            } else {
                this.car1.setViralLoad(this.car1.getViralLoad() + this.car2.getViralLoad());
                this.car2.setViralLoad((int) (this.car2.getViralLoad() * 1.1));
                return this.car2;
            }
        }

    }
}
