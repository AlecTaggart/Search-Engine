import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.TreeSet;

public class InvertedIndex {

	/** Tab character used for pretty JSON output. */
	public final static char TAB = '\t';

	/** End of line character used for pretty JSON output. */
	public final static char END = '\n';

	private final TreeMap<String, TreeMap<String, TreeSet<Integer>>> index;

	public InvertedIndex() {
		index = new TreeMap<>();
	}

	/**
	 * adds word to data structure with path and position
	 * 
	 * @param word
	 *            word in text
	 * @param path
	 *            path of file
	 * @param position
	 *            positions of word in text
	 */
	public void add(String word, String path, int position) {
		if (!index.containsKey(word)) {
			index.put(word, new TreeMap<String, TreeSet<Integer>>());
		}

		if (!index.get(word).containsKey(path)) {
			TreeSet<Integer> set = new TreeSet<>();
			index.get(word).put(path, set);
		}

		index.get(word).get(path).add(position);
	}

	public static String cleanLine(String line) {
		return line.toLowerCase().replaceAll("\\p{Punct}+", "").trim();
	}

	/**
	 * take all data from index2 and add to index
	 * 
	 * @param index2
	 */
	public void addAll(InvertedIndex index2) {
		for (String key : index2.index.keySet()) {
			if (!index.containsKey(key)) {
				index.put(key, index2.index.get(key));
			} else {
				for (String path : index2.index.get(key).keySet()) {
					if (!index.get(key).containsKey(path)) {
						index.get(key).put(path, index2.index.get(key).get(path));
					} else {
						index.get(key).get(path).addAll(index2.index.get(key).get(path));
					}
				}
			}
		}
	}

	/**
	 * @return returns key value pairs
	 */
	public String toString() {
		return index.toString();
	}

	/**
	 * 
	 * @param word
	 *            to check if word is in index
	 * @return true or false
	 */
	public boolean containsWord(String word) {
		if (index.containsKey(word)) {
			return true;
		}
		return false;
	}

	/*
	 * @return size of index
	 */
	public int size() {
		return index.size();
	}

	/**
	 * 
	 * @param word
	 *            key value that contains file
	 * @param file
	 *            file name to check
	 * @return true or false if the word is in index
	 */
	public boolean containsFile(String word, String file) {
		return index.get(word).containsKey(file);
	}

	/**
	 * 
	 * @param output
	 *            location to write to
	 */
	public boolean writeWords(Path path) {
		try (BufferedWriter writer = Files.newBufferedWriter(path);) {
			writer.write("{" + END);
			int size = 0;
			if (!index.isEmpty()) {
				for (String key : index.keySet()) {
					writer.write(TAB + "\"" + key.toString() + "\": {" + END);
					int size2 = 0;
					if (!index.get(key).isEmpty()) {
						for (String keys : index.get(key).keySet()) {
							writer.write(TAB + "" + TAB + "\"" + keys + "\": [" + END);
							if (!index.get(key).get(keys).isEmpty()) {
								TreeSet<Integer> values = index.get(key).get(keys);
								writer.write(TAB + "" + TAB + "" + TAB + values.first().toString());

								for (Integer inside : index.get(key).get(keys)) {
									if (inside != values.first()) {
										writer.write("," + END + "" + TAB + "" + TAB + "" + TAB + inside.toString());
									}
								}
								writer.write(END);
								size2++;
								if (size2 == index.get(key).keySet().size()) {
									writer.write(TAB + "" + TAB + "]" + END);
								} else
									writer.write(TAB + "" + TAB + "]," + END);
							} else
								writer.write(TAB + "" + TAB + "]" + END);
						}
					}

					size++;
					if (size == index.keySet().size()) {
						writer.write(TAB + "}" + END);
					} else
						writer.write(TAB + "}," + END);
				}

			}
			writer.write("}");
			return true;

		} catch (IOException e) {
			System.err.println("Failed to write index to " + path.toString());
			return false;
		}
	}

	/**
	 * helper function for JSON writing
	 * 
	 * @param n
	 * @return
	 */
	public static String tab(int n) {
		char[] tabs = new char[n];
		Arrays.fill(tabs, TAB);
		return String.valueOf(tabs);
	}

	/**
	 * preforms a partial search on a query string
	 * 
	 * @param queryWords
	 * @return
	 */
	public ArrayList<SearchObject> partialSearch(String[] queryWords) {
		HashMap<String, SearchObject> visited = new HashMap<>();
		ArrayList<SearchObject> searchResults = new ArrayList<>();

		for (String queryWord : queryWords) {

			for (String word : index.tailMap(queryWord).keySet()) {
				if (word.startsWith(queryWord)) {
					// TODO private void searchHelper(String word, List<> list,
					// Map<> map);
					for (String path : index.get(word).keySet()) {
						if (visited.containsKey(path)) {
							visited.get(path).addFrequency(index.get(word).get(path).size());
							visited.get(path).setPosition(index.get(word).get(path).first());
						} else {
							SearchObject result = new SearchObject(path, index.get(word).get(path).size(),
									index.get(word).get(path).first());
							visited.put(path, result);
							searchResults.add(result);
						}
					}
				} else {
					break;
				}
			}
		}

		Collections.sort(searchResults);
		return searchResults;
	}
	


	/**
	 * preforms a exact search on a query string
	 * 
	 * @param queryWords
	 * @return
	 */
	public ArrayList<SearchObject> exactSearch(String[] queryWords) {
		HashMap<String, SearchObject> visited = new HashMap<>();
		ArrayList<SearchObject> searchResults = new ArrayList<>();

		for (String queryWord : queryWords) {

			if (index.containsKey(queryWord)) {
				for (String path : index.get(queryWord).keySet()) {
					if (visited.containsKey(path)) {
						visited.get(path).addFrequency(index.get(queryWord).get(path).size());
						visited.get(path).setPosition(index.get(queryWord).get(path).first());
					} else {
						SearchObject result = new SearchObject(path, index.get(queryWord).get(path).size(),
								index.get(queryWord).get(path).first());
						visited.put(path, result);
						searchResults.add(result);
					}
				}
			}
		}

		Collections.sort(searchResults);
		return searchResults;
	}
}
