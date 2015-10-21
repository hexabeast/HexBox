package com.hexabeast.sandbox;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class SoundManager {

		float factorMusic = 1;
		float factorFX = 1;
		public static SoundManager instance;
		
		public int musicnumber = 5;

		public int stoneSound = 0;
		public int woodSound = 1;
		public int metalSound = 2;
		public int dirtSound = 3;
		
		public float[] ambianceVolumes = {0.8f,1,0.7f,1,1};
		
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
			
			ambiance[1] = new MusicPlus(Gdx.audio.newMusic(Gdx.files.internal("music/ambiance2.mp3")));
			
			ambiance[2] = new MusicPlus(Gdx.audio.newMusic(Gdx.files.internal("music/ambiance3.mp3")));
			
			ambiance[3] = new MusicPlus(Gdx.audio.newMusic(Gdx.files.internal("music/ambiance4.mp3")));
			
			ambiance[4] = new MusicPlus(Gdx.audio.newMusic(Gdx.files.internal("music/ambiance5.mp3")));
			
			for(int i = 0; i<ambiance.length; i++)
			{
				ambiance[i].setLooping(false);
				ambiance[i].setVolume(ambianceVolumes[i]);
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
			playSound(sound,Math.max(0, (2000-distance)/2000)*volume, pitch, pan);
		}
		
		public void playSound(Sound sound, float volume, float pitch)
		{
			playSound(sound, volume, pitch, 0);
		}
		
		public void playSound(Sound sound, float volume, float pitch, float pan)
		{
			sound.play(volume*factorFX);
		}
		
		public void updateVolume()
		{
			factorMusic = (float)Math.pow((float)(Parameters.i.volume*Parameters.i.volumeMusic)/100f,2.5f);
			factorFX = (float)Math.pow((float)(Parameters.i.volume*Parameters.i.volumeFX)/100f,2.5f);
			menuTheme.setVolume(1*factorMusic);
			for(int i = 0; i<ambiance.length; i++)
			{
				ambiance[i].setVolume(ambianceVolumes[i]*factorMusic);
			}
		}
		
		public void playAmbiance()
		{
			oldPlay ++;
			if(oldPlay==musicnumber)oldPlay = 0;
			ambiance[playOrder[oldPlay]].play();
		}
}
