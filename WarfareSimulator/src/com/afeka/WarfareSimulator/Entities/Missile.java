package com.afeka.WarfareSimulator.Entities;
import java.lang.Thread;

import com.afeka.WarfareSimulator.Utils.WarStatistics;
import com.afeka.WarfareSimulator.Utils.WarTimer;

public class Missile implements Runnable, Comparable<Missile> {

	private String id;
	private String destination;
	private int launchTime;
	private int finishTime;
	private int flyTime;
	private double damage;
	private boolean isFlying;
	private boolean isDestroyed;
	private int destructAfterLaunch;

	public Missile(String id, String destination, int launchTime, int flyTime, double damage) {
		this.id = id;
		this.destination = destination;
		this.launchTime = launchTime;
		this.flyTime = flyTime;
		this.damage = damage;
		this.isFlying = false;
		this.isDestroyed = false;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public synchronized boolean isFlying() {
		return isFlying;
	}

	public synchronized void setFlying(boolean isFlying) {
		this.isFlying = isFlying;
	}

	public synchronized boolean isDestroyed() {
		return isDestroyed;
	}

	public synchronized void setDestroy(boolean isDestroyed) {
		this.isDestroyed = isDestroyed;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public int getLaunchTime() {
		return launchTime;
	}

	public void setLaunchTime(int launchTime) {
		this.launchTime = launchTime;
	}

	public int getFlyTime() {
		return flyTime;
	}

	public void setFlyTime(int flyTime) {
		this.flyTime = flyTime;
	}

	public double getDamage() {
		return damage;
	}

	public void setDamage(double damage) {
		this.damage = damage;
	}
	
	public int getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(int finishTime) {
		this.finishTime = finishTime;
	}

	@Override
	public void run() {
		setFlying(true);
		setLaunchTime(WarTimer.getInstance().getTime());
		WarStatistics.getInstance().addLaunch();
		
		try {
			Thread.sleep(this.flyTime * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		if(isDestroyed) {
			WarStatistics.getInstance().addDistroyMissile();
		}
		else {
			setFinishTime(WarTimer.getInstance().getTime());
			WarStatistics.getInstance().addHit();
			WarStatistics.getInstance().addDamage(this.damage);
		}
		setFlying(false);
	}

	
	public int getDestructAfterLaunch() {
		return destructAfterLaunch;
	}

	public void setDestructAfterLaunch(int destructAfterLaunch) {
		this.destructAfterLaunch = destructAfterLaunch;
	}

	public void destroy() {
		if(isFlying()) {
			setDestroy(true);
			setFinishTime(WarTimer.getInstance().getTime());
		}
	}
	
	@Override
	public int compareTo(Missile m) {
		return (this.getLaunchTime() + this.getDestructAfterLaunch() - m.getLaunchTime() + m.getDestructAfterLaunch() );
	}

	
//	@Override
//	public int compareTo(Missile m) {
//		return (this.launchTime - m.launchTime); 
//	}

}
