/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.xbug.search.woody.core.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Default implementation of a page signature. It calculates an MD5 hash of the
 * raw binary content of a page. In case there is no content, it calculates a
 * hash from the page's URL.
 * 
 */
public class MD5Signature {

	private final static char HEX_DIGITS[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',
			'e', 'f' };

	private static String byteArrayToHexString(byte[] data) {
		char arr[] = new char[16 * 2];
		int k = 0;
		for (int i = 0; i < 16; i++) {
			byte b = data[i];
			arr[k++] = HEX_DIGITS[b >>> 4 & 0xf];
			arr[k++] = HEX_DIGITS[b & 0xf];
		}
		return new String(arr);
	}

	private static byte[] encrypt(byte[] data) {
		MessageDigest mdInst;
		byte[] md = null;
		try {
			mdInst = MessageDigest.getInstance("MD5");
			mdInst.update(data);
			md = mdInst.digest();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return md;
	}

	public static String md5Str(String text) {
		if (StringUtil.isNullOrEmpty(text))
			return "";
		byte[] bytes = text.getBytes();
		byte[] encrypt = encrypt(bytes);
		return byteArrayToHexString(encrypt);
	}

}
