package com.tfl.api.services.statusupdate;

import com.tfl.api.config.AppProperties;
import com.tfl.api.domain.Line;
import com.tfl.api.domain.enums.Modes;
import com.tfl.api.services.StatusUpdateService;
import com.tfl.api.services.utils.JSONHelper;
import com.tfl.api.services.utils.ResponseUtils;
import com.tfl.api.services.utils.XMLHelper;

import java.util.List;

import static com.jayway.restassured.RestAssured.get;

public class BusStatusUpdateServiceImpl implements StatusUpdateService {

	@Override
	public List<Line> getLineStatusListFromAPI() {
		String busResponse = ResponseUtils.getBetaLineResponseByModes(Modes.BUS);
		List<Line> lineDTO = JSONHelper.getBusLineDTOList(busResponse);
		return lineDTO;
	}

	@Override
	public List<Line> getWeekendLineStatusFromAPI(Boolean isStub) {
		String weekendResponse = ResponseUtils
				.getWeekendBetaLineResponseByModes(Modes.BUS);
		List<Line> weekendLineStatusDTO = JSONHelper
				.getBusLineDTOList(weekendResponse);
		return weekendLineStatusDTO;
	}

	@Override
	public List<Line> getLiveLineStatusListFromESUI() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Line> getLiveLineStatusListFromICS() {
		String XML = get(AppProperties.LIVE_BUS_ICS_FEED_END_POINT).andReturn()
				.asString();
		List<Line> lineBusDto = XMLHelper.getICSStatusDTO(XML);
		return lineBusDto;
	}

	@Override
	public List<Line> getWeekendLiveLineStatusFromICS(Boolean isStub) {
		String XML = get(AppProperties.LIVE_BUS_ICS_WEEKEND_FEED_END_POINT)
				.andReturn().asString();
		List<Line> lineBusDto = XMLHelper.getICSStatusDTO(XML);
		return lineBusDto;
	}

	@Override
	public String getLiveLineDisruptionFromICSByName(String name) {
		String XML = get(AppProperties.LIVE_BUS_ICS_WEEKEND_FEED_END_POINT)
				.andReturn().asString();
		return XMLHelper.getDisruptionByLineFromICS(XML,name);
	}

	@Override
	public String getLiveLineDisruptionFromAPIByName(String name) {
		String JSON = ResponseUtils.getBetaLineResponseByModes(Modes.BUS);
		//String JSON = StubJSONResponse.BUS_LINE_STATUS_WITH_DISRUPTION_JSON;
		return JSONHelper.getDisruptionByLineFromAPI(JSON, name);
	}
}
