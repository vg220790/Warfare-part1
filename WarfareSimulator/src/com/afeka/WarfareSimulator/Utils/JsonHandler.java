package com.afeka.WarfareSimulator.Utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import com.afeka.WarfareSimulator.Model.WarGame;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonHandler {

	//Singleton
	static JsonHandler instance = null;
	
	public static JsonHandler getInstance() {
		if (instance == null)
			instance = new JsonHandler();
		
		return instance;
	}
	
	
	public  void readObjectsFromJSONFile(WarGame model) throws InterruptedException {	
		// create ObjectMapper instance
		ObjectMapper objectMapper = new ObjectMapper();

		try {

			// read json file data to String
			byte[] jsonData = Files.readAllBytes(Paths.get("bin/com/afeka/WarfareSimulator/Resources/config.json"));

			// read JSON like DOM Parser
			JsonNode rootNode = objectMapper.readTree(jsonData);
			JsonNode warNode = rootNode.path("war");

			// ****** reading missile launchers ********

			JsonNode missileLaunchers_Node = warNode.path("missileLaunchers");
			JsonNode launchersNode = missileLaunchers_Node.path("launcher");
			Iterator<JsonNode> launcher_elements = launchersNode.elements();

			while (launcher_elements.hasNext()) {

				JsonNode launcher = launcher_elements.next();

				JsonNode launcherId_Node = launcher.path("id");
				JsonNode launcherIsHidden_Node = launcher.path("isHidden");

				model.addLauncher(launcherId_Node.asText(),
						launcherIsHidden_Node.asBoolean());
				
				JsonNode launcherMissiles_Node = launcher.path("missile");
				Iterator<JsonNode> missile_elements = launcherMissiles_Node.elements();

				while (missile_elements.hasNext()) { 

					JsonNode missile = missile_elements.next();
					JsonNode missileId_Node = missile.path("id");
					JsonNode missileDestination_Node = missile.path("destination");
					JsonNode missileLaunchTime_Node = missile.path("launchTime");
					JsonNode missileFlyTime_Node = missile.path("flyTime");
					JsonNode missileDamage_Node = missile.path("damage");

					model.launchMissile(launcherId_Node.asText(), missileId_Node.asText(), missileDestination_Node.asText(),
							missileLaunchTime_Node.asInt(), missileFlyTime_Node.asInt(), missileDamage_Node.asInt());
				}
			}

			// ****** reading missile destructors ********

			JsonNode missileDestructors_Node = warNode.path("missileDestructors");
			JsonNode missileDestructorsNode = missileDestructors_Node.path("destructor");
			Iterator<JsonNode> missileDestructor_elements = missileDestructorsNode.elements();

			while (missileDestructor_elements.hasNext()) {

				JsonNode missiledestructor = missileDestructor_elements.next();

				JsonNode missiledestructorId_Node = missiledestructor.path("id");

				model.addMissileDestructor(missiledestructorId_Node.asText());

				JsonNode destructedMissiles_Node = missiledestructor.path("destructdMissile");
				Iterator<JsonNode> destructedMissiles_elements = destructedMissiles_Node.elements();
				while (destructedMissiles_elements.hasNext()) { // *** reading DMissiles *****

					JsonNode destructedMissile = destructedMissiles_elements.next();

					JsonNode destructedMissileId_Node = destructedMissile.path("id");
					JsonNode destructAfterLaunch_Node = destructedMissile.path("destructAfterLaunch");
					
					model.interceptMissile(destructedMissileId_Node.asText(), missiledestructorId_Node.asText(), destructAfterLaunch_Node.asInt());
					
				}
			}


			// ****** reading missile launcher destructors ********

			JsonNode missileLauncherDestructors_Node = warNode.path("missileLauncherDestructors");
			JsonNode launcherDestructorsNode = missileLauncherDestructors_Node.path("destructor");
			Iterator<JsonNode> missileLauncherDestructor_elements = launcherDestructorsNode.elements();
			
			while (missileLauncherDestructor_elements.hasNext()) {

				JsonNode missileLauncherDestructor_JSONobject = missileLauncherDestructor_elements.next();

				JsonNode missileLauncherDestructorType_Node = missileLauncherDestructor_JSONobject.path("type");

				model.addLauncherDestructor(missileLauncherDestructor_JSONobject.asText(),missileLauncherDestructorType_Node.asText());

				JsonNode destructedMissileLaunchers_Node = missileLauncherDestructor_JSONobject
						.path("destructedLanucher");
				Iterator<JsonNode> destructedLaunchers_elements = destructedMissileLaunchers_Node.elements();
				
				while (destructedLaunchers_elements.hasNext()) { // *** reading DLaunchers *****

					JsonNode destructedLauncher = destructedLaunchers_elements.next();

					JsonNode destructedLauncherId_Node = destructedLauncher.path("id");
					JsonNode destructAfterLaunchDestructTime_Node = destructedLauncher.path("destructTime");
					model.interceptLauncher(destructedLauncherId_Node.asText(), missileLauncherDestructor_JSONobject.asText(), destructAfterLaunchDestructTime_Node.asInt());
				
				}
			}
		} catch (IOException e) {
			e.getMessage();
		}
	}
}
