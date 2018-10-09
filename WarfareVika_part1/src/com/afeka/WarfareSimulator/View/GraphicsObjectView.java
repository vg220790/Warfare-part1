package com.afeka.WarfareSimulator.View;

import java.io.File;

import com.sun.javafx.geom.Point2D;

import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class GraphicsObjectView extends VBox {

    public static final Image LAUNCHER_IMAGE = new Image((new File("bin/com/afeka/WarfareSimulator/Resources/launcher.png")).toURI().toString());
    public static final Image HID_LAUNCHER_IMAGE = new Image((new File("bin/com/afeka/WarfareSimulator/Resources/launcher_hid.png")).toURI().toString());
    public static final Image DIS_LAUNCHER_IMAGE = new Image((new File("bin/com/afeka/WarfareSimulator/Resources/launcher_dis.png")).toURI().toString());
    public static final Image MD_IMAGE = new Image((new File("bin/com/afeka/WarfareSimulator/Resources/md.png")).toURI().toString());
    public static final Image SHIP_IMAGE = new Image((new File("bin/com/afeka/WarfareSimulator/Resources/ship.png")).toURI().toString());
    public static final Image PLANE_IMAGE = new Image((new File("bin/com/afeka/WarfareSimulator/Resources/plane.png")).toURI().toString());
    public static final Image MISSILE_IMAGE = new Image((new File("bin/com/afeka/WarfareSimulator/Resources/missile.png")).toURI().toString());
    public static final Image HIT_IMAGE = new Image((new File("bin/com/afeka/WarfareSimulator/Resources/hit.png")).toURI().toString());
    public static final Image INTERCEPT_IMAGE = new Image((new File("bin/com/afeka/WarfareSimulator/Resources/intercept.png")).toURI().toString());

    
    
    private ImageView imageObject;
    private Point2D coordinates;
    private Text nameObject;
    private TranslateTransition transition;


    GraphicsObjectView(String id, Point2D coordinates, Image image){
        this.coordinates = coordinates;
        imageObject = new ImageView(image); 
        nameObject = new Text(id);
        nameObject.setX(imageObject.getImage().getWidth()/2);
        nameObject.setStyle("-fx-font: bold 18px \"Serif\"");
        this.setLayoutX(coordinates.x);
        this.setLayoutY(coordinates.y);
        this.getChildren().add(imageObject);
        this.getChildren().add(nameObject);
    }

    public ImageView getImageObject(){
        return imageObject;
    }
    
    public void setImageObject(Image image){
    	Platform.runLater(() -> {
    	this.getChildren().remove(nameObject);
    	this.getChildren().remove(imageObject);
    	this.imageObject = new ImageView(image);
        this.getChildren().add(imageObject);
        this.getChildren().add(nameObject);
    	});
    }

    public Text getObjectName(){
        return nameObject;
    }
    public Point2D getCoordinates(){
        return new Point2D(coordinates.x,coordinates.y);
    }
    public void setCoordinates(Point2D coordinates){
        this.coordinates = coordinates;
    }

	public void StartTransition(float x1, float x2, int time)
	{
		this.transition= new TranslateTransition();
		transition.setDuration(Duration.seconds(time));
		transition.setFromX(x1);
		transition.setToX(x2);
		transition.setNode(this);
		transition.play();
	}
	
	public void StopTransition()
	{
		transition.stop();
	}
	
	public double getTransitionX() {
		return transition.getToX();
	}
	
	
}
