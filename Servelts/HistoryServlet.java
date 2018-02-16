import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HistoryServlet extends CookieBaseServlet{

	public static String version;
	public HistoryServlet(String version){
		super();
		HistoryServlet.version = version;
	
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
		Cookie[] cookies = request.getCookies();

		out.printf("<html>%n");
		out.printf("<head>");
		out.printf("\t<meta charset=\"utf-8\">%n");
		out.printf("\t<meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">%n");
		out.printf("<title>Search Engine %s</title><style>", version);
		out.printf("a { color: #fff;}");
		out.printf("</style></head>%n");
		out.printf("<body background=\"https://www.walldevil.com/wallpapers/w01/527560-abstract-black-background-dark-side-darth-vader-death-star-propaganda-simple-simplistic-spoof-star-wars.jpg\">");
		out.printf("<p><font face=\"Courier New\" size=\"3\" color=\"white\"></p>%n");
		out.printf("<p><font face=\"Courier New\" size=\"13\" color=\"white\">Search History.</p>%n");
		out.printf("<form action ='/' method='get'>");
		out.printf("<input type = \"submit\" value=\"MainMenu\" style=\" height:30px; width:132px; font-size: 15px; font-family: Courier New;\">");
		out.printf("</form>");
		out.printf("<form action ='/history' method='get'>");
		out.printf("<input type = \"submit\" value=\"Clear History\" name=\"clearHistory\" style=\" position: absolute; top: 78px; left: 170px; height:30px; width:132px; font-size: 15px; font-family: Courier New;\">");
		String userName = getUsername(request);
		if (request.getParameter("clearHistory") != null) {
			for (Cookie cookie : cookies) {
				if(cookie.getName().startsWith("Query") && cookie.getName().endsWith(userName)){
					cookie.setValue(null);
					cookie.setMaxAge(0);
					response.addCookie(cookie);
				}
			}
        }
		out.printf("</form>");
		out.printf("<p><font face=\"Courier New\" size=\"5\" color=\"white\">");
		
		if(cookies.length==0){
			out.printf("History Empty.");
		}
		for(Cookie cookie: cookies){
			String name = cookie.getName();
			if(name.startsWith("Query") && cookie.getValue()!=null && cookie.getName().endsWith(userName)){
				out.printf("<p>%s</p>", cookie.getValue());
			}
		}
		out.printf("</p>");
		response.setStatus(HttpServletResponse.SC_OK);
	
	}
	
}
