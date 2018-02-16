package pl.maciejpajak.domain.util;

public enum GameStatus {
    
    UPCOMING, CANCELLED, LIVE, ENDED, UNKNOWN;
    
//    UPCOMING("upcoming"),
//    CANCELLED("cancelled"),
//    LIVE("live"),
//    ENDED("ended"),
//    UNKNOWN("unknown");
//    
//    private String value;
//    
//    private GameStatus(String value) {
//        this.value = value;
//    }
//    
//    public GameStatus fromValue(String value) {
//        for (GameStatus gs : values()) {
//            if (gs.value.equalsIgnoreCase(value)) {
//                return gs;
//            }
//        }
//        throw new IllegalArgumentException("Unknown enum type " + value);
//    }

}
