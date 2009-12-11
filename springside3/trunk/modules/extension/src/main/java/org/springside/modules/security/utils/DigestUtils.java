/**
 * Copyright (c) 2005-2009 springside.org.cn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * 
 * $Id$
 */
package org.springside.modules.security.utils;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;

import org.springside.modules.utils.EncodeUtils;

/**
 * 支持SHA-1/MD5消息摘要的Util方法集合.
 * 
 * 支持Hex与Base64两种编码方式.
 * 
 * @author calvin
 */
public class DigestUtils {

	private static final String SHA1 = "SHA-1";
	private static final String MD5 = "MD5";

	//-- String Hash function --//
	/**
	 * 对输入字符串进行sha1散列, 返回Hex编码的结果.
	 */
	public static String sha1ToHex(String input) {
		byte[] digestResult = digest(input, SHA1);
		return EncodeUtils.hexEncode(digestResult);
	}

	/**
	 * 对输入字符串进行sha1散列, 返回Base64编码的结果.
	 */
	public static String sha1ToBase64(String input) {
		byte[] digestResult = digest(input, SHA1);
		return EncodeUtils.base64Encode(digestResult);
	}

	/**
	 * 对输入字符串进行sha1散列, 返回Base64编码的URL安全的结果.
	 */
	public static String sha1ToBase64Url(String input) {
		byte[] digestResult = digest(input, SHA1);
		return EncodeUtils.base64UrlEncode(digestResult);
	}

	/**
	 * 对字符串进行散列.
	 */
	private static byte[] digest(String input, String algorithm) {
		try {
			MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
			return messageDigest.digest(input.getBytes());
		} catch (GeneralSecurityException e) {
			throw new IllegalStateException("Security exception", e);
		}
	}

	//-- InputStream Hash function --//
	/**
	 * 对文件进行md5散列,返回Hex编码结果.
	 */
	public static String md5ToHex(InputStream input) throws IOException {
		return digest(input, MD5);
	}

	/**
	 * 对文件进行sha1散列,返回Hex编码结果.
	 */
	public static String sha1ToHex(InputStream input) throws IOException {
		return digest(input, SHA1);
	}

	/**
	 * 对文件进行散列.
	 */
	private static String digest(InputStream input, String algorithm) throws IOException {
		try {
			MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
			int STREAM_BUFFER_LENGTH = 1024;
			byte[] buffer = new byte[STREAM_BUFFER_LENGTH];
			int read = input.read(buffer, 0, STREAM_BUFFER_LENGTH);

			while (read > -1) {
				messageDigest.update(buffer, 0, read);
				read = input.read(buffer, 0, STREAM_BUFFER_LENGTH);
			}

			return EncodeUtils.hexEncode(messageDigest.digest());

		} catch (GeneralSecurityException e) {
			throw new IllegalStateException("Security exception", e);
		}
	}
}
