package crawler_interface;
import java.util.List;

public class Metadata {

	private String delDate;
	private List<String> delKeywords;
	private String projName;
	private String delDescription;
	private List<String> delTargets;
	private String url;
	private String fileName;
	private String deliverableBudget;
	private String projectBudget;
	private List<Partner> partners;
	private String[] simWord;

	public String getDelDate() {
		return delDate;
	}

	public void setDelDate(String delDate) {
		this.delDate = delDate;
	}

	public String[] getsimWord() {
		return simWord;
	}

	public void setsimWord(String[] simWord) {
		this.simWord = simWord;
	}

	public List<String> getDelKeywords() {
		return delKeywords;
	}

	public void setDelKeywords(List<String> delKeywords) {
		this.delKeywords = delKeywords;
	}

	public String getProjName() {
		return projName;
	}

	public String getDeliverableBudget() {
		return deliverableBudget;
	}

	public void setDeliverableBudget(String deliverableBudget) {
		this.deliverableBudget = deliverableBudget;
	}

	public void setProjName(String projName) {
		this.projName = projName;
	}

	public String getDelDescription() {
		return delDescription;
	}

	public void setDelDescription(String delDescription) {
		this.delDescription = delDescription;
	}

	public List<String> getDelTargets() {
		return delTargets;
	}

	public void setDelTargets(List<String> delTergets) {
		this.delTargets = delTergets;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getProjectBudget() {
		return projectBudget;
	}

	public void setProjectBudget(String projectBudget) {
		this.projectBudget = projectBudget;
	}

	public List<Partner> getPartners() {
		return partners;
	}

	public void setPartners(List<Partner> lpartners) {
		this.partners = lpartners;
	}

	private class DoubleForm {

		private String ShortForm;
		private String CompleteForm;

		public String getShortForm() {
			return ShortForm;
		}

		public void setShortForm(String shortForm) {
			ShortForm = shortForm;
		}

		public String getCompleteForm() {
			return CompleteForm;
		}

		public void setCompleteForm(String completeForm) {
			CompleteForm = completeForm;
		}
	}

}
