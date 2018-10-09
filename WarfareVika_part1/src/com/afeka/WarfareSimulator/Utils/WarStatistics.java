package com.afeka.WarfareSimulator.Utils;

public class WarStatistics {

	private int launch_missile_number;
	private int destroy_missile_number;
	private int hit_missile_number;
	private int destroy_launcher_number;
	private double total_damage;
	
	//Singleton
	static WarStatistics instance = null;
	
	private WarStatistics() {
		this.destroy_missile_number = 0;
		this.destroy_launcher_number = 0;
		this.hit_missile_number = 0;
		this.launch_missile_number = 0;
		this.total_damage = 0;
	}
	
	public static WarStatistics getInstance() {
		if (instance == null)
			instance = new WarStatistics();
		
		return instance;
	}
	
	public void addHit() {
		this.hit_missile_number++;
	}
	
	public void addLaunch() {
		this.launch_missile_number++;
	}
	
	public void addDistroyMissile() {
		this.destroy_missile_number++;
	}
	
	public void addDistroylauncher() {
		this.destroy_launcher_number++;
	}
	
	public void addDamage(double damage) {
		this.total_damage += damage;
	}
	
	public int getLaunch_missile_number() {
		return launch_missile_number;
	}

	public int getDestroy_missile_number() {
		return destroy_missile_number;
	}

	public int getHit_missile_number() {
		return hit_missile_number;
	}

	public int getDestroy_launcher_number() {
		return destroy_launcher_number;
	}

	public double getTotal_damage() {
		return total_damage;
	}
	
	@Override
	public String toString() {
		return "War Statistics: " +
	" | Launch missiles: " + launch_missile_number +
	" | Intercepted missiles: " + destroy_missile_number +
	" | Hit missiles: " + hit_missile_number + 
	" | Destroyed launchers: " + destroy_launcher_number +
	" | Total demage: " + total_damage;
	}
}
