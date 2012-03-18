package org.squidwrench.gameworks.squizgame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

/*
SquizGame
Squidwrench.org 2012
Brian Neugebauer, 

*/

public class SquizGame extends Activity implements SensorEventListener {
	public int numplayers = 1;
	public int numquestions = 10;
	public int questionNumber = 1;
	public String questionArray[][][] = new String[10][40][1];
	public int score = 0;
	public String playername = "X", startingplayer = "X";
	public int pointcount[] = new int[8]; //R1,R2,R3,C1,C2,C3,DD,DU = ways to win, 3 or -3 means a win
	public int squaremoves[] = new int[15]; //0 = first move, positive = O, negative = X, TL,TM,TR,ML,MM,MR,BL,BM,BR
	public boolean gameover = false, computeropponent = false, toe = false;
	public boolean firstOnResume = true;
	private SensorManager sensMgr;
	private Sensor accelerometer;
    private static SoundPool sounds;
    private static int xbeep, obeep, toebeep, gamewin, gametie, complaugh; 
    private static boolean sound = true;
    public Random rand = new Random();
    public boolean online = false;
    public int viewWidth = 0;
    public int viewHeight = 0;
    public int orient; //0 = vertical, 1 = horizontal
    private PopupWindow pwcredits;
    public final List<Integer> answerButtons = new ArrayList<Integer>(Arrays.asList(R.id.buta,R.id.butb,R.id.butc,R.id.butd)); 
    public TextView tvQuestion;
    public Button aBut, bBut, cBut, dBut;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	popup("hello");
        super.onCreate(savedInstanceState);    
		sensMgr = (SensorManager)getSystemService(SENSOR_SERVICE);
        accelerometer = sensMgr.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        setContentView(R.layout.main);
        
        //Load sounds 
        SharedPreferences settings = getSharedPreferences("PREF",0);
        sound = settings.getBoolean("sound", true);
        if (sound) {
        	loadSounds();
        }

        //Get Dimensions of screen and size buttons based on that
	    Display display = getWindowManager().getDefaultDisplay();
	    int dwidth = display.getWidth();
	    int dheight = display.getHeight();
	    int setsize, Qsize, Asize;
	    if (dheight > dwidth) {
	    	orient = 0; //vertical
	    	setsize = dwidth/3;
	    	Qsize = dheight/2;
	    	Asize = dheight/10;
	    }
	    else {
	    	orient = 1; //horizontal
	    	Qsize = dheight/2;
	    	Asize = dheight/6;
	    }
	    tvQuestion = (TextView) findViewById(R.id.textViewQuestion);
	    tvQuestion.getLayoutParams().height = Qsize;
	    aBut = (Button) findViewById(R.id.buta);
	    bBut = (Button) findViewById(R.id.butb);
	    cBut = (Button) findViewById(R.id.butc);
	    dBut = (Button) findViewById(R.id.butd);
	    aBut.getLayoutParams().height = Asize;
	    bBut.getLayoutParams().height = Asize;
	    cBut.getLayoutParams().height = Asize;
	    dBut.getLayoutParams().height = Asize;
	    aBut.setTextSize(Asize/2);
	    bBut.setTextSize(Asize/2);
	    cBut.setTextSize(Asize/2);
	    dBut.setTextSize(Asize/2);
		aBut.getBackground().setColorFilter(new LightingColorFilter(0xFFEEEEEE, 0xFFFF0000));
		bBut.getBackground().setColorFilter(new LightingColorFilter(0xFFEEEEEE, 0xFF0000FF));
		cBut.getBackground().setColorFilter(new LightingColorFilter(0xFFEEEEEE, 0xFF00FF00));
		dBut.getBackground().setColorFilter(new LightingColorFilter(0xFFEEEEEE, 0xFFFF00FF));
	    
		//showWhoseTurn();
		//showScore();
		 
    }
    
    public int findOrientation() {
	    Display display = getWindowManager().getDefaultDisplay();
	    if (display.getHeight() > display.getWidth()) {
	    	orient = 0; //vertical
	    	return 0;
	    }
	    else {
	    	orient = 1; //horizontal
	    	return 1;
	    }
    }
    
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
      //Save gamestate for orientation change or interruption by phonecall, etc
      savedInstanceState.putString("gmoves", Arrays.toString(squaremoves).replace("[", "").replace("]", "").replace(" ", ""));
      savedInstanceState.putBoolean("gameover", gameover);
      savedInstanceState.putString("playername", playername);
      savedInstanceState.putBoolean("vscomputer", computeropponent);
      savedInstanceState.putBoolean("toe", toe);
//      savedInstanceState.putInt("xwins", xscore);
//      savedInstanceState.putInt("owins", oscore);
//      savedInstanceState.putInt("twins", tscore);   
      super.onSaveInstanceState(savedInstanceState);
    }
    
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
      super.onRestoreInstanceState(savedInstanceState);
      // Restore UI state from the savedInstanceState.
      loadGame(savedInstanceState.getString("gmoves"),savedInstanceState.getBoolean("gameover"),savedInstanceState.getString("playername"),savedInstanceState.getBoolean("vscomputer"),savedInstanceState.getBoolean("toe"),savedInstanceState.getInt("xwins"),savedInstanceState.getInt("owins"),savedInstanceState.getInt("twins"));
    }
    
    @Override
	protected void onResume() {
    	super.onResume();
    	sensMgr.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }
    
    
    @Override
	protected void onPause() {
    	super.onPause();
    	sensMgr.unregisterListener(this);
    }
    

	public void buttonsClickable(boolean clickable) {
        aBut.setClickable(clickable);
        bBut.setClickable(clickable);
        cBut.setClickable(clickable);
        dBut.setClickable(clickable);
	}
	
	public void getQuestions(int numquestions, String category) {
		//Select * from question,answer,category.questioncategory where category = x, etc
		//populate question array
	}
	
	public void askQuestion() {
		Collections.shuffle(Arrays.asList(questionArray[questionNumber]));
		tvQuestion.setText(questionArray[questionNumber][0].toString());
		aBut.setText(questionArray[questionNumber][0].toString());
		aBut.setTag(questionArray[questionNumber][0][0]);
		bBut.setText(questionArray[questionNumber][1].toString());
		bBut.setTag(questionArray[questionNumber][1][0]);
		cBut.setText(questionArray[questionNumber][2].toString());
		cBut.setTag(questionArray[questionNumber][2][0]);
		dBut.setText(questionArray[questionNumber][3].toString());
		dBut.setTag(questionArray[questionNumber][3][0]);
	}

	public void chooseAnswer(View view) {
		chosenGlow(view.getId());
		//delay
		checkAnswer(view.getId());
    }
    
    public boolean checkForWinCondition() {  		
    	int check[] = pointcount.clone();
    	Arrays.sort(check);
    	if (check[0] == -3 || check[7] == 3)
    		return true;    		
    	return false;
    }
    
    public void checkAnswer(int buttonID) {
    	Button button = (Button) findViewById(buttonID);
    	if (button.getTag() == "1") {
    		//correct
    	}
    	else {
    		//incorrect
    	}
    		
    }

    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.replay, menu);
    	return true;
    }
    
    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
    		case R.id.startOver:
    			startOver();
    			return true;
    		case R.id.saveGame:
    			saveGame("SAVEGAME");
    			return true;
    		case R.id.loadGame:
    			SharedPreferences settings = getSharedPreferences("SAVEGAME", 0);        
//       			loadGame(settings.getString("gmoves",""),settings.getBoolean("gameover",gameover),settings.getString("playername",playername),settings.getBoolean("vscomputer",computeropponent),settings.getBoolean("toe",toe),settings.getInt("xwins",xscore),settings.getInt("owins",oscore),settings.getInt("twins",tscore));
    			return true;
    		case R.id.clearScore:
    			clearScore();
    			return true;
    		case R.id.credits:
    			showCredits();
    			return true;
    		case R.id.toggleSound:
    			toggleSound();
    			return true;
    		default:
    			return super.onOptionsItemSelected(item);
    	}
    }

	private void startOver() {
		gameover = false;

		Arrays.fill(pointcount, 0);
		Arrays.fill(squaremoves, 0);
		startingplayer = (startingplayer.equals("X")) ? "O" : "X";
		playername = startingplayer;
		showWhoseTurn();
		showScore();

	}

	public void showScore() {
		TextView tvs = (TextView) findViewById(R.id.textViewScore);
		tvs.setText("Score:   X:" + score + "   O:" + score + "   Tie:" + score);
	}
	
	public void showWhoseTurn() {
		TextView tv = (TextView) findViewById(R.id.textView1);
		tv.setText("Player " + playername + "'s turn");
	}
	
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		return;		
	}

	public void onSensorChanged(SensorEvent event) {
		if(event.values[0] > 5) {
		  startOver();
		}
		return;
	}
	
	public void popup(String debug) {
		CharSequence text = debug;
		int duration = Toast.LENGTH_LONG;
		Toast toast = Toast.makeText(this, text, duration);
		toast.show();
	}
	
	public void popup(int debug) {
		CharSequence text = Integer.toString(debug);
		int duration = Toast.LENGTH_LONG;
		Toast toast = Toast.makeText(this, text, duration);
		toast.show();
	}
 	

	public void correctGlow(int winner) {
		int glowcolor;
		Button button;
		button = (Button) findViewById(winner);
		button.getBackground().setColorFilter(new LightingColorFilter(0xFFEEEEEE, 0xFFFF0000));
	}
	
	public void chosenGlow(int chosen) {
		Button button;
		button = (Button) findViewById(chosen);
		button.getBackground().setColorFilter(new LightingColorFilter(0xFFEEEEEE, 0xFFFF0000));
	}

	public void saveGame(String preffilename) {		
	    SharedPreferences settings = getSharedPreferences(preffilename, 0);
	    SharedPreferences.Editor editor = settings.edit();
	    editor.putString("gmoves", Arrays.toString(squaremoves).replace("[", "").replace("]", "").replace(" ", ""));
		editor.putBoolean("gameover", gameover);
		editor.putString("playername", playername);
		editor.putBoolean("vscomputer", computeropponent);
	    editor.putBoolean("toe", toe);
//	    editor.putInt("xwins", xscore);
//	    editor.putInt("owins", oscore);
//	    editor.putInt("twins", tscore);
	    editor.commit();
	}
	
	public static void playSound(int soundid) {
	    if (sound) 
	    	sounds.play(soundid, 1, 1, 1, 0, 1);
	}
	
	public void toggleSound() {		
		sound = !sound; 
		if (sound)
			loadSounds();
	    SharedPreferences settings = getSharedPreferences("PREF", 0);
	    SharedPreferences.Editor editor = settings.edit();
		editor.putBoolean("sound", sound);
	    editor.commit();
	}
	
	public void loadSounds() {		
	    sounds = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
	    xbeep = sounds.load(this, R.raw.ttt_x, 1);
	    obeep = sounds.load(this, R.raw.ttt_o, 1);
	    toebeep = sounds.load(this, R.raw.toejam,1);
	    gamewin = sounds.load(this, R.raw.gamewin, 1);
	    gametie = sounds.load(this, R.raw.gametie, 1);
	    complaugh = sounds.load(this, R.raw.computerlaugh, 1);
	}
	
		  
	public void loadGame(String lmoves, boolean lgameover, String lplayername, boolean lvscomputer, boolean ltoe, int lxwins, int lowins, int ltwins) { 
    	try{
    		//Loadgame plays through the recorded moves 
			if(lmoves == "") return; //First time program runs
			
			//disable sound
			boolean remsound = sound;
			sound = false;
	        	        
	        computeropponent = lvscomputer;
		    CheckBox cb = (CheckBox) findViewById(R.id.checkBoxAI);
			if (computeropponent) {
				if (!cb.isChecked()) {
					playername = "X";
					cb.setChecked(true);
				}
			}
			else {
				if (cb.isChecked())
					cb.setChecked(false);
			}
			
	        toe = ltoe;
		    CheckBox cbtoe = (CheckBox) findViewById(R.id.checkBoxToe);
			if (toe) {
				if (!cbtoe.isChecked()) {
					cbtoe.setChecked(true);
				}
			}
			else {
				if (cbtoe.isChecked())
					cbtoe.setChecked(false);
			}
			
			startOver();
			
			String token = ",";
	        int[] convertedIntArray = StringToArrayConverter.convertTokenizedStringToIntArray(lmoves, token);
	        
	        boolean toehold = toe;
	        boolean comphold = computeropponent;
	                
	        toe = false;
	        computeropponent = false;
	    
	        ImageButton button;
	        Integer mov;
	        for (int s = 0; s < 15; s++) {
	            mov = convertedIntArray[s];
	            if (mov == 0) 
	            	break;
	            if (mov < 0)
	            	playername = "X";
	            else
	            	playername = "O";
	            
	            	button = (ImageButton) findViewById(Math.abs(mov));
	    	        button.performClick();
	            
	        }
	        
	        sound = remsound;
	        toe = toehold;
	        computeropponent = comphold;
	        
//	        xscore = Math.max(lxwins,xscore);
//	        oscore = Math.max(lowins,oscore);
//	        tscore = Math.max(ltwins,tscore);
	        showScore();
		
			gameover = lgameover;
			if (!gameover) {
				playername = lplayername;
				showWhoseTurn();
			}

    	}catch(NullPointerException e){
    		startOver();
    	}
	}
	
	public void clearScore() {
//		xscore = 0;
//		oscore = 0;
//		tscore = 0;
		showScore();
	}
	
	private void showCredits() {
		/*Show credits popup window when trigged from menu.*/
	    try {
	        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	        //Inflate the view from a predefined XML layout
	        View layout = inflater.inflate(R.layout.credits,(ViewGroup) findViewById(R.id.popup_credits));
	        // create a 300px width and 470px height PopupWindow
	        pwcredits = new PopupWindow(layout, 300, 470, true);
	        // display the popup in the center
	        pwcredits.showAtLocation(layout, Gravity.CENTER, 0, 0);
	 
	        layout.setOnClickListener(dismiss_credits);
	 
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	 
	private OnClickListener dismiss_credits = new OnClickListener() {
		/*Click anywhere to dismiss Credits popup window*/
	    public void onClick(View v) {
	        pwcredits.dismiss();
	    }
	};
 }
 