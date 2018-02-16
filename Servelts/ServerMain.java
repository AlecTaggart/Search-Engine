

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;


public class ServerMain {
	

	public ServerMain(int PORT, InvertedIndex index, WebCrawlerInterface crawler) throws Exception {
		System.setProperty("org.eclipse.jetty.LEVEL", "DEBUG");
		Server server = new Server(PORT);
		ResourceHandler resourceHandler = new ResourceHandler();
		resourceHandler.setDirectoriesListed(true);
		String version = "v2.2";
		


		ServletHandler handler = new ServletHandler();
		
		handler.addServletWithMapping(LoginUserServlet.class,     "/login");
		handler.addServletWithMapping(LoginRegisterServlet.class, "/register");
		handler.addServletWithMapping(LoginWelcomeServlet.class,  "/welcome");
		handler.addServletWithMapping(LoginRedirectServlet.class, "/*");
//		handler.addServletWithMapping(new ServletHolder(new CoreServlet(version)), "/");
		handler.addServletWithMapping(new ServletHolder(new ExactSearchServlet(index, version)), "/exactSearch");
		handler.addServletWithMapping(new ServletHolder(new PartialSearchServlet(index, version)), "/partialSearch");
		handler.addServletWithMapping(new ServletHolder(new CrawlServlet(crawler, version)), "/crawl");
		handler.addServletWithMapping(new ServletHolder(new HistoryServlet(version)), "/history");
		handler.addServletWithMapping(new ServletHolder(new suggestedServlet(version)), "/suggested");
		handler.addServletWithMapping(new ServletHolder(new IncognitoServlet(index, version)), "/Incognito");
		handler.addServletWithMapping(new ServletHolder(new favoriteServlet(version)), "/favorites");

		// Setup handlers (and handler order)
		HandlerList handlers = new HandlerList();
		handlers.setHandlers(new Handler[] { resourceHandler, handler });

		server.setHandler(handler);
		server.start();
		server.join();
	


	}

}
