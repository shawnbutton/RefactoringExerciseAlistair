package com.valuablecode.transform;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Knows how data warehouse values should be cleaned by hard coding a list of cleanable strings.
 */
class ValueCleaningPolicy {

    private static final Pattern cleanableStringRegExp = Pattern.compile(" |%|<|>|~|extended|venous");
    private static final Pattern leadingTextRegExp = Pattern.compile("(\\S)+\\s");

    public String clean(String trimmedDataWarehouseValue) {
        if (null == trimmedDataWarehouseValue) return null;
        
        return removeCleanableStrings(keepOnlyLeadingText(trimmedDataWarehouseValue));
    }

    private String keepOnlyLeadingText(String trimmedDataWarehouseValue) {
        Matcher matcher = leadingTextRegExp.matcher(trimmedDataWarehouseValue);
        
        return matcher.find() ? matcher.group() : trimmedDataWarehouseValue;
    }

    private String removeCleanableStrings(String trimmedDataWarehouseValue) {
        return cleanableStringRegExp.matcher(trimmedDataWarehouseValue).replaceAll("");
    }

}
