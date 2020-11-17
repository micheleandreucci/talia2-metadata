package crawler_interface;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.jsoup.select.Elements;
import org.quartz.CronScheduleBuilder;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

import com.github.junrar.exception.RarException;

public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;

	static void connessioneSql(Category cat, HttpServletResponse response) throws SQLException, IOException {
		PrintWriter pw = response.getWriter();
		response.setContentType("text/html");
		Connection cn;
		Statement st;
//		ResultSet rs;
		String sql;
		String populatePartners = "";
		String populateProjects = "";
		String populateDeliverables = "";
		String populateTargets = "";
//		HashSet totalprojects = new HashSet();
		// connessione
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			System.out.println("ClassNotFoundException: ");
			System.err.print(e.getMessage());
		} // fine try-catch

		// Creo la connessione al database
		cn = DriverManager.getConnection(
				"jdbc:mysql://localhost:3301/talia2?useUnicode=yes&characterEncoding=UTF-8&allowPublicKeyRetrieval=true&useSSL=false&user=talia2&password=root");
		// json è il nome del database
		sql = "INSERT ignore INTO communities VALUES('" + cat.getCollection() + "');";
		st = (Statement) cn.createStatement();
		try {
			st.executeUpdate(sql);
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
						String query = "ALTER TABLE PROJECTS AUTO_INCREMENT = 1;";
						st.executeUpdate(query);
						populateProjects = "insert ignore into Projects values (NULL, " + proj.getAxis() + ", "
								+ proj.getObjective() + ", '" + proj.getAcronym().replace("'", "") + "', '"
								+ proj.getLabel().replace("'", "") + "', '" + proj.getSummary().replace("'", "")
								+ "', '" + proj.getCall().replace("'", "") + "',' " + sqlstart + "', '" + sqlend
								+ "', '" + proj.getType() + "', " + proj.getErdf() + ", " + proj.getIpa() + ", "
								+ proj.getAmount() + ", " + proj.getCofinancing() + ", '" + proj.getStatus() + "', '"
								+ proj.getDeliverablesUrl() + "', '" + cat.getCollection() + "');";
						st.executeUpdate(populateProjects);

					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					// aggiunta dati relativi ai partners

					for (Partner partner : proj.getPartners()) {

						String checkDuplicatePartners = "select partner_name from partners where partner_name = '"
								+ partner.getName().replace("'", "") + "';";

						if (st.executeQuery(checkDuplicatePartners).first() == false) {
							byte[] nuts3 = partner.getNuts3().replace("'", "").getBytes("UTF-8");
							String nuts3ciao = new String(nuts3, "UTF-8");
							String query = "ALTER TABLE PARTNERS AUTO_INCREMENT = 1;";
							st.executeUpdate(query);
							populatePartners = "insert ignore into Partners values (NULL," + partner.isLP() + ", '"
									+ partner.getName().replace("'", "") + "', '" + partner.getNature().replace("'", "")
									+ "', '" + partner.getCountry().replace("'", "") + "', '" + partner.getPostalCode()
									+ "', '" + partner.getArea().replace("'", "") + "', '" + nuts3ciao + "', "
									+ partner.getErdf() + ", " + partner.getErdfContribution() + ", " + partner.getIpa()
									+ ", " + partner.getIpaContribution() + ", " + partner.getAmount() + ");";
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

						String addProjectPartner = "insert ignore into projectPartners values (" + projectId + ", "
								+ partnerId + ");";
						st.executeUpdate(addProjectPartner);
					}

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
					int project_id = 0;
					Partner partner = new Partner();
					ResultSet project_result = st.executeQuery(
							"select project_id from projects where project_acronym='" + proj.getAcronym() + "';");
					if (project_result.next()) {
						project_id = project_result.getInt(1);
					}
					int partner_id = 0;
					ResultSet partner_result = st.executeQuery(
							"select partner_name from partners where partner_name='" + partner.getName() + "';");
					if (partner_result.next()) {
						partner_id = partner_result.getInt(1);
					}
					String title = deliv.getTitle();
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

					String query = "ALTER TABLE DELIVERABLES AUTO_INCREMENT = 1;";
					st.executeUpdate(query);
					populateDeliverables = "insert ignore into Deliverables values (NULL, '" + deliv.getUrl() + "', '"
							+ title + "', " + delivdate + ", '" + description + "', '" + type + "',0, " + project_id
							+ ");";
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

	private static void downloadXlsFile(String fileUrl, Date date, String path, HttpServletResponse response)
			throws ParseException, IOException {

		PrintWriter out = response.getWriter();
		response.setContentType("text/html");
		File file = new File(path);
		URL urlFPartners = null;
		try {
			urlFPartners = new URL(fileUrl);
			if (!file.exists()) {
				FileUtils.copyURLToFile(urlFPartners, file, 30000, 30000);
				out.print("File " + file.toString() + "\n created ");
			} else {
				Date creationDate = new Date(0);
				BasicFileAttributes attributes = null;

				attributes = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
				creationDate = new Date(attributes.creationTime().to(TimeUnit.MILLISECONDS));

				if (date.after(creationDate)) {

					try {
						// download the file if is a update file
						FileUtils.copyURLToFile(urlFPartners, file, 30000, 30000);
					} catch (IOException e) {

						System.out.println("error downloading file");
					}
				}
			}

		} catch (MalformedURLException e) {
			e.printStackTrace();

		} catch (IOException d) {
			d.printStackTrace();
		}

	}

	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		PrintWriter out = res.getWriter();
		res.setContentType("text/html");
//		JobDetail j = JobBuilder.newJob(Login.class).build();
//		String hour = req.getParameter("hour");
//		String minute = req.getParameter("minute");
//		Trigger t = TriggerBuilder.newTrigger().withIdentity("crawler")
//				.withSchedule(CronScheduleBuilder.cronSchedule("0" + minute + hour + "? * * *")).build();
//		Scheduler s;
//		try {
//			s = StdSchedulerFactory.getDefaultScheduler();
//			s.start();
//			s.scheduleJob(j, t);
//		} catch (SchedulerException ex) {
//			Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
//		} finally {
//			out.close();
//		}
		out.println("<div align=\"center\">\r\n"
				+ "<dialog style=\"position:relative;background: rgba(255,255,255,0);border:0\" id=\"myDialog\"><img src=\"images/loading.gif\"></dialog>\r\n"
				+ "</div>");
		Document projects = null;
		try {

			projects = Jsoup.connect("https://interreg-med.eu/projects-results/our-projects").timeout(0).get();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Elements downloadLinks = projects.select("a[href*=/Projects_results/]");

		String projectFileUrl = null;
		String beneficiariesFileUrl = null;
		String pathProjects = "liste_projets_english_2020.02.18.xlsx";
		Date projectDate = null;
		String pathPartners = "liste_beneficiaires_english_2020.02.18.xlsx";
		Date beneficiariesDate = null;

		for (Element l : downloadLinks) {

			String fileUrl = l.absUrl("href");

			String urlName = l.text();

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

		try {
			downloadXlsFile(beneficiariesFileUrl, beneficiariesDate, pathPartners, res);
			downloadXlsFile(projectFileUrl, projectDate, pathProjects, res);
		} catch (ParseException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		// parse the files
		Map<String, Project> projectsMetadata = XlsParser.parseProjects(pathProjects, pathPartners, res);
		// parse the form field to find all the category name
		FormElement form = (FormElement) projects.selectFirst("form[name=\"search\"]");
		Element them = form.selectFirst("select");
		Elements options = them.getElementsByTag("option");
		List<Category> categories = new LinkedList<Category>();

		for (Element option : options) {
			// seleziona dal form di ricerca, in tematics, le categorie e prende il valore
			String categoryValue = option.attr("value");

			if (!categoryValue.equalsIgnoreCase("0")) {

				String categoryName = option.text().trim();

				Category cat = new Category(categoryName, categoryValue);
				categories.add(cat);
			}
		}

		// for each category download the file and the metadata
		for (Category cat : categories) {
			// PRENDO SOLO LA CATEGORIA SOCIAL AND CREATIVE!
//			if (cat.getCollection().toLowerCase().equals("social and creative")) {

			System.out.println("######" + cat.getCollection().toUpperCase() + "######");
			try {
				cat.parseCategory(projectsMetadata, res);
			} catch (ParseException | RarException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			cat.writeMetadata();
			Category newcat = cat.readMetadata();// Inutle se il caricamento del db avviene contestualmente al
													// crawler.

			if (newcat != null) {

				try {
					connessioneSql(newcat, res);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}
	}

	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		doPost(req, res);
	}
//
//	@Override
//	public void execute(JobExecutionContext arg0) throws JobExecutionException {
//		System.out.println("Server Time" + new Date());
//
//	}
}
