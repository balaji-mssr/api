package com.tfl.api.domain;

public class StopPoint {

	private String name;
	private String reason;
	private String statusText;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getStatusText() {
		return statusText;
	}

	public void setStatusText(String statusText) {
		this.statusText = statusText;
	}
	
	

	@Override
	public String toString() {
		return "StopPoint [name=" + name + ", reason=" + reason
				+ ", statusText=" + statusText + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((reason == null) ? 0 : reason.hashCode());
		result = prime * result
				+ ((statusText == null) ? 0 : statusText.hashCode());
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
		StopPoint other = (StopPoint) obj;
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
		if (statusText == null) {
			if (other.statusText != null)
				return false;
		} else if (!statusText.equals(other.statusText))
			return false;
		return true;
	}

	

}
