import java.io.IOException;
import java.io.PrintWriter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



public class PartialSearchServlet extends CookieBaseServlet {
	
	private static InvertedIndex index;
	public static String version;
	public PartialSearchServlet(InvertedIndex index, String version){
		super();
		PartialSearchServlet.version = version;
		PartialSearchServlet.index = index;
	}

	public static String getTime(){
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
	
	protected String getUsername(HttpServletRequest request) {
		Map<String, String> cookies = getCookieMap(request);

		String login = cookies.get("login");
		String user  = cookies.get("name");

		if ((login != null) && login.equals("true") && (user != null)) {
			// this is not necessarily safe!
			return user.replaceAll("\\W+", "");
		}

		return null;
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
		out.printf("<p><font face=\"Courier New\" size=\"3\" color=\"white\">What would you like to search for?</p>%n");
		
		out.printf("<form action ='/' method='get'>");
		out.printf("<input type = \"submit\" value=\"MainMenu\" style=\" font-size: 15px; font-family: Courier New; height:30px; width:132px; \">");
		out.printf("</form>");
		
	
		
		out.printf("<form action ='/partialSearch' method='get'>");
		out.printf("PartialSearch: <input type=\"text\" name=\"search\">");
		out.printf("<input type = \"submit\" value=\"Search\" style=\" position: absolute; top:89px; font-size: 15px; font-family: Courier New; height:19px;\">");
		
		out.printf("</form>");

		 //divide by 1000000 to get milliseconds.
		
		if(request.getParameter("search") != null){
			String userName = getUsername(request);
			String name = ("Query"+System.currentTimeMillis()+ userName );
			response.addCookie(new Cookie(name, "Searched for \""+request.getParameter("search")+"\" at "+getDate()));
		
			String line = cleanLine(request.getParameter("search"));
			String[] queryWords = line.trim().split("\\s+");
			Arrays.sort(queryWords);
			long startTime = System.currentTimeMillis();
			ArrayList<SearchObject> results = index.partialSearch(queryWords);
			long endTime = System.currentTimeMillis();
			long duration = (endTime - startTime); 
			out.printf("<p>%d results found in %s milliseconds.</p>", results.size(), duration);
			if(results.size() > 0){
				for(SearchObject result : results){
					out.printf("<p><<a href=\"%s\">%s.</a></p>", result.getPath(), result.getPath());
//					out.printf("<form method=\"get\" action=\"%s\">", result.getPath());
//					out.printf("<button type=\"submit\">%s</button>", result.getPath());
//					out.printf("</form>");
				}
			}
			
		}
//		out.printf("<p>%s. %s</p>", getDate(), version);
		out.printf("</body>%n");
		out.printf("</html>%n");
		
		response.setStatus(HttpServletResponse.SC_OK);
	
	}

}
