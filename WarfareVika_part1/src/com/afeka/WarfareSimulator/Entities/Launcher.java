package com.afeka.WarfareSimulator.Entities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.afeka.WarfareSimulator.Utils.WarLogger;
import com.afeka.WarfareSimulator.Utils.WarStatistics;
import com.afeka.WarfareSimulator.Utils.WarTimer;

public class Launcher implements Runnable, Comparable<Launcher> {

	private String id;
	private boolean isHidden;
	private boolean isAlwaysHidden;
	private List<Missile> missiles;
	private boolean isDestroyed;
	private boolean running;
	private LauncherEventListener lListener;
	private int destructTime;
	public static final MAX = 100;


	public Launcher(String id, boolean isAlwaysHidden) {
		this.id = id;
		this.isAlwaysHidden = isAlwaysHidden;
		this.missiles = Collections.synchronizedList(new ArrayList<>());
		this.isDestroyed = false;
		this.running = false;
		WarLogger.getInstance().createLogFile(this,id, false);
		lListener = null;
		setHidden(false);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public synchronized boolean isHidden() {
		return isHidden;
	}

	public synchronized void setHidden(boolean isLanch) {
		if (isLanch)
			this.isHidden = false;
		else if (this.isAlwaysHidden)
			this.isHidden = true;
		else
			this.isHidden = false;
		hiddenEvent(isHidden);
	}
	
	public void addMissile(Missile missileToAdd) {
		missiles.add(missileToAdd);
		Collections.sort(missiles);
	}

	public List<Missile> getMissiles() {
		return missiles;
	}

	public void setMissiles(List<Missile> missiles) {
		this.missiles = missiles;
	}
	
	public synchronized boolean isDestroyed() {
		return isDestroyed;
	}

	public synchronized void setDestroyed(boolean isDestroyed) {
		this.isDestroyed = isDestroyed;
	}

	public synchronized void setRunning(Boolean running){
		this.running = running;
	}
	
	public synchronized Boolean getRunning() {
		return running;
	}
	
	
	public void registerListener(LauncherEventListener newListener) {
		lListener = newListener;
	}
	
	public void launchEvent(Missile launchedMissile) {
		if (lListener != null)
		{
			lListener.onLaunchEvent(this, launchedMissile);
		}
	}
	
	public void hitEvent(Missile landMissile) {
		if (lListener != null)
		{
			lListener.onHitEvent(landMissile);
		}
	}
	
	
	public void hiddenEvent(boolean hidden) {
		if (lListener != null)
		{
			lListener.onHiddenEvent(this);
		}
	}
	
	public void InterceptedEvent(Missile interceptedMissile){
		if (lListener != null)
		{
			lListener.onInterceptedEvent(interceptedMissile);
		}
	}
	
	@Override
	public void run() {
		setRunning(true);

		while(!this.isDestroyed() && getRunning()) {
			if (!missiles.isEmpty()) {
			//Iterator<Missile> i = missiles.iterator();
			   Missile missile = missiles.get(0);
				if (WarTimer.getInstance().getTime() >= missile.getLaunchTime()) {
					setHidden(true);
					Thread mt = new Thread(missile);
					mt.start();
					launchEvent(missile);
					try {
						mt.join();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					if(missile.isDestroyed()) {
						InterceptedEvent(missile);
						WarLogger.getInstance().log(this,String.format("%s: missile %s was destroyed! launch time: %d destroy time: %d", 
								this.id, missile.getId(), missile.getLaunchTime(), missile.getFinishTime()));
					}
					else {
						hitEvent(missile);
						WarLogger.getInstance().log(this,String.format("%s: missile %s hit the target! destination: %s "
								+ "launch time: %d hit time: %d damage: %f", 
								this.id, missile.getId(), missile.getDestination().toString(), missile.getLaunchTime(), 
								missile.getFinishTime(), missile.getDamage()));
					}
					setHidden(false);
					missiles.remove(0);
				}
			}
		}
	}

	public int getDestructTime() {
		return destructTime;
	}

	public void setDestructTime(int destructTime) {
		this.destructTime = destructTime;
	}

	public void destroy() {
		if(!isHidden()) {
			WarStatistics.getInstance().addDistroylauncher();
			setDestroyed(true);
		}
	}
	
	@Override
	public int compareTo(Launcher l) {
		return (this.destructTime-l.destructTime);
	}
}
