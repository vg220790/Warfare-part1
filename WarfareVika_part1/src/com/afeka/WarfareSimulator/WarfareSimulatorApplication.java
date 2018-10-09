package com.afeka.WarfareSimulator;

import java.io.IOException;
import java.util.Scanner;

import com.afeka.WarfareSimulator.Model.WarGame;
import com.afeka.WarfareSimulator.Utils.JsonHandler;
import com.afeka.WarfareSimulator.Controller.WarGameController;
import com.afeka.WarfareSimulator.View.ConsoleView;
import com.afeka.WarfareSimulator.View.GraphicsView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class WarfareSimulatorApplication extends Application {
	
    private static boolean loadFromJson = false;
	
	public static void main(String[] args)  throws IOException, InterruptedException {
		
        @SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
        int decision = 0;
        
        String decisionStr = "";

        String newLine = System.getProperty("line.separator");//This will retrieve line separator dependent on OS.

    	System.out.println("Do you want to read game settings from config file? Y\\N");
    	String selection = scanner.next();
    	if(selection.equalsIgnoreCase("y"))
			loadFromJson =true;
    	
        while(decision != 1 && decision != 2) {
            while (!isNumeric(decisionStr)) {
            	
                System.out.println("Please Choose:" + newLine +  "1 - console" + newLine +"2 - graphical ");
                decisionStr = scanner.next();
            }
            decision = Integer.parseInt(decisionStr);
            if(decision != 1 && decision != 2)
                decisionStr = "";
        }

        if(decision == 1) {
        	final ConsoleView gameConsole =  new ConsoleView();
        	final WarGame warModel = new WarGame();
        	new WarGameController(gameConsole ,warModel );
        	if (loadFromJson)
        		JsonHandler.getInstance().readObjectsFromJSONFile(warModel);
        	gameConsole.run();

        }

        if(decision == 2)
            launch(args);
    }
	
    private static boolean isNumeric(String str)
    {
        try
        {
            Double.parseDouble(str);
        }
        catch(NumberFormatException nfe)
        {
            return false;
        }
        return true;
    }
	
	public void start(Stage primaryStage) throws Exception {
	    
		//BorderPane mainPanel;
		final GraphicsView gamePanel =  new GraphicsView();
		
		
		Scene scene = new Scene(gamePanel, GraphicsView.SCREEN_WIDTH, GraphicsView.SCREEN_HEIGHT);
		gamePanel.setId("graphicsView");
		scene.getStylesheets().add(this.getClass().getResource("Resources/style.css").toExternalForm());
		primaryStage.setScene(scene);

		primaryStage.setTitle("Warfare Game");

    	final WarGame warModel = new WarGame();
    
    	
		new WarGameController(gamePanel, warModel);

        primaryStage.setOnCloseRequest((windowEvent) -> {
        	gamePanel.exit();
            System.exit(0);
        });
        
		primaryStage.show();
		if (loadFromJson)
    		JsonHandler.getInstance().readObjectsFromJSONFile(warModel);
		
	}
	
}
