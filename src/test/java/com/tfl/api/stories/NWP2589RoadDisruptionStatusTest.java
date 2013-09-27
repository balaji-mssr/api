package com.tfl.api.stories;

import com.tfl.api.services.statusupdate.RoadDisruptionsStatusServiceImpl;
import org.junit.Test;
import org.junit.Ignore;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class NWP2589RoadDisruptionStatusTest {
	
	/*******************************************************************************************
	 * Scenario0 - Assert number of Severe severity incidents are same between API and TIMS Feed
	 *******************************************************************************************/ 
	@Test
	public void testTotalNumberOfSevereRoadIncidents() {
		RoadDisruptionsStatusServiceImpl roadService = new RoadDisruptionsStatusServiceImpl();
		int severeIncidentsFromAPI = roadService.getTotalNumberOfSevereRoadDisruptionsFromAPI();
        String severityType = "Severe";
		int severeIncidentsFromTIMS = roadService.getTotalNumberOfSevereRoadDisruptionsFromTIMSFeed(severityType);
		System.out.println("from API "+severeIncidentsFromAPI + " from TIMS " +severeIncidentsFromTIMS);
		assertEquals(severeIncidentsFromAPI,severeIncidentsFromTIMS);
	}

    /*******************************************************************************************
	 * Scenario1 - Assert number of serious severity incidents are same between API and TIMS Feed
	 *******************************************************************************************/
	@Test
	public void testTotalNumberOfSeriousRoadIncidents() {
		RoadDisruptionsStatusServiceImpl roadService = new RoadDisruptionsStatusServiceImpl();
		int seriousIncidentsFromAPI = roadService.getTotalNumberOfSeriousRoadDisruptionsFromAPI();
        String severityType = "Serious";
		int seriousIncidentsFromTIMS = roadService.getTotalNumberOfSevereRoadDisruptionsFromTIMSFeed(severityType);
		System.out.println("from API "+seriousIncidentsFromAPI + " from TIMS " +seriousIncidentsFromTIMS);
		assertEquals(seriousIncidentsFromAPI,seriousIncidentsFromTIMS);
	}
	
	/*******************************************************************************************
	 * Scenario2 - Assert number of Moderate incidents are same between API and TIMS Feed
	 *******************************************************************************************/ 
	@Test
    @Ignore
    public void testTotalNumberOfModerateRoadIncidents() {
		RoadDisruptionsStatusServiceImpl roadService = new RoadDisruptionsStatusServiceImpl();
		int noOfIncidentsFromAPI = roadService.getTotalNumberOfModerateRoadDisruptionsFromAPI();
		int noOfIncidentsFromTIMS = roadService.getTotalNumberOfModerateRoadDisruptionsFromTIMSFeed();
		System.out.println("from API "+noOfIncidentsFromAPI + " from TIMS " +noOfIncidentsFromTIMS);
		assertEquals(noOfIncidentsFromAPI,noOfIncidentsFromTIMS);
	}
	
	/****************************************************************************************************
	 * Scenario3 - Given Active (Status) and Moderate (Severity) Road disruptions details from TIMS Feed
     * I should see the same Road disruption details in API Response.
	 ***************************************************************************************************/
	@Test
    public void testCompareRoadDisruptionBetweenTIMSandAPI() {
		RoadDisruptionsStatusServiceImpl roadService = new RoadDisruptionsStatusServiceImpl();
		assertTrue(roadService.compareActiveRoadDisruptionsBetweenTIMSAndAPI());
	}
	
	/*******************************************************************************************
	 * Scenario4 - Given the same Road Corridor I should see the same Road Status
	 * Between TIMS Feed and API for the given from and to date
	 *******************************************************************************************/
    @Test
    public void testRoadPlannedWorksForGivenCorridorBetweenAPIAndTIMS() {
        RoadDisruptionsStatusServiceImpl roadService = new RoadDisruptionsStatusServiceImpl();
        String corridorID = "A10";
        assertTrue(roadService.compareRoadPlannedWorksBetweenAPIAndTIMS(corridorID));
    }

    @Test
    public void testRoadsDisruptionAPIConnectivityCheck() {
        RoadDisruptionsStatusServiceImpl roadService = new RoadDisruptionsStatusServiceImpl();
        int apiResponseStatusCode = roadService.getRoadDisruptionsAPIResponseStatusCode();
        assertEquals(200,apiResponseStatusCode);
    }
}
