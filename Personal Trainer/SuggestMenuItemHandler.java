package hw3;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class SuggestMenuItemHandler implements EventHandler<ActionEvent>{	
	PersonalTrainer pt = new PersonalTrainer();
	Stage suggestStage = new Stage();
	Workout suggestWorkout = new Workout();
	Scene suggestScene;
	TextField time = new TextField();
	TextField calories = new TextField();
	Label note = new Label();
	Button suggestButton = new Button("Suggest");
	Button cancelButton = new Button("Cancel");
	SuggestMenuItemHandler(PersonalTrainer pt){
		this.pt = pt;
	}
	public void setupSuggestScene() {//set up the scene for the suggest function
		GridPane grid = new GridPane();
		Label timeInput = new Label("Enter time in minutes");
		Label caloriesInput = new Label("Enter calories to burn");

		time.setPrefWidth(150);
		calories.setPrefWidth(150);
		timeInput.setPrefWidth(200);
		caloriesInput.setPrefWidth(200);
		
		time.setAlignment(Pos.CENTER);
		calories.setAlignment(Pos.CENTER);
		timeInput.setAlignment(Pos.CENTER);
		caloriesInput.setAlignment(Pos.CENTER);
		
		time.setFont(Font.font(15));
		calories.setFont(Font.font(15));
		timeInput.setFont(Font.font(15));
		caloriesInput.setFont(Font.font(15));
		
		note.setText("0 input will be ignored!");
		note.setPrefWidth(300);
		note.setFont(Font.font(15));
	
		suggestButton.setPrefWidth(100);
		cancelButton.setPrefWidth(100);
		
		grid.add(timeInput, 0, 0);
		grid.add(caloriesInput, 0, 1);
		grid.add(time, 1, 0);
		grid.add(calories, 1, 1);
		grid.add(note, 1, 2, 3, 1);

		grid.add(suggestButton, 1, 3);
		grid.add(cancelButton, 2, 3);
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);

		suggestScene= new Scene (grid, 450, 250);//add the grid to the scene
	}
	
	@Override
	public void handle(ActionEvent event) {
		time.clear();//clear up the time text field
		calories.clear();//clear up the calories text field
		setupSuggestScene();
		suggestStage.setTitle("Workout Suggestion");
		suggestStage.setScene(suggestScene);
		suggestStage.show();
		note.setTextFill(Color.BLACK);
		suggestButton.setOnAction(new EventHandler<ActionEvent>() {//anonymous class to handle the suggest button
			@Override
			public void handle(ActionEvent event) {
				try {
				suggestWorkout.timeSpent = Integer.parseInt(time.getText());
				suggestWorkout.caloriesBurned = Integer.parseInt(calories.getText());
				if (suggestWorkout.timeSpent == 0 && suggestWorkout.caloriesBurned == 0) {
					suggestStage.close();//if user input for time and calories are both 0, close the suggestStage
				} else {
					pt.ptData.selectedExercises.clear();
					ObservableList<Exercise> suggestList = suggestWorkout.buildWorkoutPlan(pt.ptData.masterData,suggestWorkout.timeSpent, suggestWorkout.caloriesBurned);
					for (int i = 0; i < suggestList.size(); i++) {
						pt.ptData.selectedExercises.add(suggestList.get(i));//add the suggestList from buildWorkoutPlan to the selectedExercises
					}
				}
				pt.loadSelectionGrid(pt.ptData.selectedExercises);//load the suggested selectedExercises into the table view
				WorkoutViewer.createViewer(pt.ptData.selectedExercises.get(0).getImageFile()).view(pt.ptView.imageStackPane);
				pt.ptView.notesTextArea.setText(pt.ptData.selectedExercises.get(0).getExerciseNotes());
				pt.ptView.exerciseTableView.scrollTo(pt.ptData.selectedExercises.get(0)); 
				pt.ptView.exerciseTableView.getSelectionModel().select(pt.ptData.selectedExercises.get(0));
				suggestStage.close();
				}catch (NumberFormatException e) {//if user's input is not integer, catch the exception
					note.setTextFill(Color.RED);
					note.setText("Please enter numeric value only");
				}
			}
		});
		cancelButton.setOnAction(new EventHandler<ActionEvent>() {//anonymous class to handle the cancel button
			@Override
			public void handle(ActionEvent event) {
				suggestStage.close();
			}
		});
	}

}
