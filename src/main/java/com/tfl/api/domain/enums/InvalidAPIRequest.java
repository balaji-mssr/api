package com.tfl.api.domain.enums;

/**
 * Created with IntelliJ IDEA.
 * User: Balaji Ravivarman
 * Date: 13/08/13
 * Time: 12:42
 */
public enum InvalidAPIRequest {
    LINE("line?modes="), LINE_BUS("line?modes=buss"), LINE_RIVER(
            "line?modes=reee"), LINE_WITHOUT_AUTH("line?modes=tube");

    private String name;

    InvalidAPIRequest(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
