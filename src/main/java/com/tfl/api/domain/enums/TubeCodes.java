package com.tfl.api.domain.enums;

/**
 * Created with IntelliJ IDEA.
 * User: Balaji Ravivarman
 * Date: 13/08/13
 * Time: 12:39
 */
public enum TubeCodes {
    Bakerloo("B"), Central("C"), District("D"), HammersmithAndCirle("H"), Jubilee(
            "J"), Metropolitan("M"), Northern("N"), Piccadilly("P"), Victoria(
            "V"), Waterloo("W");

    private String lineName;

    TubeCodes(String lineName) {
        this.lineName = lineName;
    }

    @Override
    public String toString() {
        return lineName;
    }
}
