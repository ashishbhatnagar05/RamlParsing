package com.ashish.parsertest;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import com.ashish.parser.JsonHelper;

public class JsonHelperTest {

	private JsonHelper sut;

	@Before
	public void setup() {
		sut = new JsonHelper();
	}

	@Test
	public void test() {
		JSONObject json = new JSONObject(sut.readFile("response.json"));
		List<String> sensitive = new ArrayList<String>();
		sensitive.add("pincode");
		sensitive.add("isFatal");
		sensitive.add("age");
		sut.logJsonHelper(json, sensitive);
	}

}
