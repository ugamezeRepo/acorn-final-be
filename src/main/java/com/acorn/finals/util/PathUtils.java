package com.acorn.finals.util;

import jakarta.servlet.ServletContext;
import org.springframework.http.server.RequestPath;
import org.springframework.web.util.pattern.PathPatternParser;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class PathUtils {
    public static Map<String, String> extractPath(String pattern, String input) {
        var result = new HashMap<String, String>();
        var parser = new PathPatternParser();


        var pathPattern = parser.parse(pattern);

        var patternContainer = RequestPath.parse(pattern, "");
        var inputContainer = RequestPath.parse(input, "");


        var extractedPattern = pathPattern.extractPathWithinPattern(patternContainer);
        var extractedInput = pathPattern.extractPathWithinPattern(inputContainer);

        var len = extractedPattern.elements().size();
        assert (extractedInput.elements().size() == len);
        for (int i = 0; i < len; i++) {
            var key = extractedPattern.elements().get(i).value();
            var value = extractedInput.elements().get(i).value();

            if (key.equals(value) || key.equals("*")) continue;

            assert (key.charAt(0) == '{' && key.charAt(key.length() - 1) == '}');

            var extractedKey = key.substring(1, key.length() - 1);
            result.put(extractedKey, value);
        }
        return result;
    }

    public static String convertUriToPathExceptForContextPath(URI uri, ServletContext context) {
        return uri.getPath().substring(context.getContextPath().length());
    }
}
