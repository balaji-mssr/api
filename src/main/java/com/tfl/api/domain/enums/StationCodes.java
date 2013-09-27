package com.tfl.api.domain.enums;

/**
 * Created with IntelliJ IDEA.
 * User: Balaji Ravivarman
 * Date: 13/08/13
 * Time: 12:40
 */
public enum StationCodes {
    ActonTown("ACT"), Bank("BNK"), BakerStreet("BST"), CharingCross("CHX"), Euston(
            "EUS"), Hammersmith("HMD"), KingsCross("KXX"), Paddington("PAD"), PutneyBridge(
            "PUT"), Waterloo("WLO");

    private String stationCode;

    StationCodes(String stationCode) {
        this.stationCode = stationCode;
    }

    @Override
    public String toString() {
        return stationCode;
    }

}
