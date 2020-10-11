package deliveryCrawler.crawler;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.jsoup.select.Elements;

public class Scraper {

	static void connessioneSql(Category cat) throws SQLException, UnsupportedEncodingException {
		Connection cn;
		Statement st;
		ResultSet rs;
		String sql;
		String sql1;
		String populatePartners = "";
		String populateProjects = "";
		String populateDeliverables = "";
		String populateTargets = "";
		HashSet totalprojects = new HashSet();
		// ________________________________connessione
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			System.out.println("ClassNotFoundException: ");
			System.err.println(e.getMessage());
		} // fine try-catch

		// Creo la connessione al database
		cn = DriverManager.getConnection(
				"jdbc:mysql://localhost:3306/talia2?useUnicode=yes&characterEncoding=UTF-8&user=root&password=root");
		// json è il nome del database
		sql1 = "INSERT ignore INTO communities VALUES('" + cat.getCollection() + "');";
		st = (Statement) cn.createStatement();
		try {
			st.executeUpdate(sql1);
			for (Resource res : cat.getDocuments()) {
				Delivery deliv = res.getDelivery();
				Project proj = deliv.getProgetto();
				DateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
				String checkDuplicateProjects = "select project_acronym from projects where project_acronym='"
						+ proj.getAcronym().replace("'", "") + "';";

				// aggiunta dati relativi al progetto
				if (st.executeQuery(checkDuplicateProjects).first() == false) {
					try {
						Date start = formatter.parse(proj.getStart().toString());
						java.sql.Date sqlstart = new java.sql.Date(start.getTime());
						Date end = formatter.parse(proj.getEnd().toString());
						java.sql.Date sqlend = new java.sql.Date(end.getTime());

						populateProjects = "insert into Projects values (NULL, " + proj.getAxis() + ", "
								+ proj.getObjective() + ", '" + proj.getAcronym().replace("'", "") + "', '"
								+ proj.getLabel().replace("'", "") + "', '" + proj.getSummary().replace("'", "")
								+ "', '" + proj.getCall().replace("'", "") + "', '" + sqlstart + "', '" + sqlend
								+ "', '" + proj.getType() + "', " + proj.getErdf() + ", " + proj.getIpa() + ", "
								+ proj.getAmount() + ", " + proj.getCofinancing() + ", '" + proj.getStatus() + "', '"
								+ proj.getDeliverablesUrl() + "', '" + cat.getCollection() + "');";
						st.executeUpdate(populateProjects);

					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					// aggiunta dati relativi ai partners
					try {
						for (Partner partner : proj.getPartners()) {
							String checkDuplicatePartners = "select partner_name from partners where partner_name='"
									+ partner.getName().replace("'", "") + "';";

							if (st.executeQuery(checkDuplicatePartners).first() == false) {
								byte[] nuts3 = partner.getNUTS3().replace("'", "").getBytes("UTF-8");
								String nuts3ciao = new String(nuts3, "UTF-8");
								populatePartners = "insert into Partners values (NULL, " + partner.isLP() + ", '"
										+ partner.getName().replace("'", "") + "', '"
										+ partner.getNature().replace("'", "") + "', '"
										+ partner.getCountry().replace("'", "") + "', '" + partner.getPostalCode()
										+ "', '" + partner.getArea().replace("'", "") + "', '" + nuts3ciao + "', "
										+ "NULL, NULL, " + partner.getErdf() + ", " + partner.getErdfContribution()
										+ ", " + partner.getIpa() + ", " + partner.getIpaContribution() + ", "
										+ partner.getAmount() + ");";
								st.executeUpdate(populatePartners);
							}
							// aggiunta dati relativi ai partner del progetto correntemente esaminato
							String getProjectId = "select project_id from projects where project_acronym='"
									+ proj.getAcronym().replace("'", "") + "';";
							String getPartnerId = "select partner_id from partners where partner_name='"
									+ partner.getName().replace("'", "") + "';";

							ResultSet ProjIdRes = st.executeQuery(getProjectId);

							int projectId = 0;
							if (ProjIdRes.next()) {
								projectId = ProjIdRes.getInt(1);
							}
							ResultSet PartIdRes = st.executeQuery(getPartnerId);
							int partnerId = 0;
							if (PartIdRes.next()) {
								partnerId = PartIdRes.getInt(1);
							}
							String addProjectPartner = "insert into projectPartners values (" + projectId + ", '"
									+ partnerId + "');";
							st.executeUpdate(addProjectPartner);
						}
					} catch (SQLException e) {
						System.out.println("errore:" + e.getMessage());
					} // fine try-catch
				}

				// aggiunta dati relativi al deliverable
				try {
					Date temp = deliv.getDate();
					String delivdate = "";
					if (temp != null) {
						delivdate = temp.toString();
						Date date = formatter.parse(delivdate);
						delivdate = "'";
						delivdate += new java.sql.Date(date.getTime()).toString() + "'";
					} else {
						delivdate = "null";
					}
					int id = 0;
					ResultSet result = st.executeQuery(
							"select project_id from projects where project_acronym='" + proj.getAcronym() + "';");
					if (result.next()) {
						id = result.getInt(1);
					}
					String title = res.getName();
					if (title != null) {
						title = title.replace("'", "");
					}
					String description = deliv.getDescription();
					if (description != null) {
						description = description.replace("'", "");
					}
					String type = deliv.getType();
					if (type != null) {
						type = type.replace("'", "");
					}
					populateDeliverables = "insert ignore into Deliverables values (NULL, '" + deliv.getUrl() + "', '"
							+ title + "', " + delivdate + ", '" + description + "', '" + type + "', 0," + id + ");";
					st.executeUpdate(populateDeliverables);

					int deliverable_id = 0;
					String getDeliverableId = "select deliverable_id from deliverables where deliverable_title='"
							+ title + "';";
					ResultSet deliverableID = st.executeQuery(getDeliverableId);

					if (deliverableID.next()) {
						deliverable_id = deliverableID.getInt(1);
					}
					if (deliv.getKeywords() != null) {
						for (String keyword : deliv.getKeywords()) {
							String insertDeliverableKeywords = "insert ignore into DeliverableKeywords values ("
									+ deliverable_id + ", '" + keyword + "');";
							st.executeUpdate(insertDeliverableKeywords);
						}
					}
					List<String> targets = deliv.getTargets();
					if (targets != null) {
						for (String target : targets) {

							populateTargets = "insert ignore into DeliverableTargets values (" + deliverable_id + ", '"
									+ target + "');";
							st.executeUpdate(populateTargets);
						}
					}
				} catch (SQLException e) {
					System.out.println("errore:" + e.getMessage());
				} // fine try-catch
				catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} catch (SQLException e) {
			System.out.println("errore:" + e.getMessage());
		} // fine try-catch
		cn.close(); // chiusura connessione
	}

	public static void main(String[] args) throws UnsupportedEncodingException {

		Document projects = null;

		try {

			projects = Jsoup.connect("https://interreg-med.eu/projects-results/our-projects").timeout(0).get();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Elements downloadLinks = projects.select(".download-link");
		String projectFileUrl = null;
		String beneficiariesFileUrl = null;
		String pathProjects = "projects.xlsx";
		Date projectDate = null;
		String pathPartners = "partners.xlsx";
		Date beneficiariesDate = null;

		for (Element l : downloadLinks) {

			String fileUrl = l.absUrl("href");
			System.out.println("Url: " + fileUrl);
			String urlName = l.text();
			System.out.println("Nome: " + urlName);

			// extract the text of the link
			String[] urlNamePart = urlName.split("-");
			// extract the date from the link
			String dateStr = urlNamePart[urlNamePart.length - 1].trim();
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			Date date = null;
			try {
				date = sdf.parse(dateStr);
			} catch (ParseException e) {

				System.out.println("Error parsing date xls:" + urlName + ", " + e);
			}

			if (l.text().contains("projects")) {

				projectFileUrl = fileUrl;
				projectDate = date;
			}

			if (l.text().contains("beneficiaries")) {

				beneficiariesFileUrl = fileUrl;
				beneficiariesDate = date;
			}

		}

		// download the files
		downloadXlsFile(beneficiariesFileUrl, beneficiariesDate, pathPartners);
		downloadXlsFile(projectFileUrl, projectDate, pathProjects);

		// parse the files
		Map<String, Project> projectsMetadata = XlsParser.parseProjects(pathProjects, pathPartners);

		// parse the form field to find all the category name
		FormElement form = (FormElement) projects.selectFirst("form[name=\"search\"]");
		Element them = form.selectFirst("select");
		Elements options = them.getElementsByTag("option");
		List<Category> categories = new LinkedList<Category>();

		for (Element option : options) {

			String categoryValue = option.attr("value");

			if (!categoryValue.equalsIgnoreCase("0")) {

				String categoryName = option.text().trim();

				Category cat = new Category(categoryName, categoryValue);
				categories.add(cat);
			}
		}

		for (Category cat : categories) {
			// PRENDO SOLO LA CATEGORIA SOCIAL AND CREATIVE!
			// if (cat.getCollection().toLowerCase().equals("blue growth")) {
			// for each category download the file and the metadata
			System.out.println("######" + cat.getCollection().toUpperCase() + "######");
			// cat.parseCategory(projectsMetadata);
			// cat.writeMetadata();
			Category newcat = cat.readMetadata();// Inutle se il caricamento del db avviene contestualmente al
													// crawler.
			if (newcat != null) {

				try {
					connessioneSql(newcat);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			// }
		}
	}

	/**
	 * Downloads the xls file from the fileUrl. If there is already a file in the
	 * path, download only if the new file date is after the date of last
	 * modification of the file already downloaded.
	 *
	 * @param fileUrl the ourl from which download the file
	 * @param date    the date of last modification of the new file
	 * @param path    the path to download the file
	 */
	private static void downloadXlsFile(String fileUrl, Date date, String path) {

		File filePartners = new File(path);
		long beneficiariesOldDateL = filePartners.lastModified();

		// if the file doesn't exist (lastmodified == 0L) the date is null
		Date beneficiariesOldDate = (beneficiariesOldDateL == 0L) ? null : new Date(beneficiariesOldDateL);

		// download only the url is not null and the old file is not up to date
		if (fileUrl != null && beneficiariesOldDate != null && date.after(beneficiariesOldDate)) {

			URL urlFPartners = null;
			try {
				urlFPartners = new URL(fileUrl);
			} catch (MalformedURLException e) {

			}

			if (urlFPartners != null) {
				try {
					// download the file
					FileUtils.copyURLToFile(urlFPartners, filePartners, 30000, 30000);
				} catch (IOException e) {

					System.out.println("error downloading partners file");
				}
			}

		}
	}

}
