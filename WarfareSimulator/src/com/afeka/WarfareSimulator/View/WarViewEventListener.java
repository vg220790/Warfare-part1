package com.afeka.WarfareSimulator.View;

public interface WarViewEventListener {
	
	//actions 
	public boolean addMissileLauncherToView();
	public boolean addMissileDestructorToView();
	public boolean addLauncherDestructorToView();
	public boolean launchMissileToView();
	public boolean interceptMissileToView();
	public boolean interceptLauncherToView();
	
	
	public boolean exitFromView();
}
