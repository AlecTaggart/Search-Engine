import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;



public class ThreadedQueryHelper implements QueryHelperInterface{
	private final WorkQueue								workers;
	private final ThreadSafeInvertedIndex				index;
	private final TreeMap<String, List<SearchObject>>	query;

	
	public ThreadedQueryHelper(ThreadSafeInvertedIndex index, WorkQueue workers) {
		this.workers = workers;
		this.index = index;
		this.query = new TreeMap<String, List<SearchObject>>();
	}

	/**
	 * parses path with specified searchType
	 */
	@Override
	public void parseQuery(Path path, boolean searchType) {
		try (BufferedReader reader = Files.newBufferedReader(path)) {
			String line;
			while ((line = reader.readLine()) != null) {
				// parseLine(line, searchType);
				workers.execute(new Minion(line, searchType));
			}
		} catch (IOException e) {
			System.out.println("Failed to parseQuery.");
		}
		
		workers.finish();
	}

	/**
	 * takes in a line and parses it then calls the desired search method
	 * 
	 * @param line
	 * @param search
	 */
	private void parseLine(String line, boolean searchType) {
		line = QueryHelper.cleanLine(line);
		String[] split = line.trim().split("\\s+");
		Arrays.sort(split);

		String sortedLine = String.join(" ", split);
		ArrayList<SearchObject> results;
		if (searchType == false) {
			results = index.partialSearch(split);
		} else {
			results = index.exactSearch(split);
		}
		synchronized (query) {
			query.put(sortedLine, results);
		}
	}

	public void writeOut(Path path) {
		QueryHelper.writeQuery(path, query);
	}

	private class Minion implements Runnable {

		private final boolean	search;
		private final String	line;

		public Minion(String line, boolean search) {
			this.line = line;
			this.search = search;
		}

		@Override
		public void run() {
			parseLine(line, search);
		}

	}

}
