package org.squidwrench.gameworks.squizgame;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
/*
Squidwrench.org
Brian Neugebauer 2012
Show splash image and play jingle
*/
public class Splash extends Activity{

	MediaPlayer splashMusic;
	@Override
	protected void onCreate(Bundle SGTime) {
		super.onCreate(SGTime);
		setContentView(R.layout.splash);
		splashMusic = MediaPlayer.create(Splash.this, R.raw.splashtune);
		splashMusic.start();
		Thread timer = new Thread(){
			@Override
			public void run(){
				try{
					sleep(3000);
				} catch(InterruptedException e) {
					e.printStackTrace();
				} finally {
					Intent openStartingPoint = new Intent("org.squidwrench.gameworks.squizgame.SELECTMODE");
					startActivity(openStartingPoint);
				}
			}
		};
		timer.start();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		splashMusic.release();
		finish();
	}
}
