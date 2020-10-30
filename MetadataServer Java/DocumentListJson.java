import java.util.LinkedList;
import java.util.List;

import jsonObjects.*;

public class DocumentListJson {
	/**
	 * the collection name
	 */
	private String collection;
	/**
	 * the metadata of all documents
	 */
	private List<Document> documents = new LinkedList<Document>();

	public DocumentListJson() {
		// DEFAULT CONSTRUCTOR
	}

	public String getCollection() {
		return collection;
	};

	public List<Document> getDocuments() {
		return documents;
	}

	public String toString() {
		return collection;
	}
}
