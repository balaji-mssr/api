package com.tfl.api.stories;

import com.tfl.api.domain.Line;
import com.tfl.api.services.StatusUpdateService;
import com.tfl.api.services.statusupdate.RiverBusStatusServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class NWP2589RiverBusServiceStatusTest {
	@Test
	public void testCompareRiverStatusBetweenICSandAPI() {
		StatusUpdateService statusService = new RiverBusStatusServiceImpl();
		List<Line> riverBusICSDTO = statusService.getLiveLineStatusListFromICS();
		List<Line> riverBusAPIDTO = statusService.getLineStatusListFromAPI();
		assertNotNull(riverBusICSDTO);
		assertNotNull(riverBusAPIDTO);
		boolean isGoodRiverBusServiceFromICS = Boolean.TRUE;
		if (riverBusICSDTO.size() > 0) {
			isGoodRiverBusServiceFromICS = Boolean.FALSE;
			for (Line line : riverBusICSDTO) {
				System.out.println("Line Name : " + line.getName()
						+ "   Status : " + line.getStatusSeverityDescription()
						+ "   No Of Distruptions: " + line.getDistruptionsCount());
			}
		}
		boolean isGoodRiverBusServiceFromAPI = Boolean.TRUE;
		for (Line line : riverBusAPIDTO) {
			if (!StringUtils.equalsIgnoreCase("Good Service", line.getStatusSeverityDescription())) {
				isGoodRiverBusServiceFromAPI = Boolean.FALSE;
				Line distruptedLine = new Line();
				System.out.println("Line Name : " + line.getName()
						+ "   Status : " + line.getStatusSeverityDescription()
						+ "   No Of Distruptions: " + line.getDistruptionsCount());
			}
		}
		assertEquals(isGoodRiverBusServiceFromICS,isGoodRiverBusServiceFromAPI);
	}
	
	@Test
	public void testCableCarWeekendLineStatusBetweenAPIandICS() {
		StatusUpdateService statusService = new RiverBusStatusServiceImpl();
		List<Line> riverBusWeekendICSDTO = statusService.getWeekendLiveLineStatusFromICS(false);
		List<Line> riverBusWeekendAPIDTO = statusService.getWeekendLineStatusFromAPI(false);
		assertNotNull(riverBusWeekendICSDTO);
		assertNotNull(riverBusWeekendAPIDTO);
		boolean isGoodRiverBusServiceFromICS = Boolean.TRUE;
		if (riverBusWeekendICSDTO.size() > 0) {
			isGoodRiverBusServiceFromICS = Boolean.FALSE;
			for (Line line : riverBusWeekendICSDTO) {
				System.out.println("Line Name : " + line.getName()
						+ "   Status : " + line.getStatusSeverityDescription()
						+ "   No Of Distruptions: " + line.getDistruptionsCount());
			}
		}
		boolean isGoodRiverBusServiceFromAPI = Boolean.TRUE;
		for (Line line : riverBusWeekendAPIDTO) {
			if (!StringUtils.equalsIgnoreCase("Good Service", line.getStatusSeverityDescription())) {
				isGoodRiverBusServiceFromAPI = Boolean.FALSE;
				Line distruptedLine = new Line();
				System.out.println("Line Name : " + line.getName()
						+ "   Status : " + line.getStatusSeverityDescription()
						+ "   No Of Distruptions: " + line.getDistruptionsCount());
			}
		}
		assertEquals(isGoodRiverBusServiceFromICS,isGoodRiverBusServiceFromAPI);
		if (Boolean.FALSE.equals(isGoodRiverBusServiceFromICS) && Boolean.FALSE.equals(isGoodRiverBusServiceFromAPI)) {
			//Assert the disrupted status route between ICS and API
		}
	}

}
