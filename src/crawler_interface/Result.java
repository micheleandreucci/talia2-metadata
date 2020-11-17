package crawler_interface;

public class Result {

	private String id;
	private String summary;
	private String score;
	private String simwords;
	private String scoreperc;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSimwords() {
		return simwords;
	}

	public void setSimwords(String simwords) {
		this.simwords = simwords;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

	public String getScoreperc() {
		return scoreperc;
	}

	public void setScoreperc(String scoreperc) {
		double x = Double.parseDouble(score);
		x = x * 100;
		String newscore = Double.toString(x);
		this.scoreperc = newscore;
	}
}
