package util.reader;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import map.Map;
import util.Constants;

public class FileReaderUtil {

    //public static final String pathMatcher = "^([A-Z]:\\\\){0,1}([^\\\\\\/\\?\\|<>:\\*\\\"\\+\\,\\;\\=\\[\\]]+(\\\\){0,1})*$"; // dobar je valjda
    
    public static final String configurationFilePath = "D:\\JAVA\\PROJEKTNI ZADATAK\\ProjektniZadatak2021\\ProjektniZadatak\\config\\ConfigurationFile.txt";
    public static final String appPath = "D:\\JAVA\\PROJEKTNI ZADATAK\\ProjektniZadatak2021\\ProjektniZadatak";
    private static String trainDirectoryPath = null;
    private static String movementDirectoryPath = null;

    static{
        readFoldersPaths();
    }
    

    public FileReaderUtil() { 
       readRoadInfo();
       System.out.println("Train Directory : " + trainDirectoryPath);
       System.out.println("Movement Directory : " + movementDirectoryPath);
    }

    public void readRoadInfo(){
        try(Stream<String> stream = Files.lines(Paths.get(configurationFilePath))){

            List<String> roadInfoLines = stream.limit(3).collect(Collectors.toList());
            if(roadInfoLines.size() != 3){
                throw new IllegalArgumentException("Configuration file is not valid.");
            }

            for(String line : roadInfoLines){
                String[] seg = line.split(":");

                try{
                    if(seg.length != 3){
                        throw new IllegalArgumentException("Road info is not valid.");
                    }

                    int roadIndex = Integer.parseInt(seg[0]);
                    if(roadIndex <= 0 || roadIndex >= 4){
                        throw new IllegalArgumentException("Road info is not valid.");
                    }

                    int roadMaxSpeed = Integer.parseInt(seg[1]);
                    int roadNumberOfVehicles = Integer.parseInt(seg[2]);

                    if(roadNumberOfVehicles < 0){
                        throw new IllegalArgumentException("Road info is not valid.");
                    }

                    // do ove linije ako smo dosli, sve je korektno parsirano

                    if(roadMaxSpeed >= Constants.MIN_SPEED){
                        Map.getRoad(roadIndex).setMaxSpeed(roadMaxSpeed);
                    }
                    Map.getRoad(roadIndex).setNumberOfVehicles(roadNumberOfVehicles);

                } catch (IllegalArgumentException ex){
                    Logger.getLogger(FileReaderUtil.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
                }
            }
        } catch (Exception ex){
            Logger.getLogger(FileReaderUtil.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    public String getTrainDirectoryPath(){
        return trainDirectoryPath;
    }

    public String getMovementDirectoryPath(){
        return movementDirectoryPath;
    }

    private static void readFoldersPaths(){
        try (Stream<String> stream = Files.lines(Paths.get(configurationFilePath))) {
            List<String> paths = stream.skip(3).collect(Collectors.toList());
            for(String path : paths){
                if(path.contains("kretanja")){
                    movementDirectoryPath = getPath(path);
                }

                if(path.contains("vozovi")){
                    trainDirectoryPath = getPath(path);
                }
            }

            if(movementDirectoryPath == null){
                movementDirectoryPath = appPath + File.separator + "kretanja";
                new File(movementDirectoryPath).mkdir();
            }

            if(trainDirectoryPath == null){
                trainDirectoryPath = appPath + File.separator + "vozovi";
                new File(trainDirectoryPath).mkdir();
            }

        } catch (Exception ex) {
            Logger.getLogger(FileReaderUtil.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    private static String getPath(String directoryPath) {
        try{
            Path path = Paths.get(directoryPath);
            System.out.println("Putanja potoji.");
            if (Files.isDirectory(path)){
                return directoryPath;
            } else {
                new File(directoryPath).mkdir();
                return directoryPath;
            }
        } catch (Exception ex){
            Logger.getLogger(FileReaderUtil.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return null;
    }
}

class RailwayStationsGraph {

    public final static HashMap<String, LinkedList<String>> graph = new HashMap<>();

    static {
        LinkedList<String> neighboursA = new LinkedList<>();
        neighboursA.offer("B");
        graph.put("A", neighboursA);

        LinkedList<String> neighboursB = new LinkedList<>();
		neighboursB.offer("A");
		neighboursB.offer("C");
		graph.put("B", neighboursB);
		
		LinkedList<String> neighboursC = new LinkedList<>();
		neighboursC.offer("B");
		neighboursC.offer("D");
		neighboursC.offer("E");
		graph.put("C", neighboursC);
		
		LinkedList<String> neighboursD = new LinkedList<>();
		neighboursD.offer("C");
		graph.put("D", neighboursD);
		
		LinkedList<String> neighboursE = new LinkedList<>();
		neighboursE.offer("C");
		graph.put("E", neighboursE);
    }

    private RailwayStationsGraph() { }

    private static void travelGraph(String src, String dest, HashMap<String, String> pred){
        LinkedList<String> queue = new LinkedList<>();
        ArrayList<String> visited = new ArrayList<>();

        visited.add(src);
        queue.add(src);

        while(!queue.isEmpty()){
            String node = queue.poll();
            if(node != null){
                for(String neigh :graph.get(node)){
                    if(!visited.contains(neigh)){
                        visited.add(neigh);
                        pred.put(neigh, node);
                        queue.add(neigh);

                        if(neigh.equals(dest)){
                            return;
                        }
                    }
                }
            }
        }
    }

    public static ArrayList<String> findRoute(String src, String dest){

        if(src.equals(dest)){
            return null;
        }

        HashMap<String, String> pred = new HashMap<>();
        ArrayList<String> path = new ArrayList<>();
        travelGraph(src, dest, pred);

        String crawl = dest;
        path.add(crawl);

        while(pred.get(crawl) != null){
            path.add(pred.get(crawl));
            crawl = pred.get(crawl);
        }

        Collections.reverse(path);
        return path;
    }
}
