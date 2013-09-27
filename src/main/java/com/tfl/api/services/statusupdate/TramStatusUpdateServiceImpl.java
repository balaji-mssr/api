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

public class TramStatusUpdateServiceImpl implements StatusUpdateService {

	@Override
	public List<Line> getLineStatusListFromAPI() {
		String response = ResponseUtils.getBetaLineResponseByModes(Modes.TRAM);
		List<Line> tramICSDto = JSONHelper.getLineDTOList(response);
		return tramICSDto;
	}

	@Override
	public List<Line> getLiveLineStatusListFromESUI() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Line> getLiveLineStatusListFromICS() {
		String XML = get(AppProperties.STAGE_TRAM_ICS_FEED_END_POINT).andReturn().asString();
		List<Line> lineICSDto = XMLHelper.getICSStatusDTO(XML);
		return lineICSDto;
	}

	@Override
	public List<Line> getWeekendLineStatusFromAPI(Boolean isStub) {
		String rootResponse = ResponseUtils.getWeekendBetaLineResponseByModes(Modes.TRAM);
		List<Line> tramWeekendICSDto = JSONHelper.getLineDTOList(rootResponse);
		return tramWeekendICSDto;
	}

	@Override
	public List<Line> getWeekendLiveLineStatusFromICS(Boolean isStub) {
		String XML = get(AppProperties.LIVE_TRAM_ICS_WEEKEND_FEED_END_POINT).andReturn().asString();
		List<Line> lineICSDto = XMLHelper.getICSStatusDTO(XML);
		return lineICSDto;
	}

	@Override
	public String getLiveLineDisruptionFromICSByName(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getLiveLineDisruptionFromAPIByName(String name) {
		// TODO Auto-generated method stub
		return null;
	}

}
