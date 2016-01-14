/**
 * @author ligang.yao
 */

//storage value
var string;

/**
 * transform to string from object
 */
function toString($this) {
	string = new Packages.java.lang.String($this);
	return string;
}

function __new__($this) {
	return toString($this);
}

/**
 * return this income value
 */
function self($this) {
	return $this;
}

function trim($this) {
	return __new__($this).trim();
}

function replace($this, old, replacement) {
	return __new__($this).replace(old, replacement);
}

function replaceAll($this, regex, replacement) {
	return __new__($this).replaceAll(regex, replacement);
}

function lenght($this) {
	return __new__($this).lenght();
}

function substring($this, start, end) {
	return __new__($this).substring(start, end);
}

function substring($this, start) {
	return __new__($this).substring(start);
}

function endsWith($this, suffix) {
	return __new__($this).endsWith(suffix);
}
