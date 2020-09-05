package com.afeka.WarfareSimulator.Entities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.afeka.WarfareSimulator.Utils.WarLogger;
import com.afeka.WarfareSimulator.Utils.WarTimer;

public class MissileDestructor implements Runnable {

	private String id;
	private List<Missile> destructedMissiles;
	private boolean running;
	protected MissileDestructorEventListener mlListener;


	public MissileDestructor(String id) {
		this.id = id;
		this.destructedMissiles = Collections.synchronizedList(new ArrayList<>());
		this.running = false;
		WarLogger.getInstance().createLogFile(this,id, false);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<Missile> getDestructedMissiles() {
		return destructedMissiles;
	}
	
	public void addDestructedMissile(Missile destructedMissileToAdd) {
		destructedMissiles.add(destructedMissileToAdd);
		Collections.sort(destructedMissiles);
	} 

	public void setDestructedMissiles(ArrayList<Missile> destructedMissiles) {
		this.destructedMissiles = destructedMissiles;
	}

	public synchronized void setRunning(Boolean running){
		this.running = running;
	}
	
	public synchronized Boolean getRunning() {
		return running;
	}
	
	public void registerListener(MissileDestructorEventListener newListener) {
		mlListener = newListener;
	}
	
	public void interceptEvent(Missile targetMissile) {
		if (mlListener != null)
		{
			mlListener.onInterceptEvent(this, targetMissile);
		}
	}
	
	public void missedEvent(Missile targetMissile) {
		if (mlListener != null)
		{
			mlListener.onMissedEvent(this, targetMissile);
		}
	}
	
	
	@Override
	public void run() {

		int check = 0;
		check ++;
		ArrayList<String> a = new ArrayList<String>();

		setRunning(true);
		while (getRunning()) {
			if (!destructedMissiles.isEmpty()) {
				Missile destructedMissile = destructedMissiles.get(0);
				if (WarTimer.getInstance().getTime() >= (destructedMissile.getLaunchTime() + destructedMissile.getDestructAfterLaunch())) {
					destructedMissile.destroy();
					if(destructedMissile.isDestroyed()) {
						interceptEvent(destructedMissile);
						WarLogger.getInstance().log(this, String.format("%s: missile %s was destroyed!", 
								this.id, destructedMissile.getId()));
					}
					else {
						missedEvent(destructedMissile);
						WarLogger.getInstance().log(this, String.format("%s: faild to destroy missile %s! damage: %f", 
								this.id, destructedMissile.getId(), destructedMissile.getDamage()));
					}
					destructedMissiles.remove(0);
				}
		}
		}
		
	}
	
}
