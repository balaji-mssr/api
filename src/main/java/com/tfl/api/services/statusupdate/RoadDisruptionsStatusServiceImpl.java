package com.tfl.api.services.statusupdate;

import com.jayway.restassured.internal.path.xml.NodeImpl;
import com.jayway.restassured.path.xml.element.Node;
import com.jayway.restassured.response.Response;
import com.tfl.api.config.AppProperties;
import com.tfl.api.domain.RoadDisruption;
import com.tfl.api.domain.enums.RoadSeverity;
import com.tfl.api.services.utils.JSONHelper;
import com.tfl.api.services.utils.ResponseUtils;
import com.tfl.api.services.utils.XMLHelper;
import com.tfl.api.utils.DateUtils;
import org.apache.commons.lang.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.jayway.restassured.RestAssured.get;
import static com.jayway.restassured.path.json.JsonPath.from;
import static com.jayway.restassured.path.xml.XmlPath.with;

public class RoadDisruptionsStatusServiceImpl {

	public List<RoadDisruption> getLiveRoadDistruptionsFromTIMSFeed() {
			String XML = get(AppProperties.LIVE_ROADS_DISRUPTIONS_TIMS_END_POINT)
					.andReturn().asString();
			List<RoadDisruption> roadDisruption = XMLHelper.getRoadDisruptionDTO(XML);
			return roadDisruption;
	}

	public List<RoadDisruption> getLiveRoadDistruptionsFromAPI() {
        boolean isStripContent = Boolean.TRUE;
		String rootResponse = ResponseUtils.getBetaRoadDisruptions(RoadSeverity.MODERATE, isStripContent);
		List<RoadDisruption> roadDisruptionDTO = JSONHelper.getRoadDisruptionDTOList(rootResponse);
		return roadDisruptionDTO;
	}
	
	public int getTotalNumberOfSevereRoadDisruptionsFromAPI() {
		String JSON = ResponseUtils.getBetaRoadDisruptions(RoadSeverity.SEVERE, false);
		return getNUmberOfRoadIncidentsFromJSON(JSON);
	}

    public int getTotalNumberOfSeriousRoadDisruptionsFromAPI() {
		String JSON = ResponseUtils.getBetaRoadDisruptions(RoadSeverity.SERIOUS, false);
		return getNUmberOfRoadIncidentsFromJSON(JSON);
	}

    public int getRoadDisruptionsAPIResponseStatusCode() {
        Response response = ResponseUtils.getRoadDisruptionAPIResponse();
        return response.getStatusCode();
    }
	
	
	public int getTotalNumberOfModerateRoadDisruptionsFromAPI() {
		String JSON = ResponseUtils.getBetaRoadDisruptions(RoadSeverity.MODERATE, false);
		return getNUmberOfRoadIncidentsFromJSON(JSON);
	}

	private int getNUmberOfRoadIncidentsFromJSON(String JSON) {
		int numberOfIncidents = 0;
		List<Map<String, String>> nodes = from(JSON).get("");
		numberOfIncidents = nodes.size();
		return numberOfIncidents;
	}
	
	public int getTotalNumberOfSevereRoadDisruptionsFromTIMSFeed(String severity) {
		int numberOfSevereIncidents = 0;
		String XML = get(AppProperties.LIVE_ROADS_DISRUPTIONS_TIMS_END_POINT).andReturn().asString();
		try {
			final List<Node> seriousDisruptions = with(XML).get("Root.Disruptions.Disruption.findAll { Disruption -> def severity = Disruption.severity.toString(); severity == '"+severity+"' }");
            numberOfSevereIncidents = seriousDisruptions.size();
		} catch (Exception e) {
			numberOfSevereIncidents = 1;
		}
		return numberOfSevereIncidents;
	}

    public boolean compareActiveRoadDisruptionsBetweenTIMSAndAPI() {
        boolean isIncidentMatches = Boolean.FALSE;
        String severityTYpe = "Moderate";
        List<RoadDisruption> roadDisruptionsFromTIMS = getRoadDisruptionDTOFromTIMS(severityTYpe);
        List<RoadDisruption> roadDisruptionsFromAPI = getLiveRoadDistruptionsFromAPI();
        if (null != roadDisruptionsFromTIMS && roadDisruptionsFromTIMS.size() > 0) {
            //Given Active Status - Moderate Severity Road Disruption Location from TIMSFeed
            // I should see the same incident exist in API
            for (int i=0; i<roadDisruptionsFromTIMS.size(); i++) {
                boolean isActiveDisruption = StringUtils.equalsIgnoreCase("Active",roadDisruptionsFromTIMS.get(i).getStatus());
                if (isActiveDisruption) {
                    isIncidentMatches = checkTheGivenRoadDisruptionMatchesInAPI(roadDisruptionsFromTIMS.get(i),roadDisruptionsFromAPI);
                }
            }
        } else {
            System.out.println("TIMS Responses are Empty");
        }
        return isIncidentMatches;
    }

    private boolean checkTheGivenRoadDisruptionMatchesInAPI(RoadDisruption roadDisruptionFromTIMS,List<RoadDisruption> roadDisruptionsFromAPI) {
        boolean isSameLocation = Boolean.FALSE;
        if (null != roadDisruptionsFromAPI && roadDisruptionsFromAPI.size() > 0) {
            for (RoadDisruption rdFromAPI:roadDisruptionsFromAPI) {
                isSameLocation = StringUtils.equalsIgnoreCase(rdFromAPI.getLocation(),roadDisruptionFromTIMS.getLocation());
                if (isSameLocation) {
                    //System.out.println("Given Road Disruption From TIMS - " + roadDisruptionFromTIMS.getLocation() + " Exist in API. Happy Days!!!");
                    break;
                }
            }
        } else {
            System.out.println("API Responses are Empty");
        }
        if (!isSameLocation) {
            System.out.println("Given Road Disruption From TIMS - " + roadDisruptionFromTIMS.getLocation() + " Not Exist in API");
            System.out.println(" Road Disruption List from API ");
            System.out.println(roadDisruptionsFromAPI.toString());
            isSameLocation = Boolean.FALSE;
        }
        return isSameLocation;
    }

    @Deprecated
    public boolean compareRoadDisruptionsByDateBetweenTIMSAndAPI() {
		int noOfDisruptionsFromTIMS = 0;
		int noOfDisruptionsFromAPI = 0;
		Calendar c = Calendar.getInstance();
		String date = DateUtils.getDateString(DateUtils.SDF_YYYYMMDD_HYPEN, c.getTime());
		String XML = get(AppProperties.LIVE_ROADS_DISRUPTIONS_TIMS_END_POINT).andReturn().asString();
		String JSON = ResponseUtils.getBetaRoadDisruptions(RoadSeverity.ALL, false);
		try {
			final List<Node> disruptionsFromTIMS = with(XML).get("Root.Disruptions.Disruption.findAll { Disruption -> def startTime = Disruption.startTime.toString(); startTime.startsWith('"+date+"') }");
			final List<String> disruptionsFromAPI = from(JSON).get("startDateTime.findAll {startDateTime -> startTime = startDateTime.toString(); startTime.startsWith('"+date+"')}");
			noOfDisruptionsFromTIMS = disruptionsFromTIMS.size();
			noOfDisruptionsFromAPI = disruptionsFromAPI.size();
		} catch (ClassCastException cc) {
			noOfDisruptionsFromTIMS = 1;
			noOfDisruptionsFromAPI = 1;
		} catch (Exception e) {
			noOfDisruptionsFromTIMS = 1;
			noOfDisruptionsFromAPI = Integer.MIN_VALUE;
		}
		System.out.println("noOfDisruptionsFromTIMS>>>"+noOfDisruptionsFromTIMS+ "for the date>>>"+date);
		System.out.println("noOfDisruptionsFromAPI>>>"+noOfDisruptionsFromAPI+ "for the date>>>"+date);
		return noOfDisruptionsFromTIMS == noOfDisruptionsFromAPI;
		
	}
	
	
	public int getTotalNumberOfModerateRoadDisruptionsFromTIMSFeed() {
		String XML = get(AppProperties.LIVE_ROADS_DISRUPTIONS_TIMS_END_POINT).andReturn().asString();
        String severityType = "Moderate";
        List<RoadDisruption> roadsDisruptionBySeverityForGivenDate = getRoadsDisruptionBySeverityForGivenDate(XML,severityType);
		return roadsDisruptionBySeverityForGivenDate.size();
	}

    private List<RoadDisruption> getRoadDisruptionDTOFromTIMS(String severityType) {
        String XML = get(AppProperties.LIVE_ROADS_DISRUPTIONS_TIMS_END_POINT).andReturn().asString();
        List<RoadDisruption> roadsDisruptionBySeverityForGivenDate = getRoadsDisruptionBySeverityForGivenDate(XML,severityType);
        return roadsDisruptionBySeverityForGivenDate;
    }

    private List<RoadDisruption> getRoadsDisruptionBySeverityForGivenDate(String XML, String severity) {
        List<RoadDisruption> roadDisruptionList = new ArrayList<RoadDisruption>();
        List<RoadDisruption> roadDisruptionsForGivenDate = new ArrayList<RoadDisruption>();
        try {
            final List<Map<String, ?>> moderateDisruptions = with(XML).get("Root.Disruptions.Disruption.findAll { Disruption -> def severity = Disruption.severity.toString(); severity == '"+severity+"' }");
            //Assuming End Date Time if Null in TIMS Feed to StartDateTime
            Date givenDate = null;
            constructRoadDisruptions(roadDisruptionList, moderateDisruptions);
            boolean isToday = Boolean.TRUE;
            givenDate = getDateToCompare(isToday);
            roadDisruptionsForGivenDate = getRoadDisruptionForGivenDate(roadDisruptionList, givenDate);
        } catch (Exception e) {
            System.out.println("Exception Occurred " + e.getStackTrace());
        }
    return roadDisruptionsForGivenDate;
    }

    public boolean compareRoadPlannedWorksBetweenAPIAndTIMS(String corridorName) {
        List<RoadDisruption> roadDisruptionList = new ArrayList<RoadDisruption>();
        String XML = get(AppProperties.LIVE_ROADS_DISRUPTIONS_TIMS_END_POINT).andReturn().asString();
        List<RoadDisruption> roadDisruptionForGivenDate = getRoadPlannedWorksFromTIMS(roadDisruptionList, XML,corridorName);
        String json = ResponseUtils.getRoadPlannedWorks(corridorName);
        //String json = StubJSONResponse.ROAD_PLANNED_WORKS_JSON_RESPONSE;
        List<RoadDisruption> roadDisruptionListFromAPI = getRoadPlannedWorksListFromAPI(json);
        //Same number of locations
        boolean isNumberOfDisruptionsSame = roadDisruptionForGivenDate.size() == roadDisruptionListFromAPI.size() ? true:false;
        if (isNumberOfDisruptionsSame && roadDisruptionForGivenDate.size() == 0) {
            System.out.println("There are no planned works for the given corridor - A10");
            return true;
        }
        if (isNumberOfDisruptionsSame) {
            //Check is the location name and disruption reason are same between API and TIMS.
            boolean isSameLocation = Boolean.FALSE;
            for (RoadDisruption rdListFromTIMS:roadDisruptionForGivenDate) {
                for (RoadDisruption rdListFromAPI:roadDisruptionListFromAPI) {
                    isSameLocation = StringUtils.equalsIgnoreCase(rdListFromTIMS.getLocation(),rdListFromAPI.getLocation());
                    boolean isSameReason = StringUtils.equalsIgnoreCase(rdListFromTIMS.getComments(),rdListFromAPI.getComments());
                    if (isSameLocation && !isSameReason) {
                       System.out.println(" location "+rdListFromAPI.getLocation());
                       System.out.println(" reason from API " +rdListFromAPI.getComments());
                       System.out.println(" reason from TIMS Feed " +rdListFromTIMS.getComments());
                       return false;
                    } else {
                        break;
                    }
                }
            }
            if (!isSameLocation) {
                //None of the Location Name Matched so failure
                System.out.println(" None of the Location matched between API and TIMS ");
                String locationFromAPI = "";
                String locationFromTIMS = "";
                for (RoadDisruption rdAPI:roadDisruptionListFromAPI) {
                    locationFromAPI = locationFromAPI + " " + rdAPI.getLocation();
                }
                for (RoadDisruption rdTIMS:roadDisruptionForGivenDate) {
                    locationFromTIMS = locationFromTIMS + " " + rdTIMS.getLocation();
                }
                System.out.println("Location From TIMS Feed" + locationFromTIMS);
                System.out.println("Location From API " + locationFromAPI);
                return false;
            }
        } else {
            System.out.println("Number of Planned Works for Given Corridor and Date is not same between TIMS Feed and API");
            String locationFromAPI = "";
            String locationFromTIMS = "";
            for (RoadDisruption rdAPI:roadDisruptionListFromAPI) {
                locationFromAPI = locationFromAPI + " " + rdAPI.getLocation();
            }
            for (RoadDisruption rdTIMS:roadDisruptionForGivenDate) {
                locationFromTIMS = locationFromTIMS + " " + rdTIMS.getLocation();
            }
            System.out.println("Number of Planned works from TIMS "+roadDisruptionForGivenDate.size() + " Location Details "+locationFromTIMS);
            System.out.println("Number of Planned works from API "+roadDisruptionListFromAPI.size() + ">>>location details  " + locationFromAPI);
        }
        return isNumberOfDisruptionsSame;
    }

    private List<RoadDisruption> getRoadPlannedWorksListFromAPI(String json) {
        List<RoadDisruption> roadDisruptionListFromAPI = new ArrayList<RoadDisruption>();
        List<Map<String,String>> disruptionsFromAPI = from(json).get("");
        for (int i=0; i<disruptionsFromAPI.size(); i++) {
            RoadDisruption roadDisruptionFromAPI = new RoadDisruption();
            roadDisruptionFromAPI.setLocation(disruptionsFromAPI.get(i).get("location"));
            roadDisruptionFromAPI.setComments(disruptionsFromAPI.get(i).get("comments"));
            roadDisruptionListFromAPI.add(roadDisruptionFromAPI);
        }
        return roadDisruptionListFromAPI;
    }

    private List<RoadDisruption> getRoadPlannedWorksFromTIMS(List<RoadDisruption> roadDisruptionList, String XML, String corridorName) {
        final List<Map<String, ?>> disruptions = with(XML).get("Root.Disruptions.Disruption.findAll { Disruption -> def corridor = Disruption.corridor.toString(); corridor.startsWith('"+corridorName+"') }");
        //Assuming End Date Time if Null in TIMS Feed to StartDateTime
        Date givenDate = null;
        constructRoadDisruptions(roadDisruptionList, disruptions);
        try {
            givenDate = getDateToCompare(false);
        } catch (Exception e) {
            System.out.println("Exception Occurred " + e.getStackTrace());
        }
        return getRoadDisruptionForGivenDate(roadDisruptionList, givenDate);
    }

    private void constructRoadDisruptions(List<RoadDisruption> roadDisruptionList, List<Map<String, ?>> disruptions) {
        String startDateAndTime = "";
        String endDateAndTime = "";
        for (int i=0; i<disruptions.size(); i++) {
            NodeImpl nodeImpl = (NodeImpl) disruptions.get(i);
            RoadDisruption roadDisruption = new RoadDisruption();
            roadDisruption.setLocation(nodeImpl.children().get("location").toString());
            roadDisruption.setComments(nodeImpl.children().get("comments").toString());
            roadDisruption.setStatus(nodeImpl.children().get("status").toString());
            startDateAndTime =  String.valueOf(nodeImpl.children().get("startTime"));
            roadDisruption.setStartDateAndTime(getStrippedDate(startDateAndTime));
            endDateAndTime = null !=nodeImpl.children().get("endTime") ? nodeImpl.children().get("endTime").toString():startDateAndTime;
            roadDisruption.setEndDateAndTime(getStrippedDate(endDateAndTime));
            roadDisruptionList.add(roadDisruption);
        }
    }

    private List<RoadDisruption> getRoadDisruptionForGivenDate(List<RoadDisruption> roadDisruptionList, Date givenDate) {
        List<RoadDisruption> roadDisruptions = new ArrayList<RoadDisruption>();
        try {
            for (RoadDisruption rd:roadDisruptionList) {
                //System.out.println(rd.getLocation() + "Start Date Time "+rd.getStartDateAndTime() + " End Date Time " + rd.getEndDateAndTime() );
              if(givenDate.after(rd.getStartDateAndTime()) && givenDate.before(rd.getEndDateAndTime())) {
                  RoadDisruption roadDisruption = new RoadDisruption();
                  roadDisruption.setLocation(rd.getLocation());
                  roadDisruption.setComments(rd.getComments());
                  roadDisruption.setStatus(rd.getStatus());
                  roadDisruption.setStartDateAndTime(rd.getStartDateAndTime());
                  roadDisruption.setEndDateAndTime(rd.getEndDateAndTime());
                  roadDisruptions.add(roadDisruption);
              }
            }
        } catch (Exception e) {
           System.out.println("Exception Occurred " + e.getStackTrace());
        }
        return roadDisruptions;
    }

    private Date getDateToCompare(boolean isToday) throws ParseException {
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        String strCurrDate = sdfDate.format(DateUtils.getSaturday(Calendar.SATURDAY));
        if (isToday) {
            strCurrDate = sdfDate.format(c.getTime());
        }
        return new SimpleDateFormat("yyyy-MM-dd").parse(strCurrDate);
    }

    private Date getStrippedDate(String dateTime) {
        String strippedDateString = dateTime.substring(0,dateTime.indexOf("T"));
        Date strippedDate = null;
        try {
            strippedDate = DateUtils.SDF_YYYYMMDD_HYPEN.parse(strippedDateString);
        }  catch (Exception e) {
            System.out.println("strippedDateString" + strippedDateString);
            System.out.println(e.getStackTrace());
        }
        return strippedDate;
    }
}
