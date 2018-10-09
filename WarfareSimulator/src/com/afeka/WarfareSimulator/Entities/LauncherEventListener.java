package com.afeka.WarfareSimulator.Entities;

public interface LauncherEventListener {
	void onLaunchEvent(Launcher lancher, Missile launchedMissile);
	void onHitEvent(Missile hittedMissile);
	void onInterceptedEvent(Missile interceptedMissile);
	void onHiddenEvent(Launcher lancher);
}
