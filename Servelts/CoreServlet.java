import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CoreServlet extends CookieBaseServlet{
	
	public static final String VISIT_DATE = "Visited";
	public static final String VISIT_COUNT = "Count";

	public static String version;
	public CoreServlet(String version){
		super();
		CoreServlet.version = version;

	}

	public static String getTime() {
		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		Date date = new Date();
		return dateFormat.format(date);
	}
	
	public static String getDate() {
		String format = "hh:mm a 'on' EEEE, MMMM dd yyyy";
		DateFormat formatter = new SimpleDateFormat(format);
		return formatter.format(new Date());
	}
	
	public static String cleanLine(String line) {
		return line.toLowerCase().replaceAll("\\p{Punct}+", "").trim();
	}

		
	public static String dayOfWeek() {
		return Calendar.getInstance().getDisplayName(
				Calendar.DAY_OF_WEEK,
				Calendar.LONG,
				Locale.ENGLISH);
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
		out.printf("<p><font face=\"Courier New\" size=\"15\" color=\"white\">The Dark Search</p>%n");
		out.printf("<form action ='/exactSearch' method='get'>");
		out.printf("<input type = \"submit\" size=\"13\" value=\"ExactSearch\" style=\" position: absolute; left: 25px; top: 78px; font-size: 15px;  font-family: Courier New; height:30px; width:142px\">");
		out.printf("</form>");
		out.printf("<form action ='/partialSearch' method='get'>");
		out.printf("<input type = \"submit\" size=\"13\" value=\"PartialSearch\" style=\" position: absolute; left: 180px; top: 78px; font-size: 15px;  font-family: Courier New; height:30px; width:142px\">");
		out.printf("</form>");
	
		out.printf("<form action ='/crawl' method='get'>");
		out.printf("<input type = \"submit\" size=\"13\" value=\"Crawl\" style=\" position: absolute; left: 25px; top: 121px; font-size: 15px; font-family: Courier New; height:30px; width:142px\">");
		out.printf("</form>");
		
		out.printf("<form action ='/' method='get'>");
		out.printf("<input type = \"submit\" size=\"13\" name=\"clearButton\" value=\"Clear Cookies\" style=\" position: absolute; left: 180px; top: 121px; font-size: 15px; font-family: Courier New; height:30px; width:142px\">");
		if (request.getParameter("clearButton") != null) {
            clearCookies(request, response);
        }
		out.printf("</form>");
		
		out.printf("<form action ='/history' method='get'>");
		out.printf("<input type = \"submit\" size=\"13\" value=\"Search History\" style=\" position: absolute; left: 25px; top: 164px; font-size: 15px; font-family: Courier New; height:30px; width:142px\">");
		out.printf("</form>");
		
		out.printf("<form action ='/Incognito' method='get'>");
		out.printf("<input type = \"submit\" size=\"13\" value=\"Incognito\" style=\" position: absolute; left: 180px; top: 164px; font-size: 15px; font-family: Courier New; height:30px; width:142px\">");
		out.printf("</form>");
		
		out.printf("<form action ='/favorites' method='get'>");
		out.printf("<input type = \"submit\" size=\"13\" value=\"Favorites\" style=\" position: absolute; left: 25px; top: 207px; font-size: 15px; font-family: Courier New; height:30px; width:142px\">");
		out.printf("</form>");
		
		

		
		
		Map<String, String> cookies = getCookieMap(request);

		String visitDate = cookies.get(VISIT_DATE);
		String visitCount = cookies.get(VISIT_COUNT);
		
		out.printf("<footer>");
		out.printf("<p><font face=\"Courier New\" size=\"3\" style=\"position: absolute; bottom: 5px;\">");
	
	
		if ((visitDate == null) || (visitCount == null)) {
			visitCount = "0";
			out.printf("Thank you for visiting.");
		}
		else {
			visitCount = Integer.toString(Integer.parseInt(visitCount) + 1);
			out.printf("Visited our pages %s times. ", visitCount);
			out.printf("Last visited on %s.", visitDate);
		}
	
		out.printf("</p>%n");
		response.addCookie(new Cookie("Visited", getLongDate()));
		response.addCookie(new Cookie("Count", visitCount));
		out.printf("</footer>");
		out.printf("</body>%n");
		out.printf("</html>%n");
		
		response.setStatus(HttpServletResponse.SC_OK);

	
	}
}
