package map;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;

public class RailwayStationsGraph {
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
