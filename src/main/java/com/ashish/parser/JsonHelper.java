package com.ashish.parser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class JsonHelper {

	public String readFile(String fileName) {
		String file = "";
		try {
			file = new String(Files.readAllBytes(Paths.get(fileName)));
			System.out.println(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return file;
	}

	public void logJsonHelper(JSONObject json, List<String> sensitive) {
		ArrayList<String> list = new ArrayList<String>();
		Iterator<String> keys = list.iterator();
		logJson(json, keys, sensitive);
		System.out.println(json);
	}

	public void logJson(JSONObject json, Iterator<String> keys, List<String> sensitive) {
		keys = json.keys();
		while (keys.hasNext()) {
			String temp = keys.next();
			Object obj = json.get(temp);
			if (obj instanceof JSONObject) {
				//System.out.println("JSONObject");
				logJson((JSONObject) obj, keys, sensitive);
			} else if (obj instanceof JSONArray) {
				checkJsonArray(keys, sensitive, temp, obj);
			} else if (obj instanceof String) {
				checkSenstive(keys, sensitive, temp);
			}
		}
	}

	private void checkJsonArray(Iterator<String> keys, List<String> sensitive, String temp, Object obj) {
		//System.out.println("JSONArray");
		JSONArray array = (JSONArray) obj;
		for (Object arr : array) {
			if (arr instanceof JSONObject) {
				logJson((JSONObject) arr, keys, sensitive);
			} else if (obj instanceof String) {
				checkSenstive(keys, sensitive, temp);
			}
		}
	}

	private void checkSenstive(Iterator<String> keys, List<String> sensitive, String temp) {
		for (String str : sensitive) {
			if (temp.equals(str)) {
				System.out.println(temp + " is sensitive");
				keys.remove();
			}
		}
	}
}