var msg = "hello";

/**
 * @author ligang.yao
 */

/**
 * transform to string from object 
 */
function toString($this) {
	var str = new Packages.java.lang.String($this);
	return str;
}

/**
 * trim op
 */
function trim($this) {
	
	return toString($this).trim();
}

/**
 * replaceAll
 */
function replaceAll($this, regex, replacement) {
	return toString($this).replaceAll(regex, replacement);
}

/**
 * replaceAll
 */
function replace($this, _old, _new) {
	return toString($this).replace(_old, _new);
}

function getDoc($url) {
	var userAgent = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:22.0) Gecko/20100101 Firefox/22.0";
	var timeout = 60000;
	var _doc;
	try {
		_doc = Packages.org.jsoup.Jsoup.connect($url).userAgent(userAgent).timeout(timeout).followRedirects(true).get();
	}catch(e){println(e)}
	return _doc;
}

function selectList($doc, xpath) {
	var list = new Packages.java.util.ArrayList();
	var eles = $doc.select(xpath);
	for (var i = 0; i < eles.size(); i++) {
		list.add(eles.get(i).text());
	}
	return list;
}

function select($doc, xpath) {
	return selectList($doc, xpath).get(0);
}

function sayHello() {
	println(msg);
}