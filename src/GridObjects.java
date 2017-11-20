public enum GridObjects {

	Obstacle("obstacle"),
	Rock("rock"),
	Agent("agent"),
	Teleportal("teleportal"),
	PressurePad("pressurePad"),
	FreeSpace("freeSpace");
	
	String fact;
	private GridObjects(String fact){
		this.fact = fact;
	}
	public String getFactName() {
		return fact;
	}
	
	
	
}