import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;



public class QueryHelper implements QueryHelperInterface{
	/** Tab character used for pretty JSON output. */
	public final static char TAB = '\t';

	public final static char Q = '\"';

	/** End of line character used for pretty JSON output. */
	public final static char END = '\n';

	private final TreeMap<String, List<SearchObject>> query;

	private final InvertedIndex index;

	public QueryHelper(InvertedIndex index) {
		this.query = new TreeMap<>();
		this.index = index;
	}

	/**
	 * calls parseLines and fills our query TreeMap
	 * 
	 * @param path
	 * @param search
	 */
	public void parseQuery(Path path, boolean searchType) {
		try (BufferedReader reader = Files.newBufferedReader(path)) {
			String line;
			while ((line = reader.readLine()) != null) {
				parseLines(line, searchType);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public ArrayList<SearchObject> parseQuery(String line){
		line = cleanLine(line);
		String[] split = line.trim().split("\\s+");
		Arrays.sort(split);
		ArrayList<SearchObject> values = index.partialSearch(split);
		return values;
	}

	/**
	 * takes in a line and parses it then calls the desired search method
	 * 
	 * @param line
	 * @param search
	 */
	public void parseLines(String line, boolean searchType) {
		line = cleanLine(line);
		String[] split = line.trim().split("\\s+");
		Arrays.sort(split);

		String sortedLine = String.join(" ", split);

		if (searchType == false) {
			query.put(sortedLine, index.partialSearch(split));
		}
		if (searchType == true) {
			query.put(sortedLine, index.exactSearch(split));
		}
	}

	/**
	 * TODO
	 * 
	 * @param line
	 * @return
	 */
	public static String cleanLine(String line) {
		return line.toLowerCase().replaceAll("\\p{Punct}+", "").trim();
	}

	public static String tab(int n) {
		char[] tabs = new char[n];
		Arrays.fill(tabs, TAB);
		return String.valueOf(tabs);
	}

	/**
	 * JSON writer for search objects
	 * 
	 * @param path
	 * @param index
	 * @return
	 */
	public static boolean writeQuery(Path path, TreeMap<String, List<SearchObject>> index) {
		try (BufferedWriter writer = Files.newBufferedWriter(path);) {
			writer.write("{" + END);
			int count1 = 0;
			int count2 = 0;
			if (!index.isEmpty()) {
				for (String key : index.keySet()) {
					if (count1 == 0) {
						writer.write(tab(1) + Q + key + Q + ": [");
						count1++;
					} else {
						writer.write("," + END + tab(1) + Q + key + Q + ": [");

					}
					for (SearchObject element : index.get(key)) {
						if (count2 == 0) {
							writer.write(END + tab(2) + "{" + END + tab(3) + Q + "where" + Q + ": " + Q
									+ element.getPath() + Q + "," + END + tab(3) + Q + "count" + Q + ": "
									+ element.getFrequency() + "," + END + tab(3) + Q + "index" + Q + ": "
									+ element.getPosition() + END);
							count2++;
						} else {
							writer.write("," + END + tab(2) + "{" + END + tab(3) + Q + "where" + Q + ": " + Q
									+ element.getPath() + Q + "," + END + tab(3) + Q + "count" + Q + ": "
									+ element.getFrequency() + "," + END + tab(3) + Q + "index" + Q + ": "
									+ element.getPosition() + END);
						}
						writer.write(tab(2) + "}");
					}
					count2 = 0;
					writer.write(END + tab(1) + "]");
				}
				count1 = 0;
				writer.write(END);
			}
			writer.write("}");
			return true;

		} catch (IOException e) {
			System.err.println("Caught IOException: " + path.toString());
			return false;
		}
	}

	/**
	 * write query helper function
	 * 
	 * @param path
	 */
	public void writeOut(Path path) {
		QueryHelper.writeQuery(path, query);
	}

}
