public class Partner {
	private String lead;
	private String name;
	private String country;
	private String budget;
	
	public String getBudget() {
		return budget;
	}
	public void setBudget(String budget) {
		this.budget = budget;
	}
	public String isLead() {
		return lead;
	}
	public void setLead(String vecPartners) {
		this.lead = vecPartners;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCountry() {
		return country;
	}
	public String getLead() {
		return this.lead;
	}
	public void setCountry(String country) {
		this.country = country;
	}
}