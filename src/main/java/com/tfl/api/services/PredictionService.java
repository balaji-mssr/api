package com.tfl.api.services;


import com.tfl.api.domain.Prediction;

public interface PredictionService {
	
	Prediction getPredictionSummaryByLineAndStationFromTrackerNet(String line, String station);
	Prediction getPredictionSummaryByLineAndStationFromAPI(String line, String station,Boolean isStub);

}
