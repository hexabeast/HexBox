package com.hexabeast.sandbox;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class SoundManager {

		
		public static SoundManager instance;
		
		public int musicnumber = 5;

		public int stoneSound = 0;
		public int woodSound = 1;
		public int metalSound = 2;
		public int dirtSound = 3;
		
		int oldPlay = 0;
		
		public Sound[] Break = new Sound[4];
		public Sound PickUp;
		
		public Sound click;
		
		public MusicPlus menuTheme;
		public MusicPlus[] ambiance;
		public int[] playOrder;
		public SoundManager() 
		{
			ambiance = new MusicPlus[musicnumber];
			playOrder = new int[ambiance.length];
			
			for(int i = 0; i<ambiance.length; i++)
			{
				playOrder[i] = i;
			}
			
			Tools.shuffleArray(playOrder);
			
			Break[stoneSound] = Gdx.audio.newSound(Gdx.files.internal("sound/pierre.wav"));
			Break[woodSound] = Gdx.audio.newSound(Gdx.files.internal("sound/bois.wav"));
			Break[metalSound] = Gdx.audio.newSound(Gdx.files.internal("sound/fer.wav"));
			Break[dirtSound] = Gdx.audio.newSound(Gdx.files.internal("sound/terre.wav"));
			PickUp = Gdx.audio.newSound(Gdx.files.internal("sound/pickup.wav"));
			click = Gdx.audio.newSound(Gdx.files.internal("sound/click.mp3"));
			menuTheme = new MusicPlus(Gdx.audio.newMusic(Gdx.files.internal("music/themeMenu.mp3")));
			menuTheme.setLooping(true);
			menuTheme.setVolume(1);

			ambiance[0] = new MusicPlus(Gdx.audio.newMusic(Gdx.files.internal("music/ambiance1.mp3")));
			ambiance[0].setLooping(false);
			ambiance[0].setVolume(0.8f);
			
			ambiance[1] = new MusicPlus(Gdx.audio.newMusic(Gdx.files.internal("music/ambiance2.mp3")));
			ambiance[1].setLooping(false);
			ambiance[1].setVolume(1);
			
			ambiance[2] = new MusicPlus(Gdx.audio.newMusic(Gdx.files.internal("music/ambiance3.mp3")));
			ambiance[2].setLooping(false);
			ambiance[2].setVolume(0.7f);
			
			ambiance[3] = new MusicPlus(Gdx.audio.newMusic(Gdx.files.internal("music/ambiance4.mp3")));
			ambiance[3].setLooping(false);
			ambiance[3].setVolume(1);
			
			ambiance[4] = new MusicPlus(Gdx.audio.newMusic(Gdx.files.internal("music/ambiance5.mp3")));
			ambiance[4].setLooping(false);
			ambiance[4].setVolume(1);
			
			for(int i = 0; i<ambiance.length; i++)
			{
				ambiance[i].setOnCompletionListener(new Music.OnCompletionListener(){
			        @Override
			        public void onCompletion(Music aMusic){  
			            playAmbiance();
			        }
			    }
			    ); 
			}
		}
		
		public void playSound(Sound sound, float volume, float pitch, float x, float y)
		{
			x = x-GameScreen.player.PNJ.middle.x;
			y = y-GameScreen.player.PNJ.middle.y;
			float distance = (float) Math.sqrt(x*x+y*y);
			
			float pan = Math.min(0.5f, Math.abs(x)/1500f)*(x/Math.abs(x));
			sound.play(Math.max(0, (2000-distance)/2000)*volume, pitch, pan);
		}
		
		public void playAmbiance()
		{
			oldPlay ++;
			if(oldPlay==musicnumber)oldPlay = 0;
			ambiance[playOrder[oldPlay]].play();
		}
}
