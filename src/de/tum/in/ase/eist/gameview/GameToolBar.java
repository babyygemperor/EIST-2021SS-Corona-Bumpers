package de.tum.in.ase.eist.gameview;

import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.text.Text;

import java.util.Optional;

/**
 * This class visualizes the tool bar with start and stop buttons above the game board.
 */



public class GameToolBar extends ToolBar {
	private final Button start;
	private final Button stop;
	private final Text textField;
	private final Text virusViralLoad;

	private static final double ROUND_OFF_CONSTANT = 100d;


	public GameToolBar() {
		this.start = new Button("Start");
		this.stop = new Button("Stop");
		this.textField = new Text("Viral Load on you = 0");
		this.virusViralLoad = new Text("Virus potency = 0");
		// the game is stopped initially
		updateToolBarStatus(false);
		getItems().addAll(this.start, new Separator(), this.stop, new Separator(), this.textField, new Separator(), this.virusViralLoad);
	}

	/**
	 * Initializes the actions of the toolbar buttons.
	 */

	public void refreshText(GameBoardUI gameBoardUI) {
		this.textField.setText("Viral Load on you = " + (double)Math.round(gameBoardUI.getGameBoard().getPlayerCar().getViralLoad() * ROUND_OFF_CONSTANT) / ROUND_OFF_CONSTANT);
		this.virusViralLoad.setText("Virus potency =  " + (double)Math.round(gameBoardUI.getGameBoard().getCovidCar().getViralLoad() * ROUND_OFF_CONSTANT) / ROUND_OFF_CONSTANT);
	}

	public void initializeActions(GameBoardUI gameBoardUI) {
		this.start.setOnAction(event -> {
			Alert alert = new Alert(AlertType.CONFIRMATION, """
					THIS NEW MODIFIED VERSION IS VERY CONTEMPORARY AND APT\s
					Some cars are wearing a mask, some aren't, and there's a virus roaming around. THE MORE THE CONTACT, THE MORE THE VIRUS SPREADS
					You die if your viral load is more than 300K
					AVOID THE VIRUS AND STILL WIN BUMPER CARS, GOOD LUCK""", ButtonType.YES);
			alert.setTitle("Preface");
			alert.setHeaderText("2020/21 BUMPER CARS");

			Optional<ButtonType> result = alert.showAndWait();

			if (result.isPresent() && result.get() == ButtonType.YES)
				gameBoardUI.startGame();
			else {
				gameBoardUI.stopGame();
			}
		});

		this.stop.setOnAction(event -> {
			// stop the game while the alert is shown
			gameBoardUI.stopGame();

			Alert alert = new Alert(AlertType.CONFIRMATION, "Do you really want to stop the game?", ButtonType.YES,
					ButtonType.NO);
			alert.setTitle("Stop Game Confirmation");
			// By default the header additionally shows the Alert Type (Confirmation)
			// but we want to disable this to only show the question
			alert.setHeaderText("");

			Optional<ButtonType> result = alert.showAndWait();
			// reference equality check is OK here because the result will return the same
			// instance of the ButtonType
			if (result.isPresent() && result.get() == ButtonType.YES) {
				// reset the game board to prepare the new game
				gameBoardUI.setup();
			} else {
				// continue running
				gameBoardUI.startGame();
			}
		});

	}

	/**
	 * Updates the status of the toolbar. This will for example enable or disable
	 * buttons.
	 *
	 * @param running true if game is running, false otherwise
	 */
	public void updateToolBarStatus(boolean running) {
		this.start.setDisable(running);
		this.stop.setDisable(!running);
	}
}
