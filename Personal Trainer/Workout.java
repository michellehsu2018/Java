package hw3;

import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Workout {
	int timeSpent, caloriesBurned;
	public ObservableList<Exercise> buildWorkoutPlan(List<Exercise> exercisesAll, int timeInput, int caloriesInput) {
		// write your code here
		timeSpent = 0;
		caloriesBurned = 0;
		int totalTime = 0;
		int totalCalories = 0;
		boolean meetTarget = false;
		ObservableList<Exercise> tempResult = FXCollections.observableArrayList();
		for(int i = 0; i < exercisesAll.size(); i++) {
			totalTime += exercisesAll.get(i).getRepTime();//calculate the time needed for a complete one round of all the exercise
			totalCalories += exercisesAll.get(i).getCalories();//calculate the calories burned for a complete one round of all the exercise
		}
		if((timeInput <= totalTime) && (caloriesInput <= totalCalories)) {//user input time and calories less than or equal to 13 and 100
			int count = 0; //count the lines that meets both the time and calories target
			for(int i = 0; i < exercisesAll.size(); i++) {
				if(timeInput > timeSpent || caloriesInput > caloriesBurned) {//check whether the iteration meets the target
					timeSpent += exercisesAll.get(i).repTime.intValue();
					caloriesBurned += exercisesAll.get(i).calories.intValue();
					count ++;
				}
			}
			for(int j = 0; j < count; j++) { //store the data that meets the target into tempResult
				tempResult.add(new Exercise(exercisesAll.get(j).getName(),exercisesAll.get(j).getLevel()
						                   ,exercisesAll.get(j).repTime.intValue(),exercisesAll.get(j).repCount.intValue()
						                   ,exercisesAll.get(j).calories.intValue(), exercisesAll.get(j).getImageFile()
						                   ,exercisesAll.get(j).getExerciseNotes()));
			}
		}else{
			for(int j = 0; j < exercisesAll.size(); j++) {
				tempResult.add(new Exercise(exercisesAll.get(j).getName(),exercisesAll.get(j).getLevel()
		                   ,exercisesAll.get(j).repTime.intValue(),exercisesAll.get(j).repCount.intValue()
		                   ,exercisesAll.get(j).calories.intValue(), exercisesAll.get(j).getImageFile()
		                   ,exercisesAll.get(j).getExerciseNotes()));
			}
			int[] round = new int[exercisesAll.size()];//use to store the round needed for each exercise
			int[] tempTime = new int[exercisesAll.size()];//use to temporarily store the total time needed for each exercise
			int[] tempCalories = new int[exercisesAll.size()];//use to temporarily store the total calories burned for each exercise
			int[] tempCount = new int[exercisesAll.size()];//use to temporarily store the total count needed for each exercise
			for(int h = 0; h < exercisesAll.size(); h++) {//start each exercise with one round
				round[h]=1;
			}
			while(meetTarget!=true) {
				if((timeInput > timeSpent) || (caloriesInput > caloriesBurned)) {
					for(int k=0; k < exercisesAll.size(); k++) {
						timeSpent += exercisesAll.get(k).repTime.intValue();
						caloriesBurned+= exercisesAll.get(k).calories.intValue();
						tempTime[k] = exercisesAll.get(k).repTime.intValue()*round[k];
						tempCalories[k] = exercisesAll.get(k).calories.intValue()*round[k];
						tempCount[k] = exercisesAll.get(k).repCount.intValue()*round[k];
						if((timeInput <= timeSpent) && (caloriesInput <= caloriesBurned)){//if both time and calories meet the standard	
							break; //if both time and calories meet the standard, break out of the loop
						}else { //keep on adding rounds for each exercise if either of the standard is not met	
							round[k]++;
						}
					}	
				}else {
					meetTarget=true;
				}
			}
			for(int i = 0; i < exercisesAll.size(); i++) {// load the total time, calories, counts for each exercise
				
				tempResult.get(i).repTime.set(tempTime[i]); 
				tempResult.get(i).calories.set(tempCalories[i]);
				tempResult.get(i).repCount.set(tempCount[i]);
			}
		}
		return tempResult;	
	}
}
