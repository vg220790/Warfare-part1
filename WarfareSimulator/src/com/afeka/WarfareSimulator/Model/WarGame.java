package com.afeka.WarfareSimulator.Model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.afeka.WarfareSimulator.Entities.Launcher;
import com.afeka.WarfareSimulator.Entities.LauncherDestructor;
import com.afeka.WarfareSimulator.Entities.LauncherDestructorEventListener;
import com.afeka.WarfareSimulator.Entities.LauncherEventListener;
import com.afeka.WarfareSimulator.Entities.Missile;
import com.afeka.WarfareSimulator.Entities.MissileDestructor;
import com.afeka.WarfareSimulator.Entities.MissileDestructorEventListener;
import com.afeka.WarfareSimulator.Utils.WarLogger;
import com.afeka.WarfareSimulator.Utils.WarTimer;

public class WarGame implements LauncherEventListener,MissileDestructorEventListener, LauncherDestructorEventListener{
	
	// thread pool
	private ExecutorService executor;
	
	//Collections
	private Map<String, Launcher> missileLaunchers;
	private Map<String, MissileDestructor> missileDestructors;
	private Map<String, LauncherDestructor> launcherDestructors;
	private Map<String, Missile> allMissiles;
	private Map<String, Missile> activeMissiles;
	private Map<String, Launcher> activeLaunchers;
	
	// listener
	private WarGameEventListener warGameEventListener;

	
	public WarGame() {
		this.missileLaunchers = Collections.synchronizedMap(new HashMap<String, Launcher>());
		this.setMissileDestructors(Collections.synchronizedMap(new HashMap<String, MissileDestructor>()));
		this.setLauncherDestructors(Collections.synchronizedMap(new HashMap<String, LauncherDestructor>()));
		this.allMissiles = Collections.synchronizedMap(new HashMap<String, Missile>());
		this.executor = Executors.newFixedThreadPool(15);
		this.setActiveMissiles(Collections.synchronizedMap(new HashMap<String, Missile>()));
		this.setActiveLaunchers(Collections.synchronizedMap(new HashMap<String, Launcher>()));
		this.warGameEventListener = null;
		WarLogger.getInstance().createLogFile(this, "gameWareLog", true);
		WarTimer.getInstance().start();
	}

	public boolean addLauncher(String launcherId, boolean isAlwaysHidden) {
		if (missileLaunchers.containsKey(launcherId))
			return false;
		Launcher launcherToAdd = new Launcher(launcherId, isAlwaysHidden);
		launcherToAdd.registerListener(this); //
		missileLaunchers.put(launcherId, launcherToAdd);
		getActiveLaunchers().put(launcherId, launcherToAdd);
		
		executor.execute(launcherToAdd);
		warGameEventListener.addMissileLauncherToModel(launcherId, isAlwaysHidden);
		return true;
	}
	
	public boolean addMissileDestructor(String missileDestructorId) {
		MissileDestructor missileDestructorToAdd = new MissileDestructor(missileDestructorId);
		if (getMissileDestructors().containsKey(missileDestructorId))
			return false;
		missileDestructorToAdd.registerListener(this);
		getMissileDestructors().put(missileDestructorId, missileDestructorToAdd);
		executor.execute(missileDestructorToAdd);
		warGameEventListener.addMissileDestructorToModel(missileDestructorId);
		return true;
	}
	
	public boolean addLauncherDestructor(String launcherDestructorId, String type) {
		if (getLauncherDestructors().containsKey(launcherDestructorId))
			return false;
		LauncherDestructor launcherDestructorToAdd = new LauncherDestructor(launcherDestructorId, type);
		launcherDestructorToAdd.registerListener(this);
		getLauncherDestructors().put(launcherDestructorId, launcherDestructorToAdd);
		executor.execute(launcherDestructorToAdd);
		warGameEventListener.addLauncherDestructorToModel(launcherDestructorId, type);
		return true;
	}

	public boolean launchMissile(String launcherId , String missileId, String destination, int launchTime, int flyTime, double damage) {
		if (allMissiles.containsKey(missileId))
			return false;
		if (!getActiveLaunchers().containsKey(launcherId))
			return false;
		Missile missile = new Missile(missileId, destination, launchTime, flyTime, damage);
		allMissiles.put(missileId, missile);
		missileLaunchers.get(launcherId).addMissile(missile);
		getActiveMissiles().put(missileId, missile);
		return true;
	}

	public boolean interceptMissile(String missileId, String missileDestructorId, int destructAfterLaunch ) {
		if (!getActiveMissiles().containsKey(missileId))
			return false;
		if (!getMissileDestructors().containsKey(missileDestructorId))
			return false;
		
		Missile missileToDestruct = allMissiles.get(missileId);
		missileToDestruct.setDestructAfterLaunch(destructAfterLaunch);
		
		getMissileDestructors().get(missileDestructorId).addDestructedMissile(missileToDestruct);
		return true;
	}
	
	public boolean interceptLauncher(String launcherId, String launcherDestructorId, int destructTime) {
		if (!getActiveLaunchers().containsKey(launcherId))
			return false;
		if (!getLauncherDestructors().containsKey(launcherDestructorId))
			return false;
		
		Launcher launcherToDestruct =  missileLaunchers.get(launcherId);
		launcherToDestruct.setDestructTime(destructTime);
		
		getLauncherDestructors().get(launcherDestructorId).addDestructedLauncher(launcherToDestruct);
		return true;
	}
	
	public boolean exit() {
		executor.shutdown();
		
		for (Launcher entry : missileLaunchers.values()){
			entry.setRunning(false);
		}
		
		for (LauncherDestructor entry : getLauncherDestructors().values()){
			entry.setRunning(false);
		}
		
		for (MissileDestructor entry : getMissileDestructors().values()) {
			entry.setRunning(false);
		}
		
		while (!executor.isTerminated()) {}
		
		return true;	
	}
	
	public void registerListener(WarGameEventListener newListener) {
		warGameEventListener = newListener;
	}
	
	
	// launcher events
	@Override
	public void onLaunchEvent(Launcher lancher, Missile launchedMissile) {
		if (warGameEventListener != null) {
			warGameEventListener.LauncherOnLaunchEvent(lancher.getId(), launchedMissile.getId(), launchedMissile.getFlyTime());
		}
	}

	@Override
	public void onHitEvent(Missile hittedMissile) {
		if (warGameEventListener != null) {

		warGameEventListener.LauncherOnHitEvent(hittedMissile.getId());
		getActiveMissiles().remove(hittedMissile.getId());
		}
	}


	@Override
	public void onInterceptedEvent(Missile interceptedMissile) {
		if (warGameEventListener != null) {

		warGameEventListener.LauncherOnInterceptedEvent(interceptedMissile.getId());
		getActiveMissiles().remove(interceptedMissile.getId());
		}
	}

	
	@Override
	public void onHiddenEvent(Launcher lancher) {
		if (warGameEventListener != null) {
			warGameEventListener.LauncherOnHiddenEvent(lancher.getId(), lancher.isHidden(), lancher.isDestroyed());
		}
	}

	// missile Destructor events
	@Override
	public void onInterceptEvent(MissileDestructor md,Missile targetMissile) {
		if (warGameEventListener != null) {
			warGameEventListener.missileDestructorInterceptEvent(md.getId(), targetMissile.getId());
		}
		
	}

	@Override
	public void onMissedEvent(MissileDestructor md,Missile targetMissile) {
		if (warGameEventListener != null) {
			warGameEventListener.missileDestructorMissedEvent(md.getId(), targetMissile.getId());
			
		}
		
	}

	// launcher destructor events
	@Override
	public void onInterceptEvent(LauncherDestructor ld, Launcher target) {
		if (warGameEventListener != null) {
			warGameEventListener.LauncherDestructorOnInterceptEvent(ld.getId(), target.getId());
		}
		
	}

	@Override
	public void onMissedEvent(LauncherDestructor ld, Launcher target) {
		if (warGameEventListener != null) {
			warGameEventListener.LauncherDestructorOnMissedEvent(ld.getId(),target.getId());
		}
		
	}

	public Map<String, LauncherDestructor> getLauncherDestructors() {
		return launcherDestructors;
	}

	public void setLauncherDestructors(Map<String, LauncherDestructor> launcherDestructors) {
		this.launcherDestructors = launcherDestructors;
	}

	public Map<String, Launcher> getActiveLaunchers() {
		return activeLaunchers;
	}

	public void setActiveLaunchers(Map<String, Launcher> activeLaunchers) {
		this.activeLaunchers = activeLaunchers;
	}

	public Map<String, MissileDestructor> getMissileDestructors() {
		return missileDestructors;
	}

	public void setMissileDestructors(Map<String, MissileDestructor> missileDestructors) {
		this.missileDestructors = missileDestructors;
	}

	public Map<String, Missile> getActiveMissiles() {
		return activeMissiles;
	}

	public void setActiveMissiles(Map<String, Missile> activeMissiles) {
		this.activeMissiles = activeMissiles;
	}
}
