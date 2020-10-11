import java.io.Serializable;
import java.util.ArrayList;

public class Model implements Serializable {
	/**
	* 
	*/
	private static final long serialVersionUID = 1L;
	private ArrayList<Concept> conceptRighe;
	private ArrayList<Concept> conceptColonne;
	private String collection;
	private String author;
	private String date;
	private String description;
	private String name_A;
	private ArrayList<Object> phrases1;
	private ArrayList<Object> phrases2;

	public ArrayList<Object> getPhrases1() {
		return phrases1;
	}

	public void setPhrases1(ArrayList<Object> phrases1) {
		this.phrases1 = phrases1;
	}

	public ArrayList<Concept> getConceptRighe() {
		return conceptRighe;
	}

	public void setConceptRighe(ArrayList<Concept> conceptRighe) {
		this.conceptRighe = conceptRighe;
	}

	public ArrayList<Concept> getConceptColonne() {
		return conceptColonne;
	}

	public void setConceptColonne(ArrayList<Concept> conceptColonne) {
		this.conceptColonne = conceptColonne;
	}

	public String getCollection() {
		return collection;
	}

	public void setCollection(String collection) {
		this.collection = collection;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getName_A() {
		return name_A;
	}

	public void setName_A(String name_A) {
		this.name_A = name_A;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public ArrayList<Object> getPhrases2() {
		return phrases2;
	}

	public void setPhrases2(ArrayList<Object> phrases2) {
		this.phrases2 = phrases2;
	}
}
