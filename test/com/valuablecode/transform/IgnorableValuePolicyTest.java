package com.valuablecode.transform;

import static java.text.MessageFormat.format;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;


@RunWith(Parameterized.class)
public class IgnorableValuePolicyTest {
    
    static final boolean IGNORABLE = true;
    static final boolean NOT_IGNORABLE = false;

    final String trimmedDataWarehouseValue;
    final boolean expected;
    final String reason;

    final IgnorableValuePolicy sut = new IgnorableValuePolicy();
    
    public IgnorableValuePolicyTest(boolean expected, String trimmedDataWarehouseValue) {
        this.expected = expected;
        this.trimmedDataWarehouseValue = trimmedDataWarehouseValue;
        
        this.reason = createReason(expected, trimmedDataWarehouseValue);
    }
    
    private String createReason(boolean expected, String trimmedDataWarehouseValue) {
        return format("Expected ''{0}'' to be an {1} data warehouse value.", trimmedDataWarehouseValue,
                        expected ? "ignorable" : "acceptable");
    }

    @Parameters
    public static Collection<Object[]> durationValues() {
        return asList(new Object[][] {
                     { NOT_IGNORABLE, "120/180" },
                     { NOT_IGNORABLE, "Mild Intermittent" },
                     
                     { IGNORABLE, "A" },
                     { IGNORABLE, "UNABLE TO CALCULATE" },
                     { IGNORABLE, "Uanble to Calculate" },
                     { IGNORABLE, "NOT CALCULATED" },
                     { IGNORABLE, "unable to perform" },
                     
                     { IGNORABLE, " UNABLE TO CALCULATE " },
                     { IGNORABLE, "Not Calculated  " },
                     
                     { IGNORABLE, null },
                     });
    }
    
    @Test public void
    should_detect_ignorable_trimmed_data_warehouse_values() {
         assertThat(reason, sut.isIgnorableValue(trimmedDataWarehouseValue), equalTo(expected));
    }

}
