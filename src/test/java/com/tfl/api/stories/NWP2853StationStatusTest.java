package com.tfl.api.stories;

import com.tfl.api.domain.StopPoint;
import com.tfl.api.services.statusupdate.StationStatusServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class NWP2853StationStatusTest {
	
	private StationStatusServiceImpl stationStatusService;
	
	@Before
	public void setup() {
		stationStatusService = new StationStatusServiceImpl();
	}
	
	/********************************************************************************************
	 * Scenario 1 - Compare the Station Disruption details between API and ICS					*
	 ********************************************************************************************/
	@Test
	public void testCompareStationStatusBetweenAPIAndICS() {
		List<StopPoint> stopPointListFromICS = stationStatusService.getDisruptedStopPointsFromICS(false);
		List<StopPoint> stopPointListFromAPI = stationStatusService.getDisruptedStopPointsFromAPI(false);
        assertTrue(stopPointListFromICS.size() > 0);
		assertTrue(stopPointListFromAPI.size() > 0);
        assertTrue(checkStopPointAffectedReasonsAreSame(stopPointListFromICS,stopPointListFromAPI));
	}

    private boolean checkStopPointAffectedReasonsAreSame(List<StopPoint> stopPointListFromICS,List<StopPoint> stopPointListFromAPI) {
        for (StopPoint stopPointFromICS:stopPointListFromICS) {
            for (StopPoint stopPointFromAPI:stopPointListFromAPI) {
                boolean isSameStation = StringUtils.equalsIgnoreCase(stopPointFromAPI.getName(), stopPointFromICS.getName());
                boolean isSameAffectedReason = StringUtils.equalsIgnoreCase(stopPointFromAPI.getReason().trim().replace("\n", ""),stopPointFromICS.getReason().trim().replace("\n", ""));
                if (!StringUtils.equalsIgnoreCase(stopPointFromAPI.getName(),"Bank")) {
                    if (isSameStation && !isSameAffectedReason) {
                        System.out.println("Stop Point Disruption Reason is not same >>>Station Name from API " + stopPointFromAPI.getName() + "Station Name from Source "+stopPointFromICS.getName() + " Affected Reason from API " + stopPointFromAPI.getReason() + " Affected Reason from Source "+ stopPointFromICS.getReason());
                        return false;
                    }
                }
            }
        }
        return true;
    }
	
	/********************************************************************************************
	 * Scenario 2 - Compare the Station Disruption details between API and ICS from the STUB	*
	 ********************************************************************************************/
	@Test
	public void testCompareStationStatusBetweenAPIAndICSStubs() {
		boolean isStub = Boolean.TRUE;
		List<StopPoint> stopPointListFromICS = stationStatusService.getDisruptedStopPointsFromICS(isStub);
		List<StopPoint> stopPointListFromAPI = stationStatusService.getDisruptedStopPointsFromAPI(isStub);
		assertNotNull(stopPointListFromICS);
		assertNotNull(stopPointListFromAPI);
	}
	
	/*************************************************************************************************************
	 * Scenario 3 - Number of Stations - Affected Stop Points should be same between ICS and API *
	 * ***********************************************************************************************************/
	
	@Test
	public void testNumberOfAffectedStopPointsBetweenAPIAndICS() {
		assertEquals(stationStatusService.getNumberOfAffectedStopPointsFromICS(),stationStatusService.getNumberOfAffectedStopPointsFromAPI());
	}
}
