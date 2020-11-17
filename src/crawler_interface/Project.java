package crawler_interface;


import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

public class Project {

	private String acronym;
	private int axis;
	private int objective;
	private String label;
	private String summary;
	private String call;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private Date start;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private Date end;
	private String type;
	private double erdf;
	private double ipa;
	private double amount;
	private String cofinancing;
	private String status;
	private String deliverablesUrl;
	private List<Partner> partners;

	public Project() {
		// DEFAULT CONSTRUCTOR
	}

	public String getAcronym() {
		return acronym;
	}

	public void setAcronym(String acronym) {
		this.acronym = acronym;
	}

	public int getAxis() {
		return axis;
	}

	public void setAxis(int axis) {
		this.axis = axis;
	}

	public int getObjective() {
		return objective;
	}

	public void setObjective(int objective) {
		this.objective = objective;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getCall() {
		return call;
	}

	public void setCall(String call) {
		this.call = call;
	}

	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public double getErdf() {
		return erdf;
	}

	public void setErdf(double erdf) {
		this.erdf = erdf;
	}

	public double getIpa() {
		return ipa;
	}

	public void setIpa(double ipa) {
		this.ipa = ipa;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getCofinancing() {
		return cofinancing;
	}

	public void setCofinancing(String cofinancing) {
		this.cofinancing = cofinancing;
	}

	public List<Partner> getPartners() {
		return partners;
	}

	public void setPartners(List<Partner> partners) {
		this.partners = partners;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDeliverablesUrl() {
		return deliverablesUrl;
	}

	public void setDeliverablesUrl(String deliverablesUrl) {
		this.deliverablesUrl = deliverablesUrl;
	}

}
