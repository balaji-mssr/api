package com.tfl.api.services.utils;

import com.tfl.api.domain.*;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

import static com.jayway.restassured.path.xml.XmlPath.with;

public class XMLHelper {

	private static final String PREDICTION_TRACKERNET_LINE_NAME_ELEMENT = "ROOT.LineName";
	private static final String PREDICTION_TRACKERNET_STATION_NAME_ELEMENT = "ROOT.S.@N";

	public enum SchemaType {
		TRACK("disruptions-track-offset"), BUS("disruptions-bus-offset");

		private String mode;

		SchemaType(String mode) {
			this.mode = mode;
		}

		@Override
		public String toString() {
			return mode;
		}
	}

	public static List<Line> getICSStatusDTO(String XML) {
		List<Line> lineICSDto = new ArrayList();
		String disruptionReason = "";
		int items = Integer.parseInt(with(XML).get(
				"feed.period.lines.line.size()").toString());
		for (int i = 0; i < items; i++) {
			int distruptions = 0;
			constructLineDTO(XML, lineICSDto, disruptionReason, i, Boolean.TRUE);
			disruptionReason = "";
		}
		return lineICSDto;
	}

	public static String getDisruptionByLineFromICS(String XML, String lineName) {
		final String disruption = with(XML)
				.get("feed.period.lines.line.findAll { line -> def lineName = line.@name; lineName == '211' }");
		return disruption;
	}

	public static List<Line> getWeekendStatusFromICSByLine(String XML,
			String lineName) {
		List<Line> lineICSDto = new ArrayList();
		String name = "";
		String disruptionReason = "";
		int items = Integer.parseInt(with(XML).get(
				"feed.period.lines.line.size()").toString());
		for (int i = 0; i < items; i++) {
			name = with(XML).get("feed.period.lines.line[" + i + "].@name");
			if (StringUtils.equalsIgnoreCase(name, lineName)) {
				constructLineDTO(XML, lineICSDto, disruptionReason, i,
						Boolean.TRUE);
				disruptionReason = "";
			}
		}
		return lineICSDto;
	}

	private static void constructLineDTO(String XML, List<Line> lineICSDto,
			String disruptionReason, int i, boolean extractDisruption) {
		String name = "";
		String status = "";
		String disruptionStatus = "";
		String disruptionComments = "";
		String disruptionStopPoints = "";
		String schemaType = "";
		List<Disruption> disruptionsList = new ArrayList();
		schemaType = with(XML).get("feed.@schema");
		name = with(XML).get("feed.period.lines.line[" + i + "].@name");
        if (StringUtils.equalsIgnoreCase(name,"Docklands Light Railway")) {
          name = "DLR";
        }
		status = with(XML).get("feed.period.lines.line[" + i + "].@status");
        int distruptions = Integer.parseInt(with(XML).get(
                "feed.period.lines.line[" + i
                        + "].disruptions.disruption.size()").toString());
        if (distruptions > 1) {
            for (int z = 0; z < distruptions; z++) {
                disruptionReason = disruptionReason + with(XML).get("feed.period.lines.line[" + i + "].disruptions.disruption.text[" + z + "]");
            }
        } else {
		disruptionReason = disruptionReason
				+ with(XML).get(
						"feed.period.lines.line[" + i
								+ "].disruptions.disruption.text[0]");
        }

		if (extractDisruption) {
			for (int j = 0; j < distruptions; j++) {
				try {
					Disruption disruption = new Disruption();
					disruptionComments = with(XML).get(
							"feed.period.lines.line[" + i
									+ "].disruptions.disruption[" + j
									+ "].text[0]");
                    //System.out.println("first element of disruption >>> "+with(XML).get("feed.period.lines.line[" + i + "].disruptions.disruption[" + j + "].text[0]"));
					if (schemaType.contains(SchemaType.TRACK.toString())) {
						disruptionStatus = with(XML).get(
								"feed.period.lines.line[" + i
										+ "].disruptions.disruption[" + j
										+ "].@status");
						disruptionStopPoints = with(XML).get(
								"feed.period.lines.line[" + i
										+ "].disruptions.disruption[" + j
										+ "].stops.stop.@name").toString();
						disruption.setStatus(disruptionStatus);
						disruption.setStopPoints(disruptionStopPoints);
					}
					disruption.setReason(disruptionComments);
					disruptionsList.add(disruption);
				} catch (ClassCastException cce) {
					System.out.println("Dont have disruptions>>>"
							+ distruptions);
				}
			}
		}
		if (StringUtils.contains(status, ",")) {
			Set<String> ts = new TreeSet<String>(Arrays.asList(status
					.split("\\s*,\\s*")));
			status = ts.toString().substring(1, ts.toString().length() - 1);
		}
		Line line = new Line();
		line.setName(name);
		line.setStatusSeverityDescription(status);
		line.setDistruptionsCount(distruptions);
		line.setReason(disruptionReason);
		line.setDisruptionList(disruptionsList);
		lineICSDto.add(line);
	}

	public static List<Line> getESUIStatusDTO(String XML) {
		List<Line> lineESUIDto = new ArrayList();
		String name = "";
		String status = "";
        String reason = "";
		int items = Integer.parseInt(with(XML).get(
				"ExternalWebServiceDisruptions.LineStatus.Line.size()")
				.toString());
		for (int i = 0; i < items; i++) {
			name = with(XML).get(
					"ExternalWebServiceDisruptions.LineStatus.Line[" + i
							+ "].@LineName");
			int distruptions = Integer.parseInt(with(XML).get(
					"ExternalWebServiceDisruptions.LineStatus.Line[" + i
							+ "].Disruptions.Disruption.size()").toString());
			if (distruptions > 1) {
				final List<String> statusList = with(XML)
						.get("ExternalWebServiceDisruptions.LineStatus.Line["
								+ i
								+ "].Disruptions.Disruption.Status.@Description");
				Set<String> hashsetList = new HashSet<String>(statusList);
				status = hashsetList.toString().substring(1,
						hashsetList.toString().length() - 1);
			} else {
				status = with(XML)
						.get("ExternalWebServiceDisruptions.LineStatus.Line["
								+ i
								+ "].Disruptions.Disruption.Status.@Description")
						.toString();
			}

            if (!StringUtils.equalsIgnoreCase("Good Service",status)) {
                //Need to get the incident message
                String disruptedLineName = "";
                int reasonSize = Integer.parseInt(with(XML).get("ExternalWebServiceDisruptions.IncidentMessages.Incident.LineStatus.size()").toString());
                for (int j=0; j<reasonSize; j++) {
                    disruptedLineName =  with(XML).get("ExternalWebServiceDisruptions.IncidentMessages.Incident.LineStatus.Line["+j+"].@Name").toString();
                    if (StringUtils.equalsIgnoreCase(disruptedLineName,name)) {
                       reason = with(XML).get("ExternalWebServiceDisruptions.IncidentMessages.Incident.LineStatus["+j+"].StatusMessages.StatusText").toString();
                    }
                }

            }
			Line line = new Line();
			line.setName(name);
			line.setStatusSeverityDescription(status);
            line.setReason(reason);
			lineESUIDto.add(line);
		}
		return lineESUIDto;
	}

	public static List<RoadDisruption> getRoadDisruptionDTO(String XML) {
		List<RoadDisruption> roadDisruption = new ArrayList();
		int items = Integer.parseInt(with(XML).get(
				"Root.Disruptions.Disruption.size()").toString());
		String severity = "";
		String location = "";

		for (int i = 0; i < 25; i++) {
			severity = with(XML).get(
					"Root.Disruptions.Disruption[" + i + "].severity")
					.toString();
			if (StringUtils.equalsIgnoreCase(severity, "Moderate")) {
				location = with(XML).get(
						"Root.Disruptions.Disruption[" + i + "].location")
						.toString();
				RoadDisruption road = new RoadDisruption();
				// road.setCategory(with(XML).get("Root.Disruptions.Disruption["
				// + i + "].category").toString());
				// road.setComments(with(XML).get("Root.Disruptions.Disruption["
				// + i + "].comments").toString());
				// road.setStatus(with(XML).get("Root.Disruptions.Disruption[" +
				// i + "].status").toString());
				road.setLocation(location);
				road.setSeverity(severity);
				roadDisruption.add(road);
			}
		}
		return roadDisruption;
	}

	public static List<StopPoint> getStopPointDTOListFromICS(String XML) {
		List<StopPoint> stopPointList = new ArrayList();
		String name = "";
		String reason = "";
		String statusType = "";
		int items = Integer.parseInt(with(XML).get(
				"feed.period.stops.stop.size()").toString());
		for (int i = 0; i < items; i++) {
			name = with(XML).get("feed.period.stops.stop[" + i + "].@name");
			try {
				statusType = with(XML).get(
						"feed.period.stops.stop[" + i + "].@closed");
			} catch (Exception e) {
				statusType = "Information";
			}
			reason = with(XML).get("feed.period.stops.stop[" + i + "].text");
			StopPoint stopPoint = new StopPoint();
			stopPoint.setName(name);
			stopPoint.setReason(reason);
			stopPoint.setStatusText(statusType);
			stopPointList.add(stopPoint);
		}
		return stopPointList;
	}
	
	public static int getNumberOfAffectedStopPoints(String XML) {
		int items = Integer.parseInt(with(XML).get(
				"feed.period.stops.stop.size()").toString());
		return items;
	}
	
	public static Prediction getPredictionListFromTrackerNet(String XML) {
		Prediction prediction = new Prediction();
		prediction.setLineName(getElementValue(XML,PREDICTION_TRACKERNET_LINE_NAME_ELEMENT));
		prediction.setStationName(getElementValue(XML,PREDICTION_TRACKERNET_STATION_NAME_ELEMENT).replace(".", ""));
		return prediction;
	}

	public static String getLineNameFromTrackerNet(String XML) {
		return getElementValue(XML, PREDICTION_TRACKERNET_LINE_NAME_ELEMENT);

	}

	private static String getElementValue(String XML, String elementName) {
		return with(XML).get(elementName);
	}
}
