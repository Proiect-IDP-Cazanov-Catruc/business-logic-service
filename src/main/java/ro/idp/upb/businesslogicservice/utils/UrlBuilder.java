/* Ionel Catruc 343C3, Veaceslav Cazanov 343C3 | IDP BUSINESS-LOGIC-SERVICE | (C) 2024 */
package ro.idp.upb.businesslogicservice.utils;

import java.util.Map;
import org.apache.commons.text.StringSubstitutor;

public final class UrlBuilder {

	private UrlBuilder() {
		throw new RuntimeException("Cannot instantiate UrlBuilder class!");
	}

	public static String replacePlaceholdersInString(
			String stringTemplate, Map<String, Object> arguments) {
		return replacePlaceholdersInString(stringTemplate, arguments, "${", "}");
	}

	public static String replacePlaceholdersInString(
			String stringTemplate,
			Map<String, Object> arguments,
			String placeholderStart,
			String placeholderEnd) {
		return StringSubstitutor.replace(stringTemplate, arguments, placeholderStart, placeholderEnd);
	}
}
