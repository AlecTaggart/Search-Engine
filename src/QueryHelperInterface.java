import java.nio.file.Path;



public interface QueryHelperInterface {

	/**
	 * parses path using searchType
	 * @param path
	 * @param searchType
	 */
	public void parseQuery(Path path, boolean searchType);
	
	/**
	 * Calls JSON write function and outputs to file
	 * @param path
	 */
	public void writeOut(Path path);
	
	
}
