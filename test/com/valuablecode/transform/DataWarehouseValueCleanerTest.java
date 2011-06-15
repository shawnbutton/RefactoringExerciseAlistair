package com.valuablecode.transform;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;


@RunWith(Parameterized.class)
public class DataWarehouseValueCleanerTest {

    final String dataWarehouseValue;
    final String expected;
    final String reason;
    final ResultValueType resultValueType;
    
    final DataWarehouseValueCleaner sut = new DataWarehouseValueCleaner();


    public DataWarehouseValueCleanerTest(String reason, String expected, String dataWarehouseValue,
                    ResultValueType resultValueType) {
        this.reason = reason;
        this.expected = expected;
        this.dataWarehouseValue = dataWarehouseValue;
        this.resultValueType = resultValueType;
    }
    
    @Parameters
    public static Collection<Object[]> durationValues() {
        return asList(new Object[][] {
        		{ "Leave null COMPOUND as is", null, null, ResultValueType.COMPOUND },
        		{ "Leave COMPOUND as is", "Some Compound", "Some Compound", ResultValueType.COMPOUND },
	
                { "Leave null FLOAT as is", null, null, ResultValueType.FLOAT },
                { "With leading zero", "0.72", " 0.72", ResultValueType.FLOAT },
                { "Add leading zero", "0.72", ".72", ResultValueType.FLOAT },
                { "Skip malformed float", null, ". 72", ResultValueType.FLOAT },
                { "Skip not applicable", null, "N/A", ResultValueType.FLOAT },
                { "Skip not applicable", null, "NA", ResultValueType.FLOAT },
                        
                { "Leave null DATE as is", null, null, ResultValueType.DATE },
                { "Skip ignorable", null, "NOT CALCULATED", ResultValueType.DATE },
                { "Drop trailing text", "2011/05/31", "2011/05/31 Extra", ResultValueType.DATE },
                        
                { "Leave null RANGE as is", null, null, ResultValueType.RANGE },
                { "Drop trailing text", "120/180", "120/180 DEP", ResultValueType.RANGE },
                { "Skip ignorable value", null, "unable to perform", ResultValueType.RANGE },

                { "Leave null TEXT as is", null, null, ResultValueType.TEXT },
                { "Leave TEXT as is", "Some Text", "Some Text", ResultValueType.TEXT },
                });
    }
   
    @Test public void
    should_clean_data_warehouse_value() {
        assertThat(reason, sut.cleanIncomingValues(dataWarehouseValue, resultValueType), equalTo(expected));
    }
    
}
