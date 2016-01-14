package org.xbug.search.woody.core.example;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.xbug.search.woody.core.model.Inject;
import org.xbug.search.woody.core.model.Model;
import org.xbug.search.woody.core.model.annotation.ExprType;
import org.xbug.search.woody.core.model.annotation.ExtractBy;
import org.xbug.search.woody.core.model.annotation.KV;
import org.xbug.search.woody.core.model.annotation.Setting;
import org.xbug.search.woody.core.parser.AnnotationExtractor;


public class M {

	public static void main(String[] args) throws IOException, InstantiationException, IllegalAccessException,
			InvocationTargetException {
		String url = "http://q.115.com/t-1033-22270.html";
		Document doc = Jsoup.connect(url).timeout(60000)
				.userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:23.0) Gecko/20100101 Firefox/23.0").get();
		String html = doc.html();
		MM model = AnnotationExtractor.me(url).process(html, MM.class);
		StringBuffer buff = new StringBuffer();
		buff.append("</html>");
		buff.append("<head><title>").append(model.title).append("</title></head>");
		buff.append("<body>");
		for (String img : model.dImgs)
			buff.append("<img src='").append(img).append("'/><br/>\n");
		buff.append("</body>");
		buff.append("<html>");
		System.out.println(buff);
	}

	@Inject
	public static class MM extends Model {

		@ExtractBy(value = "h2.article-title", type = ExprType.CSS)
		String title;

		@ExtractBy(value = "img[src~=^/imgload\\?r=.+]", type = ExprType.CSS, multi = true, setting = @Setting(attrName = "abs:src"))
		List<String> dImgs;

		@ExtractBy(value = "span.item-side span", type = ExprType.CSS, setting = @Setting(), dataMap = { @KV(key = "sct.date.format", value = "yyyy-MM-dd HH:mm:ss") })
		Date updateDate;
	}
}
