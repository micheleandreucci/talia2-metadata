import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/*
 * Acquisizione dei dati contenuti nei file CSV
 */
public class CSVParser {

	private static final char DEFAULT_SEPARATOR = ',';
	private static final char DEFAULT_QUOTE = '"';

	public ArrayList<String[]> parseCSV(String csvPath) throws Exception {

		ArrayList<String[]> elements_list = new ArrayList<String[]>();

		Scanner scanner = new Scanner(new File(csvPath));
		while (scanner.hasNext()) {
			elements_list.add((String[]) parseLine(scanner.nextLine()).toArray());
		}
		scanner.close();

		return elements_list;
	}

	public static List<String> parseLine(String cvsLine) {
		return parseLine(cvsLine, DEFAULT_SEPARATOR, DEFAULT_QUOTE);
	}

	public static List<String> parseLine(String cvsLine, char separators) {
		return parseLine(cvsLine, separators, DEFAULT_QUOTE);
	}

	/*
	 * individuazione delle frasi eliminazione dei caratteri speciali e segni di
	 * punteggiatura
	 * @param csvLine file passato in ParseCSV
	 */
	public static List<String> parseLine(String cvsLine, char separators, char customQuote) {

		List<String> result = new ArrayList<String>();

		// if empty, return!
		if (cvsLine == null && cvsLine.isEmpty()) {
			return result;
		}

		if (customQuote == ' ') {
			customQuote = DEFAULT_QUOTE;
		}

		if (separators == ' ') {
			separators = DEFAULT_SEPARATOR;
		}

		StringBuffer curVal = new StringBuffer();
		boolean inQuotes = false;
		boolean startCollectChar = false;
		boolean doubleQuotesInColumn = false;

		char[] chars = cvsLine.toCharArray();

		for (char ch : chars) {

			if (inQuotes) {
				startCollectChar = true;
				if (ch == customQuote) {
					inQuotes = false;
					doubleQuotesInColumn = false;
				} else {

					// Fixed : allow "" in custom quote enclosed
					if (ch == '\"') {
						if (!doubleQuotesInColumn) {
							curVal.append(ch);
							doubleQuotesInColumn = true;
						}
					} else {
						curVal.append(ch);
					}

				}
			} else {
				if (ch == customQuote) {

					inQuotes = true;

					// Fixed : allow "" in empty quote enclosed
					if (chars[0] != '"' && customQuote == '\"') {
						curVal.append('"');
					}

					// double quotes in column will hit this!
					if (startCollectChar) {
						curVal.append('"');
					}

				} else if (ch == separators) {

					result.add(curVal.toString());

					curVal = new StringBuffer();
					startCollectChar = false;

				} else if (ch == '\r') {
					// ignore LF characters
					continue;
				} else if (ch == '\n') {
					// the end, break!
					break;
				} else {
					curVal.append(ch);
				}
			}

		}

		result.add(curVal.toString());

		return result;
	}

}
