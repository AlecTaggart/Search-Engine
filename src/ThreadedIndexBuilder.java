import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;



public class ThreadedIndexBuilder implements IndexBuilderInterface{

	private final WorkQueue					workers;
	private final ThreadSafeInvertedIndex	index;

	public ThreadedIndexBuilder(ThreadSafeInvertedIndex index, WorkQueue workers) {
		this.workers = workers;
		this.index = index;
	}
	
	/**
	 * helper function that calls build
	 */
	@Override
	public void setUp(Path directory){
		try {
			build(directory);
		} catch (IOException e) {
			System.out.println("Failed To Build Directory.");
		}
		workers.finish();
	}
	/**
	 * Builds directory of files and executes a minion on files found
	 * @param directory
	 * @throws IOException
	 */
	public void build(Path directory) throws IOException {

		try (DirectoryStream<Path> listing = Files.newDirectoryStream(directory)) {
			for (Path file : listing) {
				if (Files.isDirectory(file)) {
					build(file);
				} else if (file.toString().toLowerCase().endsWith(".txt")) {
					workers.execute(new Minion(file));
				}
			}
		}
	}


	private class Minion implements Runnable {

		private final Path			path;
		private final InvertedIndex	localIndex;

		/**
		 * Initializes the minion.
		 * @param path
		 */
		public Minion(Path path) {
			this.path = path;
			localIndex = new InvertedIndex();
		}

		@Override
		public void run() {
			InvertedIndexBuilder.addWords(path, localIndex);
			index.addAll(localIndex);
		}

	}

}
