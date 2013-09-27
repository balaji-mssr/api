package com.tfl.api.stories;

import com.tfl.api.domain.Line;
import com.tfl.api.domain.enums.InvalidAPIRequest;
import com.tfl.api.services.StatusUpdateService;
import com.tfl.api.services.statusupdate.BusStatusUpdateServiceImpl;
import com.tfl.api.services.utils.ResponseUtils;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class NWP2589BusServiceStatusTest {
	
	/*******************************************************************************************
	 * Scenario 1 - I should see the same number of disruptions (Count) for "Now" between API Response and 
	 * Current Live Site Response - ICS 
	 * Line DTO - LineName, DisruptionCount
	 * First Assertion - Count of Line Responses between ICS and API
	 * Second Assertion - Line DTO - Each element data comparison between ICS and API
	 *******************************************************************************************/
	@Test
	public void testCompareBusStatusBetweenICSandAPI() {
		StatusUpdateService statusService = new BusStatusUpdateServiceImpl();
		List<Line> busICSDTO = statusService.getLiveLineStatusListFromICS();
        List<Line> busAPIDTO = statusService.getLineStatusListFromAPI();
        assertTrue(busAPIDTO.size() > 0);
        assertTrue(busICSDTO.size() > 0);
		assertTrue(listEquals(busICSDTO, busAPIDTO));
	}
	/*******************************************************************************************
	 * Scenario 2 - Given Bus Number, I should see the same disruption reason between
	 * API Response and Current Live Site Response - ICS 
	 * Line DTO - LineName, DisruptionReason
	 * Assertion - Disruption reason should be identical for the given bus number - i.e 211
	 *******************************************************************************************/
	@Test
	public void testSpecificBusStatusBetweenICSAndAPI() {
		StatusUpdateService statusService = new BusStatusUpdateServiceImpl();
		String busName = "211";
		String disruptionFromICS = statusService.getLiveLineDisruptionFromICSByName(busName);
		String disruptionFromAPI = statusService.getLiveLineDisruptionFromAPIByName(busName);
        boolean isSameReason = StringUtils.startsWith(disruptionFromAPI.trim(),disruptionFromICS);
        if (!isSameReason) {
            System.out.println("disruption reason from ICS " + disruptionFromICS);
            System.out.println("disruption reason from API " + disruptionFromAPI);
        }
        assertTrue(isSameReason);
	}

	/*******************************************************************************************
	 * Scenario 3 - I should see the same number of disruptions (Count) for "Weekend" between 
	 * API Response and Current Live Site Response - ICS 
	 * Line DTO - LineName, DisruptionCount
	 * First Assertion - Count of Line Responses between ICS and API
	 * Second Assertion - Line DTO - Each element data comparison between ICS and API
	 *******************************************************************************************/ 	

	@Test
	public void testWeekendLineStatusBetweenAPIandICS() {
		StatusUpdateService statusService = new BusStatusUpdateServiceImpl();
		List<Line> busICSWeekendStatusDTO = statusService.getWeekendLiveLineStatusFromICS(false);
		List<Line> busAPIWeekendStatusDTO = statusService.getWeekendLineStatusFromAPI(false);
		assertTrue(busICSWeekendStatusDTO.size() > 0);
		assertTrue(busAPIWeekendStatusDTO.size() > 0);
		assertTrue(listEquals(busICSWeekendStatusDTO, busAPIWeekendStatusDTO));
	}
	/*********************************************************************************************
	 * Scenario 4 - Given Invalid parameter (Query string) to API Request
	 * I should see "404 Response Status Code (HTTP)
	 * e.g - http://dev01.dev.beta.tfl.gov.uk:81/line?modes=buss
	 *********************************************************************************************/
	@Test
	public void testInvalidAPIRequestStatusCodes() {
		assertEquals("404",ResponseUtils.getStatusCodeForInvalidRequest(InvalidAPIRequest.LINE_BUS));
	}

	private static boolean listEquals(List<Line> lineStatusFromSource,
			List<Line> lineStatusFromAPI) {
		if (lineStatusFromSource.size() != lineStatusFromAPI.size()) {
			System.out.println("Count of Line statuses are not same between API and Live");
            Set<String> uniqueBusNames = new TreeSet<String>();
            boolean isBusNameExist;
            for (Line lineFromSource:lineStatusFromSource) {
                isBusNameExist = Boolean.FALSE;
                 for (Line lineFromAPI:lineStatusFromAPI) {
                     if (StringUtils.equalsIgnoreCase(lineFromSource.getName(),lineFromAPI.getName())) {
                         isBusNameExist = Boolean.TRUE;
                         break;
                     }
                 }
                if(!isBusNameExist) {
                    uniqueBusNames.add(lineFromSource.getName());
                }
            }
            System.out.println(uniqueBusNames.toString());
			System.out.println("No of Disrupted Sizes from ICS " + lineStatusFromSource.size());
			System.out.println("No of Disrupted Sizes from API " + lineStatusFromAPI.size());

			return false;
		}
		for (Line i : lineStatusFromSource) {
			for (Line j : lineStatusFromAPI) {
                boolean isSameLineName = StringUtils.equalsIgnoreCase(i.getName(), j.getName());
                boolean isSameReasonForDelays = StringUtils.equalsIgnoreCase(i.getReason(), j.getReason());
                if (isSameLineName && !isSameReasonForDelays) {
                        System.out.print(i.getName() + ">>" + j.getName() + "<<<< Status from Source i.e ICS >>>>"
                                + i.getStatusSeverityDescription() + "<<<Status from API >>>>>"
                                + j.getStatusSeverityDescription() + "<<<Delays Reason from Source >>>>>"
                                + i.getReason() + "<<<Delays Reason from API >>>"
                                + j.getReason());
                        return false;
                }
			}
		}
		return true;
	}

}
