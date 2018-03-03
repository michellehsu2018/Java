package hw3;

import java.io.File;
import javafx.application.Application;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;

public class PersonalTrainer extends Application {
	
	PTViewer ptView = new PTViewer();  //will perform all view-related operations that do not need data-components
	PTData ptData = new PTData(); //will perform all data-related operations that do not need view-components
	Stage mainStage; //to be used for FileChooser in OpenHandler
	GridPane workoutGrid; //will hold the central grid populated with GUI components and will be attached to root in New and Open handlers
	DataFiler dataFiler; //will hold CSVFiler or XMLFiler
	Exercise currentExercise; //this points to whichever exercise is selected in exerciseComboBox or in exercisetableView 
	
	static final String PT_DATA_PATH = "resources"; //relative path for all data files to reside 
	static final String PT_IMAGE = "personaltrainer.jpg";	//Welcome image
	static final String PT_MUSIC = "Kalimba.mp3";	//audio played in background for images
	static final String PT_YOUDIDIT_IMAGE = "youdidit.jpg";//workout completion image
	
	WorkoutPlayer player = new WorkoutPlayer();  //Used in Play or Close handlers 
	static MediaPlayer videoPlayer, audioPlayer;
	

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		mainStage = primaryStage;
		mainStage.setTitle("Personal Trainer");
		ptView.setupMenus();
		ptView.setupWelcomeScreen();
		Background b = new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY));
		ptView.root.setBackground(b);
		Scene scene = new Scene(ptView.root, 875, 500);
		mainStage.setScene(scene);
		workoutGrid = ptView.setupScreen();//populate the grid, but don't attach to the root yet
		setActions();
		mainStage.show();
	}
	
	private void loadInputGrid() { 
		//enter your code here from HW2
		ptView.exerciseComboBox.valueProperty().addListener(new ChangeListener<Exercise>() { //attach listener to exerciseComboBox
			@Override
			public void changed(ObservableValue<? extends Exercise> observable, Exercise oldValue, Exercise newValue) {
				if(ptData.masterData.isEmpty()!=true) {
					ptView.timeSlider.setValue(newValue.getRepTime()); //set the initial timeSlider to the selected exercise's rep time
					ptView.timeValue.setText(newValue.getRepTime().toString()); //set initial timeValue to the selected exercise's rep time
					ptView.repsCountValue.setText(newValue.getRepCount().toString()); //set initial repsCountValue to the selected exercise's rep count
					ptView.caloriesValue.setText(newValue.getCalories().toString());//set initial caloriesValue to the selected exercise's burned calories
					ptView.notesTextArea.setText(newValue.getExerciseNotes());//set initial notesTextArea to the selected exercise's note
					WorkoutViewer.createViewer(newValue.getImageFile()).view(ptView.imageStackPane);
				}
			}
		});
		ptView.timeSlider.valueProperty().addListener(new ChangeListener<Number>() {//attach listener to timeSlider==> when user changes the timeSlider, the related values will change accordingly
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				if(ptData.masterData.isEmpty()!=true ) {
				int sliderTime = newValue.intValue();
				int time, count, calories;
				double factor = 0;
				if(ptView.exerciseComboBox.getSelectionModel().getSelectedIndex() >= 0) {//check if any of the exercise is selected from combo box
					/*get the underlying data of selected exercise from combo box*/
					time = ptView.exerciseComboBox.getSelectionModel().getSelectedItem().getRepTime();
					count = ptView.exerciseComboBox.getSelectionModel().getSelectedItem().getRepCount();
					calories = ptView.exerciseComboBox.getSelectionModel().getSelectedItem().getCalories();
					factor = ((double)sliderTime/(double)time); // used to calculate the updated repsCountValue and caloriesValue
				}else {
					time = 0;
					count = 0;
					calories = 0;
				}
				ptView.timeValue.setText(Integer.toString(sliderTime));
				ptView.repsCountValue.setText(Integer.toString((int)(count*Math.floor(factor)))); 
				ptView.caloriesValue.setText(Integer.toString((int)(calories*factor)));
				}
			}
		});	
		ptView.notesTextArea.setOnKeyTyped(new UpdateButtonHandler());
	}
	
	/**loadSelectionGrid() reads the data from data file and populates the components in selectionGrid.  
	 * This method is called in two scenarios. One - when user opens a data file, and two- when the user invokes
	 * the menu option Tools-Suggest. The workout loaded from the data file or as suggested in the SuggestMenuItemHandler is passed as a 
	 * parameter selectedExercises to this method. This method first clears the table-view from past-data, if any, and then
	 * adds all exercises from selectedExercises array to various components.   
	 * @param selectedExercises
	 */
	void loadSelectionGrid(ObservableList<Exercise> selectedExercises) {
		//enter your code here from HW2
		ptView.exerciseTableView.setItems(selectedExercises); //load the exerciseTableView with selectedExercises 
		int currentTotalTime = 0;
		int currentTotalCalories = 0;
		for(Exercise i : selectedExercises) { 
			currentTotalTime += i.getRepTime();//calculate the updated total time spent
			currentTotalCalories += i.getCalories();//calculate the updated total calories burned
		}
		ptView.totalTimeValue.setText(Integer.toString(currentTotalTime)); 
		ptView.totalCaloriesValue.setText(Integer.toString(currentTotalCalories));
		ptView.exerciseTableView.setOnMouseClicked(new SelectTableRowHandler());

	}
	
	/** setActions() method attaches all action-handlers to their respective
	 * GUI components. All GUI has been defined in PTViewer.
	 */
	private void setActions() {
		ptView.openWorkoutMenuItem.setOnAction(new OpenWorkoutHandler());
		ptView.aboutHelpMenuItem.setOnAction(new AboutHandler());
		//enter your code here 
		ptView.closeWorkoutMenuItem.setOnAction(new CloseWorkoutHandler());
		ptView.exitWorkoutMenuItem.setOnAction(new ExitWorkoutHandler());
		ptView.addButton.setOnAction(new AddButtonHandler());
		ptView.removeButton.setOnAction(new RemoveButtonHandler()); 
	    ptView.searchButton.setOnAction(new SearchButtonHandler());
		SuggestMenuItemHandler suggest = new SuggestMenuItemHandler(this);
		ptView.suggestWorkoutMenuItem.setOnAction(suggest);
		ptView.saveWorkoutMenuItem.setOnAction(new SaveMenuItemHandler());
		ptView.playWorkoutMenuItem.setOnAction(new PlayWorkoutHandler());
		BooleanProperty check = new SimpleBooleanProperty(ptData.masterData.isEmpty());//check if any data is selected by the user==> check == false
		ptView.closeWorkoutMenuItem.disableProperty().bind(check); // the closeWorkoutMenuItem is disabled
		ptView.suggestWorkoutMenuItem.disableProperty().bind(check);//the suggestWorkoutMenuItem is disabled
		ptView.saveWorkoutMenuItem.disableProperty().bind(check);//the saveWorkoutMenuItem is disabled
		ptView.playWorkoutMenuItem.disableProperty().bind(check);//the playWorkoutMenuItem is disabled
		ptView.exerciseComboBox.setPromptText("Select exercise"); 
		ptView.timeSlider.setSnapToTicks(true);	//force timeSlider to stop at whole number
	}

	//write your event handlers' inner classes here
	private class SelectTableRowHandler implements EventHandler<MouseEvent>{ //use EventHandler with parameterized type MouseEvent to control the image and notes displayed with mouse clicks
		@Override
		public void handle(MouseEvent event) {
			int index = ptView.exerciseTableView.getSelectionModel().getSelectedIndex();//get the index of the exercise that mouse clicks on
			WorkoutViewer.createViewer(ptData.selectedExercises.get(index).getImageFile()).view(ptView.imageStackPane);//display the video of the exercise that mouse clicks on
			ptView.notesTextArea.setText(ptData.selectedExercises.get(index).getExerciseNotes());//display the notes of the exercise that mouse clicks on
		}
	}
	
	private class AddButtonHandler implements EventHandler<ActionEvent>{//attach the EventHandler to AddButtonHandler
		@Override
		public void handle(ActionEvent event){
			if (ptView.exerciseComboBox.getSelectionModel().getSelectedItem() == null) { // check if any exercise from combo box is selected
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Reminder");
				alert.setHeaderText("No exercise selected");
				alert.setContentText("Please select an exercise from drop down box");
				alert.showAndWait();
			} else {
				currentExercise = ptView.exerciseComboBox.getSelectionModel().getSelectedItem();// store the data from selected exercise in combo box into currentExercise
				if(ptView.timeSlider.getValue()!=0) { //check if the time specified for the exercise is 0, if not, add the exercise into the table
					Exercise exerciseToAdd = new Exercise(currentExercise.getName(), currentExercise.getLevel(),
							(int) ptView.timeSlider.getValue(), Integer.parseInt(ptView.repsCountValue.getText()),
							Integer.parseInt(ptView.caloriesValue.getText()), currentExercise.getImageFile(),
							currentExercise.getExerciseNotes()); // pass the data of currentExercise to exerciseToAdd, which will be added to selectedObservables later
					ptData.selectedExercises.add(exerciseToAdd); // add the exerciseToAdd to selectedObservables
					loadSelectionGrid(ptData.selectedExercises); // reload the updated selectedObservables into exerciseTableView with loadSelectionGrid method
					ptView.exerciseTableView.scrollTo(exerciseToAdd); 
					ptView.exerciseTableView.getSelectionModel().select(exerciseToAdd); //set the attempt-to-select signal(gray bar) to exerciseToAdd
				}else { // if the time specified is 0, pop out the reminder
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Reminder");
					alert.setHeaderText("No exercise time specified");
					alert.setContentText("Please choose an exercise time from slider");
					alert.showAndWait();
				}
			}
		}
	}
	private class RemoveButtonHandler implements EventHandler<ActionEvent>{//attach the EventHandler to RemoveButtonHandler
		@Override
		public void handle(ActionEvent event) {
			if (ptView.exerciseTableView.getSelectionModel().isEmpty()) { //check if there is any exercise in the table
				ptView.notesTextArea.setText("");// if not, print out the warning in notesTextArea
				WorkoutViewer.createViewer(PersonalTrainer.PT_IMAGE).view(ptView.imageStackPane);
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Reminder");
				alert.setHeaderText("No exercise selected");
				alert.setContentText("There is no exercise selected to be removed from the table");
				alert.showAndWait();
			} else {
				int index = ptView.exerciseTableView.getSelectionModel().getSelectedIndex(); // get the index of the selected exercise from table
				ptData.selectedExercises.remove(index);//remove the selected exercise by its index
				loadSelectionGrid(ptData.selectedExercises); //reload the updated selectedObservables into exerciseTableView with loadSelectionGrid method
				if (ptData.selectedExercises.isEmpty()) {
					ptView.notesTextArea.setText("");
					ptView.imageView.setImage(null);
				} else {
					if (index == 0) {
						WorkoutViewer.createViewer(ptData.selectedExercises.get(index).getImageFile()).view(ptView.imageStackPane);//display the video of updated exercise with new index 0
						ptView.notesTextArea.setText(ptData.selectedExercises.get(index).getExerciseNotes());//show the notes of updated exercise with new index 0
						ptView.exerciseTableView.scrollTo(ptData.selectedExercises.get(index));
					} else {
						WorkoutViewer.createViewer(ptData.selectedExercises.get(index-1).getImageFile()).view(ptView.imageStackPane);//display the video of updated exercise with new index of (index-1)
						ptView.notesTextArea.setText(ptData.selectedExercises.get(index - 1).getExerciseNotes());//show the notes of updated exercise with new index of (index-1)
						ptView.exerciseTableView.scrollTo(ptData.selectedExercises.get(index - 1));
					}
				}
			}
		}
	}
	
	private class CloseWorkoutHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {	
			ptData.masterData.clear();
			ptData.selectedExercises.clear();
			ptView.root.setCenter(null);
			ptView.setupWelcomeScreen();
			BooleanProperty check = new SimpleBooleanProperty(true); // after the close button is clicked, disable the close button
			ptView.closeWorkoutMenuItem.disableProperty().bind(check);
			ptView.timeSlider.valueProperty().set(0);
			BooleanProperty check1 = new SimpleBooleanProperty(false); // after the open button is clicked, disable the open button
			ptView.openWorkoutMenuItem.disableProperty().bind(check1);
			BooleanProperty check2 = new SimpleBooleanProperty(ptData.masterData.isEmpty());
			ptView.suggestWorkoutMenuItem.disableProperty().bind(check2);//if masterData is empty, disable the suggest button
			ptView.saveWorkoutMenuItem.disableProperty().bind(check2);
			ptView.playWorkoutMenuItem.disableProperty().bind(check2);
		}
	}
	private class ExitWorkoutHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
		 mainStage.close(); //exit the window
		}
	}
	
	/**OpenWorkoutHandler has been provided as a dummy to display the
	 * workoutGrid. The workoutGrid needs to be populated with data
	 * in this handler
	 */
	private class OpenWorkoutHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			File f = null;
			try {
				ptView.root.setBottom(null);
				ptView.root.setCenter(workoutGrid);
				FileChooser fc = new FileChooser();
				fc.setTitle("Select file");
				fc.getExtensionFilters().addAll(new ExtensionFilter("CSV Files", "*.csv", "*.xml"),
						new ExtensionFilter("All Files", "*.*"));
				fc.setInitialDirectory(new File(PT_DATA_PATH));
				if ((f = fc.showOpenDialog(mainStage)) != null) { // Open file-dialog control
					if (f.getAbsolutePath().isEmpty() != true) {
						ptData.loadData(f.getAbsolutePath());
						BooleanProperty check = new SimpleBooleanProperty(ptData.masterData.isEmpty()); // check if any file has been selected
						ptView.closeWorkoutMenuItem.disableProperty().bind(check);// if not, disable the close button
						ptView.suggestWorkoutMenuItem.disableProperty().bind(check);// if not, disable the suggest button
						ptView.saveWorkoutMenuItem.disableProperty().bind(check);// if not, disable the save as button
						ptView.playWorkoutMenuItem.disableProperty().bind(check);//if not, disable the play button
						ptView.exerciseComboBox.setItems(ptData.masterData); // bind exerciseComboBox with data
						ptView.exerciseTableView.setItems(ptData.selectedExercises); // set the initial table with all the exercise data
						int totalTime = 0;
						int totalCalories = 0;
						for (Exercise i : ptData.selectedExercises) {
							totalTime += i.getRepTime();
							totalCalories += i.getCalories();
						}
						ptView.totalTimeValue.setText(Integer.toString(totalTime));
						ptView.totalCaloriesValue.setText(Integer.toString(totalCalories));
						WorkoutViewer.createViewer(ptData.masterData.get(0).getImageFile()).view(ptView.imageStackPane);
						ptView.notesTextArea.setText(ptData.selectedExercises.get(0).getExerciseNotes());
						loadSelectionGrid(ptData.selectedExercises);
						ptView.root.setBottom(null);
						ptView.root.setCenter(workoutGrid);
						loadInputGrid();
						ptView.exerciseTableView.getSelectionModel().selectFirst();// set the selection bar on the first row of table
					}
					BooleanProperty check1 = new SimpleBooleanProperty(true); // after the open button is clicked, disable the open button
					ptView.openWorkoutMenuItem.disableProperty().bind(check1);
				}
				ptView.repsCountValue.setText("0");
				ptView.caloriesValue.setText("0");
				ptView.timeValue.setText("0");
				ptView.workoutNameValue.setText(f.getName());//set the workoutNameValue to filename
			} catch (ArrayIndexOutOfBoundsException e) {//handle the exception if the user choice the wrong file
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("File Format Error");
				alert.setHeaderText("The Personal Trainer");
				alert.setContentText("Invalid Format in " + f.getName() + "\n"+ "Expected CSV format String, String, int, int, int, String, String");
				alert.showAndWait();
				ptView.setupWelcomeScreen();//after user press okay, take it back to the welcome screen
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	private class AboutHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("About");
			alert.setHeaderText("The Personal Trainer");
			alert.setContentText("Version 1.0 \nRelease 1.0\nCopyleft Java Nerds\nThis software is designed purely for educational purposes.\nNo commercial use intended");
			Image image = new Image(getClass().getClassLoader().getResourceAsStream(PT_IMAGE));
			ImageView imageView = new ImageView();
			imageView.setImage(image);
			imageView.setFitWidth(300);
			imageView.setPreserveRatio(true);
			imageView.setSmooth(true);
			alert.setGraphic(imageView);
			alert.showAndWait();
		}
	}
	
	//To be attached to Play menu item that will be activated only when a file is open.
	//You should not need to make any changes to this handler. 
	private class PlayWorkoutHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			player.pauseButton.setDisable(false);  //if last playlist was completed, the button would be disabled
			player.pauseButton.setText("Pause");  //if the player was closed with this button on Resume status, it needs to be reset back to "Pause"
			player.playWorkout(ptData.selectedExercises); //play the exercises stored in selectedExercises
		}
	}
	
	//write your event handlers' inner classes here
	private class UpdateButtonHandler implements EventHandler<KeyEvent> {
		@Override
		public void handle(KeyEvent event) {
			ptView.updateButton.setTextFill(Color.RED);//when user types in the notesTextArea, the text on updateButton is turned into red 
			int index = ptView.exerciseTableView.getSelectionModel().getSelectedIndex();
			ptView.updateButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
		        @Override
		        public void handle(MouseEvent event) {//after user clicks on updateButton, the changed notes are updated.
		        		ptData.selectedExercises.get(index).exerciseNotes.set(ptView.notesTextArea.getText());
					ptView.updateButton.setTextFill(Color.BLACK);// the text on the button is changed to black
		        }
		    });
		}			
	}
	private class SearchButtonHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			ptView.searchTextField.setStyle("-fx-text-inner-color: black;");//reset the text in searchTextField to black
			ptData.selectedExercises.clear();
			boolean flag = false;
			String search = ptView.searchTextField.getText().toLowerCase();
			for(int i = 0; i < ptData.masterData.size(); i++) {//search the word in masterData 
				if(ptData.masterData.get(i).name.toString().toLowerCase().contains(search) || 
						ptData.masterData.get(i).level.toString().toLowerCase().contains(search) || 
						ptData.masterData.get(i).exerciseNotes.toString().toLowerCase().contains(search)){
							flag = true;//if the search word is found, the flag turns into true
							ptData.selectedExercises.add(ptData.masterData.get(i)); // add the exercise with the search word to selectedObservables
				}
			}
			if(!flag) {//if the search word is not found
				ptView.searchTextField.setText("Sorry "+search+" not found!");
				ptView.searchTextField.setStyle("-fx-text-inner-color: red;");
				for(int i = 0; i < ptData.masterData.size(); i++) {//populate the table view with masterData 
					ptData.selectedExercises.add(ptData.masterData.get(i));
				}
			}
			loadSelectionGrid(ptData.selectedExercises); // reload the updated selectedObservables into exerciseTableView with loadSelectionGrid method
			ptView.exerciseTableView.scrollTo(ptData.selectedExercises.get(0)); 
			ptView.exerciseTableView.getSelectionModel().select(ptData.selectedExercises.get(0));
			WorkoutViewer.createViewer(ptData.selectedExercises.get(0).getImageFile()).view(ptView.imageStackPane);
			ptView.notesTextArea.setText(ptData.selectedExercises.get(0).getExerciseNotes());
		}
	}
	
	private class SaveMenuItemHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			FileChooser fc = new FileChooser();
			fc.setTitle("Save file");
			fc.getExtensionFilters().addAll(new ExtensionFilter("Text Files(*.csv)", "*.csv"), new ExtensionFilter("Text Files(*.xml)", "*.xml"), new ExtensionFilter("Text Files(*.txt)","*.txt"), new ExtensionFilter("All Files(*.*)", "*.*"));
			fc.setInitialDirectory(new File(PT_DATA_PATH));//save file in the same path as the file opened
			File f;
			if ((f = fc.showSaveDialog(mainStage)) != null) {// show the save file dialog
				CSVFiler saveFile = new CSVFiler();
				saveFile.writeData(ptData.selectedExercises, f);
			}	
			ptView.workoutNameValue.setText(f.getName());
		}
	}	
}
