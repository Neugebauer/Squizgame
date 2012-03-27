package org.squidwrench.gameworks.squizgame;

import java.util.Arrays;

public class Team {
	private int score = 0;  
	private int answerChosenPlayer[] = new int[4];
	private int answerChosenTeam = 0;
	private float elapsedTime = 0;
	private float totalTime = 0;
	
	public void reset() {
		score = 0;  
		Arrays.fill(answerChosenPlayer, 0);
		answerChosenTeam = 0;
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
		answerChosenPlayer[chosen] += 1;
	}
	
	public int[] getAnswerChosenPlayer() {
		return answerChosenPlayer;
	}
	
	public int getAnswerChosenTeam() {
		int max = 0, choice = 0;
		for(int a = 0;a < 4; a++) {
			if (answerChosenPlayer[a] > max) {
				max = answerChosenPlayer[a];
				choice = a;
			}
		}
		answerChosenTeam = choice;
		return answerChosenTeam;
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