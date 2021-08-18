package util;

import java.util.List;

import element.ElementColor;

public final class Constants {
    
    public static final int MAP_SIZE = 30;
    public static final int MIN_SPEED = 500; // in miliseconds
    public static final int CREATION_TIME_GAP = 2_000; // in miliseconds

    public static final List<String> STATION_NAMES = List.of("A", "B", "C", "D", "E");

    public static final String[] BRANDS = {"Mercedes", "Volvo", "Ford", "Chevrolet", "Jeep", "Toyota", "Nissan", "Honda", "Volkswagen", "Audi", "Cadillac", "Citroen", "Fiat", "Ferrari", "Hyundai", "Kia", "Lamborghini", "Mazda", "Peugeot", "Renault", "Suzuki"};
    public static final String[] MODELS = {"Sedan", "Coupe", "Sports", "Station wagon", "Hatchback", "Convertibile", "SUV", "Milivan", "Pickup truck"};

    public enum LocomotiveLabels {
        PASSENGER("PL"),
        LOAD("TL"),
        UNIVERSAL("UL"),
        SHUNTING("ML");

        private String value;

        private LocomotiveLabels(String value){
            this.value = value;
        }

        public String getValue(){
            return this.value;
        }

        public static LocomotiveLabels fromString(String label){
            for(LocomotiveLabels l : LocomotiveLabels.values()){
                if(l.getValue().equals(label)){
                    return l;
                }
            }
            return null;
        }
    }

    public enum WagonLabels {
        SEAT("SV"),
        BED("LV"),
        SLEEP("ZV"),
        RESTAURANT("RV"),
        LOAD("TV"),
        SPECIAL("XV");

        private String value;

        private WagonLabels(String value){
            this.value = value;
        }

        public String getValue(){
            return this.value;
        }

        public static List<String> getPassegnerWagonLabels(){
            return List.of(SEAT.getValue(), BED.getValue(), SLEEP.getValue(), RESTAURANT.getValue());
        }

        public static WagonLabels fromString(String label){
            for(WagonLabels l : WagonLabels.values()){
                if(l.getValue().equals(label)){
                    return l;
                }
            }
            return null;
        }
    }

    
    // GUI part
    public static final int MIN_SIZE_LABEL = 10;
    public static final int PREF_SIZE_LABEL = 30;

    public static final ElementColor ROAD_COLOR = new ElementColor(154, 222, 250);
    public static final ElementColor RAILWAY_COLOR = new ElementColor(196, 214, 222);
    public static final ElementColor CROSSING_COLOR = new ElementColor(0, 0, 0);
    public static final ElementColor RAILWAY_STATION_COLOR = new ElementColor(255, 0, 0);

    private Constants(){ }
}
