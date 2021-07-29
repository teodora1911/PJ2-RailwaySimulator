package util;

import java.util.List;

import element.ElementColor;

public class Constants {
    
    public static final int MAP_SIZE = 30;
    public static final int MIN_SPEED = 500; // in miliseconds
    public static final int CREATION_TIME_GAP = 2_000; // in miliseconds

    public static final List<String> STATION_NAMES = List.of("A", "B", "C", "D", "E");

    public enum LocomotiveLabels {
        PASSENGER("PL"),
        LOAD("LL"),
        UNIVERSAL("UL"),
        SHUNTING("SL");

        private String value;

        private LocomotiveLabels(String value){
            this.value = value;
        }

        public String getValue(){
            return this.value;
        }
    }

    public enum WagonLabels {
        PASSENGER("PW"),
        SEAT("SW"),
        BED("BW"),
        SLEEP("ZW"),
        RESTAURANT("RW"),
        LOAD("LW"),
        SPECIAL("XW");

        private String value;

        private WagonLabels(String value){
            this.value = value;
        }

        public String getValue(){
            return this.value;
        }
    }

    
    // GUI part
    public static final int MIN_SIZE_LABEL = 10;
    public static final int PREF_SIZE_LABEL = 30;

    public static final ElementColor ROAD_COLOR = new ElementColor(128, 193, 225);
    public static final ElementColor RAILWAY_COLOR = new ElementColor(186, 211, 204);
    public static final ElementColor CROSSING_COLOR = new ElementColor(0, 0, 0);
    public static final ElementColor RAILWAY_STATION_COLOR = new ElementColor(255, 0, 0);

    private Constants(){ }
}
