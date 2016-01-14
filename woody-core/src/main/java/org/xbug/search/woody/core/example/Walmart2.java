package org.xbug.search.woody.core.example;

import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.xbug.search.woody.core.model.Inject;
import org.xbug.search.woody.core.model.Inject.Type;
import org.xbug.search.woody.core.model.Model;
import org.xbug.search.woody.core.model.annotation.ComboExtract;
import org.xbug.search.woody.core.model.annotation.ComboExtracts;
import org.xbug.search.woody.core.model.annotation.ExprType;
import org.xbug.search.woody.core.model.annotation.ExtractBy;
import org.xbug.search.woody.core.model.annotation.OP;
import org.xbug.search.woody.core.model.annotation.Setting;
import org.xbug.search.woody.core.model.annotation.Setting.Function;
import org.xbug.search.woody.core.parser.AnnotationExtractor;
import org.xbug.search.woody.core.parser.BasicExtractor;
import org.xbug.search.woody.core.util.StringUtil;

public class Walmart2 extends Model {

	@Inject(Type.JSON)
	public String url;

	@Inject
	@ComboExtract(value = {
			@ExtractBy(value = "//meta[@itemprop='price']/@content", setting = @Setting(function = @Function(value = "replaceAll", args = {
					"[\\$\\s]+", "" }))),
			@ExtractBy(value = "div.product-price > div.price-display", type = ExprType.CSS, setting = @Setting(function = @Function(value = "replaceAll", args = {
					"[\\$\\s]+", "" }))) }, op = OP.OR)
	private String price;

	@Inject
	@ExtractBy(value = "//meta[@itemprop='availability']/@content")
	private String status;

	@Inject
	// @ExtractBy(value = "ol.breadcrumb-list > nav > li.breadcrumb:last-child > a > span", type = ExprType.CSS)
	@ExtractBy(value = "ol.breadcrumb-list li.breadcrumb", type = ExprType.CSS, multi = true, asString = true, delimiter = "|")
	private String category;

	@Inject
	@ComboExtracts(value = {
			@ComboExtract(value = { @ExtractBy(value = "//meta[@itemprop='brand']/@content") }),
			@ComboExtract(value = {
					@ExtractBy(value = "tbody.main-table", type = ExprType.CSS, setting = @Setting(outerHtml = true)),
					@ExtractBy(value = "(?i)<td>Publisher:</td>\\s*<td>(.+?)</td>", type = ExprType.REGEX) 
			}, op = OP.AND), }, op = OP.OR)
	private String manufacturer;

	@Inject
	
	@ComboExtracts(value = {
			@ComboExtract(value = { @ExtractBy(value = "//meta[@itemprop='model']/@content") }),
			@ComboExtract(value = {
					@ExtractBy(value = "tbody.main-table", type = ExprType.CSS, setting = @Setting(outerHtml = true)),
					@ExtractBy(value = "(?i)<td>Model No\\.:</td>\\s*<td>\\s*([\\w\\-]+?)\\s*</td>", type = ExprType.REGEX) 
			}, op = OP.AND) }, op = OP.OR)
	private String mpn;

	@Inject
	@ComboExtract(value = { 
			@ExtractBy(value = "h1.product-name", type = ExprType.CSS),
			@ExtractBy(value = "//meta[@name='keywords']/@content") 
	}, op = OP.OR)
	private String productName;

	@Inject
	@ExtractBy(value = "div.see-all-reviews", type = ExprType.CSS, setting = @Setting(attrName = "data-product-id"))
	private String productId;

	@Inject
	@ExtractBy(value = "div.product-subhead-walmartnumber", type = ExprType.CSS, setting = @Setting(function = @Function(value = "replace", args = { "Walmart #:", "" })))
	private String channelSKU;

	@Inject
	@ComboExtract(value = { 
			@ExtractBy(value = "//meta[@itemprop='productID']/@content"),
			@ExtractBy(value = "\"upc\":\"(.+?)\"", type = ExprType.REGEX) }, op = OP.OR)
	private String upc;

	@Inject
	@ExtractBy(value = "http://content.webcollage.net/walmart/resources/content-player/v2/content-player.min.js", type = ExprType.TEST)
	private boolean wcPlayer;

	@Inject
	@ComboExtract(value = {
			@ExtractBy(value = "http://content.webcollage.net/walmart/resources/content-player/v2/ppp.min.js", type = ExprType.TEST),
			@ExtractBy(value = "div#wc-aplus", type = ExprType.CSS) }, op = OP.OR)
	private boolean wcEmc;

	@Inject
	@ComboExtract(value = { @ExtractBy(value = "(?i)isDisplayable\\s*:\\s*(true|false)", type = ExprType.REGEX) })
	private boolean isDisplayable;

	@Inject
	@ComboExtract(value = { @ExtractBy(value = "(?i)isComingSoon\\s*:\\s*(true|false)", type = ExprType.REGEX) })
	private boolean isComingSoon;

	@Inject
	@ComboExtract(value = { @ExtractBy(value = "(?i)isPreOrder\\s*:\\s*(true|false),", type = ExprType.REGEX) })
	private boolean isPreOrder;

	@Inject
	@ComboExtract(value = { @ExtractBy(value = "(?i)isPreOrderOOS\\s*:\\s*(true|false)*,", type = ExprType.REGEX) })
	private boolean isPreOrderOOS;

	@Inject
	@ComboExtract(value = { @ExtractBy(value = "(?i)isRunout\\s*:\\s*(true|false)", type = ExprType.REGEX) })
	private boolean isRunout;

	@Inject(Type.ALL)
	public int statueCode;

	@Inject(Type.ALL)
	public String statusMsg;

	public String html;

	public Walmart2() {
		super();
		price = "";
		status = "";
		category = "";
		manufacturer = "";
		mpn = "";
		productName = "";
		productId = "";
		channelSKU = "";
		upc = "";
		statueCode = 200;
		statusMsg = "OK";
		url = "";
		wcPlayer = false;
		wcEmc = false;
		isDisplayable = false;
		isComingSoon = false;
		isPreOrder = false;
		isPreOrderOOS = false;
		isPreOrderOOS = false;
		html = "";
	}

	@Override
	public boolean isValid() {
		if (StringUtil.isNullOrEmpty(this.productId))
			return false;
		return true;
	}

	public static Walmart2 newInstance() {
		return new Walmart2();
	}

	public Map<String, Boolean> toCounters() {
		Map<String, Boolean> map = new HashMap<String, Boolean>();
		map.put("price", "".equals(this.price));
		map.put("price", "".equals(this.price));
		map.put("price", "".equals(this.price));
		map.put("price", "".equals(this.price));
		return map;
	}

	public static void main(String[] args) throws Exception {
		String url = "http://www.walmart.com/ip/20700208";
//		url = "http://www.walmart.com/ip/2045620";
//		url = "http://www.walmart.com/ip/29096508";
//		url = "http://www.walmart.com/ip/35879245";
		Document doc = null;
		doc = Jsoup.connect(url).timeout(60000)
				.userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:23.0) Gecko/20100101 Firefox/23.0").get();
		// doc = Jsoup.parse(new File("/tmp/walmart.html"), "utf-8", url);
		String html = doc.html();
		BasicExtractor extactor = AnnotationExtractor.me();
		extactor.baseUri(url);
		Walmart2 model = extactor.process(html, Walmart2.class);
		model.url = url;
		System.out.println(model);
		System.out.println(model.toHeader());
		System.out.println(url + "\t" + model.toTsv());
		System.out.println(url + "\t" + model.toJson());
	}
}