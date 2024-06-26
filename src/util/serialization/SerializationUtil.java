package util.serialization;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import train.Movement;
import util.LoggerUtil;

public final class SerializationUtil {
    
    private static String path;
    public static final String EXTENSION = ".ser";
    private static ReentrantReadWriteLock lock;

    private static FileHandler handler;
    private static Logger logger = Logger.getLogger(SerializationUtil.class.getName());

    static {
        LoggerUtil.setLogger(logger, handler, "serialization.log");
    }
    

    private SerializationUtil(){
        super();
    }

    public static void setLock(ReentrantReadWriteLock lock){
        SerializationUtil.lock = lock;
    }

    public static void setPath(String path){
        SerializationUtil.path = path;
    }

    public static String getPath(){
        return path;
    }

    public static void serializeMovement(Movement movement, String filename){
        if(lock != null){
            lock.writeLock().lock();
            try(ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(path + filename + EXTENSION))){
                out.writeObject(movement);
            } catch (IOException ex){
                logger.log(Level.SEVERE, "Serialization failed!", ex);
            } finally {
                lock.writeLock().unlock();
            }
        }
    }

    public static Movement deserializeMovement(String filename){
        Movement movement = null;
        try(ObjectInputStream in = new ObjectInputStream(new FileInputStream(path + filename))) {
            movement = (Movement)in.readObject();
        } catch (IOException | ClassNotFoundException ex) {
            logger.log(Level.SEVERE, "Deserialization failed!", ex);
        }
        return movement;
    }
}