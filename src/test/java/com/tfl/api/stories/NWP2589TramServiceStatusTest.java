package com.tfl.api.stories;

import com.tfl.api.domain.Line;
import com.tfl.api.services.StatusUpdateService;
import com.tfl.api.services.statusupdate.TramStatusUpdateServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class NWP2589TramServiceStatusTest {
	
	@Test
	public void testCompareTramStatusBetweenICSandAPI() {
		StatusUpdateService statusService = new TramStatusUpdateServiceImpl();
		List<Line> tramICSDTO = statusService.getLiveLineStatusListFromICS();
		List<Line> tramAPIDTO = statusService.getLineStatusListFromAPI();
		assertNotNull(tramICSDTO);
		assertNotNull(tramAPIDTO);
        assertTrue(tramAPIDTO.size() > 0);
		boolean isGoodTramServiceFromICS = Boolean.TRUE;
		if (tramICSDTO.size() > 0) {
			isGoodTramServiceFromICS = Boolean.FALSE;
			for (Line line : tramICSDTO) {
				System.out.println("Line Name : " + line.getName()
						+ "   Status : " + line.getStatusSeverityDescription()
						+ "   No Of Distruptions: " + line.getDistruptionsCount());
			}
		}
		boolean isGoodTramServiceFromAPI = Boolean.TRUE;
		for (Line line : tramAPIDTO) {
			if (!StringUtils.equalsIgnoreCase("Good Service", line.getStatusSeverityDescription())) {
				isGoodTramServiceFromAPI = Boolean.FALSE;
				Line distruptedLine = new Line();
				System.out.println("Line Name : " + line.getName()
						+ "   Status : " + line.getStatusSeverityDescription()
						+ "   No Of Distruptions: " + line.getDistruptionsCount());
			}
		}
		assertEquals(isGoodTramServiceFromICS,isGoodTramServiceFromAPI);
		if (Boolean.FALSE.equals(isGoodTramServiceFromICS) && Boolean.FALSE.equals(isGoodTramServiceFromAPI)) {
			//Assert the disrupted status route between ICS and API - There are only 4 Routes
		}
	}
	
	@Test
	public void testTramWeekendLineStatusBetweenAPIandICS() {
		StatusUpdateService statusService = new TramStatusUpdateServiceImpl();
		List<Line> tramWeekendICSDTO = statusService.getWeekendLiveLineStatusFromICS(false);
		List<Line> tramWeekendAPIDTO = statusService.getWeekendLineStatusFromAPI(false);
		assertNotNull(tramWeekendICSDTO);
		assertNotNull(tramWeekendAPIDTO);
        assertTrue(tramWeekendAPIDTO.size() > 0);
		boolean isGoodTramServiceFromICS = Boolean.TRUE;
		if (tramWeekendICSDTO.size() > 0) {
			isGoodTramServiceFromICS = Boolean.FALSE;
			for (Line line : tramWeekendICSDTO) {
				System.out.println("Line Name : " + line.getName()
						+ "   Status : " + line.getStatusSeverityDescription()
						+ "   No Of Distruptions: " + line.getDistruptionsCount());
			}
		}
		boolean isGoodTramServiceFromAPI = Boolean.TRUE;
		for (Line line : tramWeekendAPIDTO) {
			if (!StringUtils.equalsIgnoreCase("Good Service", line.getStatusSeverityDescription())) {
				isGoodTramServiceFromAPI = Boolean.FALSE;
				Line distruptedLine = new Line();
				System.out.println("Line Name : " + line.getName()
						+ "   Status : " + line.getStatusSeverityDescription()
						+ "   No Of Distruptions: " + line.getDistruptionsCount());
			}
		}
		assertEquals(isGoodTramServiceFromICS,isGoodTramServiceFromAPI);
		if (Boolean.FALSE.equals(isGoodTramServiceFromICS) && Boolean.FALSE.equals(isGoodTramServiceFromAPI)) {
			//Assert the disrupted status route between ICS and API
		}
	}

}
