import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;

public class WebCrawler implements WebCrawlerInterface {

	private final LinkedList<String>	linkQueue;
	private final HashSet<String>		linkSet;
	private final InvertedIndex			index;

	public WebCrawler(InvertedIndex index) {
		this.index = index;
		this.linkQueue = new LinkedList<String>();
		this.linkSet = new HashSet<String>();
	}

	/**
	 * takes in a seed url and crawls until we process 50 links
	 * 
	 * @param seed
	 * @param index
	 * @throws MalformedURLException
	 */
	public void crawl(String seed) throws MalformedURLException {

		seed = cleanLink(seed);
		linkSet.add(seed);
		linkQueue.add(seed);

		while (linkSet.size() <= 50 && !linkQueue.isEmpty()) {
			crawlOneLink(linkQueue.removeFirst());
		}
	}

	/**
	 * processes one link
	 * 
	 * @param linkInLinkQueue
	 * @throws MalformedURLException
	 */
	public void crawlOneLink(String linkInLinkQueue) throws MalformedURLException {

		String linkInLinkQueueHTML = HTMLCleaner.fetchHTML(linkInLinkQueue);
		// System.out.println("WORKING WITH THIS LINK = " + linkInLinkQueue);

		ArrayList<String> links = LinkParser.listLinks(linkInLinkQueueHTML);

		processLinks(links, linkInLinkQueue);
		processWords(HTMLCleaner.parseWords(HTMLCleaner.cleanHTML(linkInLinkQueueHTML)), linkInLinkQueue);

	}

	/**
	 * adds links to linkset/linkQueue
	 * 
	 * @param links
	 * @param linkInLinkQueue
	 * @throws MalformedURLException
	 */
	private void processLinks(ArrayList<String> links, String linkInLinkQueue) throws MalformedURLException {
		for (String link : links) {
			if (linkSet.size() >= 50) {
				break;
			} else {
				try {
					URL base = new URL(linkInLinkQueue);
					URL absolute = new URL(base, link);
					link = absolute.toString();
					link = cleanLink(link);
					if (!linkSet.contains(link)) {
						linkSet.add(link);
						linkQueue.add(link);
					}
				} catch (MalformedURLException e) {
					System.out.println("Failed to process current link - " + linkInLinkQueue);
				}
			}
		}
	}

	/**
	 * Cleans given Link
	 */
	public String cleanLink(String link) throws MalformedURLException {
		URL newLink = new URL(link);
		URL finished = new URL(newLink.getProtocol(), newLink.getHost(), newLink.getFile());
		String cleaned = finished.toString();
		return cleaned;
	}

	/**
	 * adds given words to index
	 * 
	 * @param words
	 * @param link
	 */
	protected void processWords(String[] words, String link) {
		for (int i = 0; i < words.length; i++) {
			index.add(words[i], link, i + 1);
		}
	}

}
