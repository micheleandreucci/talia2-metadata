package crawler_interface;

public class DetailedResult {

	private Result result;
	private Metadata metadata;

	public DetailedResult(Result result, Metadata metadata) {

		this.result = result;
		this.metadata = metadata;
	}

	public Result getResult() {
		return result;
	}

	public void setResult(Result result) {
		this.result = result;
	}

	public Metadata getMetadata() {
		return metadata;
	}

	public void setMetadata(Metadata metadata) {
		this.metadata = metadata;
	}

}
