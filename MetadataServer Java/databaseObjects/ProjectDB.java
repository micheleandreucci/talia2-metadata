package databaseObjects;
import java.sql.Date;
import java.sql.ResultSet;

public class ProjectDB {
	int project_id;
	int project_axis;
	int project_objective;
	String project_acronym;
	String project_label;
	String operation_summary;
	String call_for_proposals;
	Date start_date;
	Date end_date;
	String project_type;
   	double erdf;
   	double ipa_funds;
  	double project_amount;
  	double cofinancing_rate;
  	String project_status;
  	String project_deliverables_url;
	String project_community;
	
	public ProjectDB() {};
	
	public int getProject_id() {
		return project_id;
	}
	public void setProject_id(int project_id) {
		this.project_id = project_id;
	}
	public int getProject_axis() {
		return project_axis;
	}
	public void setProject_axis(int project_axis) {
		this.project_axis = project_axis;
	}
	public int getProject_objective() {
		return project_objective;
	}
	public void setProject_objective(int project_objective) {
		this.project_objective = project_objective;
	}
	public String getProject_acronym() {
		return project_acronym;
	}
	public void setProject_acronym(String project_acronym) {
		this.project_acronym = project_acronym;
	}
	public String getProject_label() {
		return project_label;
	}
	public void setProject_label(String project_label) {
		this.project_label = project_label;
	}
	public String getOperation_summary() {
		return operation_summary;
	}
	public void setOperation_summary(String operation_summary) {
		this.operation_summary = operation_summary;
	}
	public String getCall_for_proposals() {
		return call_for_proposals;
	}
	public void setCall_for_proposals(String call_for_proposals) {
		this.call_for_proposals = call_for_proposals;
	}
	public Date getStart_date() {
		return start_date;
	}
	public void setStart_date(Date start_date) {
		this.start_date = start_date;
	}
	public Date getEnd_date() {
		return end_date;
	}
	public void setEnd_date(Date end_date) {
		this.end_date = end_date;
	}
	public String getProject_type() {
		return project_type;
	}
	public void setProject_type(String project_type) {
		this.project_type = project_type;
	}
	public double getErdf() {
		return erdf;
	}
	public void setErdf(double erdf) {
		this.erdf = erdf;
	}
	public double getIpa_funds() {
		return ipa_funds;
	}
	public void setIpa_funds(double ipa_funds) {
		this.ipa_funds = ipa_funds;
	}
	public double getProject_amount() {
		return project_amount;
	}
	public void setProject_amount(double project_amount) {
		this.project_amount = project_amount;
	}
	public double getCofinancing_rate() {
		return cofinancing_rate;
	}
	public void setCofinancing_rate(double cofinancing_rate) {
		this.cofinancing_rate = cofinancing_rate;
	}
	public String getProject_status() {
		return project_status;
	}
	public void setProject_status(String project_status) {
		this.project_status = project_status;
	}
	public String getProject_deliverables_url() {
		return project_deliverables_url;
	}
	public void setProject_deliverables_url(String project_deliverables_url) {
		this.project_deliverables_url = project_deliverables_url;
	}
	public String getProject_community() {
		return project_community;
	}
	public void setProject_community(String project_community) {
		this.project_community = project_community;
	}
	
	
}
