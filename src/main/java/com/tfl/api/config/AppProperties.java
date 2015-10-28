package com.tfl.api.config;

public class AppProperties {

	public static final String BETA_API_KEY_AND_TOKEN = "";
	public static final String LIVE_ESUI_API_END_POINT = "http://www.tfl.gov.uk/tfl/syndication/feeds/esubs/service-disruptions.xml";
	
	// Tube DLR Overground
	public static final String LIVE_ICS_API_END_POINT = "http://www.tfl.gov.uk/tfl/syndication/feeds/disruptions-track-offset-now.xml";
	public static final String LIVE_ICS_WEEKEND_API_END_POINT = "http://www.tfl.gov.uk/tfl/syndication/feeds/disruptions-track-offset-weekend.xml";
	
	// Bus
	public static final String LIVE_BUS_ICS_FEED_END_POINT = "http://tfl.gov.uk/tfl/syndication/feeds/disruptions-buses-offset-now.xml";
	public static final String LIVE_BUS_ICS_WEEKEND_FEED_END_POINT = "http://origin.tfl.gov.uk/tfl/syndication/feeds/disruptions-buses-offset-weekend.xml";
	
	// Roads
	public static final String LIVE_ROADS_CORRIDORS_ESUI_END_POINT = "http://www.tfl.gov.uk/tfl/syndication/feeds/kml/corridors/corridors_true_described.kml";
	public static final String LIVE_ROADS_DISRUPTIONS_TIMS_END_POINT="http://www.tfl.gov.uk/tfl/syndication/feeds/tims_feed.xml";
	
	// Trams
	public static final String STAGE_TRAM_ICS_FEED_END_POINT = "http://tfl.gov.uk/tfl/syndication/feeds/disruptions-trams-offset-now.xml";
	public static final String LIVE_TRAM_ICS_FEED_END_POINT = "http://tfl.gov.uk/tfl/syndication/feeds/disruptions-trams-offset-now.xml";
	public static final String LIVE_TRAM_ICS_WEEKEND_FEED_END_POINT = "http://tfl.gov.uk/tfl/syndication/feeds/disruptions-trams-offset-weekend.xml";

	// River Bus
	public static final String LIVE_RIVER_ICS_FEED_END_POINT = "http://www.tfl.gov.uk/tfl/syndication/feeds/disruptions-river-offset-now.xml";
	public static final String LIVE_RIVER_ICS_WEEKEND_FEED_END_POINT = "http://www.tfl.gov.uk/tfl/syndication/feeds/disruptions-river-offset-weekend.xml";

	// Cable-Car
	public static final String LIVE_CABLE_CAR_ICS_FEED_END_POINT = "http://www.tfl.gov.uk/tfl/syndication/feeds/disruptions-cable-car-offset-now.xml";
	public static final String LIVE_CABLE_CAR_ICS_WEEKEND_FEED_END_POINT = "http://www.tfl.gov.uk/tfl/syndication/feeds/disruptions-cable-car-offset-weekend.xml";
	
	//Prediction API
	public static final String LIVE_TUBE_PREDICTIONS_TRACKER_NET_END_POINT = "http://cloud.tfl.gov.uk/TrackerNet/PredictionDetailed/";
	
	public static final String LINE_STATUS_API = "line";
	public static final String ROAD_STATUS_API = "RoadDisruptions";
	public static final String ROAD_PLANNED_WORKS_API = "RoadPlannedWorks";
	public static final String STATION_STATUS_API = "StopPoints/GetAffectedStopPoints";


}
