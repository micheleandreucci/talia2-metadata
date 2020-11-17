package crawler_interface;


public class Resource {

	private String name;
	private Delivery delivery;

	public Resource() {
		// DEFAULT CONSTRUCTOR
	}

	public Resource(String name) {

		this.name = name;
	}

	public Delivery getDelivery() {

		return delivery;
	}

	public void setDelivery(Delivery delivery) {

		this.delivery = delivery;
	}

	public void setName(String name) {

		this.name = name;
	}

	public String getName() {

		return this.name;
	}

}
