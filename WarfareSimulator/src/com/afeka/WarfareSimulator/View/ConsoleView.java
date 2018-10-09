package com.afeka.WarfareSimulator.View;

import java.util.Scanner;

import com.afeka.WarfareSimulator.Utils.WarStatistics;

public class ConsoleView implements AbstractWarView, Runnable{

	private WarViewEventListener warViewEventListener;
	private boolean isRunning = true;

	
	public ConsoleView()
	{
	}
	
	private static void printMenu() {
		for (int i = 0; i < MENU.length; i++) {
			System.out.println(i+1 + " - " + MENU[i]);
		}
		
	}
	
	private void selectMenu(int selection, Scanner s) {
		switch (selection) {
		case 1:
			warViewEventListener.addMissileLauncherToView();
			break;
		case 2:
			warViewEventListener.addLauncherDestructorToView();
			break;	
		case 3:
			warViewEventListener.addMissileDestructorToView();
			break;
		case 4:
			warViewEventListener.launchMissileToView();
			break;
		case 5: 
			warViewEventListener.interceptLauncherToView();
			break;
		case 6:			
			warViewEventListener.interceptMissileToView();
			break;
		case 7:
			System.out.println(WarStatistics.getInstance());
			break;
		case 8:
			warViewEventListener.exitFromView();
			stopThread();
		default:
			break;
		}
		
	}
	@Override
	public void addMissileLauncher(String launcherId, boolean isHidden) {
		System.out.println(launcherId);
		System.out.println(isHidden);
	}

	@Override
	public void addMissileDestructor(String mdId) {
		System.out.println(mdId);
	}

	@Override
	public void addLauncherDestructor(String ldId, String type) {
		System.out.println(ldId);
		System.out.println(type);
	}

	@Override
	public boolean exit() {
		stopThread();
		return true;
	}


	@Override
	public void LauncherOnLaunchEvent(String launcherId, String missileId, int flytime) {
		System.out.println(launcherId);
		System.out.println(missileId);
		System.out.println(flytime);
		
	}

	@Override
	public void LauncherOnHitEvent(String missileId) {
		System.out.println(missileId);
		
	}

	@Override
	public void LauncherOnInterceptedEvent(String missileId) {
		System.out.println(missileId);
		
	}

	@Override
	public void LauncherOnHiddenEvent(String launcherId, boolean hidden, boolean isDestroyed) {
		System.out.println(launcherId);
		System.out.println(hidden);
		System.out.println(isDestroyed);	
	}

	@Override
	public void missileDestructorInterceptEvent(String missileDestructorId, String missileId) {
		System.out.println(missileDestructorId);
		System.out.println(missileId);
	}

	@Override
	public void missileDestructorMissedEvent(String missileDestructorId, String missileId) {
		System.out.println(missileDestructorId);
		System.out.println(missileId);		
	}

	@Override
	public void LauncherDestructorOnInterceptEvent(String launcherDestructorId, String destructedLauncherId) {
		System.out.println(launcherDestructorId);
		System.out.println(destructedLauncherId);		
	}

	@Override
	public void LauncherDestructorOnMissedEvent(String launcherDestructorId, String destructedLauncherId) {
		System.out.println(launcherDestructorId);
		System.out.println(destructedLauncherId);			
	}

	@Override
	public void registerListener(WarViewEventListener listener) {
		warViewEventListener = listener;
	}

	@Override
	public void run() {
		Scanner s = new Scanner(System.in);
		int selection;
		while(isRunning){
			printMenu();
			selection =s.nextInt();
			selectMenu(selection,s);
		}
	}
	
	public void stopThread() {
		isRunning = false;
	}
	

}
