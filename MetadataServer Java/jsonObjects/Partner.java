package jsonObjects;

public class Partner {
	private boolean lp;
	private String name;
	private String postalCode;
	private double erdf;
	private double erdfContribution;
	private double ipa;
	private double ipaContribution;
	private double amount;
	private int id;
	private String country;
	private String nuts3;
	private String nature;
	private String area;

	public Partner() {
		// DEFAULT CONSTRUCTOR
	}

	public int getID() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isLP() {
		return lp;
	}

	public void setLP(boolean isLP) {
		this.lp = isLP;
	}

	public String getNature() {
		return nature;
	}

	public void setNature(String _nature) {
		nature = _nature;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getNuts3() {
		return nuts3;
	}

	public void setNuts3(String nUTS3) {
		nuts3 = nUTS3;
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
