package com.tfl.api.services;

import java.util.List;

import com.tfl.api.domain.Line;

public interface StatusUpdateService {
	List<Line> getLineStatusListFromAPI();
	List<Line> getLiveLineStatusListFromESUI();
	List<Line> getLiveLineStatusListFromICS();	
	List<Line> getWeekendLineStatusFromAPI(Boolean isStub);
	List<Line> getWeekendLiveLineStatusFromICS(Boolean isStub);
	String getLiveLineDisruptionFromICSByName(String name);
	String getLiveLineDisruptionFromAPIByName(String name);
}
