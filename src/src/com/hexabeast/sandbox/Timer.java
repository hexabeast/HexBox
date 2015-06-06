package com.hexabeast.sandbox;

public class Timer {
	public float rate = 0;
	public float last = 0;
	
	public Timer(float r)
	{
		last = Main.time;
		rate = r;
	}
	
	public boolean check()
	{
		if(last+rate<Main.time)
		{
			reboot();
			return true;
		}
		return false;
	}
	
	public int multicheck()
	{
		float total = Main.time-last;
		int c = 0;
		while(rate<total)
		{
			total-=rate;
			reboot();
			c++;
		}
		return c;
	}
	
	public boolean check(float r)
	{
		if(last+r<Main.time)
		{
			reboot();
			return true;
		}
		return false;
	}
	
	public boolean checkNoReboot()
	{
		if(last+rate<Main.time)
		{
			return true;
		}
		return false;
	}
	
	public void reboot()
	{
		last = Main.time;
	}
}
