public class UnknownDeliverableException extends Throwable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UnknownDeliverableException() {
		System.out.println("No deliverable found with specified title.");
	}
}