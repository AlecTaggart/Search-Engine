import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;


public class Driver {

	public static void main(String[] args) throws IOException {

		Path output = null;
		try {
			ArgumentParser parser = new ArgumentParser(args);
			parser.parseArguments(args);
			int numThreads;
			if (parser.hasFlag("-multi") && parser.hasValue("-multi")) {
				numThreads = Integer.parseInt(parser.getValue("-multi"));
				if (numThreads <= 0) {
					numThreads = 5;
				}
			} else {
				numThreads = 5;
			}

			InvertedIndex index;
			IndexBuilderInterface builder;
			QueryHelperInterface helper; 
			WorkQueue workers = new WorkQueue(numThreads);
			WebCrawlerInterface crawler;

			if (parser.hasFlag("-multi")) {
				ThreadSafeInvertedIndex safeIndex = new ThreadSafeInvertedIndex();
				index = safeIndex;
				builder = new ThreadedIndexBuilder(safeIndex, workers);
				helper = new ThreadedQueryHelper(safeIndex, workers);
				crawler = new ThreadedWebCrawler(safeIndex, workers);
			} else {
				index = new InvertedIndex();
				builder = new InvertedIndexBuilder(index);
				helper = new QueryHelper(index);
				crawler = new WebCrawler(index);
			}

			if (parser.hasDirValue("-dir") && parser.hasFlag("-dir")) {
				Path input = Paths.get(parser.getValue("-dir"));
				builder.setUp(input);
			} else if (parser.hasValue("-url") && parser.hasFlag("-url")) {
				String url = parser.getValue("-url");
				crawler.crawl(url);
			} else {
				System.out.println("please enter a valid directory or link. ");
			}

			if (parser.hasValue("-query") && parser.hasFlag("-query")) {
				String Query = parser.getValue("-query");
				Path queryPath = Paths.get(Query);
				helper.parseQuery(queryPath, false);
			}

			if (parser.hasFlag("-exact") && parser.hasValue("-exact")) {
				String Query = parser.getValue("-exact");
				Path queryPath = Paths.get(Query);
				helper.parseQuery(queryPath, true);
			}

			if (parser.hasFlag("-index")) {
				output = Paths.get(parser.getValue("-index", "index.json"));
				index.writeWords(output);
			}

			if (parser.hasFlag("-results")) {
				Path results = Paths.get(parser.getValue("-results", "results.json"));
				helper.writeOut(results);
			}
			
			if(parser.hasFlag("-port")){
				int port;
				if(parser.hasValue("-port")){
					port = Integer.parseInt(parser.getValue("-port"));
				}else{
					port = 8080;
				}
				try {
					@SuppressWarnings("unused")
					ServerMain server = new ServerMain(port, index, crawler);
				} catch (Exception e) {
					System.out.println("Failed to create server on port " + port);
				}
				
			}
			
			if (workers != null) {
				workers.shutdown();
			}
		} catch (NumberFormatException e) {
			System.out.println("incorrect number format");
		}

	
	}
}