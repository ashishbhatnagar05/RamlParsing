package com.ashish.parsertest;

import org.junit.Before;
import org.junit.Test;

import com.ashish.parser.ParsingHelper;

public class ParserTest {
	private ParsingHelper sut;

	@Before
	public void setup() {
		sut = new ParsingHelper();
	}

	@Test
	public void success() {
		sut.parseRaml(sut.readRaml("input.raml"));
	}

}
