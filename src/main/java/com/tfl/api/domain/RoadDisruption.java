package com.tfl.api.domain;

import java.util.Date;
import java.util.List;

public class RoadDisruption {

	private String location;
	private String status;
	private String severity;
	private String comments;
	private String currentUpdate;
	private String category;
    private Date startDateAndTime;
    private Date endDateAndTime;
	private List<String> CorridorIds;

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getSeverity() {
		return severity;
	}

	public void setSeverity(String severity) {
		this.severity = severity;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getCurrentUpdate() {
		return currentUpdate;
	}

	public void setCurrentUpdate(String currentUpdate) {
		this.currentUpdate = currentUpdate;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public List<String> getCorridorIds() {
		return CorridorIds;
	}

	public void setCorridorIds(List<String> corridorIds) {
		CorridorIds = corridorIds;
	}

    public Date getStartDateAndTime() {
        return startDateAndTime;
    }

    public void setStartDateAndTime(Date startDateAndTime) {
        this.startDateAndTime = startDateAndTime;
    }

    public Date getEndDateAndTime() {
        return endDateAndTime;
    }

    public void setEndDateAndTime(Date endDateAndTime) {
        this.endDateAndTime = endDateAndTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RoadDisruption that = (RoadDisruption) o;

        if (location != null ? !location.equals(that.location) : that.location != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = location != null ? location.hashCode() : 0;
        return result;
    }

    @Override
    public String toString() {
        return "RoadDisruption{" +
                "location='" + location + '\'' +
                '}';
    }
}
