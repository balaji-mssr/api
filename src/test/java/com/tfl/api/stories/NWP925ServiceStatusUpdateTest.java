package com.tfl.api.stories;

import com.tfl.api.config.AppProperties;
import com.tfl.api.domain.Disruption;
import com.tfl.api.domain.Line;
import com.tfl.api.domain.enums.InvalidAPIRequest;
import com.tfl.api.domain.enums.Modes;
import com.tfl.api.services.StatusUpdateService;
import com.tfl.api.services.statusupdate.StatusUpdateServiceImpl;
import com.tfl.api.services.utils.JSONHelper;
import com.tfl.api.services.utils.ResponseUtils;
import com.tfl.api.services.utils.XMLHelper;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import java.util.List;

import static com.jayway.restassured.RestAssured.get;
import static org.junit.Assert.*;

public class NWP925ServiceStatusUpdateTest {

	/**********************************************************************************************
	 * Scenario 1 - Assert the Track - Tube, DLR, OG Line Statuses for "Now" between ESUI and API *
	 * Line DTO - LineName, LineSeverityDescription, DisruptionReason							  *
	 * First Assertion - Count of Line Responses between ESUI and API							  *
	 * Second Assertion - Line DTO - Each element data comparison between ESUI and API			  *
	 **********************************************************************************************/ 
	@Test
	public void testCompareLineStatusBetweenESUIandAPI() {
		StatusUpdateService statusUpdateService = new StatusUpdateServiceImpl();
		List<Line> listBetaDTO = statusUpdateService
				.getLineStatusListFromAPI();
		List<Line> listESUIDto = statusUpdateService
				.getLiveLineStatusListFromESUI();
		assertTrue(listESUIDto.size() > 0);
		assertTrue(listBetaDTO.size() > 0);
		assertTrue(listEquals(listBetaDTO, listESUIDto, false));
	}

	/*********************************************************************************************
	 * Scenario 2 - Assert the Track - Tube, DLR, OG Line Statuses for "Now" between ICS and API *
	 * Line DTO - LineName, LineSeverityDescription, DisruptionReason							 *
	 * First Assertion - Count of Line Responses between ICS and API							 *
	 * Second Assertion - Line DTO - Each element data comparison between ICS and API			 *
	 *********************************************************************************************/ 
	@Test
	public void testCompareLineStatusBetweenICSandAPI() {
		StatusUpdateService statusUpdateService = new StatusUpdateServiceImpl();
		List<Line> listICSDto = statusUpdateService
				.getLiveLineStatusListFromICS();
		List<Line> listBetaDTO = statusUpdateService
				.getLineStatusListFromAPI();
		assertNotNull(listBetaDTO);
		assertNotNull(listICSDto);
		assertTrue(listEquals(listBetaDTO, listICSDto, true));
	}

	/*************************************************************************************************
	 * Scenario 3 - Assert the Track - Tube, DLR, OG Line Statuses for "Week End" between ICS and API*
	 * Line DTO - LineName, LineSeverityDescription, DisruptionReason								 *
	 * First Assertion - Count of Line Responses between ICS and API								 *
	 * Second Assertion - Line DTO - Each element data comparison between ICS and API				 *
	 *************************************************************************************************/ 
	@Test
	public void testWeekendLineStatusBetweenAPIandICS() {
		StatusUpdateService statusService = new StatusUpdateServiceImpl();
		List<Line> weekendAPILineStatus = statusService.getWeekendLineStatusFromAPI(false);
		List<Line> weekendICSLineStatus = statusService.getWeekendLiveLineStatusFromICS(false);
		assertNotNull(weekendAPILineStatus);
		assertNotNull(weekendICSLineStatus);
        boolean debugEnabled = true;
		assertTrue(listEquals(weekendAPILineStatus, weekendICSLineStatus, debugEnabled));

	}
	
	/**********************************************************************************************
	 * Scenario 4 - Assert the Track - Overground Line Statuses for "Week End" between ICS and API*
	 * A Line can have multiple disruptions between service routes (from and to) i.e Overground   *
	 * Part Closure between Harrow and Watford, Part Closure between Willesden and Queens Park	  *
	 * Line DTO - LineName, LineSeverityDescription, DisruptionReason							  *
	 * Disruption DTO - Disruption Status (Each), StopPoints, Disruption Reason					  *
	 * First Assertion - Count of Line Responses between ICS and API							  *
	 * Second Assertion - Line DTO - Each element data comparison between ICS and API			  *
	 * Third Assertion - Disruption DTO - Each Element Data Comparison 			  				  *
	 **********************************************************************************************/ 
	@Test
	public void testOvergroundWeekendDisruptionBetweenICSAndAPI() {
		String XML = get(AppProperties.LIVE_ICS_WEEKEND_API_END_POINT).andReturn()
				.asString();
		String JSONResponse = ResponseUtils.getWeekendBetaLineResponseByModes(Modes.OVERGROUND);
		List<Line> overgroundStatus = XMLHelper.getWeekendStatusFromICSByLine(XML, "Overground");
        List<Line> overGroundAPIDTO = JSONHelper.getLineDTOList(JSONResponse);
        boolean printLineStatus = true;
		assertTrue(listEquals(overGroundAPIDTO, overgroundStatus, printLineStatus));
	}
	
	/*******************************************************************************************
	 * Scenario 5 - Assert the Track - Tube, DLR, OG Line Statuses for "Weekend" between 
	 * ICS Stub Response and API Stub Response
	 * Line DTO - LineName, LineSeverityDescription, DisruptionReason
	 * First Assertion - Count of Line Responses between ICS and API
	 * Second Assertion - Line DTO - Each element data comparison between ICS and API
	 *******************************************************************************************/ 
	public void testTrackWeekendStatusBetweenICSandAPIStubResponse() {
		StatusUpdateService statusService = new StatusUpdateServiceImpl();
		boolean isStub = Boolean.TRUE;
		List<Line> weekendAPILineStatus = statusService.getWeekendLineStatusFromAPI(isStub);
		List<Line> weekendICSLineStatus = statusService.getWeekendLiveLineStatusFromICS(isStub);
		assertTrue(listEquals(weekendAPILineStatus, weekendICSLineStatus, false));
	}
	
	/*********************************************************************************************
	 * Scenario 6 - Given Invalid parameter (Query string) to API Request
	 * I should see "404 Response Status Code (HTTP)
	 * e.g - http://dev01.dev.beta.tfl.gov.uk:81/line?modes=
	 *********************************************************************************************/
	@Test
	public void testInvalidAPIRequestStatusCodes() {
		assertEquals("404",ResponseUtils.getStatusCodeForInvalidRequest(InvalidAPIRequest.LINE_BUS));
	}
	
	/*********************************************************************************************
	 * Scenario 7 - Accessing API gateway without Authorization Key and App ID
	 * I should see "401 Response Status Code (HTTP)
	 * e.g - http://dev01.dev.beta.tfl.gov.uk:81/line?modes=
	 *********************************************************************************************/
	@Test
	public void testAuthorisedAccessToAPIGateway() {
		assertEquals("401",ResponseUtils.getStatusCodeForUnAuthorizedAPIAccess(InvalidAPIRequest.LINE_WITHOUT_AUTH));
	}
	private static boolean listEquals(List<Line> lineStatusFromAPI,
			List<Line> lineStatusFromSource, Boolean debugEnabled) {
		if (lineStatusFromSource.size() != lineStatusFromAPI.size()) {
			System.out.println("Count of Line statuses are not same between API and Live");
			return false;
		}
		if (debugEnabled) {
			System.out.println("**********************************************************************************");
			System.out.println("Line Name    "+"      "+"    Status From ICS     "+"            Status From API  ");
			System.out.println("**********************************************************************************");
		}
		for (Line i : lineStatusFromSource) {
			for (Line j : lineStatusFromAPI) {
				boolean isSameLineName = StringUtils.equalsIgnoreCase(i.getName(), j.getName());
				if (isSameLineName) {
					if (debugEnabled) {
						String formatChar = "\t\t%s";
						String formatICSChar = "\t\t\t%s";
						if (i.getName().length() <= 7) {
							formatChar = "\t\t\t%s";
							formatICSChar = "\t\t\t%s";
						} else if (i.getName().length() > 15) {
							formatChar = "\t%s";
							formatICSChar = "\t\t\t%s";
						}
						System.out.println(j.getName() + String.format(formatChar , i.getStatusSeverityDescription()) +  String.format(formatICSChar , j.getStatusSeverityDescription()));
					}
					boolean isSameSeverityStatus = StringUtils.equalsIgnoreCase(i.getStatusSeverityDescription(),j.getStatusSeverityDescription());
					boolean isSameReasonForDelays = StringUtils.equalsIgnoreCase(i.getReason(), j.getReason());
					boolean isDelayService = !StringUtils.equalsIgnoreCase(j.getStatusSeverityDescription(), "Good Service" );
                    if (i.getDistruptionsCount() > 1) {
                        boolean isSameDisruptionReason = Boolean.FALSE;
                        for (Disruption disruptionFromICS:i.getDisruptionList()) {
                            isSameDisruptionReason = StringUtils.contains(j.getReason(),disruptionFromICS.getReason());
                            if (isSameDisruptionReason) {
                               System.out.println("Matching the Disruption Reason ");
                               break;
                            }
                        }
                        if (!isSameSeverityStatus || isDelayService && !isSameDisruptionReason) {
                            System.out.print(i.getName() + "<<<< Status from Source i.e ICS >>>>" + i.getStatusSeverityDescription() + "<<<Delays Reason from Source >>>>>"+ i.getReason());
                            System.out.print(j.getName() + "<<<Status from API >>>>>" + j.getStatusSeverityDescription() +  "<<<Delays Reason from API >>>" + j.getReason());
                            return false;
                        }
                    } else {
                        if (!isSameSeverityStatus || isDelayService && !isSameReasonForDelays) {
                            System.out.print(i.getName() + "<<<< Status from Source i.e ICS >>>>" + i.getStatusSeverityDescription() + "<<<Delays Reason from Source >>>>>"+ i.getReason());
                            System.out.print(j.getName() + "<<<Status from API >>>>>" + j.getStatusSeverityDescription() +  "<<<Delays Reason from API >>>" + j.getReason());
                            return false;
                        }
                    }

					if (isDelayService) {
						//System.out.println("Now Comparing Disruptions for the Delayed Services");
						//return compareDisruptionsBetweenICSAndAPI(j, i);
					}
				}
			}
		}
		return true;
	}
	
	private static boolean compareDisruptionsBetweenICSAndAPI(Line lineFromAPI, Line lineFromICS) {
		for (Disruption d : lineFromAPI.getDisruptionList()) {
				for (Disruption disruptionFromSource : lineFromICS.getDisruptionList()) {
					//boolean isSameDisruptionStatus = StringUtils.equalsIgnoreCase(d.getStatus(), disruptionFromSource.getStatus());
					boolean isSameStopPoints = StringUtils.equalsIgnoreCase(d.getStopPoints(), disruptionFromSource.getStopPoints());
					if (!isSameStopPoints) {
						System.out.println("Disruptions Stop Points are different >>> Line Name : " + lineFromAPI.getName()+ " Stop Points from API " + d.getStopPoints() + " Stop Points from Source i.e ICS " + disruptionFromSource.getStopPoints());
						return false;
					}
				}
		}
		return true;
	}
}
