package com.tfl.api.stories;

import com.tfl.api.domain.Prediction;
import com.tfl.api.services.PredictionService;
import com.tfl.api.services.predictions.TubePredictionsServiceImpl;
import com.tfl.api.domain.enums.StationCodes;
import com.tfl.api.domain.enums.TubeCodes;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class NWP2667TubePredictionsComparisionTest {

	private PredictionService predictionService;

	@Before
	public void setup() {
		predictionService = new TubePredictionsServiceImpl();
	}

	/*******************************************************************************************
	 * Epic - Assert the following PredictionDTO between API and Tracker Net
	 * Prediction DTO - LineName - i.e Bakerloo, Central, District - Station -
	 * i.e BakerStreet, Bank, Temple - Current Time
	 *******************************************************************************************/
	@Test
	public void testCompareCentralLineBankStationPredictionsBetweenTrackernetAndAPI() {
		String lineName = TubeCodes.Central.toString();
		String stationCode = StationCodes.Bank.toString();
		Prediction predictionDTOFromTrackerNet = predictionService
				.getPredictionSummaryByLineAndStationFromTrackerNet(lineName,
						stationCode);
		Prediction predictionDTOFromAPI = predictionService
				.getPredictionSummaryByLineAndStationFromAPI(lineName,
						stationCode, true);
		assertEquals(predictionDTOFromTrackerNet.getLineName(),
				predictionDTOFromAPI.getLineName());
		assertEquals(predictionDTOFromTrackerNet.getStationName(),
				predictionDTOFromAPI.getStationName());

	}

     @Test
     @Ignore
     public void testCompareBakerlooLineBakerStreetStationPredictionsBetweenTrackernetAndAPI() {
        String lineName = TubeCodes.Bakerloo.toString();
        String stationCode = StationCodes.CharingCross.toString();
        Prediction predictionDTOFromTrackerNet = predictionService
                .getPredictionSummaryByLineAndStationFromTrackerNet(lineName,
                        stationCode);
        Prediction predictionDTOFromAPI = predictionService
                .getPredictionSummaryByLineAndStationFromAPI(lineName,
                        stationCode, true);
        assertEquals(predictionDTOFromTrackerNet.getLineName(),
                predictionDTOFromAPI.getLineName());
        assertEquals(predictionDTOFromTrackerNet.getStationName(),
                predictionDTOFromAPI.getStationName());

    }

    @Test
    @Ignore
    public void testCompareMetropolitanLineBakerStreetStationPredictionsBetweenTrackernetAndAPI() {
        String lineName = TubeCodes.Metropolitan.toString();
        String stationCode = StationCodes.BakerStreet.toString();
        Prediction predictionDTOFromTrackerNet = predictionService
                .getPredictionSummaryByLineAndStationFromTrackerNet(lineName,
                        stationCode);
        Prediction predictionDTOFromAPI = predictionService
                .getPredictionSummaryByLineAndStationFromAPI(lineName,
                        stationCode, true);
        assertEquals(predictionDTOFromTrackerNet.getLineName(),
                predictionDTOFromAPI.getLineName());
        assertEquals(predictionDTOFromTrackerNet.getStationName(),
                predictionDTOFromAPI.getStationName());

    }

    @Test
    @Ignore
    public void testCompareJubileeLineBakerStreetStationPredictionsBetweenTrackernetAndAPI() {
        String lineName = TubeCodes.Jubilee.toString();
        String stationCode = StationCodes.Waterloo.toString();
        Prediction predictionDTOFromTrackerNet = predictionService
                .getPredictionSummaryByLineAndStationFromTrackerNet(lineName,
                        stationCode);
        Prediction predictionDTOFromAPI = predictionService
                .getPredictionSummaryByLineAndStationFromAPI(lineName,
                        stationCode, true);
        assertEquals(predictionDTOFromTrackerNet.getLineName(),
                predictionDTOFromAPI.getLineName());
        assertEquals(predictionDTOFromTrackerNet.getStationName(),
                predictionDTOFromAPI.getStationName());

    }

}
