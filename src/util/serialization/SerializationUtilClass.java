package util.serialization;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import train.Movement;

public final class SerializationUtilClass {
    
    private static String path;
    private static final String EXTENSION = ".ser";

    private SerializationUtilClass(){
        super();
    }

    public static void setPath(String path){
        SerializationUtilClass.path = path;
    }

    public static String getPath(){
        return path;
    }

    public static void serializeMovement(Movement movement, String filename){
        try(ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(path + filename + EXTENSION))){
            out.writeObject(movement);
        } catch (IOException ex){
            Logger.getLogger(SerializationUtilClass.class.getName()).log(Level.SEVERE, "Serialization failed!", ex);
        }
    }

    public static Movement deserializeMovement(String filename){
        Movement movement = null;
        try(ObjectInputStream in = new ObjectInputStream(new FileInputStream(path + filename))) {
            movement = (Movement)in.readObject();
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(SerializationUtilClass.class.getName()).log(Level.SEVERE, "Deserialization failed!", ex);
        }
        return movement;
    }
}

