package crawler_interface;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
/*
 * XlsParser.java si occupa di leggere i dati dagli xls Files.
 */

public class XlsParser {

	/**
	 * Parses the xls project file in the projectFilePath and associate at each
	 * project the partners in the partnerFilePath.
	 * 
	 * @param projectFilePath  the local path of the projects xls file.
	 * @param partnersFilePath the local path of the partners xls file.
	 * @return
	 */
	public static Map<String, Project> parseProjects(String projectFilePath, String partnersFilePath, HttpServletResponse response) {

		Map<String, List<Partner>> partners = parsePartners(partnersFilePath);

		Map<String, Project> projects = new HashMap<String, Project>();

		File projectsFile = new File(projectFilePath);

		// open the xls file
		FileInputStream fis = null;
		XSSFWorkbook workbook = null;
		try {

			fis = new FileInputStream(projectsFile);
			workbook = new XSSFWorkbook(fis);
		} catch (IOException e) {

			System.out.println("Error opening projects file: " + e);
		}

		XSSFSheet sheet = workbook.getSheetAt(0);

		// iterate over row
		Iterator<Row> rowIt = sheet.iterator();

		int rowStart = 3;
		for (int i = 0; i < rowStart; i++) {

			rowIt.next();
		}

		while (rowIt.hasNext()) {

			Row row = rowIt.next();
			Project p = new Project();

			// axis
			Iterator<Cell> cellIt = row.cellIterator();
			Cell axisCell = cellIt.next();

			switch (axisCell.getCellType()) {
			case NUMERIC:
				p.setAxis((int) axisCell.getNumericCellValue());
				break;
			default:
				break;
			}

			// objective
			Cell objCell = cellIt.next();
			switch (objCell.getCellType()) {
			case NUMERIC:
				p.setObjective((int) objCell.getNumericCellValue());
				break;
			default:
				break;
			}
			// acronym
			Cell acronymCell = cellIt.next();
			String acronym = acronymCell.toString().replaceAll("\\s+", "");
			p.setAcronym(acronym);

			// label
			Cell labelCell = cellIt.next();
			p.setLabel(labelCell.toString().trim());

			// summary
			Cell sumCell = cellIt.next();
			p.setSummary(sumCell.toString().trim());

			// ignore cell
			cellIt.next();

			// ignore country
			cellIt.next();

			// ignore postalcode
			cellIt.next();

			// call
			Cell callCell = cellIt.next();
			p.setCall(callCell.toString().trim());

			// start date
			Date start = cellIt.next().getDateCellValue();
			p.setStart(start);

			// end date
			Date end = cellIt.next().getDateCellValue();
			p.setEnd(end);

			// type
			Cell typeCell = cellIt.next();
			p.setType(typeCell.toString().trim());

			// erdf
			double erdf = cellIt.next().getNumericCellValue();
			p.setErdf(erdf);

			// ipa
			double ipa = cellIt.next().getNumericCellValue();
			p.setIpa(ipa);

			// amount
			double amount = cellIt.next().getNumericCellValue();
			p.setAmount(amount);

			// cofinancing
			Cell cofCell = cellIt.next();
			p.setCofinancing(cofCell.toString().trim());

			// retrieve list of partners from map
			List<Partner> pPartners = partners.get(acronym);
			// associate it to partner object
			p.setPartners(pPartners);

			acronym = acronym.toLowerCase();

			// populate the map
			projects.put(acronym, p);

		}

		try {
			workbook.close();
			fis.close();
		} catch (IOException e) {

			System.out.print("Error closing stream");
		}

		return projects;
	}

	/**
	 * Parse the xls file of the partners and create a map containing all the
	 * partners data for each project.
	 * 
	 * @param filePath
	 * @return A dictionary mapping the project name with the list of all its
	 *         partners
	 */
	public static Map<String, List<Partner>> parsePartners(String filePath) {

		File partnersFile = new File(filePath);

		FileInputStream fis = null;
		XSSFWorkbook workbook = null;
		Map<String, List<Partner>> partners = new HashMap<String, List<Partner>>();

		try {
			fis = new FileInputStream(partnersFile);

			workbook = new XSSFWorkbook(fis);

		} catch (IOException e) {

			System.out.println("Error opening partners file: " + e);
		}

		XSSFSheet sheet = workbook.getSheetAt(0);

		Iterator<Row> rowIt = sheet.iterator();

		int rowStart = 3;
		for (int i = 0; i < rowStart; i++) {

			rowIt.next();
		}

		while (rowIt.hasNext()) {
			Row row = rowIt.next();
			Partner p = new Partner();

			Iterator<Cell> cellIt = row.cellIterator();

			// ignore axis
			cellIt.next();

			// ignore objective
			cellIt.next();

			// acronym
			Cell acrCell = cellIt.next();
			String project = acrCell.toString().replaceAll("\\s+", "");
			
			// Lead Partner
			String isLPStr = cellIt.next().toString().trim();
			boolean isLp = isLPStr.equals("1.0");
			p.setLP(isLp);

			// name
			Cell nameCell = cellIt.next();
			String name = nameCell.toString().trim();
			p.setName(name);

			// nature
			Cell natureCell = cellIt.next();
			String nature = natureCell.toString().trim();
			p.setNature(nature);

			// country
			Cell countryCell = cellIt.next();
			p.setCountry(countryCell.toString().trim());

			// area
			Cell areaCell = cellIt.next();
			p.setArea(areaCell.toString().trim());

			// NUTS3
			Cell nuts3Cell = cellIt.next();
			p.setNuts3(nuts3Cell.toString().trim());

			//postal code
			String zip = cellIt.next().toString().trim();
			zip = zip.replaceFirst("\\.0", "");
			p.setPostalCode(zip);

			// erdf
			double erdf = cellIt.next().getNumericCellValue();
			p.setErdf(erdf);

			// erdf contribution
			double erdfContr = cellIt.next().getNumericCellValue();
			p.setErdfContribution(erdfContr);

			// ipa
			double ipa = cellIt.next().getNumericCellValue();
			p.setIpa(ipa);

			// ipa contribution
			double ipaContr = cellIt.next().getNumericCellValue();
			p.setIpaContribution(ipaContr);

			// amount
			double amount = cellIt.next().getNumericCellValue();
			p.setAmount(amount);

			// retrieve partner list if already created
			List<Partner> ps = partners.get(project);

			// if there is no list, create it
			if (ps == null) {

				ps = new LinkedList<Partner>();
			}

			// add the partner at the list
			ps.add(p);

			// insert the partner list in the map, if there is replace it
			partners.put(project, ps);
		}

		try {
			workbook.close();
			fis.close();
		} catch (IOException e) {

			System.out.print("Error closing stream");
		}

		return partners;
	}

}
