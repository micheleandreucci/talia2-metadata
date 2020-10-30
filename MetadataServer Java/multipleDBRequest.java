
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.LinkedList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.jsoup.select.Elements;

import deliveryCrawler.crawler.Category;
import jsonObjects.*;

public class multipleDBRequest {
	private DatabaseInterface db;

	public multipleDBRequest() throws SQLException {
		db = new DatabaseInterface();
	}

	/*
	 * public void loadDeliverablesData() throws SQLException, ParseException {
	 * 
	 * for (Document document : json_object.getDocuments()) { Delivery deliverable =
	 * document.getDelivery(); String project_acronym =
	 * deliverable.getProgetto().getAcronym();
	 * 
	 * db.loadDeliverableData(deliverable.getUrl(), deliverable.getTitle(),
	 * deliverable.getDate(), deliverable.getDescription(), deliverable.getType(),
	 * project_acronym); } }
	 * 
	 * public void loadProjectsData() throws NumberFormatException, SQLException {
	 * 
	 * for (Document document : json_object.getDocuments()) { Delivery deliverable =
	 * document.getDelivery(); Project project = deliverable.getProgetto();
	 * 
	 * db.loadProjectData(project.getAxis(), project.getObjective(),
	 * project.getAcronym(), project.getLabel(), project.getSummary(),
	 * project.getCall(), project.getStart(), project.getEnd(), project.getType(),
	 * project.getErdf(), project.getIpa(), project.getAmount(),
	 * Double.parseDouble(project.getCofinancing()), project.getStatus(),
	 * project.getDeliverablesUrl(), json_object.getCollection()); } }
	 * 
	 * public void loadPartnersData() throws UnsupportedEncodingException,
	 * SQLException {
	 * 
	 * for (Document document : json_object.getDocuments()) { Delivery deliverable =
	 * document.getDelivery(); Project project = deliverable.getProgetto();
	 * 
	 * for (Partner partner : project.getPartners()) {
	 * 
	 * db.loadPartnerData(partner.isLP(), partner.getName(), partner.getNature(),
	 * partner.getCountry(), partner.getPostalCode(), partner.getArea(),
	 * partner.getNUTS3(), null, null, partner.getErdf(),
	 * partner.getErdfContribution(), partner.getIpa(),
	 * partner.getIpaContribution(), partner.getAmount()); } } }
	 * 
	 * public void loadDeliverablesKeywords() {
	 * 
	 * for (Document document : json_object.getDocuments()) {
	 * 
	 * } }
	 */

	public void loadAllJsonData(DocumentListJson json_object) throws SQLException, ParseException,
			UnsupportedEncodingException, UnknownDeliverableException, NumberFormatException {
		List<String> keywords;
		List<String> targets;

		db.loadCommunityData(json_object.getCollection());

		for (jsonObjects.Document document : json_object.getDocuments()) {
			Delivery deliverable = document.getDelivery();
			String project_acronym = deliverable.getProgetto().getAcronym();
			Project project = deliverable.getProgetto();

			db.loadProjectData(project.getAxis(), project.getObjective(), project.getAcronym(), project.getLabel(),
					project.getSummary(), project.getCall(), project.getStart(), project.getEnd(), project.getType(),
					project.getErdf(), project.getIpa(), project.getAmount(), project.getCofinancing(),
					project.getStatus(), project.getDeliverablesUrl(), json_object.getCollection());
			for (jsonObjects.Partner partner : project.getPartners()) {
				db.loadPartnerData(partner.isLP(), partner.getName(), partner.getNature(), partner.getCountry(),
						partner.getPostalCode(), partner.getArea(), partner.getNuts3(), partner.getErdf(),
						partner.getErdfContribution(), partner.getIpa(), partner.getIpaContribution(),
						partner.getAmount());

				db.loadProjectPartner(project.getAcronym(), partner.getName());

				db.loadDeliverableData(deliverable.getUrl(), document.getName(), deliverable.getDate(),
						deliverable.getDescription(), deliverable.getType(), project_acronym, partner.getName());
			}

			keywords = deliverable.getKeywords();
			if (keywords != null) {
				db.loadDeliverableKeywords(document.getName(), keywords);
			}

			targets = deliverable.getTargets();
			if (targets != null) {
				db.loadDeliverableTargets(document.getName(), targets);
			}
		}
	}

	public static void main(String args[]) {
		List<Category> categories = new LinkedList<Category>();
		org.jsoup.nodes.Document projects = null;
		try {
			projects = Jsoup.connect("https://interreg-med.eu/projects-results/our-projects").timeout(0).get();
		} catch (IOException e) {
			e.printStackTrace();
		}
		FormElement form = (FormElement) projects.selectFirst("form[name=\"search\"]");
		Element them = form.selectFirst("select");
		Elements options = them.getElementsByTag("option");
		for (Element option : options) {
			// seleziona dal form di ricerca, in tematics, le categorie e prende il valore
			String categoryValue = option.attr("value");

			if (!categoryValue.equalsIgnoreCase("0")) {

				String categoryName = option.text().trim();

				Category cat = new Category(categoryName, categoryValue);
				categories.add(cat);
			}
		}
		for (Category cat : categories) {
//			if (cat.getCollection() == "Social and Creative") {
			Json_Parser jParse = new Json_Parser("./" + cat.getCollection(), cat.getCollection());

			DocumentListJson collectionJson = jParse.parseJson();
			System.out.println("json " + collectionJson);
			try {
				multipleDBRequest db = new multipleDBRequest();
				db.loadAllJsonData(collectionJson);
			} catch (SQLException | NumberFormatException | UnsupportedEncodingException | ParseException
					| UnknownDeliverableException e) {
				e.printStackTrace();
			}
		}
	}

//	}
}