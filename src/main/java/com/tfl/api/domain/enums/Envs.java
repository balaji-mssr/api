package com.tfl.api.domain.enums;

import com.tfl.api.config.AppProperties;

/**
 * Created with IntelliJ IDEA.
 * User: Balaji Ravivarman
 * Date: 13/08/13
 * Time: 12:43
 */
public enum Envs {
    BETA(AppProperties.BETA_API_END_POINT),COBWEB(AppProperties.COBWEB_API_END_POINT),DEV(AppProperties.DEV_API_END_POINT),STAGE(AppProperties.STAGING_API_END_POINT);

    private String env;

    Envs(String env) {
        this.env = env;
    }

    @Override
    public String toString() {
        return env;
    }
}