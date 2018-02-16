import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CrawlServlet extends HttpServlet {
	private static WebCrawlerInterface crawler;
	public static String version;
	public CrawlServlet(WebCrawlerInterface crawler, String version){
		super();
		CrawlServlet.version = version;
		CrawlServlet.crawler = crawler;
	}

	
	public static String getDate() {
		String format = "hh:mm a 'on' EEEE, MMMM dd yyyy";
		DateFormat formatter = new SimpleDateFormat(format);
		return formatter.format(new Date());
	}
	
	public String cleanLink(String link) throws MalformedURLException {
		URL newLink = new URL(link);
		URL finished = new URL(newLink.getProtocol(), newLink.getHost(), newLink.getFile());
		String cleaned = finished.toString();
		return cleaned;
	}


    private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(
			HttpServletRequest request,
			HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_OK);

		PrintWriter out = response.getWriter();
		out.printf("<html>%n");
		out.printf("<head>");
		out.printf("\t<meta charset=\"utf-8\">%n");
		out.printf("\t<meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">%n");
		out.printf("<title>Search Engine %s</title><style>", version);
		out.printf("a { color: #fff;}");
		out.printf("</style></head>%n");
		out.printf("<body background=\"https://www.walldevil.com/wallpapers/w01/527560-abstract-black-background-dark-side-darth-vader-death-star-propaganda-simple-simplistic-spoof-star-wars.jpg\">");
		out.printf("<p><font face=\"Courier New\" size=\"3\" color=\"white\"></p>%n");
		out.printf("<p><font face=\"Courier New\" size=\"15\" color=\"white\">Crawl</p>%n");
	

		out.printf("<form action ='/' method='get'>");
		out.printf("<input type = \"submit\" value=\"MainMenu\" style=\"height:30px; width:132px; font-size: 15px; font-family: Courier New;\">");
		out.printf("</form>");
		
		out.printf("<form action =\"/crawl\" method=\"post\">");
		out.printf("<p><font face=\"Courier New\" size=\"4\" color=\"white\"></p>");
		out.printf("URL: <input type=\"text\" name=\"link\">");
		out.printf("<input type = \"submit\" size=\"4\" value=\"Crawl\" style=\"position: absolute; font-size: 15px; top: 158px; height:19px; width:100px; font-family: Courier New;\">");
		out.printf("<p>Failure to redirect to the main menu indicates a bad link.</p>");
		out.printf("</form>");
		
	
	
		
		out.printf("<footer>");
		out.printf("<p><font face=\"Courier New\" size=\"3\" style=\"position: absolute; bottom: 5px; color=\"white\">%s. %s</p>", getDate(), version);
		out.printf("</footer>");
		out.printf("</body>%n");
		out.printf("</html>%n");
		
		response.setStatus(HttpServletResponse.SC_OK);
	
	}
	@Override
	protected void doPost(
			HttpServletRequest request,
			HttpServletResponse response)
			throws ServletException, IOException {
		
		String link = request.getParameter("link");
		try{
			link = cleanLink(link);
			crawler.crawl(link);
			response.sendRedirect(response.encodeRedirectURL("/"));
		}catch(MalformedURLException e){
			response.sendRedirect(response.encodeRedirectURL("/crawl"));
			
		}
	}
}
