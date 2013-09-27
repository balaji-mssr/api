package com.tfl.api.services.statusupdate;

import com.tfl.api.config.AppProperties;
import com.tfl.api.domain.Line;
import com.tfl.api.domain.enums.Modes;
import com.tfl.api.resources.StubJSONResponse;
import com.tfl.api.resources.StubXMLResponse;
import com.tfl.api.services.StatusUpdateService;
import com.tfl.api.services.utils.JSONHelper;
import com.tfl.api.services.utils.ResponseUtils;
import com.tfl.api.services.utils.XMLHelper;

import java.util.List;

import static com.jayway.restassured.RestAssured.get;

public class StatusUpdateServiceImpl implements StatusUpdateService {

	@Override
	public List<Line> getLineStatusListFromAPI() {
		String tubeResponse = ResponseUtils
				.getBetaLineResponseByModes(Modes.TRACK);
		List<Line> lineDTO = JSONHelper.getLineDTOList(tubeResponse);
		return lineDTO;
	}

	@Override
	public List<Line> getWeekendLineStatusFromAPI(Boolean isStub) {
		String weekendResponse = ResponseUtils
				.getWeekendBetaLineResponseByModes(Modes.TRACK);
		if (isStub) {
			weekendResponse = StubJSONResponse.TUBE_WEEKEND_DISRUPTION_STRIPPED_JSON;
		}
		List<Line> weekendLineStatusDTO = JSONHelper
				.getLineDTOList(weekendResponse);
		return weekendLineStatusDTO;
	}

	@Override
	public List<Line> getLiveLineStatusListFromESUI() {
		String XML = get(AppProperties.LIVE_ESUI_API_END_POINT).andReturn()
				.asString();
		List<Line> lineESUIDto = XMLHelper.getESUIStatusDTO(XML);
		return lineESUIDto;
	}

	@Override
	public List<Line> getLiveLineStatusListFromICS() {
		String XML = get(AppProperties.LIVE_ICS_API_END_POINT).andReturn()
				.asString();
		List<Line> lineICSDto = XMLHelper.getICSStatusDTO(XML);
		return lineICSDto;
	}

	@Override
	public List<Line> getWeekendLiveLineStatusFromICS(Boolean isStub) {
		String XML = get(AppProperties.LIVE_ICS_WEEKEND_API_END_POINT)
				.andReturn().asString();
		if (isStub) {
			XML = StubXMLResponse.TUBE_ICS_WEEKEND_XML;
		}
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
