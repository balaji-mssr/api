package com.tfl.api.domain;

public class Disruption {

	private String status;
	private String stopPoints;
	private String reason;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStopPoints() {
		return stopPoints;
	}

	public void setStopPoints(String stopPoints) {
		this.stopPoints = stopPoints;
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
		result = prime * result + ((reason == null) ? 0 : reason.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result
				+ ((stopPoints == null) ? 0 : stopPoints.hashCode());
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
		Disruption other = (Disruption) obj;
		if (reason == null) {
			if (other.reason != null)
				return false;
		} else if (!reason.equals(other.reason))
			return false;
		if (status == null) {
			if (other.status != null)
				return false;
		} else if (!status.equals(other.status))
			return false;
		if (stopPoints == null) {
			if (other.stopPoints != null)
				return false;
		} else if (!stopPoints.equals(other.stopPoints))
			return false;
		return true;
	}

}
