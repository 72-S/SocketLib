package dev.socketlib;

import dev.socketlib.config.ConfigManager;

public class Logger {
    private final org.slf4j.Logger logger;
    private static Logger instance;
    private String debug = ConfigManager.getInstance().getKey("debug"); 

    public Logger(org.slf4j.Logger logger) {
       this.logger = logger; 
    }

    public void info(String string) {
        logger.info(string);
    }

    public void warn(String string) {
        logger.warn(string);
    }


    public void debug(String string) {
       if (debug.equals("true")) {
           logger.debug(string);
       } 
    }

    public void error(String string) {
       logger.error(string); 
    }

    public void error(String string, Exception e) {
       logger.error(string, e); 
    }

    public static Logger getInstance() {
        if (instance == null) {
            instance = new Logger(null);
        }
        return instance;
    }


    
}
