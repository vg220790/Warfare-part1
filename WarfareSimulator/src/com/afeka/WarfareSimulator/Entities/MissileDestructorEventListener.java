package com.afeka.WarfareSimulator.Entities;

public interface MissileDestructorEventListener {
	void onInterceptEvent(MissileDestructor md, Missile targetMissile);
	void onMissedEvent(MissileDestructor md, Missile targetMissile);
}
