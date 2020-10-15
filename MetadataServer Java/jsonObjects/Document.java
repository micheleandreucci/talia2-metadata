package jsonObjects;
public class Document {
  String name;
  Delivery delivery;

  
	public Document() {
		//DEFAULT CONSTRUCTOR
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
