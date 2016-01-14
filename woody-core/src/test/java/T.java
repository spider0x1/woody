import java.io.IOException;
import java.math.BigInteger;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;


public class T {

	public static void main(String[] args) throws IOException {
		Document doc = Jsoup
		.connect("http://www.gyyg.com/query.php")
		.data("cardID", "00501685")
		.data("password","198611")
		.post();
		
		System.out.println(doc.text());
	}
}
