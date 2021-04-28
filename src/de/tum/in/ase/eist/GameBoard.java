package de.tum.in.ase.eist;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import de.tum.in.ase.eist.audio.AudioPlayerInterface;
import de.tum.in.ase.eist.car.Car;
import de.tum.in.ase.eist.car.CovidCar;
import de.tum.in.ase.eist.car.FastCar;
import de.tum.in.ase.eist.car.SlowCar;
import de.tum.in.ase.eist.collision.Collision;
import de.tum.in.ase.eist.collision.VirusCollision;

/**
 * Creates all car objects, detects collisions, updates car positions, notifies
 * player about victory or defeat.
 */
public class GameBoard {

	private static final int NUMBER_OF_SLOW_CARS = 5;
	private static final int NUMBER_OF_TESLA_CARS = 2;

	private static final double ROUND_OFF_CONSTANT = 100d;
	private static final int COVID_POTENCY_SEED = 690420;
	private static final double COVID_POTENCY_OFFSET = 100000.0;
	private static final int STATISTICS_SEED = 100;
	private static final int SLOW_CAR_MASK_RATE = 65;
	private static final int FAST_CAR_MASK_RATE = 45;

	private static final double PLAYER_COVID_HEALING_RATE = 0.999;
	private static final double PLAYER_COVID_VIRAL_LOAD_THRESHOLD = 300000.0;
	private static final double CAR_HEALING_RATE = 0.98;
	private static final double CAR_MASKED_HEALING_RATE = 0.85;
	private static final double CAR_COVID_VIRAL_LOAD_THRESHOLD = 300000.0;
	private static final double COVID_CAR_DECAY_RATE = 0.99;
	private static final double COVID_CAR_FATALITY_THRESHOLD = 500.0;
	private static final double INFECTIVITY_RATE = 20.0;



	/**
	 * List of all active cars, does not contain player car.
	 */
	private final List<Car> cars = new ArrayList<>();

	/**
	 * The player object with player's car.
	 */
	private final Player player;
	private final CovidCar covidCar;

	/**
	 * AudioPlayer responsible for handling music and game sounds.
	 */
	private AudioPlayerInterface audioPlayer;

	/**
	 * Dimension of the GameBoard.
	 */
	private final Dimension2D size;

	/**
	 * true if game is running, false if game is stopped.
	 */
	private boolean running;

	/**
	 * List of all loser cars (needed for testing, DO NOT DELETE THIS)
	 */
	private final List<Car> loserCars = new ArrayList<>();

	/**
	 * The outcome of this game from the players perspective. The game's outcome is open at the beginning.
	 */
	private GameOutcome gameOutcome = GameOutcome.OPEN;

	/**
	 * Creates the game board based on the given size.
	 *
	 * @param size of the game board
	 */
	public GameBoard(Dimension2D size) {
		this.size = size;
		FastCar playerCar = new FastCar(size);
		this.player = new Player(playerCar);
		this.player.setup();


		this.covidCar = new CovidCar(this.size);

		this.covidCar.setInfected(true);
		this.covidCar.setWearingMask(false);

		this.covidCar.setViralLoad(new Random().nextInt(COVID_POTENCY_SEED) + COVID_POTENCY_OFFSET);

		this.cars.add(covidCar);

		createCars();
	}

	/**
	 * Creates as many cars as specified by {@link #NUMBER_OF_SLOW_CARS} and adds
	 * them to the cars list.
	 */

	private void createCars() {
		int random;
		Random randomGenerator = new Random();
		SlowCar slowCar;
		FastCar fastCar;

		for (int i = 0; i < NUMBER_OF_SLOW_CARS; i++) {
			random = randomGenerator.nextInt(STATISTICS_SEED);
			slowCar = new SlowCar(this.size);

			if (random < SLOW_CAR_MASK_RATE)
				slowCar.setWearingMask(true);

			slowCar.setViralLoad(0);

			this.cars.add(slowCar);

		}

		for (int i = 0; i < NUMBER_OF_TESLA_CARS; i++) {
			random = randomGenerator.nextInt(STATISTICS_SEED);
			fastCar = new FastCar(this.size);

			if (random < FAST_CAR_MASK_RATE)
				fastCar.setWearingMask(true);

			fastCar.setViralLoad(0.0);
			this.cars.add(fastCar);
		}
	}

	public Dimension2D getSize() {
		return size;
	}

	/**
	 * Returns if game is currently running.
	 *
	 * @return true if the game is currently running, false otherwise
	 */
	public boolean isRunning() {
		return this.running;
	}

	/**
	 * Sets whether the game should be currently running.
	 * <p>
	 * Also used for testing on Artemis.
	 *
	 * @param running true if the game should be running
	 */
	public void setRunning(boolean running) {
		this.running = running;
	}

	public GameOutcome getGameOutcome() {
		return gameOutcome;
	}

	/**
	 * Returns all cars on the game board except the player's car as a list.
	 *
	 * @return the list of all non-player cars
	 */
	public List<Car> getCars() {
		return this.cars;
	}

	public Car getPlayerCar() {
		return this.player.getCar();
	}

	public AudioPlayerInterface getAudioPlayer() {
		return this.audioPlayer;
	}

	public void setAudioPlayer(AudioPlayerInterface audioPlayer) {
		this.audioPlayer = audioPlayer;
	}

	/**
	 * Updates the position of each car.
	 */

	public void update() {
		moveCars();
	}

	/**
	 * Starts the game. Cars start to move and background music starts to play.
	 */
	public void startGame() {
		playMusic();
		this.running = true;
	}

	/**
	 * Stops the game. Cars stop moving and background music stops playing.
	 */
	public void stopGame() {
		stopMusic();
		this.running = false;
	}

	/**
	 * Starts the background music.
	 */
	public void playMusic() {
		this.audioPlayer.playBackgroundMusic();
	}

	/**
	 * Stops the background music.
	 */
	public void stopMusic() {
		this.audioPlayer.stopBackgroundMusic();
	}

	/**
	 * @return list of loser cars
	 */
	public List<Car> getLoserCars() {
		return this.loserCars;
	}

	/**
	 * Moves all cars on this game board one step further.
	 */
	public void moveCars() {
		// update the positions of the player car and the autonomous cars
		for (Car car : this.cars) {
			car.drive(size);
		}
		this.player.getCar().drive(size);

		// iterate through all cars (except player car) and check if it is crunched

		if (player.getCar().isCrunched())
			gameOutcome = GameOutcome.LOST;

		player.getCar().setViralLoad(player.getCar().getViralLoad() * PLAYER_COVID_HEALING_RATE);

		if (player.getCar().getViralLoad() > PLAYER_COVID_VIRAL_LOAD_THRESHOLD)
			gameOutcome = GameOutcome.LOST_COVID;

		if (allCarsCrunched())
			gameOutcome = GameOutcome.WON;

		for (Car car : cars) {

			if (car.isCrunched()) {
				// because there is no need to check for a collision
				continue;
			}

			if (!(car instanceof CovidCar)) {
				if (car.isWearingMask())
					car.setViralLoad(car.getViralLoad() * CAR_MASKED_HEALING_RATE);
				else
					car.setViralLoad(car.getViralLoad() * CAR_HEALING_RATE);
				if (car.getViralLoad() > CAR_COVID_VIRAL_LOAD_THRESHOLD) {
					this.loserCars.add(car);
					printDeath(car);
					car.crunch();
				}
			} else {
				car.setViralLoad(car.getViralLoad() * COVID_CAR_DECAY_RATE);

				if (car.getViralLoad() < COVID_CAR_FATALITY_THRESHOLD) {
					car.crunch();

					this.loserCars.add(car);
					printDeath(car);
				}
			}

			/*
			 * Hint: Make sure to create a subclass of the class Collision and store it in
			 * the new Collision package. Create a new collision object and check if the
			 * collision between player car and autonomous car evaluates as expected
			 */

			for (Car car2 : cars) {
				if (car != car2 && !car.isCrunched() && !car2.isCrunched()) {
					Collision virusCollision = new VirusCollision(car, car2);

					if (virusCollision.isCrash()) {
						Car winner = virusCollision.evaluate();
						Car loser = virusCollision.evaluateLoser();

						if (winner != null) {
							this.audioPlayer.playInfectedSound();
							printInfection(winner, loser);
						}

						if (isWinner())
							gameOutcome = GameOutcome.WON;
					}
				}
			}

			Collision collision = new Collision(player.getCar(), car);

			Collision virusCollision = new VirusCollision(player.getCar(), car);

			if (collision.isCrash() && !(car instanceof CovidCar)) {
				Car winner = collision.evaluate();
				Car loser = collision.evaluateLoser();
				printWinner(winner);
				loserCars.add(loser);

				this.audioPlayer.playCrashSound();

				loser.crunch();

				if (isWinner())
					gameOutcome = GameOutcome.WON;

			}

			if (virusCollision.isCrash()) {
				Car winner = collision.evaluate();
				Car loser = collision.evaluateLoser();

				if (winner == player.getCar()) {
					player.getCar().setViralLoad(loser.getViralLoad()/INFECTIVITY_RATE + player.getCar().getViralLoad());
				} else if (loser == player.getCar()) {
					player.getCar().setViralLoad(winner.getViralLoad()/INFECTIVITY_RATE + player.getCar().getViralLoad());
				}

				if (winner != null) {
					this.audioPlayer.playInfectedSound();
					printInfection(winner, loser);
				}

				if (isWinner())
					gameOutcome = GameOutcome.WON;
			}
		}
	}

	public CovidCar getCovidCar() {
		return covidCar;
	}

	private boolean allCarsCrunched() {
		for (Car car: cars) {
			if (!(car instanceof CovidCar) && !car.isCrunched())
				return false;
		}

		return true;
	}

	/**
	 * If all other cars are crunched, the player wins.
	 *
	 * @return true if the game is over and the player won, false otherwise
	 */
	private boolean isWinner() {
		for (Car car : getCars()) {
			if (!car.isCrunched()) {
				return false;
			}
		}
		return true;
	}

	private void printWinner(Car winner) {
		if (winner == this.player.getCar()) {
			System.out.println("The player's car won the collision!");
		} else if (winner != null) {
			System.out.println(winner.getClass().getSimpleName() + " won the collision!");
		} else {
			System.err.println("Winner car was null!");
		}
	}

	private void printDeath(Car death) {
		if (death == this.player.getCar()) {
			System.out.println("The player's died of COVID-19");
		} else if (death != null) {
			System.out.println(death.getClass().getSimpleName() + " died ");
		} else {
			System.err.println("Dead car was null!");
		}
	}

	private void printInfection(Car winner, Car loser) {
		if (winner == player.getCar())
			System.out.println("\tPlayerCar with a viral load of " + (double)Math.round(winner.getViralLoad() * ROUND_OFF_CONSTANT) / ROUND_OFF_CONSTANT
					+ " and " + (double)Math.round(loser.getViralLoad() * ROUND_OFF_CONSTANT) / ROUND_OFF_CONSTANT + " with a viral load of " + loser.getViralLoad() + " were in contact!!!");
		else if (loser == player.getCar())
			System.out.println("\t" + winner.getClass().getSimpleName() + " with a viral load of " + (double)Math.round(winner.getViralLoad() * ROUND_OFF_CONSTANT) / ROUND_OFF_CONSTANT
					+ " and PlayerCar with a viral load of " + (double)Math.round(loser.getViralLoad() * ROUND_OFF_CONSTANT) / ROUND_OFF_CONSTANT + " were in contact!!!");
		else
			System.out.println("\t" + winner.getClass().getSimpleName() + " with a viral load of " + (double)Math.round(winner.getViralLoad() * ROUND_OFF_CONSTANT) / ROUND_OFF_CONSTANT
				+ " and " + loser.getClass().getSimpleName() + " with a viral load of " + (double)Math.round(loser.getViralLoad() * ROUND_OFF_CONSTANT) / ROUND_OFF_CONSTANT + " were in contact!!!");
	}

}
