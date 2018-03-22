package com.ashish.parser;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.raml.v2.api.RamlModelBuilder;
import org.raml.v2.api.RamlModelResult;
import org.raml.v2.api.model.common.ValidationResult;
import org.raml.v2.api.model.v10.api.Api;
import org.raml.v2.api.model.v10.bodies.Response;
import org.raml.v2.api.model.v10.datamodel.ObjectTypeDeclaration;
import org.raml.v2.api.model.v10.datamodel.TypeDeclaration;
import org.raml.v2.api.model.v10.declarations.AnnotationRef;
import org.raml.v2.api.model.v10.methods.Method;
import org.raml.v2.api.model.v10.resources.Resource;

public class ParsingHelper {

	public void parseRaml(byte[] ramlBytes) {
		Reader reader = ramlBytesToReader(ramlBytes);
		Api api = validateRaml(reader);
		if (null != api) {
			System.out.println(api.baseUri().value());
			logApiData(api);
			// logNonSensitiveData(api.resources());
		}
	}

	// method could be used to generate list of paramters with sensitive annotations
	private void logApiData(Api api) {
		List<TypeDeclaration> types = api.types();
		for (TypeDeclaration apiType : types) {
			List<TypeDeclaration> properties = ((ObjectTypeDeclaration) apiType).properties();
			for (TypeDeclaration property : properties) {
				for (AnnotationRef annotation : property.annotations()) {
					if (annotation.name().equals("(sensitive)")) {
						System.out.println(annotation.name() + " annotation is present for :" + property.name());
					}
				}
			}
		}

	}

	private void logNonSensitiveData(List<Resource> resources) {
		for (Resource resource : resources) {
			System.out.println(resource.displayName().value());
			for (Method method : resource.methods()) {
				System.out.println("Resource: " + resource.displayName().value() + " Method: " + method.method());
				for (Response response : method.responses()) {
					for (TypeDeclaration body : response.body()) {
						System.out.println(body.type());

					}
				}
			}
			logNonSensitiveData(resource.resources());
		}

	}

	private Api validateRaml(Reader reader) {
		RamlModelBuilder ramlBuilder = new RamlModelBuilder();
		RamlModelResult ramlModelResult = ramlBuilder.buildApi(reader, "");
		if (ramlModelResult.hasErrors()) {
			for (ValidationResult validationResult : ramlModelResult.getValidationResults()) {
				System.out.println(validationResult.getMessage());
			}
		} else {
			Api api = ramlModelResult.getApiV10();
			return api;
		}
		return null;
	}

	private Reader ramlBytesToReader(byte[] ramlBytes) {
		Reader reader = null;
		try {
			reader = new InputStreamReader(new ByteArrayInputStream(ramlBytes), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return reader;
	}

	public byte[] readRaml(String fileName) {
		String raml = "";
		try {
			raml = new String(Files.readAllBytes(Paths.get(fileName)));
			System.out.println(raml);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return raml.getBytes();
	}
}
