package de.tum.in.ase.eist.collision;

import de.tum.in.ase.eist.car.Car;
import de.tum.in.ase.eist.car.CovidCar;

public class VirusCollision extends Collision{

    public VirusCollision(Car car1, Car car2) {
        super(car1, car2);
    }

    @Override
    public Car evaluate() {
        if (this.car1 instanceof CovidCar)
            return car1;
        else
            return this.car2;
    }
}
