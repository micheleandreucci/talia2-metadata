import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import databaseObjects.*;

public class DatabaseInterface {
	private Connection conn;
	private Statement stat;

	public DatabaseInterface() throws SQLException {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			System.out.println("ClassNotFoundException: ");
			System.err.println(e.getMessage());
		}

		// Creo la connessione al database
		conn = DriverManager.getConnection(
				"jdbc:mysql://localhost:3306/talia2?useUnicode=yes&characterEncoding=UTF-8&allowPublicKeyRetrieval=true&useSSL=false&user=root&password=root");
		stat = (Statement) conn.createStatement();

	}

	public void loadDeliverableData(String url, String title, Date date, String description, String type,
			String project_acronym) throws SQLException, ParseException {
		try {
			DateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
			int project_id = 0;
			String delivdate = "";

			if (date != null) {
				String string_date = date.toString();
				Date formatted_date = formatter.parse(string_date);
				delivdate = "'" + new java.sql.Date(formatted_date.getTime()).toString() + "'";
			} else {
				delivdate = "null";
			}

			ResultSet result = stat
					.executeQuery("select project_id from projects where project_acronym='" + project_acronym + "';");

			if (result.next()) {
				project_id = result.getInt(1);
			}

			if (title != null) {
				title = title.replace("'", "");
			}

			if (description != null) {
				description = description.replace("'", "");
			}

			if (type != null) {
				type = type.replace("'", "");
			}

			String insertDeliverable = "insert ignore into Deliverables values (NULL, '" + url + "', '" + title + "', "
					+ delivdate + ", '" + description + "', '" + type + "', 0," + project_id + ");";
			System.out.println(insertDeliverable);
			stat.executeUpdate(insertDeliverable);
		} catch (SQLException e) {
			System.err.println("errore " + e.getMessage());
		}
	};

	public void loadCommunityData(String community) throws SQLException {
		try {
			String insertCommunity = "INSERT IGNORE INTO communities VALUES('" + community + "');";
			System.out.println(insertCommunity);
			stat.executeUpdate(insertCommunity);
		} catch (SQLException e) {
			System.err.println("errore:" + e.getMessage());
		}
	};

	public void loadProjectData(int axis, int objective, String acronym, String label, String summary, String call,
			Date start_date, Date end_date, String type, double erdf, double ipa_funds, double project_amount,
			Double cofinancing_rate, String project_status, String url, String community) throws SQLException {
		try {
			DateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);

			if (acronym != null) {
				acronym = acronym.replace("'", "");
			}

			// controllo progetto già presente
			String checkDuplicateProject = "select project_acronym from projects where project_acronym='" + acronym
					+ "';";

			if (stat.executeQuery(checkDuplicateProject).first() == false) {
				// aggiunta dati relativi al progetto
				try {
					if (start_date != null) {
						Date start = formatter.parse(start_date.toString());
						System.out.println("inizio " + start);
						java.sql.Date sqlstart = new java.sql.Date(start.getTime());
						Date end = formatter.parse(end_date.toString());
						java.sql.Date sqlend = new java.sql.Date(end.getTime());

						if (label != null) {
							label = label.replace("'", "");
						}

						if (summary != null) {
							summary = summary.replace("'", "");
						}

						if (call != null) {
							call = call.replace("'", "");
						}

						String populateProjects = "insert ignore into Projects values (NULL, " + axis + ", " + objective
								+ ", '" + acronym + "', '" + label + "', '" + summary + "', '" + call + "', '"
								+ sqlstart + "', '" + sqlend + "', '" + type + "', " + erdf + ", " + ipa_funds + ", "
								+ project_amount + ", " + cofinancing_rate + ", '" + project_status + "', '" + url
								+ "', '" + community + "');";
						System.out.println(populateProjects);
						stat.executeUpdate(populateProjects);
					}
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} catch (SQLException e) {
			System.err.println("non riesco a caricare il progetto" + e);
			System.out.println("errore:" + e.getMessage());
		}
	}

	public void loadPartnerData(Boolean lp, String name, String nature, String country, String postal_code, String area,
			String nuts3, String city, String address, double erdf, double erdf_contribution, double ipa_funds,
			double ipa_contribution, double amount) throws SQLException, UnsupportedEncodingException {
		try {
			String checkDuplicatePartners = "select partner_name from partners where partner_name='"
					+ name.replace("'", "") + "';";

			if (stat.executeQuery(checkDuplicatePartners).first() == false) {
				byte[] byte_nuts3;

				if (nuts3 != null) {
					byte_nuts3 = nuts3.replace("'", "").getBytes("UTF-8");
					nuts3 = new String(byte_nuts3, "UTF-8");
				}

				if (country != null) {
					country = country.replace("'", "");
				}

				if (name != null) {
					name = name.replace("'", "");
				}

				if (nature != null) {
					nature = nature.replace("'", "");
				}

				if (area != null) {
					area = area.replace("'", "");
				}

				String insertPartner = "insert ignore into Partners values (NULL, " + lp + ", '" + name + "', '"
						+ nature + "', '" + country + "', '" + postal_code + "', '" + area + "', '" + nuts3 + "', "
						+ erdf + ", " + erdf_contribution + ", " + ipa_funds + ", " + ipa_contribution + ", " + amount
						+ ");";
				System.out.println(insertPartner);
				stat.executeUpdate(insertPartner);
			}
		} catch (NullPointerException e) {
			System.err.println("non riesco a caricare il partner " + e);
			e.printStackTrace();
		}

	}

	public void loadDeliverableKeywords(String deliverable_title, List<String> keywords)
			throws SQLException, UnknownDeliverableException {
		try {
			int deliverable_id;

			String getDeliverableId = "select deliverable_id from deliverables where deliverable_title='"
					+ deliverable_title.replace("'", "") + "';";
			ResultSet deliverableID = stat.executeQuery(getDeliverableId);

			if (deliverableID.next()) {
				deliverable_id = deliverableID.getInt(1);
			} else {
				throw new UnknownDeliverableException();
			}

			for (String keyword : keywords) {
				String insertDeliverableKeyword = "insert ignore into DeliverableKeywords values (" + deliverable_id
						+ ", '" + keyword + "');";
				System.out.println(insertDeliverableKeyword);
				stat.executeUpdate(insertDeliverableKeyword);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("errore: " + e.getMessage());
		}
	};

	public void loadDeliverableTargets(String deliverable_title, List<String> targets)
			throws SQLException, UnknownDeliverableException {
		try {
			int deliverable_id;

			String getDeliverableId = "select deliverable_id from deliverables where deliverable_title='"
					+ deliverable_title.replace("'", "") + "';";
			ResultSet deliverableID = stat.executeQuery(getDeliverableId);

			if (deliverableID.next()) {
				deliverable_id = deliverableID.getInt(1);
			} else {
				throw new UnknownDeliverableException();
			}

			for (String target : targets) {
				String insertDeliverableTargets = "insert ignore into DeliverableTargets values (" + deliverable_id
						+ ", '" + target + "');";
				System.out.println(insertDeliverableTargets);
				stat.executeUpdate(insertDeliverableTargets);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("ERRORE " + e.getMessage());
		}
	};

	public void loadStakeholderData(int stakeholder_id, String organization_name, String stakeholder_nature,
			String stakeholder_country, String postal_code, String area, String nuts3, String stakeholder_city,
			String stakeholder_address, String web_address, String email) throws SQLException {

		String insertStakeholder = "insert into Stakeholders values (" + stakeholder_id + ", '" + organization_name
				+ "', '" + stakeholder_nature + "', '" + stakeholder_country + "', '" + postal_code + "', '" + nuts3
				+ "', '" + stakeholder_city + "', '" + stakeholder_address + "', '" + web_address + "', '" + email
				+ "');";
		stat.executeUpdate(insertStakeholder);
	};

	public void loadProjectPartner(String project_acronym, String partner_name) throws SQLException {
		try {
			project_acronym = project_acronym.replace("'", "");
			partner_name = partner_name.replace("'", "");

			String getProjectId = "select project_id from projects where project_acronym='" + project_acronym + "';";
			String getPartnerId = "select partner_id from partners where partner_name='" + partner_name + "';";

			ResultSet ProjIdRes = stat.executeQuery(getProjectId);
			int projectId = 0;
			if (ProjIdRes.next()) {
				projectId = ProjIdRes.getInt(1);
			}

			ResultSet PartIdRes = stat.executeQuery(getPartnerId);
			int partnerId = 0;
			if (PartIdRes.next()) {
				partnerId = PartIdRes.getInt(1);
			}

			String insertProjectPartner = "insert ignore into projectPartners values (" + projectId + ", '" + partnerId
					+ "');";
			System.out.println(insertProjectPartner);
			stat.executeUpdate(insertProjectPartner);
		} catch (SQLException e) {
			System.err.println("errore " + e.getMessage());
			e.printStackTrace();
		}
	};

	public void loadProjectStakeholder() {
	};

	public void loadStakeholderKeywords() {
	};

	public DeliverableDB getDeliverableData(int docid) throws SQLException {

		String selectDeliverable = "select * from Deliverables where deliverable_id = " + docid + ";";

		ResultSet deliverable_rs = stat.executeQuery(selectDeliverable);
		deliverable_rs.next();

		DeliverableDB deliverable_object = new DeliverableDB();

		deliverable_object.setId(deliverable_rs.getInt(0));
		deliverable_object.setUrl(deliverable_rs.getString(1));
		deliverable_object.setTitle(deliverable_rs.getString(2));
		deliverable_object.setDate(deliverable_rs.getDate(3));
		deliverable_object.setDescription(deliverable_rs.getString(4));
		deliverable_object.setType(deliverable_rs.getString(5));
		deliverable_object.setBudget(deliverable_rs.getFloat(6));
		deliverable_object.setProjectId(deliverable_rs.getInt(7));

		return deliverable_object;
	};

	public ProjectDB getProjectData(int docid) throws SQLException {

		String selectProjectId = "select deliverable_project_id from Deliverables where deliverable_id = " + docid
				+ ";";

		ResultSet projectId_rs = stat.executeQuery(selectProjectId);

		int project_id = 0;

		if (projectId_rs.next()) {
			project_id = projectId_rs.getInt(1);
		}

		String selectProject = "select * from Projects where project_id = " + project_id + ";";

		ResultSet project_rs = stat.executeQuery(selectProject);
		project_rs.next();

		ProjectDB Project = new ProjectDB();

		Project.setProject_id(project_id);
		Project.setProject_axis(project_rs.getInt("project_axis"));
		Project.setProject_objective(project_rs.getInt("project_objective"));
		Project.setProject_acronym(project_rs.getString("project_acronym"));
		Project.setProject_label(project_rs.getString("project_label"));
		Project.setOperation_summary(project_rs.getString("operation_summary"));
		Project.setCall_for_proposals(project_rs.getString("call_for_proposals"));
		Project.setStart_date(project_rs.getDate("start_date"));
		Project.setEnd_date(project_rs.getDate("end_date"));
		Project.setProject_type(project_rs.getString("project_type"));
		Project.setErdf(project_rs.getDouble("erdf"));
		Project.setIpa_funds(project_rs.getDouble("ipa_funds"));
		Project.setProject_amount(project_rs.getDouble("project_amount"));
		Project.setCofinancing_rate(project_rs.getDouble("cofinancing_rate"));
		Project.setProject_status(project_rs.getString("project_status"));
		Project.setProject_deliverables_url(project_rs.getString("project_deliverables_url"));
		Project.setProject_community(project_rs.getString("project_community"));

		return Project;
	}

	public List<PartnerDB> getProjectPartners(int docid) throws SQLException {
		List<Integer> partnersList = new ArrayList<Integer>();

		String selectProjectId = "select deliverable_project_id from Deliverables where deliverable_id = " + docid
				+ ";";
		ResultSet projectId_rs = stat.executeQuery(selectProjectId);

		int project_id = 0;

		if (projectId_rs.next()) {
			project_id = projectId_rs.getInt(1);
		}

		String selectProjectPartners = "select project_partner_id from Project_partners where project_id =" + project_id
				+ ";";
		ResultSet projectPartners_rs = stat.executeQuery(selectProjectPartners);

		while (projectPartners_rs.next()) {
			partnersList.add(projectPartners_rs.getInt("project_partner_id"));
		}

		List<PartnerDB> PartnersList = new ArrayList<PartnerDB>();
		PartnerDB tempPartner = new PartnerDB();

		String selectPartner = "";

		for (Integer partner_id : partnersList) {

			selectPartner = "select * from Partners where partner_id = " + partner_id + ";";
			ResultSet partner_rs = stat.executeQuery(selectPartner);
			partner_rs.next();

			tempPartner.setPartner_id(partner_rs.getInt("partner_id"));
			tempPartner.setIs_lead_partner(partner_rs.getBoolean("is_lead_partner"));
			tempPartner.setPartner_name(partner_rs.getString("partner_name"));
			tempPartner.setPartner_nature(partner_rs.getString("partner_nature"));
			tempPartner.setPartner_country(partner_rs.getString("partner_nature"));
			tempPartner.setPartner_postalCode(partner_rs.getString("partner_postalCode"));
			tempPartner.setPartner_area(partner_rs.getString("partner_area"));
			tempPartner.setNuts3(partner_rs.getString("nuts3"));
			tempPartner.setPartner_city(partner_rs.getString("partner_city"));
			tempPartner.setPartner_address(partner_rs.getString("partner_address"));
			tempPartner.setErdf(partner_rs.getDouble("erdf"));
			tempPartner.setErdfContribution(partner_rs.getDouble("erdfContribution"));
			tempPartner.setIpa_funds(partner_rs.getDouble("ipa_funds"));
			tempPartner.setIpaContribution(partner_rs.getDouble("ipaContribution"));
			tempPartner.setPartner_amount(partner_rs.getDouble("partner_amount"));

			PartnersList.add(tempPartner);
		}

		return PartnersList;
	}

	public List<String> getDeliverableKeywords(int docid) throws SQLException {

		String selectKeywords = "select related_keyword from DeliverableKeywords where deliverable_id = " + docid + ";";
		ResultSet keywords_rs = stat.executeQuery(selectKeywords);

		List<String> Keywords = new ArrayList<String>();

		while (keywords_rs.next()) {
			Keywords.add(keywords_rs.getString("related_keyword"));
		}

		return Keywords;
	}

	public List<String> getDeliverableTargets(int docid) throws SQLException {

		String selectTargets = "select related_keyword from DeliverableTargets where deliverable_id = " + docid + ";";
		ResultSet targets_rs = stat.executeQuery(selectTargets);

		List<String> Targets = new ArrayList<String>();

		while (targets_rs.next()) {
			Targets.add(targets_rs.getString("deliverable_target"));
		}

		return Targets;
	}
}