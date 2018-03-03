package hw3;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CSVFiler extends DataFiler{

	@Override
	public ObservableList<Exercise> readData(String filename) {
		//enter your code here from HW2
		ObservableList<Exercise> workoutObservableList = FXCollections.observableArrayList(); //create new observable array-list object with type exercise
		String line = null;
		try {
			Scanner input = new Scanner(new File(filename));
			while(input.hasNext()) {
				line = input.nextLine();
				String[] values = line.split(",");
				/*Load the data into Exercise object*/
				Exercise exercise = new Exercise(values[0], values[1], Integer.parseInt(values[2]), Integer.parseInt(values[3]), Integer.parseInt(values[4]), values[5], values[6] );
				/*Add the loaded Exercise object to observable array-list*/
				workoutObservableList.add(exercise);
			}
			input.close();
		}catch(FileNotFoundException e) {
			e.printStackTrace();
		}
		return workoutObservableList;
	}

	public void writeData(ObservableList<Exercise> selectedExercises, File file) {
		// enter your code here for HW3
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(file));
			for(Exercise exercise : selectedExercises) {//convert elements in selectedExercises into string for BufferedWriter to write
				String text = exercise.getName() + "," + exercise.getLevel() + "," + exercise.getRepTime() + 
						"," + exercise.getRepCount() + "," + exercise.getCalories() + 
						"," + exercise.getImageFile() + "," + exercise.getExerciseNotes() + "\n";
				out.write(text);
			}
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
