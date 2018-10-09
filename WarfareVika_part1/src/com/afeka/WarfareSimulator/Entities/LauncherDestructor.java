package com.afeka.WarfareSimulator.Entities;

import com.afeka.WarfareSimulator.Utils.WarLogger;
import com.afeka.WarfareSimulator.Utils.WarTimer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LauncherDestructor implements Runnable{

	private String id;
	private String type;
	private List<Launcher> destructedLuanchers;
	private boolean running;
	protected LauncherDestructorEventListener ldListener;
	
	public LauncherDestructor(String id, String type) {
		this.id = id;
		this.type = type;
		this.destructedLuanchers = Collections.synchronizedList(new ArrayList<>());
		this.running = false;
		WarLogger.getInstance().createLogFile(this,id, false);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<Launcher> getDestructedLuanchers() {
		return destructedLuanchers;
	}
	
	public void addDestructedLauncher(Launcher destructedLauncherToAdd) {
		destructedLuanchers.add(destructedLauncherToAdd);
		Collections.sort(destructedLuanchers);
	}

	public void setDestructedLuanchers(ArrayList<Launcher> destructedLuanchers) {
		this.destructedLuanchers = destructedLuanchers;
	}
	
	public synchronized void setRunning(Boolean running){
		this.running = running;
	}
	
	public synchronized Boolean getRunning() {
		return running;
	}
	
	public void registerListener(LauncherDestructorEventListener newListener) {
		ldListener = newListener;
	}
	
	public void interceptEvent(Launcher target) {
		if (ldListener != null)
		{
			ldListener.onInterceptEvent(this, target);
		}
	}
	
	public void missedEvent(Launcher target) {
		if (ldListener != null)
		{
			ldListener.onMissedEvent(this, target);
		}
	}

	@Override
	public void run() {
		setRunning(true);
		while (getRunning()) {
			if (!destructedLuanchers.isEmpty()) {
			Launcher destructedLuancher = destructedLuanchers.get(0);
				if (WarTimer.getInstance().getTime() >= destructedLuancher.getDestructTime()) {
					if (!destructedLuancher.isDestroyed())
					{
					destructedLuancher.destroy();

					if(destructedLuancher.isDestroyed()) {
						interceptEvent(destructedLuancher);
						WarLogger.getInstance().log(this,String.format("%s: launcher %s was destroyed!", 
								this.id, destructedLuancher.getId()));
					}
					else {
						missedEvent(destructedLuancher);
						WarLogger.getInstance().log(this,String.format("%s: faild to destroy launcher %s!", 
								this.id, destructedLuancher.getId()));
					}
					destructedLuanchers.remove(0);
					}
				}
			}
		}
		
	}

}
