package com.afeka.WarfareSimulator.View;

public interface AbstractWarView {

	static final String[] MENU = {
			"Add Launcher",
			"Add L Destructor",
			"Add M Destructor",
			"Launch Missile",
			"Intercept Missile",
			"Intercept M Launcher",
			"View Game Status",
			"Exit"};
	
	//actions 
	void addMissileLauncher(String launcherId, boolean isHidden);
	void addMissileDestructor(String mdId);
	void addLauncherDestructor(String ldId, String type); 
	
	public boolean exit();
	
	// launchers events
	void LauncherOnLaunchEvent(String launcherId, String missileId, int flytime);
	void LauncherOnHitEvent(String missileId);
	void LauncherOnInterceptedEvent(String missileId);
	void LauncherOnHiddenEvent(String launcherId, boolean hidden,boolean isDestroyed);
	
	// Missile Destructor events
	void missileDestructorInterceptEvent(String missileDestructorId, String missileId);
	void missileDestructorMissedEvent(String missileDestructorId, String missileId);
	
	// Launcher Destructor events
	void LauncherDestructorOnInterceptEvent(String launcherDestructorId, String destructedLauncherId);
	void LauncherDestructorOnMissedEvent(String launcherDestructorId,String destructedLauncherId);
	
	



	public void registerListener(WarViewEventListener listener); 
}
