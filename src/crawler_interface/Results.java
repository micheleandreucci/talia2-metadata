package crawler_interface;
import java.util.List;

public class Results {

	private List<Result> results;
	private int num = 30;

	public List<Result> getResults() {
		return results;
	}

	public void setResults(List<Result> results) {
		this.results = results;
	}

	public int ResultsSize() {
		return results.size();
	}

}
