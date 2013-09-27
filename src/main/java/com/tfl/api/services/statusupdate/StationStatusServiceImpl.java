package com.tfl.api.services.statusupdate;

import static com.jayway.restassured.RestAssured.get;
import static com.jayway.restassured.path.xml.XmlPath.with;

import java.util.List;

import com.tfl.api.config.AppProperties;
import com.tfl.api.domain.StopPoint;
import com.tfl.api.resources.StubJSONResponse;
import com.tfl.api.resources.StubXMLResponse;
import com.tfl.api.services.utils.JSONHelper;
import com.tfl.api.services.utils.ResponseUtils;
import com.tfl.api.services.utils.XMLHelper;

public class StationStatusServiceImpl {
	
	public List<StopPoint> getDisruptedStopPointsFromAPI(Boolean isStub) {
		String rootResponse = "";
		if (isStub) {
			rootResponse = StubJSONResponse.STOP_POINT_API_RESPONSE;
		} else {
			rootResponse = ResponseUtils.getStopPointResponseFromAPIForTrackModes();
		}
		List<StopPoint> stopPointDTOList = JSONHelper.getStopPointDTOListFromAPI(rootResponse);		
		return stopPointDTOList;
	}
	
	public List<StopPoint> getDisruptedStopPointsFromICS(Boolean isStub) {
		String XML = "";
		if (isStub) {
			XML = StubXMLResponse.STOPPOINT_ICS_XML;
		} else {
			XML = get(AppProperties.LIVE_ICS_API_END_POINT).andReturn().asString();
		}
		List<StopPoint> lineICSDto = XMLHelper.getStopPointDTOListFromICS(XML);
		return lineICSDto;
	}
	
	public int getNumberOfAffectedStopPointsFromICS() {
		String XML = get(AppProperties.LIVE_ICS_API_END_POINT).andReturn().asString();
		return XMLHelper.getNumberOfAffectedStopPoints(XML);
	}
	
	public int getNumberOfAffectedStopPointsFromAPI() {
		String JSON = ResponseUtils.getStopPointResponseFromAPIForTrackModes();
		return JSONHelper.getNumberOfAffectedStopPoints(JSON);
	}

}
