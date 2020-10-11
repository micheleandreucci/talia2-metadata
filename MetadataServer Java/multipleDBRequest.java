import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

import jsonObjects.*;

/*
 * gestire la connessione con la base di dati
 * e il caricamento dei dati nel database a partire dai file json e csv
 */
public class multipleDBRequest {
	private DatabaseInterface db;

	public multipleDBRequest() throws SQLException {
		db = new DatabaseInterface();
	};

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

		for (Document document : json_object.getDocuments()) {
			Delivery deliverable = document.getDelivery();
			String project_acronym = deliverable.getProgetto().getAcronym();
			Project project = deliverable.getProgetto();

			db.loadProjectData(project.getAxis(), project.getObjective(), project.getAcronym(), project.getLabel(),
					project.getSummary(), project.getCall(), project.getStart(), project.getEnd(), project.getType(),
					project.getErdf(), project.getIpa(), project.getAmount(),
					Double.parseDouble(project.getCofinancing()), project.getStatus(), project.getDeliverablesUrl(),
					json_object.getCollection());

			for (jsonObjects.Partner partner : project.getPartners()) {

				db.loadPartnerData(partner.isLP(), partner.getName(), partner.getNature(), partner.getCountry(),
						partner.getPostalCode(), partner.getArea(), partner.getNUTS3(), null, null, partner.getErdf(),
						partner.getErdfContribution(), partner.getIpa(), partner.getIpaContribution(),
						partner.getAmount());

				db.loadProjectPartner(project.getAcronym(), partner.getName());
			}

			db.loadDeliverableData(deliverable.getUrl(), document.getName(), deliverable.getDate(),
					deliverable.getDescription(), deliverable.getType(), project_acronym);

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
		Json_Parser jParse = new Json_Parser("C:\\Users\\Flavio\\Desktop", "Blue Growth");

		DocumentListJson collectionJson = jParse.parseJson();

		try {
			multipleDBRequest db = new multipleDBRequest();

			db.loadAllJsonData(collectionJson);

		} catch (SQLException | NumberFormatException | UnsupportedEncodingException | ParseException
				| UnknownDeliverableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}