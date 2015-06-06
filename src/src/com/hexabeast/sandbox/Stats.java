package com.hexabeast.sandbox;

public class Stats {
public float defense;
public float speed;
public float jump;

public Stats()
{
	reboot();
}

public void reboot()
{
	defense = 0;
	speed = 0;
	jump = 0;
}

}
