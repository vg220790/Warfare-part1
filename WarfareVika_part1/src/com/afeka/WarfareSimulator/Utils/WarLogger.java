package com.afeka.WarfareSimulator.Utils;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Filter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class WarLogger {
	
	private static final String LOG_DIR_PREFIX = "log\\" ;
	private Logger warLogger;

	// Singleton
	private static WarLogger instance = null;

	protected WarLogger() {
		this.warLogger = Logger.getLogger("WarLogger");
		this.warLogger.setLevel(Level.INFO);
		//this.warLogger.setUseParentHandlers(false);
	}

	public static WarLogger getInstance() {
		if (instance == null)
			instance = new WarLogger();

		return instance;
	}

	public void createLogFile(Object obj ,String fileName, boolean logAll) {
		FileHandler fh;
		
		try {
			fh = new FileHandler(LOG_DIR_PREFIX + fileName + ".log", true);
			if (!logAll) {
				fh.setFilter(new ObjectFilter(obj));
			}

			fh.setFormatter(new SimpleFormatter());
			this.warLogger.addHandler(fh);
			
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public void log(Object obj, String message) {
		this.warLogger.log(Level.INFO, message, obj);
	}
	
	private class ObjectFilter implements Filter {

	    private Object filtered;

	    public ObjectFilter(Object toFilter) {
	        filtered = toFilter;
	    }

	    @Override
	    public boolean isLoggable(LogRecord rec) {
	        if (rec.getParameters() != null) {
	            Object temp = rec.getParameters()[0];
	            return filtered == temp;
	        }
	        return false;
	    }
	}
}
