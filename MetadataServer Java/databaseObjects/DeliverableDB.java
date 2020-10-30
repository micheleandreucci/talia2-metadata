package databaseObjects;

import java.sql.Date;

public class DeliverableDB {

	private int id;
	private String url;
	private String title;
	private Date date;
	private String description;
	private String type;
	private int project_id;
	private int partner_id;

	public DeliverableDB() {
	};

	public void setId(int _id) {
		id = _id;
	}

	public int getId() {
		return id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String _url) {
		url = _url;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String _title) {
		title = _title;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date _date) {
		date = _date;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String _description) {
		description = _description;
	}

	public String getType() {
		return type;
	}

	public void setType(String _type) {
		type = _type;
	}

	public int getProject_id() {
		return project_id;
	}

	public void setProject_id(int project_id) {
		this.project_id = project_id;
	}

	public int getPartner_id() {
		return partner_id;
	}

	public void setPartner_id(int partner_id) {
		this.partner_id = partner_id;
	}
}
