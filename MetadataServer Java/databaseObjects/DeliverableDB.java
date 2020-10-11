package databaseObjects;
import java.sql.Date;

public class DeliverableDB {

	private int id;
	private String url;
	private String title;
	private Date date;
	private String description; 
	private String type;
	private float budget;
	private int project_id;
	
	public DeliverableDB() {};
	
	public void setId (int _id) {
		id = _id;
	}
	
	public void setUrl (String _url) {
		url = _url;
	}
	
	public void setTitle (String _title) {
		title = _title;
	}
	
	public void setDate (Date _date) {
		date = _date;
	}
	
	public void setDescription (String _description) {
		description = _description;
	}
	
	public void setType (String _type) {
		type = _type;
	}
	
	public void setBudget (float _budget) {
		budget = _budget;
	}
	
	public void setProjectId (int _project_id) {
		project_id = _project_id;
	}
}
