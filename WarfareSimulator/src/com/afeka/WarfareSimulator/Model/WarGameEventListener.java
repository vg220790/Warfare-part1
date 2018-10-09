package com.afeka.WarfareSimulator.Model;


public interface WarGameEventListener {

	//actions
	void addMissileLauncherToModel(String launcherId, boolean isHidden);
	void addMissileDestructorToModel(String mdId);
	void addLauncherDestructorToModel(String ldId, String type);
	
	//void exitFromModel();
	//void loadFromJasonToModel(String fileName);
	
	
	// launchers events
	void LauncherOnLaunchEvent(String launcherId, String missileId, int flytime);
	void LauncherOnHitEvent(String missileId);
	void LauncherOnInterceptedEvent(String missileId);
	void LauncherOnHiddenEvent(String launcherId, boolean hidden, boolean isDestroyed);
	
	// Missile Destructor events
	void missileDestructorInterceptEvent(String missileDestructorId, String missileId);
	void missileDestructorMissedEvent(String missileDestructorId, String missileId);
	
	// Launcher Destructor events
	void LauncherDestructorOnInterceptEvent(String launcherDestructorId, String destructedLauncherId);
	void LauncherDestructorOnMissedEvent(String launcherDestructorId,String destructedLauncherId);
	
}
