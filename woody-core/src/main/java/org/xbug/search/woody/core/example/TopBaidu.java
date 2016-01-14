package org.xbug.search.woody.core.example;

import java.util.List;

import org.xbug.search.woody.core.model.Model;
import org.xbug.search.woody.core.model.annotation.ExprType;
import org.xbug.search.woody.core.model.annotation.ExtractBy;
import org.xbug.search.woody.core.model.annotation.ExtractedBean;
import org.xbug.search.woody.core.parser.AnnotationExtractor;


public class TopBaidu extends Model {

	@ExtractedBean(border = @ExtractBy(value = "div#news_img_box", type = ExprType.CSS, multi = true), clazz = Item.class)
	public List<Item> items;

	public TopBaidu() {
	}

	public static class Item {

		@ExtractBy(value = "h3 span", type = ExprType.CSS)
		public String title;

		@ExtractBy(value = "h3 em", type = ExprType.CSS)
		public String websiteName;

		@ExtractBy(value = "h3 span", type = ExprType.CSS)
		public String updateTime;

		@ExtractBy(value = "div.news_tex", type = ExprType.CSS)
		public String content;
	}

	public static void main(String[] args) {
		AnnotationExtractor.me().compile(TopBaidu.class);
	}

	/**
	 * 1. confirm the limit
	 */
}
