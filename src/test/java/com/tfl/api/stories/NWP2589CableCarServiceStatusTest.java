package com.tfl.api.stories;

import com.tfl.api.domain.Line;
import com.tfl.api.services.StatusUpdateService;
import com.tfl.api.services.statusupdate.CableCarStatusUpdateServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class NWP2589CableCarServiceStatusTest {
	@Test
	public void testCompareCableCarStatusBetweenICSandAPI() {
		StatusUpdateService statusService = new CableCarStatusUpdateServiceImpl();
		List<Line> cableCarICSDTO = statusService.getLiveLineStatusListFromICS();
		List<Line> cableCarAPIDTO = statusService.getLineStatusListFromAPI();
		assertNotNull(cableCarICSDTO);
		assertNotNull(cableCarAPIDTO);
        assertTrue(cableCarAPIDTO.size() > 0);
		boolean isGoodCableCarServiceFromICS = Boolean.TRUE;
		if (cableCarICSDTO.size() > 0) {
			isGoodCableCarServiceFromICS = Boolean.FALSE;
			for (Line line : cableCarICSDTO) {
				System.out.println("Line Name : " + line.getName()
						+ "   Status : " + line.getStatusSeverityDescription()
						+ "   No Of Distruptions: " + line.getDistruptionsCount());
			}
		}
		boolean isGoodCableCarServiceFromAPI = Boolean.TRUE;
		for (Line line : cableCarAPIDTO) {
			if (!StringUtils.equalsIgnoreCase("Good Service", line.getStatusSeverityDescription())) {
				isGoodCableCarServiceFromAPI = Boolean.FALSE;
				Line distruptedLine = new Line();
				System.out.println("Line Name : " + line.getName()
						+ "   Status : " + line.getStatusSeverityDescription()
						+ "   No Of Distruptions: " + line.getDistruptionsCount());
			}
		}
		assertEquals(isGoodCableCarServiceFromICS,isGoodCableCarServiceFromAPI);
	}
	
	@Test
	public void testCableCarWeekendLineStatusBetweenAPIandICS() {
		StatusUpdateService statusService = new CableCarStatusUpdateServiceImpl();
		List<Line> cableCarWeekendICSDTO = statusService.getWeekendLiveLineStatusFromICS(false);
		List<Line> cableCarWeekendAPIDTO = statusService.getWeekendLineStatusFromAPI(false);
		assertNotNull(cableCarWeekendICSDTO);
		assertNotNull(cableCarWeekendAPIDTO);
        assertTrue(cableCarWeekendAPIDTO.size() > 0);
		boolean isGoodCableCarServiceFromICS = Boolean.TRUE;
		if (cableCarWeekendICSDTO.size() > 0) {
			isGoodCableCarServiceFromICS = Boolean.FALSE;
			for (Line line : cableCarWeekendICSDTO) {
				System.out.println("Line Name : " + line.getName()
						+ "   Status : " + line.getStatusSeverityDescription()
						+ "   No Of Distruptions: " + line.getDistruptionsCount());
			}
		}
		boolean isGoodCableCarServiceFromAPI = Boolean.TRUE;
		for (Line line : cableCarWeekendAPIDTO) {
			if (!StringUtils.equalsIgnoreCase("Good Service", line.getStatusSeverityDescription())) {
				isGoodCableCarServiceFromAPI = Boolean.FALSE;
				Line distruptedLine = new Line();
				System.out.println("Line Name : " + line.getName()
						+ "   Status : " + line.getStatusSeverityDescription()
						+ "   No Of Distruptions: " + line.getDistruptionsCount());
			}
		}
		assertEquals(isGoodCableCarServiceFromICS,isGoodCableCarServiceFromAPI);
		if (Boolean.FALSE.equals(isGoodCableCarServiceFromICS) && Boolean.FALSE.equals(isGoodCableCarServiceFromAPI)) {
			//Assert the disrupted status route between ICS and API
		}
	}
}
