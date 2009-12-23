package uk.org.tubs.coffee.impl;

public class CoffeeStatus {
	private boolean isBrewing;
	private boolean isHotplateOn;
	
	public CoffeeStatus(boolean isBrewing, boolean isHotplateOn){
		this.isBrewing = isBrewing;
		this.isHotplateOn = isHotplateOn;
	}
	
	public boolean isBrewing() {
		return isBrewing;
	}
	public boolean isHotplateOn() {
		return isHotplateOn;
	}
	
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (isBrewing ? 1231 : 1237);
		result = prime * result + (isHotplateOn ? 1231 : 1237);
		return result;
	}
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CoffeeStatus other = (CoffeeStatus) obj;
		if (isBrewing != other.isBrewing)
			return false;
		if (isHotplateOn != other.isHotplateOn)
			return false;
		return true;
	}
	
	public String toString() {
		return "CoffeeStatus [isBrewing=" + isBrewing + ", isHotplateOn="
				+ isHotplateOn + "]";
	}
	
	public CoffeeStatus clone(){
		return new CoffeeStatus(isBrewing,isHotplateOn);
	}
}
