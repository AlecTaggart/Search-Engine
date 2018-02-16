import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;


public class InvertedIndexBuilder implements IndexBuilderInterface{

	private final InvertedIndex index;
	
	public InvertedIndexBuilder(InvertedIndex index) {
		this.index = index;
	}
	
	/**
	 * This function takes in a directory and an ArrayList of files the function
	 * stores all the files from the directory in the ArrayList found
	 * 
	 * @param directory
	 *            input directory
	 * @param found
	 *            files found inside input directory
	 */
	public void getFiles(Path directory, ArrayList<Path> found) throws IOException {

		try (DirectoryStream<Path> listing = Files.newDirectoryStream(directory)) {
			// Efficiently iterate through the files and subdirectories.
			for (Path file : listing) {
				if (Files.isDirectory(file)) {
					getFiles(file, found);
				} else if (file.toString().toLowerCase().endsWith(".txt")) {
					found.add(file);
					// System.out.println(file.getFileName());
				}
			}
		}
	}

	public void setUp(Path directory) throws IOException{
		ArrayList<Path> found = getFiles(directory);
		addFile(index, found);
	}
	
	/**
	 * returns all files found at directory
	 * 
	 * @param directory
	 * @return
	 * @throws IOException
	 */
	
	public ArrayList<Path> getFiles(Path directory) throws IOException {
		ArrayList<Path> found = new ArrayList<Path>();
		if (Files.isDirectory(directory)) {
			getFiles(directory, found);
		}
		return found;
	}

	/**
	 * This function takes a file and a WordIndex and runs through the file
	 * adding each word to the word index
	 * 
	 * @param file
	 *            input file
	 * @param index
	 *            index data structure used to store words
	 */
	public static void addWords(Path file, InvertedIndex index) {

		String line;
		int location = 1;

		try (BufferedReader reader = Files.newBufferedReader(file, Charset.forName("UTF8"));) {

			while ((line = reader.readLine()) != null) {
				String[] result = line.replaceAll("\\p{Punct}+", "").toLowerCase().split("\\s+");
				for (int i = 0; i < result.length; i++) {
					if (!result[i].trim().isEmpty()) {
						index.add(result[i].trim(), file.toString(), location);
						location++;
					}
				}
			}
		} catch (IOException e) {
			System.out.println("Unable to parse file: " + file);
		}
	}

	/**
	 * This function takes in the WordIndex, array of found files, and the
	 * search engine It simply runs through the files and get all the words from
	 * them storing them inside our word index
	 * 
	 * @param index
	 *            word index data structure
	 * @param found
	 *            array of files
	 */
	public static void addFile(InvertedIndex index, ArrayList<Path> found) {

		for (Path file : found) {
			addWords(file, index);
		}
	}
}
