package map;

import static util.Constants.MAP_SIZE;

import java.util.List;

import element.Element;
import engine.Simulation;

public class Map {
    
    private static Field[][] map = new Field[MAP_SIZE][MAP_SIZE];

    private static final Road road1 = new Road(1);
    private static final Road road2 = new Road(2);
    private static final Road road3 = new Road(3);

    private static final List<Road> roads = List.of(road1, road2, road3);

    static {
        initializeFields();
        initializeRoads();
    }

    private Map(){ }

    public static Field[][] getMap(){
        return map;
    }

    public static Road getRoad(int id){
        switch (id) {
            case 1:
                return road1;
        
            case 2:
                return road2;

            case 3:
                return road3;

            default:
                return null;
        }
    }

    public static int numberOfRoads() {
        return roads.size();
    }

    private static void initializeFields(){

        // roads and crossings
        for(int i = 0; i < MAP_SIZE; ++i){
            if(i == 6){
                map[13][i] = new Field(13, i, FieldType.CROSSING);
                map[14][i] = new Field(14, i, FieldType.CROSSING);
            } else {
                map[13][i] = new Field(13, i, FieldType.ROAD);
                map[14][i] = new Field(14, i, FieldType.ROAD);
            }
        }

        for(int i = 0; i <= 8; ++i){
            if(i == 2){
                map[i][20] = new Field(i, 20, FieldType.CROSSING);
                map[i][21] = new Field(i, 21, FieldType.CROSSING);
                map[MAP_SIZE - 1 - i][20] = new Field(MAP_SIZE - 1 - i, 20, FieldType.ROAD);
                map[MAP_SIZE - 1 - i][21] = new Field(MAP_SIZE - 1 - i, 21, FieldType.ROAD);
            } else if (i == 3) {
                map[i][20] = new Field(i, 20, FieldType.ROAD);
                map[i][21] = new Field(i, 21, FieldType.ROAD);
                map[MAP_SIZE - 1 - i][20] = new Field(MAP_SIZE - 1 - i, 20, FieldType.CROSSING);
                map[MAP_SIZE - 1 - i][21] = new Field(MAP_SIZE - 1 - i, 21, FieldType.CROSSING);
            } else {
                map[i][20] = new Field(i, 20, FieldType.ROAD);
                map[i][21] = new Field(i, 21, FieldType.ROAD);
                map[MAP_SIZE - 1 - i][20] = new Field(MAP_SIZE - 1 - i, 20, FieldType.ROAD);
                map[MAP_SIZE - 1 - i][21] = new Field(MAP_SIZE - 1 - i, 21, FieldType.ROAD);
            }
        }

        for(int i = 22; i < MAP_SIZE; ++i){
            map[7][i] = new Field(7, i, FieldType.ROAD);
            map[8][i] = new Field(8, i, FieldType.ROAD);
            map[MAP_SIZE - 1 - 7][i] = new Field(MAP_SIZE - 1 - 7, i, FieldType.ROAD);
            map[MAP_SIZE - 1 - 8][i] = new Field(MAP_SIZE - 1 - 8, i, FieldType.ROAD);
        }


        // railway
        for(int i = 16; i < MAP_SIZE; ++i){
            if(i != 20 && i != 21 && i != 27 && i != 28){ // skip crossings and railway stations
                map[2][i] = new Field(2, i, FieldType.RAILWAY);
            }
        }

        for(int i = 3; i <= 5; ++i){
            map[i][16] = new Field(i, 16, FieldType.RAILWAY);
        }

        for(int i = 15; i >= 6; --i){
            map[5][i] = new Field(5, i, FieldType.RAILWAY);
        }

        for(int i = 8; i < 20; ++i){
            if(i != 13 && i != 14){
                map[i][6] = new Field(i, 6, FieldType.RAILWAY);
            }
        }

        for(int i = 7; i <= 18; ++i){
            if(i < 12){ // skip railway stations
                map[19][i] = new Field(19, i, FieldType.RAILWAY);
            } else if (i > 13){
                map[20][i] = new Field(20, i, FieldType.RAILWAY);
            }
        }

        for(int i = 21; i <= 26; ++i){
            map[i][18] = new Field(i, 18, FieldType.RAILWAY);
            map[i][12] = new Field(i, 12, FieldType.RAILWAY);
        }

        for(int i = 19; i <= 24; ++i){
            if(i != 20 && i != 21){
                map[26][i] = new Field(26, i, FieldType.RAILWAY);
            }
        }

        for(int i = 27; i < MAP_SIZE; ++i){
            map[i][25] = new Field(i, 25, FieldType.RAILWAY);
        }

        for(int i = 9; i <= 11; ++i){
            map[26][i] = new Field(26, i, FieldType.RAILWAY);
        }

        map[27][9] = new Field(27, 9, FieldType.RAILWAY);
        map[28][9] = new Field(27, 9, FieldType.RAILWAY);

        for(int i = 5; i < 9; ++i){
            map[28][i] = new Field(28, i, FieldType.RAILWAY);
        }

        for(int i = 23; i < 28; ++i){
            map[i][5] = new Field(i, 5, FieldType.RAILWAY);
        }
        map[23][4] = new Field(23, 4, FieldType.RAILWAY);

        for(int i = 1; i <= 3; ++i){
            if(i == 3 || i == 1){
                map[23][i] = new Field(23, i, FieldType.RAILWAY);
            }
            map[22][i] = new Field(22, i, FieldType.RAILWAY);
        }

        map[24][1] = new Field(24, 1, FieldType.RAILWAY);
        map[25][1] = new Field(25, 1, FieldType.RAILWAY);

        // railway stations 
        // D
        map[26][1] = new Field(26, 1, FieldType.RAILWAY_STATION);
        map[26][2] = new Field(26, 2, FieldType.RAILWAY_STATION);
        map[27][1] = new Field(27, 1, FieldType.RAILWAY_STATION);
        map[27][2] = new Field(27, 2, FieldType.RAILWAY_STATION);

        // E
        map[26][26] = new Field(26, 26, FieldType.RAILWAY_STATION);
        map[26][25] = new Field(26, 25, FieldType.RAILWAY_STATION);
        map[25][25] = new Field(25, 25, FieldType.RAILWAY_STATION);
        map[25][26] = new Field(25, 26, FieldType.RAILWAY_STATION);

        // C
        map[19][12] = new Field(19, 12, FieldType.RAILWAY_STATION);
        map[19][13] = new Field(19, 13, FieldType.RAILWAY_STATION);
        map[20][12] = new Field(20, 12, FieldType.RAILWAY_STATION);
        map[20][13] = new Field(20, 13, FieldType.RAILWAY_STATION);

        // B
        map[6][5] = new Field(6, 5, FieldType.RAILWAY_STATION);
        map[6][6] = new Field(6, 6, FieldType.RAILWAY_STATION);
        map[7][5] = new Field(7, 5, FieldType.RAILWAY_STATION);
        map[7][6] = new Field(7, 6, FieldType.RAILWAY_STATION);

        // A
        map[1][27] = new Field(1, 27, FieldType.RAILWAY_STATION);
        map[1][28] = new Field(1, 28, FieldType.RAILWAY_STATION);
        map[2][27] = new Field(2, 27, FieldType.RAILWAY_STATION);
        map[2][28] = new Field(2, 28, FieldType.RAILWAY_STATION);
    }

    private static void initializeRoads(){ // mozda se moze uraditi na bolji nacin

       // 1
       for(int i = MAP_SIZE - 1; i >= 20; --i){
           road1.addLeftTrackCoordinates(8, i);
       }

       for(int i = 7; i >= 0; --i){
           road1.addLeftTrackCoordinates(i, 20);
       }

       for(int i = 0; i <= 7; ++i){
           road1.addRightTrackCoordinates(i, 21);
       }

       for(int i = 22; i < MAP_SIZE; ++i){
           road1.addRightTrackCoordinates(7, i);
       }

       //2
       for(int i = 0; i < MAP_SIZE; ++i){
           road2.addLeftTrackCoordinates(14, MAP_SIZE - 1 - i);
           road2.addRightTrackCoordinates(13, i);
       }

       // 3 
       for(int i = MAP_SIZE - 1; i >= 21; --i){
           road3.addLeftTrackCoordinates(i, 20);
       }

       for(int i = 21; i < MAP_SIZE; ++i){
           road3.addLeftTrackCoordinates(21, i);
       }

       for(int i = MAP_SIZE - 1; i >= 21; --i){
           road3.addRightTrackCoordinates(22, i);
       }

       for(int i = 23; i < MAP_SIZE; ++i){
           road3.addRightTrackCoordinates(i, 21);
       }
    }

    private static Segment contains(Coordinates c){
        return roads.stream().filter(road -> road.contains(c) != null).findAny().get().contains(c);
    }

    public static Coordinates nextCoordinates(Coordinates c){
        Segment road = contains(c);
        if(road != null){
            return road.getNextCoordinates(c, true);
        } else {
            return null;
        }
    }

    public static boolean isFieldFree(Coordinates c){
        if(c.isValid()){
            synchronized(contains(c)){
                return map[c.getX()][c.getY()].getElement() == null;
            }
        }
        return false;
    }

    public static void putElementInField(Element element, Coordinates c){
        if(!c.isValid() && element.getCoordinates().isValid()){
            synchronized(contains(element.getCoordinates())){
                map[element.getX()][element.getY()].setElement(null);
                Simulation.mwvc.makeChange();
                return;
            }
        }

        if(!c.isValid() && !element.getCoordinates().isValid()){
            return;
        }

        synchronized(contains(c)){
            if(!c.isValid()){
                map[element.getX()][element.getY()].setElement(null);
                Simulation.mwvc.makeChange();
                return;
            }

            if(Coordinates.validCoordinates(element.getX(), element.getY())){
                map[element.getX()][element.getY()].setElement(null);
            }
            map[c.getX()][c.getY()].setElement(element);
            element.setCoordinates(c);
            Simulation.mwvc.makeChange();
        }
    }
}