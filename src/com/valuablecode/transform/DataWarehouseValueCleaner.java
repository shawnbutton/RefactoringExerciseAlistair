package com.valuablecode.transform;

/**
 * Knows how to clean a raw data warehouse result.
 */
public class DataWarehouseValueCleaner {
    
    private final IgnorableValuePolicy ignorableValuePolicy = new IgnorableValuePolicy();
    private final ValueCleaningPolicy valueCleaningPolicy= new ValueCleaningPolicy();
    
    private final Cleaner compoundCleaner = new CompoundCleaner();
    private final Cleaner dateCleaner = new DateCleaner();
    private final Cleaner floatCleaner = new FloatCleaner();
    private final Cleaner rangeCleaner = new RangeCleaner();
    private final Cleaner textCleaner = new TextCleaner();

    public String cleanIncomingValues(String dataWarehouseValue, ResultValueType resultValueType) {
        return asCleaner(resultValueType).cleanIncomingValues(dataWarehouseValue);
    }
    
    private Cleaner asCleaner(ResultValueType resultValueType) {
        switch (resultValueType) {
        case COMPOUND:
            return compoundCleaner;
            
        case DATE:
            return dateCleaner;
            
        case FLOAT:
            return floatCleaner;
            
        case RANGE:
            return rangeCleaner;
        
        case TEXT:
            return textCleaner;
        }
            
        throw new TransformationException("Unexpected ResultValueType: " + resultValueType.name());
    }

    private String dropTrailingText(String trimmedDataWarehouseValue) {
        int firstSpaceIndex = trimmedDataWarehouseValue.indexOf(' ');
        
        return (0 < firstSpaceIndex) ? trimmedDataWarehouseValue.substring(0, firstSpaceIndex) : trimmedDataWarehouseValue;
    }

    /**
     * Knows how to clean data warehouse result values.
     */
    private abstract class Cleaner {
        
        protected abstract String doCleanIncomingValues(String dataWarehouseValue);
        
        public String cleanIncomingValues(String dataWarehouseValue) {
            if (null == dataWarehouseValue) return null;
        
            return doCleanIncomingValues(dataWarehouseValue);
        }

    }
    
    /**
     * Knows how to clean ResultValueType.COMPOUND data warehouse result values.
     */
    private class CompoundCleaner extends Cleaner {
        
        public String doCleanIncomingValues(String dataWarehouseValue) {
            return dataWarehouseValue;
        }

    }
    
    /**
     * Knows how to clean ResultValueType.DATE data warehouse result values.
     */
    private class DateCleaner extends Cleaner {
        
        public String doCleanIncomingValues(String dataWarehouseValue) {
            String trimmedDataWarehouseValue = dataWarehouseValue.trim();
            
            if (ignorableValuePolicy.isIgnorableValue(trimmedDataWarehouseValue)) return null;
    
            return valueCleaningPolicy.clean(dropTrailingText(trimmedDataWarehouseValue));
        }

    }
    
    /**
     * Knows how to clean ResultValueType.FLOAT data warehouse result values.
     */
    private class FloatCleaner extends Cleaner {

        public String doCleanIncomingValues(String dataWarehouseValue) {
            String trimmedDataWarehouseValue = dataWarehouseValue.trim();
            
            if (isIgnorableValue(trimmedDataWarehouseValue)) return null;
            if (isMalformedValue(trimmedDataWarehouseValue)) return null;
    
            return normalizeDecimalValue(valueCleaningPolicy.clean(dropTrailingText(trimmedDataWarehouseValue)));
        }

        private boolean isIgnorableValue(String trimmedDataWarehouseValue) {
            return ignorableValuePolicy.isIgnorableValue(trimmedDataWarehouseValue) ||
                            isIgnorableFloatValue(trimmedDataWarehouseValue);
        }

        private boolean isIgnorableFloatValue(String trimmedDataWarehouseValue) {
            return trimmedDataWarehouseValue.equals("NA") || trimmedDataWarehouseValue.equals("N/A");
        }
        
        private boolean isMalformedValue(String trimmedDataWarehouseValue) {
            return trimmedDataWarehouseValue.contains(" ");
        }
        
        private String normalizeDecimalValue(String trimmedDataWarehouseValue) {
            return trimmedDataWarehouseValue.startsWith(".") ? ("0" + trimmedDataWarehouseValue) : trimmedDataWarehouseValue;
        }

    }

    /**
     * Knows how to clean ResultValueType.RANGE data warehouse result values.
     */
    private class RangeCleaner extends Cleaner {
        
        public String doCleanIncomingValues(String dataWarehouseValue) {
            String trimmedDataWarehouseValue = dataWarehouseValue.trim();
            
            if (ignorableValuePolicy.isIgnorableValue(trimmedDataWarehouseValue)) return null;
    
            return valueCleaningPolicy.clean(dropTrailingText(trimmedDataWarehouseValue));
        }

    }
    
    /**
     * Knows how to clean ResultValueType.TEXT data warehouse result values.
     */
    private class TextCleaner extends Cleaner {
        
        public String doCleanIncomingValues(String dataWarehouseValue) {
            return dataWarehouseValue;
        }

    }
    
}
