package crawler_interface;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


public class Delivery {
	private String url;
	private String title;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private Date date;
	private String description;
	private String type;
	private List<String> keywords;
	private List<String> targets;
	private Project progetto;

	public Delivery() {

	}

	public Delivery(String url) {

		this.setUrl(url);
	}

	public Delivery(String url, String title, Date date, String description, String type) {

		this.setUrl(url);
		this.title = title;
		this.setDate(date);
		this.description = description;
		this.type = type;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<String> getKeywords() {
		return keywords;
	}

	public void setKeywords(List<String> keywords) {
		this.keywords = keywords;
	}

	public List<String> getTargets() {
		return targets;
	}

	public void setTargets(List<String> targets) {
		this.targets = targets;
	}

	public Project getProgetto() {
		return progetto;
	}

	public void setProgetto(Project pr) {
		this.progetto = pr;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String toString() {
		return "Titolo: " + this.getTitle() + "\nProgetto: " + this.getProgetto().getAcronym() + "\nSummary: "
				+ this.getProgetto().getSummary();
	}

}
