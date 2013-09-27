package com.tfl.api.domain.enums;

/**
 * Created with IntelliJ IDEA.
 * User: Balaji Ravivarman
 * Date: 13/08/13
 * Time: 12:38
 */
public enum RoadSeverity {
    MODERATE("?severities=moderate"), SEVERE("?severities=severe"),SERIOUS("?severities=serious"), ALL(
            "?severities=");

    private String severity;

    RoadSeverity(String severity) {
        this.severity = severity;
    }

    @Override
    public String toString() {
        return severity;
    }
}
