package com.afeka.WarfareSimulator.Utils;

import java.util.Timer;
import java.util.TimerTask;

public class WarTimer {
	private int currentTimeInSec;
	private Timer timer;
	private TimerTask task;

	// Singleton
	private static WarTimer instance = null;

	public static WarTimer getInstance() {
		if (instance == null) {
			instance = new WarTimer();
		}
		return instance;
	}
	
	protected WarTimer() {
		currentTimeInSec = 0;
		timer = new Timer();
		task = new TimerTask() {
			@Override
			public void run() {
				setTime(currentTimeInSec + 1);
			}
		};
	}



	public void start() {
		timer.scheduleAtFixedRate(task, 0, 1000);
	}

	public synchronized void setTime(int sec) {
		this.currentTimeInSec = sec;
	}

	public synchronized int getTime() {
		return this.currentTimeInSec;
	}
}
