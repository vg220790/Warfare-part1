package com.afeka.WarfareSimulator.View;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Vector;

import com.afeka.WarfareSimulator.Utils.WarStatistics;
import com.sun.javafx.geom.Point2D;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;

public class GraphicsView extends BorderPane  implements AbstractWarView {

	public static final double SCREEN_WIDTH = 950;
	public static final double SCREEN_HEIGHT = 600;
	
	private WarViewEventListener warViewEventListener;
	private Vector<Button> buttons = new Vector<>(); 
	private ToolBar statisticsBar = new ToolBar();
	private Text statisticsText = new Text("");


	
	//Collections
	private Map<String, GraphicsObjectView> missileLaunchers;
	private Map<String, GraphicsObjectView> missileDestructors;
	private Map<String, GraphicsObjectView> launcherDestructors;
	private Map<String, GraphicsObjectView> missiles;

	public GraphicsView() {
		
		missileLaunchers = Collections.synchronizedMap(new HashMap<String, GraphicsObjectView>());
		missileDestructors = Collections.synchronizedMap(new HashMap<String, GraphicsObjectView>());
		launcherDestructors= Collections.synchronizedMap(new HashMap<String, GraphicsObjectView>());
		missiles = Collections.synchronizedMap(new HashMap<String, GraphicsObjectView>());
		
		//build manu 
		VBox topContainer = new VBox(); //Creates a container to hold all Menu Objects.
		ToolBar toolBar = new ToolBar(); //Creates our tool-bar to hold the buttons.
		
		topContainer.getChildren().add(toolBar);
		 
		this.setTop(topContainer);
				
		for (int i = 0; i < AbstractWarView.MENU.length; i++) {
			Button btn = new Button(AbstractWarView.MENU[i]);
			buttons.addElement(btn);
			setOnClick(i);

		}
		
		toolBar.getItems().addAll(buttons);
		
		//build statistic manu
		VBox botContainer = new VBox(); 
		statisticsBar.getItems().add(statisticsText);
		botContainer.getChildren().add(statisticsBar);
		this.setBottom(botContainer);
		statisticsBar.setVisible(false);
	}

	private void setOnClick(int i) {
		//TextInputDialog dialog = new TextInputDialog();

		switch (i) {
		case 0:
			buttons.get(0).setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					try {
						warViewEventListener.addMissileLauncherToView();
					} catch (NoSuchElementException e) {

					}
				}
			});
			break;

		case 1:
			buttons.get(1).setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					try {
						warViewEventListener.addLauncherDestructorToView();
					} catch (NoSuchElementException e) {

					}
				}
			});
			break;
		case 2:
			buttons.get(2).setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					try {
						warViewEventListener.addMissileDestructorToView();
					} catch (NoSuchElementException e) {

					}
				}
			});
			break;

		case 3:
			buttons.get(3).setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					try {
						warViewEventListener.launchMissileToView();

					} catch (Exception e) {

					}
				}
			});
			break;
		case 4:
			buttons.get(4).setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					try {
						warViewEventListener.interceptMissileToView();
					} catch (Exception e) {

					}

				}
			});
			break;
		case 5:
			buttons.get(5).setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					try {
						warViewEventListener.interceptLauncherToView();
					} catch (Exception e) {

					}
				}
			});
			break;

		case 6:
			buttons.get(6).setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					updateStatisticsOnView();
					statisticsBar.setVisible(!statisticsBar.isVisible());
				}

			});
			break;
		case 7:
			buttons.get(7).setOnAction(actionEvent -> Platform.exit());
			break;
		default:
			break;
		}
	}

	private void updateStatisticsOnView() {
		Platform.runLater(() -> {
		statisticsText.setText(WarStatistics.getInstance().toString());
		});
	}

	@Override
	public void addMissileLauncher(String launcherId, boolean isHidden) {
		Point2D coordinates = new Point2D((float) (SCREEN_WIDTH*0.40),(missileLaunchers.size()+1)*100);
		GraphicsObjectView newLauncher= null;
		if (!isHidden){
			newLauncher = new GraphicsObjectView(launcherId,coordinates, GraphicsObjectView.LAUNCHER_IMAGE);
		}
		else
			newLauncher = new GraphicsObjectView(launcherId,coordinates, GraphicsObjectView.HID_LAUNCHER_IMAGE);

		missileLaunchers.put(launcherId, newLauncher);
		this.getChildren().add(newLauncher);
		
	}

	@Override
	public void addMissileDestructor(String mdId) {
		Point2D coordinates = new Point2D((float) (SCREEN_WIDTH*0.75),(missileDestructors.size()+1)*100);
		GraphicsObjectView newMDestructor = new GraphicsObjectView( mdId,coordinates, GraphicsObjectView.MD_IMAGE);
		missileDestructors.put(mdId, newMDestructor);
		this.getChildren().add(newMDestructor);
	}

	@Override
	public void addLauncherDestructor(String ldId, String type) {
		Point2D coordinates =  new Point2D((float) (SCREEN_WIDTH*0.05),(launcherDestructors.size()+1)*100);
		GraphicsObjectView newLDestructor= null;
		switch (type) {
		case "ship":
			 newLDestructor = new GraphicsObjectView(ldId,coordinates, GraphicsObjectView.SHIP_IMAGE);
			break;
		case "plane":
			newLDestructor = new GraphicsObjectView(ldId,coordinates, GraphicsObjectView.PLANE_IMAGE);
			break;
		default:
			break;
		}
		launcherDestructors.put(ldId, newLDestructor);
		this.getChildren().add(newLDestructor);	
	}

	@Override
	public boolean exit() {
		return true;
	}

	@Override
	public void LauncherOnLaunchEvent(String launcherId, String missileId, int flytime) {
		Point2D coordinates = new Point2D((float) (SCREEN_WIDTH*0.25),(missileLaunchers.get(launcherId).getCoordinates().y-30));
		GraphicsObjectView newMissile = new GraphicsObjectView(missileId,coordinates, GraphicsObjectView.MISSILE_IMAGE);
		missiles.put(missileId, newMissile);

		Platform.runLater(() -> {
		this.getChildren().add(newMissile);	
		newMissile.StartTransition(coordinates.x, coordinates.x+400,flytime);
		});
		updateStatisticsOnView();
	}

	@Override
	public void LauncherOnHitEvent(String missileId) {

		missiles.get(missileId).setImageObject(GraphicsObjectView.HIT_IMAGE);
		updateStatisticsOnView();
	}

	@Override
	public void LauncherOnInterceptedEvent(String missileId) {
		updateStatisticsOnView();
	}

	@Override
	public void LauncherOnHiddenEvent(String launcherId, boolean hidden, boolean isDestroyed) {
		if (!isDestroyed)
		{
		if (!hidden)
			missileLaunchers.get(launcherId).setImageObject(GraphicsObjectView.LAUNCHER_IMAGE);
		
		if (hidden)
			missileLaunchers.get(launcherId).setImageObject(GraphicsObjectView.HID_LAUNCHER_IMAGE);
		}
	}

	@Override
	public void missileDestructorInterceptEvent(String missileDestructorId, String missileId) {
		missiles.get(missileId).StopTransition();
		
		missiles.get(missileId).setImageObject(GraphicsObjectView.INTERCEPT_IMAGE);
		updateStatisticsOnView();
	}

	@Override
	public void missileDestructorMissedEvent(String missileDestructorId, String destructedLauncherId) {
		// TODO Auto-generated method stub	
	}

	@Override
	public void LauncherDestructorOnInterceptEvent(String launcherDestructorId, String destructedLauncherId) {
	    
		Platform.runLater(() -> {
		Line line = new Line();
		line.setStyle("-fx-stroke: red;");
		line.setStartX(missileLaunchers.get(destructedLauncherId).getCoordinates().x+15);
	    line.setStartY(missileLaunchers.get(destructedLauncherId).getCoordinates().y+15);
	    line.setEndX(launcherDestructors.get(launcherDestructorId).getCoordinates().x+15);
	    line.setEndY(launcherDestructors.get(launcherDestructorId).getCoordinates().y+15);
			this.getChildren().add(line);
			
			missileLaunchers.get(destructedLauncherId).setImageObject(GraphicsObjectView.DIS_LAUNCHER_IMAGE);
		});
		updateStatisticsOnView();

	}

	@Override
	public void LauncherDestructorOnMissedEvent(String launcherDestructorId, String destructedLauncherId) {
		Platform.runLater(() -> {
		Line line = new Line();
		line.setStyle("-fx-stroke: gray;");
		line.setStartX(missileLaunchers.get(destructedLauncherId).getCoordinates().x+15);
	    line.setStartY(missileLaunchers.get(destructedLauncherId).getCoordinates().y+15);
	    line.setEndX(launcherDestructors.get(launcherDestructorId).getCoordinates().x+15);
	    line.setEndY(launcherDestructors.get(launcherDestructorId).getCoordinates().y+15);
			this.getChildren().add(line);
		});
		updateStatisticsOnView();
		
	}

	@Override
	public void registerListener(WarViewEventListener listener) {
		warViewEventListener = listener;
	}

	

}