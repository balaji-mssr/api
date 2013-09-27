package com.tfl.api.services.utils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.tfl.api.domain.*;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

import static com.jayway.restassured.path.json.JsonPath.from;

public class JSONHelper {
	
	private static JsonArray array = new JsonArray();
	private static Gson gson = new Gson();
	private static JsonParser parser = new JsonParser();

	public static List<Line> getLineDTOList(String rootResponse) {
		List<Line> lineDTO = new ArrayList();
		List<Disruption> disruptionsList = new ArrayList();
		String lineName = "";
		String lineResponse = "";
		String lineStatus = "";
		String lineStatusesForThisLine = "";
        if (!"[null]".equals(rootResponse)) {
        List<Map<String, String>> lineRoot = from(rootResponse).get("");
            for (int i = 0; i < lineRoot.size(); i++) {
                lineResponse = String.valueOf(lineRoot.get(i));
                lineName = String.valueOf(lineRoot.get(i).get("name"));
                final List<String> lineStatusForRoot = from(lineResponse).getList(
                        "lineStatuses");
                lineStatusesForThisLine = from(lineResponse).get("lineStatuses").toString();
                if (!StringUtils.contains("[null]", from(lineStatusesForThisLine).get("disruption").toString())) {
                    disruptionsList = constructDisruptionDTO(lineStatusesForThisLine);
                }
                constructLineStatusDTO(lineDTO, disruptionsList,
                        lineName, lineStatus, lineStatusForRoot);
            }
        }
		return lineDTO;
	}

    public static List<Line> getBusLineDTOList(String json) {
        String lineResponse = "";
        String lineName = "";
        String lineStatus = "";
        List<Line> lineDTO = new ArrayList();
        if (!"[null]".equals(json)) {
            List<Map<String, String>> lineRoot = from(json).get("");
            for (int i = 0; i < lineRoot.size(); i++) {
                lineResponse = String.valueOf(lineRoot.get(i));
                lineName = String.valueOf(lineRoot.get(i).get("name"));
                final List<String> lineStatusForRoot = from(lineResponse).getList(
                        "lineStatuses");
                constructLineStatusDTO(lineDTO,lineName, lineStatus, lineStatusForRoot);
            }
        }
        return lineDTO;
    }

    private static void constructLineStatusDTO(List<Line> lineDTO, String lineName,
                                               String lineStatus, final List<String> lineStatusForRoot) {
        String json = "";
        String reason = "";
        HashSet<String> ts = new HashSet<String>();
        for (int j = 0; j < lineStatusForRoot.size(); j++) {
            json = "[" + String.valueOf(lineStatusForRoot.get(j)) + "]";
            array = parser.parse(json).getAsJsonArray();
            Line event = gson.fromJson(array.get(0), Line.class);
                lineStatus = event.getStatusSeverityDescription();
            if (j + 1 == lineStatusForRoot.size() && StringUtils.equals(lineStatus,"Information")) {
                Line line = new Line();
                lineStatus = ts.toString().trim().substring(1, ts.toString().trim().length() - 1);
                line.setName(lineName);
                line.setStatusSeverityDescription(lineStatus);
                line.setDistruptionsCount(lineStatusForRoot.size());
                reason = event.getReason();
                if (null != reason && reason.length() > 0) {
                    reason = reason.substring(reason.indexOf(":")+1,reason.length()).trim();
                }
                line.setReason(reason);
                lineStatus = "";
                lineDTO.add(line);
            }
        }
    }
	
	public static List<Line> getWeekendStatusFromAPIByLine(String rootResponse,String line) {
		List<Line> lineDTO = new ArrayList();
		List<Disruption> disruptionsList = new ArrayList();
		String lineName = "";
		String lineResponse = "";
		String lineStatus = "";
		String lineStatusesForThisLine = "";

		List<Map<String, String>> lineRoot = from(rootResponse).get("");
		for (int i = 0; i < lineRoot.size(); i++) {
			lineResponse = String.valueOf(lineRoot.get(i));
			lineName = String.valueOf(lineRoot.get(i).get("name"));	
			if (StringUtils.equalsIgnoreCase(line, lineName)) {
				final List<String> lineStatusForRoot = from(lineResponse).getList(
						"lineStatuses");
				lineStatusesForThisLine = from(lineResponse).get("lineStatuses").toString();
				if (!StringUtils.contains("[null]", from(lineStatusesForThisLine).get("disruption").toString())) {
					disruptionsList = constructDisruptionDTO(lineStatusesForThisLine);
				}
				constructLineStatusDTO(lineDTO, disruptionsList,
						lineName, lineStatus, lineStatusForRoot);
			}
		}
		return lineDTO;
	}

	private static void constructLineStatusDTO(List<Line> lineDTO,
			List<Disruption> disruptionsList, String lineName,
			String lineStatus, final List<String> lineStatusForRoot) {
		String json = "";
        String testReason = "";
		HashSet<String> ts = new HashSet<String>();
		for (int j = 0; j < lineStatusForRoot.size(); j++) {
			json = "[" + String.valueOf(lineStatusForRoot.get(j)) + "]";
			array = parser.parse(json).getAsJsonArray();
			Line event = gson.fromJson(array.get(0), Line.class);
			ts.add(event.getStatusSeverityDescription());
			if (lineStatusForRoot.size() > 1) {
				lineStatus = lineStatus
						+ ts.toString().trim().substring(1,ts.toString().trim().length() - 1);
			}
            if (null != event.getReason() && event.getReason().length() > 0) {
                testReason = event.getReason().substring(event.getReason().indexOf(":")+1,event.getReason().length()).trim();
            }
			if (j + 1 == lineStatusForRoot.size()) {
				Line line = new Line();
				lineStatus = ts.toString().trim()
						.substring(1, ts.toString().trim().length() - 1);
				if (StringUtils.endsWithIgnoreCase("059", lineName) || StringUtils.endsWithIgnoreCase("London Overground", lineName)) {
					lineName = "Overground";
				}
				line.setName(lineName);
				line.setStatusSeverityDescription(lineStatus);
				line.setDistruptionsCount(lineStatusForRoot.size());
                line.setReason(testReason);
				line.setDisruptionList(disruptionsList);
				lineStatus = "";
                testReason = "";
				lineDTO.add(line);
			}
		}
	}

	private static List<Disruption> constructDisruptionDTO(String lineStatusesForThisLine) {
		List<Disruption> disruptionsList = new ArrayList();
		String disruptionForThisLine = "";
		String affectedStopPointsByDisruption = "";
		disruptionForThisLine = from(lineStatusesForThisLine).get("disruption.description").toString();
		affectedStopPointsByDisruption = from(lineStatusesForThisLine).get("disruption.affectedStops.name").toString(); 
		Disruption disruption = new Disruption();
		disruption.setReason(disruptionForThisLine.substring(1, disruptionForThisLine.length()-1));
		disruption.setStopPoints(affectedStopPointsByDisruption.substring(1, affectedStopPointsByDisruption.length()- 1));
		disruptionsList.add(disruption);
		return disruptionsList;
	}
	
	public static List<RoadDisruption> getRoadDisruptionDTOList(String rootResponse) {
		List<RoadDisruption> roadDisruption = new ArrayList();
		List<Map<String, String>> roadRoot = from(rootResponse).get("");
		for (int i = 0; i < roadRoot.size(); i++) {
			RoadDisruption roadDisruptionDTO = new RoadDisruption();
			roadDisruptionDTO.setLocation(String.valueOf(roadRoot.get(i).get("location")));
			roadDisruptionDTO.setCategory(String.valueOf(roadRoot.get(i).get("category")));
			roadDisruptionDTO.setComments(String.valueOf(roadRoot.get(i).get("comments")));
			roadDisruptionDTO.setStatus(String.valueOf(roadRoot.get(i).get("status")));
			roadDisruption.add(roadDisruptionDTO);
		}
		return roadDisruption;
	}
	
	public static String getDisruptionByLineFromAPI(String JSON, String lineName) {
        List<Map<String, String>> lineRoot = from(JSON).get("");
		String name = "";
		String disruptionForThisLine = "";
		for (int i = 0; i < lineRoot.size(); i++) {
			name = String.valueOf(lineRoot.get(i).get("name"));
			if (StringUtils.equalsIgnoreCase(name, lineName)) {
				disruptionForThisLine = from(JSON).get("lineStatuses["+i+"].reason").toString();
				disruptionForThisLine = disruptionForThisLine.trim().substring(1, disruptionForThisLine.trim().length()-1);
                disruptionForThisLine = disruptionForThisLine.substring(disruptionForThisLine.indexOf(":")+1,disruptionForThisLine.length());
			}
		}
		return disruptionForThisLine;
	}

	public static List<StopPoint> getStopPointDTOListFromAPI(String rootResponse) {
		List<StopPoint> stopPointList = new ArrayList();
		List<Map<String, String>> stopPointRoot = from(rootResponse).get("");
        String reason = "";
		for (int i = 0; i < stopPointRoot.size(); i++) {
			StopPoint stopPointDTO = new StopPoint();
			String name = String.valueOf(stopPointRoot.get(i).get("pointName"));
			name = name.replace(" Underground Station", "");
			stopPointDTO.setName(name);
            reason = String.valueOf(stopPointRoot.get(i).get("description"));
            reason = reason.substring(reason.indexOf(":")+1,reason.length()).trim();
			stopPointDTO.setReason(reason);
			stopPointDTO.setStatusText(String.valueOf(stopPointRoot.get(i).get("type")));
			stopPointList.add(stopPointDTO);
		}
		return stopPointList;
	}
	
	public static int getNumberOfAffectedStopPoints(String json) {
		List<String> stopPoints = from(json).get("pointName");
		return stopPoints.size();
	}

	public static Prediction getPredictionSummaryDTO(String rootResponse) {
		Prediction predictionDTO = new Prediction();
		predictionDTO.setStationName(getElementValueFromJSON(rootResponse,"stationName"));
		predictionDTO.setLineName(getElementValueFromJSON(rootResponse,"lineName"));
		return predictionDTO;
	}

	private static String getElementValueFromJSON(String rootResponse, String elementName) {
		String elementValue = from(rootResponse).getString(elementName);
		return elementValue.substring(1, elementValue.length()-1);
	}
}
