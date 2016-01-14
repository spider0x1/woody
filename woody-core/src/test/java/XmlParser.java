import java.io.File;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class XmlParser {

	public static void main(String[] args) throws Exception {
		Document doc = Jsoup.parse(new File("/Users/ligang.yao/Downloads/Lucene.xml"), "utf-8");
		
		Elements dataEles = doc.select("data");
		
		for (Element dataEle :  dataEles) {
			String name = dataEle.select("name").first().text();
			System.out.println("name: " + name);
			String indexSavePath = dataEle.select("index_save_path").first().text();
			System.out.println("indexSavePath: " + indexSavePath);
			String ifOverride = dataEle.select("if_override").first().text();
			System.out.println("ifOverride: " + ifOverride);
			String databaseHost = dataEle.select("database_host").first().text();
			System.out.println("databaseHost: " + databaseHost);
			String databaseUser = dataEle.select("database_user").first().text();
			System.out.println("databaseUser: " + databaseUser);
			String databasePassword = dataEle.select("database_password").first().text();
			System.out.println("databasePassword: " + databasePassword);
			String databaseDb = dataEle.select("database_db").first().text();
			System.out.println("databaseDb: " + databaseDb);
			String databaseTable = dataEle.select("database_table").first().text();
			System.out.println("databaseTable: " + databaseTable);
			Element fieldEle = dataEle.select("fields").first();
			String id = fieldEle.select("id").first().text();
			System.out.println("id: " + id);
			String keywordstr = fieldEle.select("keywordstr").first().text();
			System.out.println("keywordstr: " + keywordstr);
		}
	}
	
}
