package com.hexabeast.sandbox;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Music.OnCompletionListener;

public class MusicPlus{
	public Music m;
	public float iniVol = 1;
	public float tempVol = 1;
	
	
	public MusicPlus(Music mp)
	{
		m = mp;
	}
	
	public void play() {
		m.play();
	}

	public void pause() {
		m.pause();
	}

	public void stop() {
		m.stop();
	}
	
	public void coolStop()
	{
		new Thread
		(
			new Runnable()
			{
				@Override
				public void run()
				{
					while(tempVol>0.05f)
					{
						tempVol -= 0.05f;
						m.setVolume(tempVol);
						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					stop();
					setVolume(iniVol);
				}
			}
		).start();
	}

	public boolean isPlaying() {
		
		return m.isPlaying();
	}

	public void setLooping(boolean isLooping) {
		m.setLooping(isLooping);
	}

	public boolean isLooping() {

		return m.isLooping();
	}

	public void setVolume(float volume) {
		iniVol = volume;
		m.setVolume(volume);
	}

	public float getVolume() {

		return m.getVolume();
	}

	public void setPan(float pan, float volume) {
	
		m.setPan(pan,volume);
	}

	public float getPosition() {
	
		return m.getPosition();
	}

	public void dispose() {
		m.dispose();
	}

	public void setOnCompletionListener(OnCompletionListener listener) {
		m.setOnCompletionListener(listener);
	}

	

}
