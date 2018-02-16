
import java.io.IOException;
import java.nio.file.Path;


public interface IndexBuilderInterface {
	/**
	 * setUp Helper for Index Builder
	 * @param directory
	 * @throws IOException
	 */
	public void setUp(Path directory) throws IOException;

}
