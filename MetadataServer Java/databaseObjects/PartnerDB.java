package databaseObjects;

public class PartnerDB {
	private int partner_id;
	private boolean is_lead_partner;
	private String partner_name;
	private String partner_nature;
	private String partner_country;
	private String partner_postalCode;
	private String partner_area;
	private String nuts3;
	private double erdf;
	private double erdfContribution;
	private double ipa_funds;
	private double ipaContribution;
	private double partner_amount;

	public int getPartner_id() {
		return partner_id;
	}

	public void setPartner_id(int partner_id) {
		this.partner_id = partner_id;
	}

	public boolean isIs_lead_partner() {
		return is_lead_partner;
	}

	public void setIs_lead_partner(boolean is_lead_partner) {
		this.is_lead_partner = is_lead_partner;
	}

	public String getPartner_name() {
		return partner_name;
	}

	public void setPartner_name(String partner_name) {
		this.partner_name = partner_name;
	}

	public String getPartner_nature() {
		return partner_nature;
	}

	public void setPartner_nature(String partner_nature) {
		this.partner_nature = partner_nature;
	}

	public String getPartner_country() {
		return partner_country;
	}

	public void setPartner_country(String partner_country) {
		this.partner_country = partner_country;
	}

	public String getPartner_postalCode() {
		return partner_postalCode;
	}

	public void setPartner_postalCode(String partner_postalCode) {
		this.partner_postalCode = partner_postalCode;
	}

	public String getPartner_area() {
		return partner_area;
	}

	public void setPartner_area(String partner_area) {
		this.partner_area = partner_area;
	}

	public String getNuts3() {
		return nuts3;
	}

	public void setNuts3(String nuts3) {
		this.nuts3 = nuts3;
	}

//	public String getPartner_city() {
//		return partner_city;
//	}
//
//	public void setPartner_city(String partner_city) {
//		this.partner_city = partner_city;
//	}
//
//	public String getPartner_address() {
//		return partner_address;
//	}
//
//	public void setPartner_address(String partner_address) {
//		this.partner_address = partner_address;
//	}

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

	public double getIpa_funds() {
		return ipa_funds;
	}

	public void setIpa_funds(double ipa_funds) {
		this.ipa_funds = ipa_funds;
	}

	public double getIpaContribution() {
		return ipaContribution;
	}

	public void setIpaContribution(double ipaContribution) {
		this.ipaContribution = ipaContribution;
	}

	public double getPartner_amount() {
		return partner_amount;
	}

	public void setPartner_amount(double partner_amount) {
		this.partner_amount = partner_amount;
	}

}
