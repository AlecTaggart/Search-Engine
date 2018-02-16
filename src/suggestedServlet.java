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

public class suggestedServlet extends CookieBaseServlet{

	public static String version;
	public suggestedServlet(String version){
		super();
		suggestedServlet.version = version;
	
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
		String username = getUsername(request);
		out.printf("<html>%n");
		out.printf("<head>");
		out.printf("\t<meta charset=\"utf-8\">%n");
		out.printf("\t<meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">%n");
		out.printf("<title>Search Engine %s</title><style>", version);
		out.printf("a { color: #fff;}");
		out.printf("</style></head>%n");
		out.printf("<body background=\"https://www.walldevil.com/wallpapers/w01/527560-abstract-black-background-dark-side-darth-vader-death-star-propaganda-simple-simplistic-spoof-star-wars.jpg\">");
		out.printf("<p><font face=\"Courier New\" size=\"3\" color=\"white\"></p>%n");
		out.printf("<p><font face=\"Courier New\" size=\"13\" color=\"white\">Suggested Searches.</p>%n");
		out.printf("<form action ='/' method='get'>");
		out.printf("<input type = \"submit\" value=\"MainMenu\" style=\" height:30px; width:132px; font-size: 15px; font-family: Courier New;\">");
		out.printf("</form>");
		
		
	
		
		out.printf("<form>");
		out.printf("<p><font face=\"Courier New\" size=\"4\" color=\"white\">");
		out.printf("Searched by other users:");
		int i = 1;
		for(Cookie cookie: cookies){
			if(i >5){
				break;
			}
			String name = cookie.getName();
			if(name.startsWith("Query") && !name.endsWith(username)){
				out.printf("<p>%s</p>", cookie.getValue());
			}i++;
		}
		out.printf("</p>");
		out.printf("</form>");
		
		
		out.printf("</p>");
		response.setStatus(HttpServletResponse.SC_OK);
	
	}
	
}
