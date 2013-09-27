package com.tfl.api.domain.enums;

/**
 * Created with IntelliJ IDEA.
 * User: Balaji Ravivarman
 * Date: 13/08/13
 * Time: 12:38
 */
public enum Modes {
    TRACK("?modes=overground,tube,dlr"), OVERGROUND("?modes=overground"), BUS(
            "?modes=public-bus"), TRAM("?modes=tram"), RIVER(
            "?modes=public-river-service"), CABLE_CAR("?modes=cable-car"), ROAD(
            "?modes=road");

    private String mode;

    Modes(String mode) {
        this.mode = mode;
    }

    @Override
    public String toString() {
        return mode;
    }
}
