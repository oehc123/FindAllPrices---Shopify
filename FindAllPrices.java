import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FindAllPrices {
	static double totalPrice = 0;
	static int totalItems =0;
	public static void main (String arg[]){
		String page ="http://shopicruit.myshopify.com/collections/all?page=1&sort_by=price-ascending";
		boolean keepGoing =true;
		final String REGEX1 = "clock";
		final String REGEX2 = "watch";
		Matcher m1, m2;
		Document doc =null;		//used for initial page
		while(keepGoing){
			keepGoing=false;
			try {
				doc = Jsoup.connect(page).get();
			} catch (IOException e) {
				e.printStackTrace();
			}
			Elements link = doc.select("a[href]");
			Pattern pClock = Pattern.compile(REGEX1);			//used to match clock
			Pattern pWatch = Pattern.compile(REGEX2);			//used to match watch
	        for (Element a : link) {
	        	String test = a.attr("abs:href");
	        	m1 = pClock.matcher(test);				//MATCHES HREF WITH KEYWORD CLOCK OR WATCH
	        	m2 = pWatch.matcher(test);			//Looks for Items with keyword Watch
	        	if(m1.find()){					//find next link
	        		getAllPric(test);
	        	}
	        	else if(m2.find()){					//find next link
	        		getAllPric(test);
	        	}
	        	else if(a.attr("title").equals("Next »")){
	        		page = a.attr("abs:href");
	            	keepGoing = true;
	        	}
	        }
        }
        System.out.println("there are " + totalItems + " number of items for a price of "+totalPrice);
	}
	
	
	private static void getAllPric(String pag) {
		Document doc2=null;
		Matcher match;
		String REGPRICE = "\\d+\\.\\d{1,2}";
		Pattern patPrice = Pattern.compile(REGPRICE);
		try {						//CONNECTS INTO CINDIVIDUAL ITEM AD ADD ALL ITS TYPES
			doc2 = Jsoup.connect(pag).get();	//connects into the following link
		} catch (IOException e) {
			e.printStackTrace();
		}
		Elements link2 = doc2.select("select");
		for (Element b : link2) {					//iterating over all elements with select tag
			String test2 = b.toString();			//converting the id into string
			match = patPrice.matcher(test2);			//match the price of all options in this category into m12
			while(match.find()){						//for each match, add their pricess
//				System.out.println(match.group());
				totalItems++;
				totalPrice += Double.parseDouble(match.group());	//add prices to total price
			}
		}
	}
}
