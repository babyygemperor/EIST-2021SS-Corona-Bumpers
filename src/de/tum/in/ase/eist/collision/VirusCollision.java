package de.tum.in.ase.eist.collision;

import de.tum.in.ase.eist.car.Car;

public class VirusCollision extends Collision {

    private static final double INFECTIVITY_RATE = 20.0;

    public VirusCollision(Car car1, Car car2) {
        super(car1, car2);
    }

    @Override
    public Car evaluate() {
        double car1ViralLoad = this.car1.getViralLoad();
        double car2ViralLoad = this.car1.getViralLoad();

        if (car1ViralLoad + car2ViralLoad == 0.0)
            return null;

        this.car1.setViralLoad(car1ViralLoad + car2ViralLoad/INFECTIVITY_RATE);
        this.car2.setViralLoad(car1ViralLoad + car2ViralLoad/INFECTIVITY_RATE);

        if (car1ViralLoad > car2ViralLoad) {
            return this.car1;
        }

        return this.car2;
    }

}
