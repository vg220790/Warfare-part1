package com.afeka.WarfareSimulator.Entities;

public interface LauncherDestructorEventListener {
	void onInterceptEvent(LauncherDestructor ld, Launcher target);
	void onMissedEvent(LauncherDestructor ld , Launcher target);
}
