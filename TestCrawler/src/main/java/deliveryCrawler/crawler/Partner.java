package deliveryCrawler.crawler;

public class Partner {

	private String name;
	private boolean isLP;
	private String Nature;
	private String Country;
	private String Area;
	private String NUTS3;
	private String postalCode;
	private double erdf;
	private double erdfContribution;
	private double ipa;
	private double ipaContribution;
	private double amount;
	
	
	public Partner() {
		//DEFAULT CONSTRUCTOR
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isLP() {
		return isLP;
	}
	public void setLP(boolean isLP) {
		this.isLP = isLP;
	}
	public String getNature() {
		return Nature;
	}
	public void setNature(String nature) {
		Nature = nature;
	}
	public String getCountry() {
		return Country;
	}
	public void setCountry(String country) {
		Country = country;
	}
	public String getArea() {
		return Area;
	}
	public void setArea(String area) {
		Area = area;
	}
	public String getNUTS3() {
		return NUTS3;
	}
	public void setNUTS3(String nUTS3) {
		NUTS3 = nUTS3;
	}
	public String getPostalCode() {
		return postalCode;
	}
	public void setPostalCode(String zip) {
		this.postalCode = zip;
	}
	public double getErdf() {
		return erdf;
	}
	public void setErdf(double erdf) {
		this.erdf = erdf;
	}
	public double getErdfContribution() {
		return erdfContribution;
	}
	public void setErdfContribution(double erdfContribution) {
		this.erdfContribution = erdfContribution;
	}
	public double getIpa() {
		return ipa;
	}
	public void setIpa(double ipa) {
		this.ipa = ipa;
	}
	public double getIpaContribution() {
		return ipaContribution;
	}
	public void setIpaContribution(double ipaContribution) {
		this.ipaContribution = ipaContribution;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	
	
}
