package org.squidwrench.gameworks.squizgame;

public class Player {
	private int score = 0;  
	private int answerChosen = 0;
	private float elapsedTime = 0;
	private float totalTime = 0;
	
	public void reset() {
		score = 0;  
		answerChosen = 0;
		elapsedTime = 0;
		totalTime = 0;
	}
	
	public void setScore(int points) {
		score += points;
	}
	
	public int getScore() {
		return score;
	}
	
	public void setAnswerChosen(int chosen) {
		answerChosen = chosen;
	}
	
	public int getAnswerChosen() {
		return answerChosen;
	}
	
	public void setElapsedTime(float elapsedSecs) {
		elapsedTime = elapsedSecs;
		totalTime += elapsedTime;
	}
	
	public float getElapsedTime() {
		return elapsedTime;
	}
	
	public float getTotalTime() {
		return totalTime;
	}
}