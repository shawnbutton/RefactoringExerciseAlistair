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
public class ValueCleaningPolicyTest {

    final String expected;
    final String trimmedDataWarehouseValue;
    final String reason;

    final ValueCleaningPolicy sut = new ValueCleaningPolicy();

    public ValueCleaningPolicyTest(String expected, String trimmedDataWarehouseValue) {
        this.expected = expected;
        this.trimmedDataWarehouseValue = trimmedDataWarehouseValue;
        
        this.reason = createReason(trimmedDataWarehouseValue);
    }

	private String createReason(String trimmedDataWarehouseValue) {
		return format("Expected \"{0}\" to be cleaned as \"{1}\".", trimmedDataWarehouseValue, expected);
	}
    
    @Parameters
    public static Collection<Object[]> durationValues() {
        return asList(new Object[][] {
                     { "50", "50%" },
                     { "50", "50% 75%" },
                     { "75.2", "<75.2" },
                     { "16.8", ">16.8" },
                     { "25.0", "~25.0" },
                     { "", " extended" },
                     { "", "venous" },
                     
                     { null, null },
                     });
    }

    @Test public void
    should_clean_trimmed_data_warehouse_values() {
         assertThat(reason, sut.clean(trimmedDataWarehouseValue), equalTo(expected));
    }

}

