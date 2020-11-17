package crawler_interface;


public class Partner {

	private boolean is_LeadPartner;
	private String name;
	private String Nature;
	private String Country;
	private String postalcode;
	private String Area;
	private String Nuts3;
	private double erdf;
	private double erdfContribution;
	private double ipa;
	private double ipaContribution;
	private double amount;

	public Partner() {
		// DEFAULT CONSTRUCTOR
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isLP() {
		return is_LeadPartner;
	}

	public void setLP(boolean isLP) {
		this.is_LeadPartner = isLP;
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

	public String getPostalCode() {
		return postalcode;
	}

	public void setPostalCode(String postalcode) {
		this.postalcode = postalcode;
	}

	public String getNuts3() {
		return Nuts3;
	}

	public void setNuts3(String nuts3) {
		Nuts3 = nuts3;
	}

}
