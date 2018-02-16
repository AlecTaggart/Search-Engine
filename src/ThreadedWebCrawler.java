import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;

public class ThreadedWebCrawler implements WebCrawlerInterface {
	private final WorkQueue					workers;
	private final HashSet<String>			linkSet;
	private final ThreadSafeInvertedIndex	index;

	public ThreadedWebCrawler(ThreadSafeInvertedIndex index, WorkQueue workers) {
		this.workers = workers;
		this.index = index;
		this.linkSet = new HashSet<String>();
	}

	/**
	 * crawls seed and calls crawl one link for new links found
	 */
	@Override
	public void crawl(String seed) throws MalformedURLException {
		seed = cleanLink(seed);
		synchronized (linkSet) {
			linkSet.add(seed);
		}
		workers.execute(new Minion(seed));
		workers.finish();
	}

	/**
	 * adds words to index
	 * 
	 * @param words
	 * @param link
	 * @param index
	 */
	
	protected void processWords(String[] words, String link, InvertedIndex index) {
		for (int i = 0; i < words.length; i++) {
			index.add(words[i], link, i + 1);
		}
	}

	/**
	 * processes one link
	 * 
	 * @param linkInLinkQueue
	 * @param localIndex
	 * @throws MalformedURLException
	 */
	private void crawlOneLink(String linkInLinkQueue, InvertedIndex localIndex) throws MalformedURLException {
		String linkInLinkQueueHTML = HTMLCleaner.fetchHTML(linkInLinkQueue);
		ArrayList<String> links = LinkParser.listLinks(linkInLinkQueueHTML);
		processLinks(links, linkInLinkQueue);
		processWords(HTMLCleaner.parseWords(HTMLCleaner.cleanHTML(linkInLinkQueueHTML)), linkInLinkQueue, localIndex);

	}

	/**
	 * cleans one link
	 */
	public String cleanLink(String link) throws MalformedURLException {
		URL newLink = new URL(link);
		URL finished = new URL(newLink.getProtocol(), newLink.getHost(), newLink.getFile());
		String cleaned = finished.toString();
		return cleaned;
	}

	/**
	 * adds links to link Queue and set
	 * 
	 * @param links
	 * @param linkInLinkQueue
	 */
	private void processLinks(ArrayList<String> links, String linkInLinkQueue) {
		synchronized (linkSet) {
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
							workers.execute(new Minion(link));
						}
					} catch (MalformedURLException e) {
						System.out.println("Failed to process current link - " + linkInLinkQueue);
					}
				}
			}
		}
	}

	private class Minion implements Runnable {

		private final String		link;
		private final InvertedIndex	localIndex;

		public Minion(String link) {
			this.link = link;
			localIndex = new InvertedIndex();
		}

		@Override
		public void run() {
			try {
				crawlOneLink(link, localIndex);
				index.addAll(localIndex);
			} catch (MalformedURLException e) {
				System.out.println("Failed to create URL.");
			}
		}

	}

}
