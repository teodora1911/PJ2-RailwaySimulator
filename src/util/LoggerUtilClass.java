package util;

import java.io.File;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public final class LoggerUtilClass {

    public static final String LOGGER_DIRECTORY = "D:\\JAVA\\PROJEKTNI ZADATAK\\ProjektniZadatak2021\\ProjektniZadatak\\loggeri";

    static {
        try{
            File loggerDirectory = new File(LOGGER_DIRECTORY);
            if(!loggerDirectory.exists()){
                loggerDirectory.mkdir();
            }
        } catch (Exception ex){
            System.out.println("Logger direktorijum se ne moze da kreira.");
        }
    }
    
    private LoggerUtilClass() { }

    public static void setLogger(Logger logger, FileHandler handler, String loggerFilename){
        try{
            handler = new FileHandler(LOGGER_DIRECTORY + File.separator + loggerFilename, true);
            handler.setFormatter(new SimpleFormatter());
            logger.setUseParentHandlers(false);
            logger.addHandler(handler);
        } catch(Exception ex){
            ex.printStackTrace();
        }
    }
}
