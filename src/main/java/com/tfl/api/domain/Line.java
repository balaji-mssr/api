package com.tfl.api.domain;

import java.util.List;

public class Line {

	private String name;
	private String statusSeverityDescription;
	private int distruptionsCount;
	private String reason;
	private List<Disruption> disruptionList;
	
	public List<Disruption> getDisruptionList() {
		return disruptionList;
	}

	public void setDisruptionList(List<Disruption> disruptionList) {
		this.disruptionList = disruptionList;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStatusSeverityDescription() {
		return statusSeverityDescription;
	}

	public void setStatusSeverityDescription(String statusSeverityDescription) {
		this.statusSeverityDescription = statusSeverityDescription;
	}

	public int getDistruptionsCount() {
		return distruptionsCount;
	}

	public void setDistruptionsCount(int distruptionsCount) {
		this.distruptionsCount = distruptionsCount;
	}

	
	
	@Override
	public String toString() {
		return "Line [name=" + name + ", statusSeverityDescription="
				+ statusSeverityDescription + "]";
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((disruptionList == null) ? 0 : disruptionList.hashCode());
		result = prime * result + distruptionsCount;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((reason == null) ? 0 : reason.hashCode());
		result = prime
				* result
				+ ((statusSeverityDescription == null) ? 0
						: statusSeverityDescription.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Line other = (Line) obj;
		if (disruptionList == null) {
			if (other.disruptionList != null)
				return false;
		} else if (!disruptionList.equals(other.disruptionList))
			return false;
		if (distruptionsCount != other.distruptionsCount)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (reason == null) {
			if (other.reason != null)
				return false;
		} else if (!reason.equals(other.reason))
			return false;
		if (statusSeverityDescription == null) {
			if (other.statusSeverityDescription != null)
				return false;
		} else if (!statusSeverityDescription
				.equals(other.statusSeverityDescription))
			return false;
		return true;
	}
	
	
}
