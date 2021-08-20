package map;

import static util.Constants.MAP_SIZE;

import java.util.List;
import java.util.Optional;

import railwaystation.RailwayStation;
import train.Train;

public final class Map {
    
    private static Field[][] map = new Field[MAP_SIZE][MAP_SIZE];

    public static RailwayStation A = new RailwayStation("A");
    public static RailwayStation B = new RailwayStation("B");
    public static RailwayStation C = new RailwayStation("C");
    public static RailwayStation D = new RailwayStation("D");
    public static RailwayStation E = new RailwayStation("E");

    private static Road road1 = new Road(1);
    private static Road road2 = new Road(2);
    private static Road road3 = new Road(3);

    private static Railway railwayAB = new Railway(1, A, B);
    private static Railway railwayBC = new Railway(2, B, C);
    private static Railway railwayCD = new Railway(3, C, D);
    private static Railway railwayCE = new Railway(4, C, E);

    private static List<Road> roads = List.of(road1, road2, road3);
    private static List<Railway> railways = List.of(railwayAB, railwayBC, railwayCD, railwayCE);
    private static List<RailwayStation> stations = List.of(A, B, C, D, E);

    public static Object updateLock = new Object();

    static {
        initializeFields();
        initializeRoads();
        initializeRailways();
        initializeStations();

        A.start();
        B.start();
        C.start();
        D.start();
        E.start();
        // stations.forEach(s -> s.start());
    }

    private Map(){ }

    public static Field[][] getMap(){
        return map;
    }

    public static Road getRoad(int id){
        for(Road road : roads){
            if(road.getId() == id){
                return road;
            }
        }
        return null;
    }

    public static Railway getRailway(int id){
        for(Railway railway : railways){
            if(railway.getId() == id){
                return railway;
            }
        }
        return null;
    }

    public static RailwayStation getStation(String name){
        for(RailwayStation station : stations){
            if(name.equals(station.getName())){
                return station;
            }
        }
        return null;
    }

    public static List<Road> getRoads(){
        return roads;
    }

    public static int numberOfRoads() {
        return roads.size();
    }

    public static boolean isStationOnField(Coordinates c){
        if(c != null && c.isValid()){
            return map[c.getX()][c.getY()].getStation() != null;
        } else {
            return false;
        }
    }

    public static boolean isElementOnField(Coordinates c){
        if(c != null && c.isValid()){
            synchronized(Map.updateLock){
                return map[c.getX()][c.getY()].getElement() != null;
            }
        } else {
            return false;
        }
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
        map[28][9] = new Field(28, 9, FieldType.RAILWAY);

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
        map[26][1] = new Field(26, 1, D);
        D.addCoordinates(26, 1);
        map[26][2] = new Field(26, 2, D);
        D.addCoordinates(26, 2);
        map[27][1] = new Field(27, 1, D);
        D.addCoordinates(27, 1);
        map[27][2] = new Field(27, 2, D);
        D.addCoordinates(27, 2);

        // E
        map[26][26] = new Field(26, 26, E);
        E.addCoordinates(26, 26);
        map[26][25] = new Field(26, 25, E);
        E.addCoordinates(26, 25);
        map[25][25] = new Field(25, 25, E);
        E.addCoordinates(25, 25);
        map[25][26] = new Field(25, 26, E);
        E.addCoordinates(25, 26);

        // C
        map[19][12] = new Field(19, 12, C);
        C.addCoordinates(19, 12);
        map[19][13] = new Field(19, 13, C);
        C.addCoordinates(19, 13);
        map[20][12] = new Field(20, 12, C);
        C.addCoordinates(20, 12);
        map[20][13] = new Field(20, 13, C);
        C.addCoordinates(20, 13);

        // B
        map[6][5] = new Field(6, 5, B);
        B.addCoordinates(6, 5);
        map[6][6] = new Field(6, 6, B);
        B.addCoordinates(6, 6);
        map[7][5] = new Field(7, 5, B);
        B.addCoordinates(7, 5);
        map[7][6] = new Field(7, 6, B);
        B.addCoordinates(7, 6);

        // A
        map[1][27] = new Field(1, 27, A);
        A.addCoordinates(1, 27); 
        map[1][28] = new Field(1, 28, A);
        A.addCoordinates(1, 28);
        map[2][27] = new Field(2, 27, A);
        A.addCoordinates(2, 27);
        map[2][28] = new Field(2, 28, A);
        A.addCoordinates(2, 28);
    }

    private static void initializeRoads(){

       // 1
       for(int i = MAP_SIZE - 1; i >= 20; --i){
           road1.addLeftTrackField(map[8][i]);
       }
       for(int i = 7; i >= 0; --i){
           road1.addLeftTrackField(map[i][20]);
       }
       for(int i = 0; i <= 7; ++i){
           road1.addRightTrackField(map[i][21]);
       }
       for(int i = 22; i < MAP_SIZE; ++i){
           road1.addRightTrackField(map[7][i]);
       }

       // 2
       for(int i = 0; i < MAP_SIZE; ++i){
           road2.addLeftTrackField(map[14][MAP_SIZE - 1 - i]);
           road2.addRightTrackField(map[13][i]);
       }

       // 3 
       for(int i = MAP_SIZE - 1; i >= 21; --i){
           road3.addLeftTrackField(map[i][20]);
       }
       for(int i = 21; i < MAP_SIZE; ++i){
           road3.addLeftTrackField(map[21][i]);
       }
       for(int i = MAP_SIZE - 1; i >= 21; --i){
           road3.addRightTrackField(map[22][i]);
       }
       for(int i = 23; i < MAP_SIZE; ++i){
           road3.addRightTrackField(map[i][21]);
       }
    }

    private static void initializeRailways(){ // inicijalizujemo bez stanica

        // A - B
        for(int i = 26; i >= 16; --i){
            railwayAB.addField(map[2][i]);
        }
        for(int i = 3; i <= 5; ++i){
            railwayAB.addField(map[i][16]);
        }
        for(int i = 15; i >= 6; --i){
            railwayAB.addField(map[5][i]);
        }

        // B - C
        for(int i = 8; i <= 19; ++i){
            railwayBC.addField(map[i][6]);
        }
        for(int i = 7; i < 12; ++i){
            railwayBC.addField(map[19][i]);
        }

        // C - E i  C - D
        for(int i = 14; i <= 18; ++i){
            railwayCE.addField(map[20][i]);
        }

        for(int i = 21; i <= 26; ++i){
            railwayCE.addField(map[i][18]);
            railwayCD.addField(map[i][12]);
        }
        for(int i = 19; i < 25; ++i){
            railwayCE.addField(map[26][i]);
        }
        for(int i = 11; i >= 9; --i){
            railwayCD.addField(map[26][i]);
        }

        railwayCD.addField(map[27][9]);
        
        for(int i = 9; i >= 5; --i){
            railwayCD.addField(map[28][i]);
        }
        for(int i = 27; i >= 23; --i){
            railwayCD.addField(map[i][5]);
        }

        railwayCD.addField(map[23][4]);
        railwayCD.addField(map[23][3]);
        railwayCD.addField(map[22][3]);
        railwayCD.addField(map[22][2]);

        for(int i = 22; i < 26; ++i){
            railwayCD.addField(map[i][1]);
        }
    }

    private static void initializeStations(){
        A.addRailway(railwayAB);

        B.addRailway(railwayAB);
        B.addRailway(railwayBC);

        C.addRailway(railwayBC);
        C.addRailway(railwayCD);
        C.addRailway(railwayCE);

        D.addRailway(railwayCD);

        E.addRailway(railwayCE);
    }

    public static boolean isFieldEmpty(Coordinates coordinates){
        synchronized(Map.updateLock){
            return map[coordinates.getX()][coordinates.getY()].getElement() == null;
        }
    }

    public static boolean isFieldUnderVoltage(Coordinates coordinates){
        synchronized(Map.updateLock){
            return map[coordinates.getX()][coordinates.getY()].isUnderVoltage();
        }
    }

    /**
     * 
     * @param field - polje mape na kom se nalazi pruzni prelaz
     */
    public static boolean freeForCrossing(Field field){
        Optional<Railway> r = railways.stream().filter(railway -> railway.getPath().contains(field)).findAny();
        if(r.isPresent()){
            Railway railway = r.get();
            int fieldIndex = railway.getPath().getSegment().indexOf(field);
            Train lastAdded = railway.getPresentLastAdded();
            if(lastAdded != null){
                boolean direction = (lastAdded.getNextStation() == railway.getEndStation());
                synchronized(Map.updateLock){
                    Coordinates lastCoordinates = lastAdded.getConfiguration().get(lastAdded.getConfiguration().size() - 1).getCoordinates();
                    if(map[lastCoordinates.getX()][lastCoordinates.getY()].getStation() != null){
                        return false;
                    } else {
                        Field lastConfigurationElementField = map[lastCoordinates.getX()][lastCoordinates.getY()];
                        int lastConfigurationElementIndex = railway.getPath().getSegment().indexOf(lastConfigurationElementField);
                        if(direction){
                            return (lastConfigurationElementIndex > fieldIndex);
                        } else {
                            return (lastConfigurationElementIndex < fieldIndex);
                        }
                    }
                }
            } else {
                return true;
            } 
        } else {
            // Ovaj dio koda nikada ne bi trebao da se izvrsi
            return false;
        }
    }
}