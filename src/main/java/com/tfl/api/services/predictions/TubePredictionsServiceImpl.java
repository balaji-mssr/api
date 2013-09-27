package com.tfl.api.services.predictions;

import com.tfl.api.config.AppProperties;
import com.tfl.api.domain.Prediction;
import com.tfl.api.resources.StubJSONResponse;
import com.tfl.api.services.PredictionService;
import com.tfl.api.services.utils.JSONHelper;
import com.tfl.api.services.utils.ResponseUtils;
import com.tfl.api.services.utils.XMLHelper;

import static com.jayway.restassured.RestAssured.get;

public class TubePredictionsServiceImpl implements PredictionService {

	@Override
	public Prediction getPredictionSummaryByLineAndStationFromTrackerNet(
			String line, String station) {		
		String XML = getXMLResponseFromTrackerNet(line, station);
		return XMLHelper.getPredictionListFromTrackerNet(XML);
	}

		
	private String getXMLResponseFromTrackerNet(String line, String station) {
		String trackerNetURL = getTrackerNetAPIURLByLineAndStation(line, station);
		return get(trackerNetURL).andReturn().asString().replace("﻿", "");
	}

	private String getTrackerNetAPIURLByLineAndStation(String line,
			String station) {
		return AppProperties.LIVE_TUBE_PREDICTIONS_TRACKER_NET_END_POINT+line+"/"+station;
	}


	@Override
	public Prediction getPredictionSummaryByLineAndStationFromAPI(String line,
			String station, Boolean isStub) {
		String JSON = StubJSONResponse.PREDICTION_API_RESPONSE;
        if (isStub) {
            JSON = ResponseUtils.getTubePredictionsByLineAndStation(line,station);
        }
		return JSONHelper.getPredictionSummaryDTO(JSON);
	}

	
}
