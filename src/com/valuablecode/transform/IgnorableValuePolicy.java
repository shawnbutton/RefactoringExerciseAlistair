package com.valuablecode.transform;

import static java.util.Arrays.asList;

import java.util.List;

/**
 * Knows which data warehouse values should be ignored by hard coding a list of case-insensitive ignorable values.
 */
class IgnorableValuePolicy {
    
    private static List<String> ignorableValues = asList(
                    "a",
                    "not calculated", 
                    "unable to calculate",
                    "uanble to calculate",      // Intended to catch a common misspelling. 
                    "unable to perform" 
                    );


    public boolean isIgnorableValue(String trimmedDataWarehouseValue) {
        if (null == trimmedDataWarehouseValue) return true;
        
        return ignorableValues.contains(trimmedDataWarehouseValue.toLowerCase().trim());
    }

}
