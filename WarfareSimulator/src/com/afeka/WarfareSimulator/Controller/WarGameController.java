package com.afeka.WarfareSimulator.Controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import com.afeka.WarfareSimulator.Entities.Launcher;
import com.afeka.WarfareSimulator.Entities.LauncherDestructor;
import com.afeka.WarfareSimulator.Entities.Missile;
import com.afeka.WarfareSimulator.Entities.MissileDestructor;
import com.afeka.WarfareSimulator.Model.WarGame;
import com.afeka.WarfareSimulator.Model.WarGameEventListener;
import com.afeka.WarfareSimulator.Utils.WarTimer;
import com.afeka.WarfareSimulator.View.AbstractWarView;
import com.afeka.WarfareSimulator.View.WarViewEventListener;

public class WarGameController implements WarGameEventListener, WarViewEventListener{
	
	private WarGame model;
	private AbstractWarView view;
	
	public WarGameController(AbstractWarView view, WarGame model) {
		this.model = model;
		this.view = view;
		
		this.model.registerListener(this);
		this.view.registerListener(this);
	}
	
	// vars for generators
	private int missile_id = 0;
	private int launcher_id = 0;
	private int missileDestructor_id=0;
	private int launcherDestructor_id=0;
	private ArrayList<String> locations = new ArrayList<String>(Arrays.asList("Beer-Sheva", "Ofakim", "Sderot"));
	
	public WarGame getModel() {
		return model;
	}
	
	// Generators
	public boolean generateMissileLauncher() {
			Boolean random_isHidden = new Random().nextBoolean();
			return model.addLauncher("L" + (++launcher_id), random_isHidden);
	}
	
	public boolean generateMissileDestructor() {
		return model.addMissileDestructor("D" + (++missileDestructor_id));
	}
	
	public boolean generateLauncherDestructor() {
		String[] types = { "plane", "ship" };
		String plane_or_ship = types[new Random().nextInt(types.length)];
		return model.addLauncherDestructor("LD" + (++launcherDestructor_id), plane_or_ship);
	}
	
	public boolean generatelaunchMissile() {
		Random generator = new Random();
		Object[] values = model.getActiveLaunchers().values().toArray();
		Launcher generatedLauncher = (Launcher) values[generator.nextInt(values.length)];
			String generatedMissileId = "M" + (++missile_id);
			String generatedLocation = locations.get(new Random().nextInt(locations.size()));
			double generatedDamage = (new Random().nextInt(10) + 1) * 500;
			int generatedFlyTime = (new Random().nextInt(12) + 1);
			int generatedLaunchTime = WarTimer.getInstance().getTime()+1;
			return model.launchMissile(generatedLauncher.getId(), generatedMissileId, generatedLocation, generatedLaunchTime, generatedFlyTime, generatedDamage);
	}
	
	public boolean generateInterceptMissile() {
		Random generator = new Random();
		Object[] values = model.getActiveMissiles().values().toArray();
		Missile generatedMissile = (Missile) values[generator.nextInt(values.length)];
		
		Object[] values2 = model.getMissileDestructors().values().toArray();
		MissileDestructor generatedMissileDestructor = (MissileDestructor) values2[generator.nextInt(values2.length)];
		
		int generatedDestructAfterLaunch = (new Random().nextInt(5) + 1);
		//System.out.println(generatedMissile.getId() + " " + generatedMissileDestructor.getId() + " " + generatedDestructAfterLaunch);
		return model.interceptMissile(generatedMissile.getId(), generatedMissileDestructor.getId(), generatedDestructAfterLaunch);
	}
	
	
	public boolean generateInterceptLauncher() {
		Random generator = new Random();
		Object[] values = model.getActiveLaunchers().values().toArray();
		Launcher generatedLauncher = (Launcher) values[generator.nextInt(values.length)];
		
		Object[] values2 = model.getLauncherDestructors().values().toArray();
		LauncherDestructor generatedLauncherDestructor = (LauncherDestructor) values2[generator.nextInt(values2.length)];
		
		int generatedDestructTime = WarTimer.getInstance().getTime()+(new Random().nextInt(12) + 1);
		
		return model.interceptLauncher(generatedLauncher.getId(), generatedLauncherDestructor.getId(), generatedDestructTime);
	}

	@Override
	public boolean addMissileLauncherToView() {
		return generateMissileLauncher();
	}

	@Override
	public boolean addMissileDestructorToView() {
		return generateMissileDestructor();
	}

	@Override
	public boolean addLauncherDestructorToView() {
		return generateLauncherDestructor();
	}

	@Override
	public boolean launchMissileToView() {
		return generatelaunchMissile();
	}

	@Override
	public boolean interceptMissileToView() {
		return generateInterceptMissile();
	}

	@Override
	public boolean interceptLauncherToView() {
		return generateInterceptLauncher();
	}

	@Override
	public boolean exitFromView() {
		return model.exit();
	}

	@Override
	public void addMissileLauncherToModel(String launcherId, boolean isHidden) {
		view.addMissileLauncher(launcherId, isHidden);
	}

	@Override
	public void addMissileDestructorToModel(String mdId) {
		view.addMissileDestructor(mdId);
		
	}

	@Override
	public void addLauncherDestructorToModel(String ldId, String type) {
		view.addLauncherDestructor(ldId,type);
		
	}

	@Override
	public void LauncherOnLaunchEvent(String launcherId, String missileId, int flytime) {
		view.LauncherOnLaunchEvent(launcherId,missileId,flytime);
	}

	@Override
	public void LauncherOnHitEvent(String missileId) {
		view.LauncherOnHitEvent(missileId);
	}

	@Override
	public void LauncherOnInterceptedEvent(String missileId) {
		view.LauncherOnInterceptedEvent(missileId);
		
	}

	@Override
	public void LauncherOnHiddenEvent(String launcherId, boolean hidden, boolean isDestroyed) {
		view.LauncherOnHiddenEvent( launcherId,  hidden, isDestroyed);
	}

	@Override
	public void missileDestructorInterceptEvent(String missileDestructorId,  String missileId) {
		view.missileDestructorInterceptEvent( missileDestructorId,  missileId);
	}

	@Override
	public void missileDestructorMissedEvent(String missileDestructorId, String missileId) {
		view.missileDestructorMissedEvent(missileDestructorId,missileId);
	}

	@Override
	public void LauncherDestructorOnInterceptEvent(String launcherDestructorId, String destructedLauncherId) {
		view.LauncherDestructorOnInterceptEvent(launcherDestructorId,destructedLauncherId);
	}

	@Override
	public void LauncherDestructorOnMissedEvent(String launcherDestructorId, String destructedLauncherId) {
		view.LauncherDestructorOnMissedEvent(launcherDestructorId,destructedLauncherId);
	}
}