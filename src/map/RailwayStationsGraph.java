package map;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;

import railwaystation.RailwayStation;

public class RailwayStationsGraph {
    private static HashMap<String, LinkedList<String>> graph = new HashMap<>();

    public RailwayStationsGraph() { }

    public void addStation(RailwayStation station, RailwayStation... neighbours){
        if(!graph.containsKey(station.getName())){
            LinkedList<String> listOfNeighbours = new LinkedList<>();
            for(RailwayStation neighbour : neighbours){
                listOfNeighbours.offer(neighbour.getName());
            }
            graph.put(station.getName(), listOfNeighbours);
        }
    }

    private void travelGraph(String src, String dest, HashMap<String, String> pred){
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

    public ArrayList<String> findRoute(String src, String dest){

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
