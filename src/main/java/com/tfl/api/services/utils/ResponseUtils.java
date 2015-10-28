package com.tfl.api.services.utils;

import com.jayway.restassured.response.Response;
import com.tfl.api.config.AppProperties;
import com.tfl.api.domain.enums.Envs;
import com.tfl.api.domain.enums.InvalidAPIRequest;
import com.tfl.api.domain.enums.Modes;
import com.tfl.api.domain.enums.RoadSeverity;
import com.tfl.api.resources.StubJSONResponse;
import com.tfl.api.utils.DateUtils;

import java.util.Calendar;

import static com.jayway.restassured.RestAssured.get;

public class ResponseUtils {

	public static String getBetaLineResponseByModes(Modes mode) {
        String startDate = DateUtils.convertDateToString(DateUtils.SDF_YYYYMMDD_HYPEN,Calendar.getInstance().getTime());
        String endDate = DateUtils.getFormattedEndDateToString(DateUtils.SDF_YYYYMMDD_HYPEN,Calendar.getInstance().getTime());
        String apiURL = getAPIEndPoint(Envs.STAGE)+ AppProperties.LINE_STATUS_API + mode.toString() + AppProperties.BETA_API_KEY_AND_TOKEN;
        if (Modes.TRACK != mode) {
            apiURL = apiURL + "&startDate=" + startDate + "&endDate=" + endDate;
        }
        System.out.println(apiURL);
		String lineResponse = get( apiURL).andReturn().asString();
		return lineResponse;
	}

	public static String getBetaRoadDisruptions(RoadSeverity severity, boolean isStripContent) {
        String lineResponse = "";
        String apiURL = getAPIEndPoint(Envs.STAGE) + AppProperties.ROAD_STATUS_API + severity.toString() + AppProperties.BETA_API_KEY_AND_TOKEN;
        String stripContent = "&stripContent=true";
        if (isStripContent) {
            apiURL = apiURL + stripContent;
        }
        System.out.println(apiURL);
        if (RoadSeverity.ALL.equals(severity)) {
            lineResponse = JSONReader.getJSON(apiURL);
        }  else {
            lineResponse = get(apiURL).andReturn().asString();
        }
		return lineResponse;
	}

    public static Response getRoadDisruptionAPIResponse() {
        String apiURL = getAPIEndPoint(Envs.STAGE) + AppProperties.ROAD_STATUS_API + RoadSeverity.SEVERE.toString() + AppProperties.BETA_API_KEY_AND_TOKEN;
        return get(apiURL).andReturn();
    }

    public static String getRoadPlannedWorks(String corridorId) {
        //http://origin.staging.beta.tfl.gov.uk:8080/RoadPlannedWorks?corridorIds=A10&severities=&swLat=&swLon=&neLat=&neLon=&startDate=2013-09-13T00:00&endDate=2013-09-13T00:00
        String startDate = DateUtils.convertDateToString(DateUtils.SDF_YYYYMMDD_HYPEN,DateUtils.getSaturday(Calendar.SATURDAY));
        String endDate = DateUtils.getFormattedEndDateToString(DateUtils.SDF_YYYYMMDD_HYPEN,DateUtils.getSaturday(Calendar.SATURDAY));
        String params = "?startDate="+startDate+"&endDate="+endDate+"&corridorIds="+corridorId+"&severities=&swLat=&swLon=&neLat=&neLon=";
        String apiURL = getAPIEndPoint(Envs.STAGE) + AppProperties.ROAD_PLANNED_WORKS_API + params + AppProperties.BETA_API_KEY_AND_TOKEN;
        System.out.println(apiURL);
		return JSONReader.getJSON(apiURL);
	}

	public static String getWeekendBetaLineResponseByModes(Modes mode) {
		String startDate = DateUtils.getWeekendDate(Calendar.SATURDAY);
		String endDate = DateUtils.getEndDateForTheWeekend(Calendar.SUNDAY);
		String apiWeekendTrackURL = getAPIEndPoint(Envs.STAGE)
				+ AppProperties.LINE_STATUS_API + mode.toString()
				+ "&startDate=" + startDate + "&endDate=" + endDate
				+ AppProperties.BETA_API_KEY_AND_TOKEN;
        System.out.println(apiWeekendTrackURL);
		String lineResponse = get(apiWeekendTrackURL).andReturn().asString();
		return lineResponse;
	}

	public static String getStatusCodeForInvalidRequest(
			InvalidAPIRequest invalidRequest) {
		String apiParams = invalidRequest.toString()
				+ AppProperties.BETA_API_KEY_AND_TOKEN;
		String invalidAPIRequest = AppProperties.LINE_STATUS_API
				+ apiParams.toString();
		Response response = get(invalidAPIRequest).andReturn();
		return String.valueOf(response.getStatusCode());
	}

	public static String getStatusCodeForUnAuthorizedAPIAccess(
			InvalidAPIRequest invalidRequest) {
		String apiParams = invalidRequest.toString();
		String invalidAPIRequest = getAPIEndPoint(Envs.BETA)
				+ apiParams.toString();
		Response response = get(invalidAPIRequest).andReturn();
		return String.valueOf(response.getStatusCode());
	}
	public static String getStopPointResponseFromAPIForTrackModes() {
		Calendar c = Calendar.getInstance();
		String startDate = DateUtils.getDateString(DateUtils.SDF_YYYYMMDD_HYPEN, c.getTime());
		String endDate = DateUtils.getDateString(DateUtils.SDF_YYYYMMDD_HYPEN, c.getTime());
		String apiParams = "?modeList=tube,dlr,overground&startDate=" + startDate + "&endDate=" + endDate;
		String apiURL = getAPIEndPoint(Envs.STAGE) + AppProperties.STATION_STATUS_API+apiParams;
		return get(apiURL).andReturn().asString();
	}

	private static String getAPIEndPoint(Envs env) {
        //Hardcoding to Dev01
        String endPoint = Envs.DEV.toString();
		return endPoint;
	}

    public static String getTubePredictionsByLineAndStation(String line, String station) {
        return StubJSONResponse.PREDICTION_API_RESPONSE;
    }
}
