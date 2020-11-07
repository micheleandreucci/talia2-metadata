package deliveryCrawler.crawler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import java.nio.file.Files;

import java.nio.file.attribute.BasicFileAttributes;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HttpsURLConnection;


import org.apache.commons.io.FileUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.junrar.Archive;
import com.github.junrar.Junrar;

import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.FileHeader;


@JsonIgnoreProperties(value = { "path", "number", "lastMetadataFile", "metadataPath", "formats" })
public class Category {
	/**
	 * the collection name
	 */
	private String collection;
	/**
	 * the path of the folder to save the files and the metadata
	 */
	private String path = "";
	/**
	 * the number of the category to send to the server in the http request
	 */
	private String number;
	/**
	 * the metadata of all documents
	 */
	private List<Resource> documents = new LinkedList<Resource>();
	/**
	 * the date of last modified of the metadata file if it exists
	 */
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private Date lastMetadataFile;

	/**
	 * the path of the metadata
	 */
	private String metadataPath;

	public String[] getFormats() {
		return formats;
	}

	public void setFormats(String[] formats) {
		this.formats = formats;
	}

	private String[] formats = { "zip", "pdf", "rar" };

	public Category() {
		// DEFAULT CONSTRUCTOR
	}

	public Category(String name, String number) {

		this.collection = name;
		this.number = number;
		this.path = name.trim();
		this.metadataPath = path + "/" + name + ".json";
		this.lastMetadataFile = metadataLastModified();
	}

	public String getCollection() {
		return collection;
	}

	public void setCollection(String collection) {
		this.collection = collection;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public List<Resource> getDocuments() {
		return documents;
	}

	public void setDocuments(List<Resource> documents) {
		this.documents = documents;
	}

	public Date getLastMetadataFile() {
		return lastMetadataFile;
	}

	public void setLastMetadataFile(Date lastMetadataFile) {
		this.lastMetadataFile = lastMetadataFile;
	}

	public String getMetadataPath() {
		return metadataPath;
	}

	public void setMetadataPath(String metadataPath) {
		this.metadataPath = metadataPath;
	}

	/**
	 * Scrape from the web the resources metadata and download all the pdf file of
	 * the deliveries
	 * 
	 * @param projectsMetadata
	 * @throws ParseException
	 */
	public void parseCategory(Map<String, Project> projectsMetadata) throws ParseException {

		// connect to category projets main page
		Document doc = null;

		try {

			doc = Jsoup.connect("https://interreg-med.eu/projects-results/our-projects/search-projects/").timeout(20000)
					.data("tx_eannuaires_pi1[search][categoryProject]", this.number).post();

		} catch (IOException e) {

			System.out.println("Category error: " + e);
			return;
		}

		// Extract paginator element in the main page
		Elements paginatorClass = doc.getElementsByClass("f3-widget-paginator");
		Element paginator = paginatorClass.get(0);

		// calculate the number of page (excluding the next element)
		int nPage = paginator.children().size() - 1;

		// for each page
		int i = 0;
		String pageUrl = null;
		do {

			// if is the first page use the previous connection
			if (i != 0) {

				pageUrl = paginator.child(i).child(0).absUrl("href");

				try {
					doc = Jsoup.connect(pageUrl).timeout(20000).get();
				} catch (IOException e) {

					System.out.println("Category page error: " + e);
				}
			}

			// extract element containing the project list
			Elements projectsTag = doc.select(".annuaire.row-projects");

			// for each project element
			for (Element tag : projectsTag) {

				// extract content
				Elements blocContent = tag.getElementsByClass("project-content");
				Element content = blocContent.get(0);

				// extract the two column describing the project
				Elements bloc50s = content.getElementsByClass("bloc50");

				// extract data from 1st column
				Element bloc50_1 = bloc50s.get(0);

				// name
				String name = bloc50_1.getElementsByTag("h3").text().trim();
				name = name.toLowerCase();
				System.out.println(name);

				String acronym = name.replaceAll("\\s+", "");

				// get project data from the map
				Project pr = projectsMetadata.get(acronym);

				if (pr == null) {

					System.out.println("Project not found in xls file");
					pr = new Project();
					pr.setAcronym(name);
				}

				// status
				String statusStr = bloc50_1.getElementsByClass("status").get(0).text();
				// remove "Status:" from the string
				String status = statusStr.split(":")[1].trim();

//				Element bloc50_2 = bloc50s.get(1);
//				String category = bloc50_2.getElementsByClass("category")
//						.get(0).text();
//
//				String typeStr = bloc50_2.getElementsByClass("address").get(0)
//						.text();

//				 // remove "Type:" from the string
//				 String type = typeStr.split(":")[1];
//				
//				 String descriptionStr = content.getElementsByClass(
//				 "description").text();
//				 // remove "Description:" from the string
//				 String description = descriptionStr.split(":")[1];

				// extract buttons element to retrieve deliverables database url
				Elements blocButton = tag.getElementsByClass("project-buttons");

				if (blocButton.size() == 0) {

					// in case of no button
					System.out.println(name + " no buttons");
					break;

				} else {

					Elements buttons = blocButton.get(0).children();

					for (Element b : buttons) {
						System.out.println("Button: " + b.text() + "\n\n");
						// if there is retrieve deliverables url from corresponding button
						if (b.text().trim().equalsIgnoreCase("deliverables")) {

							String deliverablesUrl = b.absUrl("href");

							pr.setDeliverablesUrl(deliverablesUrl);
							pr.setStatus(status);
							parseDeliverables(pr);

						}
					}

				}
			}

			i++;

		} while (i < nPage);
	}

	/**
	 * Retrieve metadata and resources of all delivery listed in the deliverables
	 * url of the project
	 * 
	 * @param pr a project with delivarables url
	 * @throws ParseException
	 */
	private void parseDeliverables(Project pr) throws ParseException {

		Document doc = null;

		// stop if there is not deliverables url
		if (pr.getDeliverablesUrl() == null) {

			System.out.print("The project: " + pr.getAcronym() + " has no delivery");
			return;
		}

		// connect to the deliverables page
		int t = 0;
		int nTry = 3;
		while (doc == null & t < nTry) {

			try {
				doc = Jsoup.connect(pr.getDeliverablesUrl()).timeout(50000).get();
			} catch (IOException e) {

				System.out.println((t + 1) + " attempt - " + " 1st deliverables page error: " + e);
				t++;
			}

		}

		// stop if can't extract the doc
		if (doc == null) {

			return;
		}

		// extract paginator element
		Elements paginatorClass = doc.getElementsByClass("f3-widget-paginator");

		int nPage;

		Element paginator = null;

		// in case of no paginator read only first page
		if (paginatorClass.size() == 0) {
			nPage = 1;
		} else {

			paginator = paginatorClass.get(0);
			nPage = paginator.children().size() - 1;
		}

		int i = 0;
		do {

			// if not the current page get the new page
			if (i != 0 && paginator != null) {

				t = 0;
				nTry = 3;
				doc = null;
				while (doc == null & t < nTry) {

					String deliverablesUrl = paginator.child(i).child(0).absUrl("href");
					try {
						doc = Jsoup.connect(deliverablesUrl).timeout(50000).get();
					} catch (IOException e) {

						System.out.println((t + 1) + " attempt - " + " deliverables page error: " + e);
						t++;
					}
				}
			}

			// extract the list of deliveries elements
			Element delDatabase = doc.selectFirst(".tx_elibrary.grid333333");

			// if there isn't a list of deliveries
			if (delDatabase == null) {

				// extract the list of resources
				Element listUploads = doc.selectFirst(".ce-uploads");

				// if there is a list of resources
				if (listUploads == null) {

					System.out.println("No delivery found");
				}

			} else {

				// extract the delivery description
				Elements deliveriesDes = delDatabase.select("li > a");

				for (Element del : deliveriesDes) {

					// extract delivery detail page url
					String delUrl = del.absUrl("href");

					// create a delivery with the url
					Delivery d = new Delivery(delUrl);
					d.setProgetto(pr);
					// parse the delivery page
					parseDelivery(d);
				}

			}

			i++;

		} while (i < nPage);
	}

	/**
	 * Download all resources of a delivery with their metadata
	 * 
	 * @param d a delivery with its url
	 * @throws ParseException
	 */
	private void parseDelivery(Delivery d) throws ParseException {

		Document doc = null;
		String url = d.getUrl();

		// stop in case of no url
		if (url == null) {
			return;
		}

		// try to connect to delivery page
		int p = 0;
		int nTry = 3;
		while (doc == null & p < nTry) {

			try {
				doc = Jsoup.connect(d.getUrl()).timeout(50000).get();
			} catch (IOException e) {

				System.out.println((p + 1) + " attempt - " + " delivery page error: " + e);
				p++;
			}
		}

		// extract title
		Elements titleBloc = doc.getElementsByClass("bloc-title");
		String title = titleBloc.get(0).text();
		d.setTitle(title);

		// extract the 2 element describing the delivery
		Element mainDetail = doc.select(".main-livrables-detail.bloc66").get(0);
		Element asideDetail = doc.select(".aside-livrables-detail.bloc33").get(0);

		// type
		String type = mainDetail.getElementsByClass("livrables-category").get(0).text();
		d.setType(type);

		// date
		Element dateTag = mainDetail.selectFirst(".date");

		Date date = null;

		if (dateTag != null) {

			String dateStr = dateTag.text();

			// parse the date
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

			try {

				date = formatter.parse(dateStr);
			} catch (ParseException e) {

				e.printStackTrace();
			}
		}

		d.setDate(date);

		// description
		String description = mainDetail.getElementsByClass("description").get(0).text();
		d.setDescription(description);

		// extract each keyword
		Elements keywordsEl = asideDetail.getElementsByClass("keywords");
		if (keywordsEl.size() != 0) {

			Elements keywordsElList = asideDetail.getElementsByClass("keywords").get(0).children();

			List<String> keywords = new LinkedList<String>();
			for (Element k : keywordsElList) {

				// split keywords if there is a , or and
				String strK = k.text();
				String[] keys = strK.split("\\,|and");
				for (String w : keys) {
					keywords.add(w.trim().toLowerCase());
				}
			}

			d.setKeywords(keywords);
		}

		// extract each target
		Elements targetsEl = asideDetail.getElementsByClass("target-audience");

		if (targetsEl.size() != 0) {
			List<String> targets = new LinkedList<String>();
			Elements targetsElList = asideDetail.getElementsByClass("target-audience").get(0).children();
			for (Element t : targetsElList) {

				String strT = t.text();
				targets.add(strT);
			}

			d.setTargets(targets);
		}

		Elements listFiles = mainDetail.getElementsByClass("livrables-list-files").get(0).children();
		for (Element fileEl : listFiles) {
			System.out.println(fileEl.child(0).text() + "[");
			System.out.println("]");

			// only in case of document resource
			if (d.getType().equalsIgnoreCase("document")) {

				// create a resource with metadata
				String fUrl = fileEl.child(0).absUrl("href");

				System.out.println("Url: " + fUrl + "\n");
				// download the resource
				List<String> fileNames = downloadResource(fUrl, path, 3, this.formats, d.getDate());

				// create resource object using downloaded file name
				for (String f : fileNames) {

					Resource r = new Resource(f);
					r.setDelivery(d);
					documents.add(r);
				}
			}
		}
	}

	/**
	 * Write all the metadata downloaded in a json file
	 */
	public void writeMetadata() {

		File folder = new File(path);
		if (!folder.exists())
			folder.mkdir();
		File json = new File(path + "/" + collection + ".json");
		ObjectMapper mapper = new ObjectMapper();
		try {
			if (!json.exists())
				mapper.writeValue(json, this);
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) { // TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * testing deserialization on json files
	 */
	public Category readMetadata() {
		ObjectMapper mapper = new ObjectMapper();

		try {
			return mapper.readValue(new File(path + "/" + collection + ".json"), Category.class);
			// System.out.println(catnew.getDocuments().get(0).getName());
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) { // TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.err.println("file non trovato");
		}
		return null;
	}

	/**
	 * Retrieve the date of last modify of the metadata file if there is
	 * 
	 * @return the last modify date
	 */
	public Date metadataLastModified() {

		File file = new File(metadataPath);

		Date lastMetadaFile = null;
		if (file.exists()) {

			lastMetadaFile = new Date(file.lastModified());

		} else {

			lastMetadaFile = null;
		}

		return lastMetadaFile;
	}

//		int nameIndex = urlStr.lastIndexOf("/");
//		this.name = urlStr.substring(nameIndex + 1, urlStr.length());
//

//	}

	/**
	 * Download the resource at the urlStr only if is a new file or if it is a new
	 * version of it. All file different from pdf or zip are not downloaded. If a
	 * connection error occurs, it retry the download up to ntry times. If the file
	 * is a zip, only the pdf files within it will be saved. If the file already
	 * exist and is not updated return anyway the name of it.
	 * 
	 * @param urlStr       the url from which download the file
	 * @param outputFolder the folder where download the file
	 * @param nTry         the number of trials downloading the file
	 * @return the name of the downloaded files
	 * @throws ParseException
	 */
	private List<String> downloadResource(String urlStr, String outputFolder, int nTry, String[] allowedFormats,
			Date date) throws ParseException {

		// extract name and extention of file
		String name = getFileName(urlStr);
		String ext = getFileExtention(name);

		URL url = null;

		List<String> fileNames = new LinkedList<String>();

		boolean allowedFormat = isAllowedFormat(ext, allowedFormats);

		// only if is pdf or zip
		if (allowedFormat) {

			// connect to url
			try {
				url = new URL(urlStr);
				System.out.println("url: " + urlStr + " ok \n");
			} catch (MalformedURLException e) {

				System.out.println("url: " + urlStr + " error");
			}

			HttpsURLConnection con = null;
			try {

				// extract the last modified of http response header
				con = (HttpsURLConnection) url.openConnection();

			} catch (IOException e1) {

				System.out.print("connection to " + name + " error");
			} finally {

				if (con != null) {

					con.disconnect();
				}
			}
			String directory = outputFolder + "/docs/";
			String path = directory + name;
			File file = new File(path);
			BasicFileAttributes attributes = null;

			Date creationDate = new Date(0); // inizializzo 1 Gen 1970
			try {
				if (file.exists()) {
					attributes = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
					creationDate = new Date(attributes.creationTime().to(TimeUnit.MILLISECONDS));

				}
			} catch (IOException e) {
				e.printStackTrace();
			} 

			if (date == null) {
				date = new Date(con.getLastModified());
				/*
				 * per la prima volta che faccio partire il Crawler, se il file che sto
				 * scaricando non presenta la data sul sito, assegno al file la data di ultima
				 * modifica
				 */
			}

			/*
			 * Se il file esiste in locale, creationDate assume il valore del giorno in cui
			 * è stato scaricato in locale il file
			 */
			if (date.after(creationDate)) {

				int i = 0;
				boolean downloaded = false;

				while (i < nTry && !downloaded) {

					try {
						File folder = null;
						if (path.contains(".zip")) {
							 folder = new File(path.replace(".zip", ""));
							 if (!folder.isDirectory()) {
								// download file
									
									FileUtils.copyURLToFile(url, file, 30000, 30000);
									
									// get the list of zip files
									fileNames = unzip(path, directory);
									System.out.println(name + " - downloded at " + (i + 1) + " attempt");
									boolean deleted = file.delete();

									if (deleted) {

										System.out.println("zip deleted");
									} else {

										System.out.println("error deleting zip");
									}
							 }
						} else if (path.contains(".rar")) {
							 folder = new File(path.replace(".rar", ""));
							 if (!folder.isDirectory()) {
								 FileUtils.copyURLToFile(url, file, 30000, 30000);
								 System.out.println(name + " - downloded at " + (i + 1) + " attempt");
								 fileNames = extract(path,directory);
								 boolean deleted = file.delete();

									if (deleted) {

										System.out.println("rar deleted");
									} else {

										System.out.println("error deleting rar");
									}
							 }
						
						} else if (ext.equalsIgnoreCase("pdf")) {
							// download it
							FileUtils.copyURLToFile(url, file, 30000, 30000);
							System.out.println(name + " - downloded at " + (i + 1) + " attempt");
							fileNames.add(name);
						}

						// FileUtils.copyURLToFile(url, file, 30000, 30000);
						downloaded = true;

					} catch (IOException e) {

						i++;
						System.out.println(name + " " + i + " download error " + e);
					}

				}

			} else if (ext.equalsIgnoreCase("zip")) {

				String nameNoExt = name.replace(".zip", "");
				File folder = new File(path + "/docs/" + nameNoExt);
				if (folder.exists()) {
					File[] listOfFiles = folder.listFiles();

					for (File f : listOfFiles) {

						fileNames.add(nameNoExt + "/" + f.getName());
					}
				}

			} else {

				File fil = new File(path + "/docs/" + name);
				if (fil.exists()) {

					fileNames.add(name);
				}
			}

		}

		return fileNames;

	}

	private static String getFileExtention(String name) {

		String[] namePoints = name.split("\\.");
		String ext = namePoints[namePoints.length - 1];
		return ext;
	}

	private static String getFileName(String urlStr) {

		int nameIndex = urlStr.lastIndexOf("/");
		String name = urlStr.substring(nameIndex + 1, urlStr.length());
		return name;
	}

	/**
	 * Unzip the files in the zip file in fileZipPath and save it in a new folder
	 * (with the same name of the zip file name) in destDisrPath.
	 * 
	 * @param fileZipPath the path of the zip file
	 * @param destDirPath the folder where unzip the file
	 * @return a list of names of file extracted
	 * @throws IOException
	 * @throws IllegalArgumentException
	 */
	private static List<String> unzip(String fileZipPath, String destDirPath) {
		List<String> fileNames = new LinkedList<String>();
		
		try {
			ZipFile zipFile = new ZipFile(fileZipPath);
			String zipName = getFileName(fileZipPath);
			String zipNameNoExt = zipName.replaceAll("\\.zip", "");
			File folder = new File(destDirPath);
			List listHeader = zipFile.getFileHeaders();
			
			if (!(zipNameNoExt.contains(folder.getName()))) {

				// crea una cartella con lo stesso nome dello zip
				File destDir = new File(destDirPath + "/" + zipNameNoExt + "/");
				destDir.mkdirs();

				System.out.println("Unzip " + zipName);
				for (int i = 0; i < listHeader.size(); i++) {
					FileHeader fileHeader = (FileHeader) listHeader.get(i);
					String fileName = fileHeader.getFileName();
					if (fileName.contains(".pdf")) {
						zipFile.extractFile(fileHeader, destDir.getAbsolutePath());
						// System.out.println("la lista " +fileHeader.getFileName());
						fileNames.add(fileName);
					}
				}
			}

		} catch (ZipException e) {
			System.err.println(e);
		}

		return fileNames;

	}
	
	private static List<String> extract(String filePath, String destDirPath) {
		List<String> fileNames = new LinkedList<String>();
		String rarName = getFileName(filePath);
		File f = new File(filePath);
		
		//cartella della categoria
		File folder = new File(destDirPath);
		String rarNameNoExt = rarName.replaceAll("\\.rar", "");
        try (Archive rarFile = new Archive (f)) {
        	
        	if (!(rarNameNoExt.contains(folder.getName()))) {

				// crea una cartella con lo stesso nome del rar
				File destDir = new File(destDirPath + "/" + rarNameNoExt + "/");
				destDir.mkdirs();
        		com.github.junrar.rarfile.FileHeader listHeader = rarFile.nextFileHeader();
            	System.out.println("decompress " + rarName);
            	String fileName = "";
				while (listHeader != null) {
					fileName = listHeader.getFileName();
					Junrar.extract(filePath, destDir.getAbsolutePath());
                    fileNames.add(fileName);
                    listHeader = rarFile.nextFileHeader();		
						
				}
				rarFile.close();
				
				
			}            	
            
        }  catch (Exception e) {
            System.err.println("unPack zip file to " + destDirPath + " fail ...." + e.getMessage() + e);
        }
        
        return fileNames;
	}
	  

	private static boolean isAllowedFormat(String ext, String[] allowedFormats) {

		boolean isAllowedFormat = false;

		for (String f : allowedFormats) {

			if (ext.equalsIgnoreCase(f)) {

				isAllowedFormat = true;
				break;
			}
		}

		return isAllowedFormat;
	}

//	public static void main(String[] args) {
//
//		try {
//			List<String> names = unzip("Non_digital_press_articles-2nd_sem.zip", "");
//			for (String n : names) {
//				System.out.println(n);
//			}
//		} catch (IllegalArgumentException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}

}