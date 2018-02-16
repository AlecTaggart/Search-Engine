import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Handles display of user information.
 *
 * @see LoginServer
 */
@SuppressWarnings("serial")
public class LoginWelcomeServlet extends LoginBaseServlet {
	
	
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

	public static String getTime() {
		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		Date date = new Date();
		return dateFormat.format(date);
	}
	
	public void clearCookies(
			HttpServletRequest request,
			HttpServletResponse response, String username) {

		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if(cookie.getName().endsWith(username)){
					cookie.setValue(null);
					cookie.setMaxAge(0);
					response.addCookie(cookie);
				}
			}
		}
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

	public static final String VISIT_DATE = "Visited";
	public static final String VISIT_COUNT = "Count";

	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String username = getUsername(request);

		if (username != null) {

			response.setContentType("text/html");
			response.setStatus(HttpServletResponse.SC_OK);

			PrintWriter out = response.getWriter();
			out.printf("<html>%n");
			out.printf("<head>");
			out.printf("\t<meta charset=\"utf-8\">%n");
			out.printf("\t<meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">%n");
			out.printf("<title>Search Engine %s</title><style>", "v2.2");
			out.printf("a { color: #fff;}");
			out.printf("</style></head>%n");
			out.printf("<body background=\"https://www.walldevil.com/wallpapers/w01/527560-abstract-black-background-dark-side-darth-vader-death-star-propaganda-simple-simplistic-spoof-star-wars.jpg\">");
			out.printf("<p><font face=\"Courier New\" size=\"15\" color=\"white\">The Dark Search</p>%n");
			out.printf("<form action ='/exactSearch' method='get'>");
			out.printf("<input type = \"submit\" size=\"13\" value=\"ExactSearch\" style=\" position: absolute; left: 25px; top: 100px; font-size: 15px;  font-family: Courier New; height:30px; width:142px\">");
			out.printf("</form>");
			out.printf("<form action ='/partialSearch' method='get'>");
			out.printf("<input type = \"submit\" size=\"13\" value=\"PartialSearch\" style=\" position: absolute; left: 180px; top: 100px; font-size: 15px;  font-family: Courier New; height:30px; width:142px\">");
			out.printf("</form>");
		
			out.printf("<form action ='/crawl' method='get'>");
			out.printf("<input type = \"submit\" size=\"13\" value=\"Crawl\" style=\" position: absolute; left: 25px; top: 143px; font-size: 15px; font-family: Courier New; height:30px; width:142px\">");
			out.printf("</form>");
			
			out.printf("<form action ='/welcome' method='get'>");
			out.printf("<input type = \"submit\" size=\"13\" name=\"clearButton\" value=\"Clear Cookies\" style=\" position: absolute; left: 180px; top: 143px; font-size: 15px; font-family: Courier New; height:30px; width:142px\">");
			if (request.getParameter("clearButton") != null) {
	            clearCookies(request, response, username);
	        }
			out.printf("</form>");
			
			out.printf("<form action ='/history' method='get'>");
			out.printf("<input type = \"submit\" size=\"13\" value=\"Search History\" style=\" position: absolute; left: 25px; top: 186px; font-size: 15px; font-family: Courier New; height:30px; width:142px\">");
			out.printf("</form>");
			
			out.printf("<form action ='/Incognito' method='get'>");
			out.printf("<input type = \"submit\" size=\"13\" value=\"Incognito\" style=\" position: absolute; left: 180px; top: 186px; font-size: 15px; font-family: Courier New; height:30px; width:142px\">");
			out.printf("</form>");
			
			out.printf("<form action ='/favorites' method='get'>");
			out.printf("<input type = \"submit\" size=\"13\" value=\"Favorites\" style=\" position: absolute; left: 25px; top: 229px; font-size: 15px; font-family: Courier New; height:30px; width:142px\">");
			out.printf("</form>");
			
			out.printf("<form action ='/login?logout' method='get'>");
			out.printf("<input type = \"submit\" size=\"13\" value=\"Logout\" style=\" position: absolute; left: 180px; top: 229px; font-size: 15px; font-family: Courier New; height:30px; width:142px\">");
			out.printf("</form>");
			
			out.printf("<form action ='/suggested' method='get'>");
			out.printf("<input type = \"submit\" size=\"13\" value=\"Suggested\" style=\" position: absolute; left: 25px; top: 271px; font-size: 15px; font-family: Courier New; height:30px; width:142px\">");
			out.printf("</form>");
			
			Map<String, String> cookies = getCookieMap(request);
			
			
			
			

			String visitDate = cookies.get(VISIT_DATE+username);
			String visitCount = cookies.get(VISIT_COUNT+username);
			
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
			response.addCookie(new Cookie("Visited"+username, getDate()));
			response.addCookie(new Cookie("Count"+username, visitCount));
			out.printf("</footer>");
			out.printf("</body>%n");
			out.printf("</html>%n");
			
			response.setStatus(HttpServletResponse.SC_OK);

		
			
			
		}
		else {
			response.sendRedirect("/login");
		}
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		doGet(request, response);
	}
}
