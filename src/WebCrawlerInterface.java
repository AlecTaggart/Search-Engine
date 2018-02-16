import java.net.MalformedURLException;

public interface WebCrawlerInterface {

	/**
	 * crawls start link then crawls additional links found
	 * 
	 * @param seed
	 * @throws MalformedURLException
	 */
	public void crawl(String seed) throws MalformedURLException;



}
