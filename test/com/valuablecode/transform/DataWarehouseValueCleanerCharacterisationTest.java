package com.valuablecode.transform;

import static java.text.MessageFormat.format;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;

import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/**
 * Warning: this DataWarehouseValueCleaner characterisation test does not give complete coverage of
 * the DataWarehouseValueCleaner class. Proceed with caution or augment the set of tests.
 */
@RunWith(Parameterized.class)
public class DataWarehouseValueCleanerCharacterisationTest {

	final String dataWarehouseValue;
	final String expected;
	final ResultValueType resultValueType;
	final String reason;

	final DataWarehouseValueCleaner sut = new DataWarehouseValueCleaner();

	public DataWarehouseValueCleanerCharacterisationTest(String expected, String dataWarehouseValue,
			ResultValueType resultValueType) {
		this.dataWarehouseValue = dataWarehouseValue;
		this.expected = expected;
		this.resultValueType = resultValueType;

		this.reason = createReason(dataWarehouseValue, resultValueType);
	}

	private String createReason(String dataWarehouseValue, ResultValueType resultValueType) {
		return format("Cleaning \"{0}\" as a {1} value.", dataWarehouseValue, resultValueType);
	}

	@Parameters
	public static Collection<Object[]> testData() {
		return asList(new Object[][] {
			{ "Some Compound", "Some Compound", ResultValueType.COMPOUND },
			
			{ null, null, ResultValueType.FLOAT }, { "0.72", ".72", ResultValueType.FLOAT },
			{ "0.72", ".72%", ResultValueType.FLOAT }, { null, ". 72", ResultValueType.FLOAT },
			{ null, "N/A", ResultValueType.FLOAT }, { null, "NA", ResultValueType.FLOAT },
			{"N/A", "N/A ", ResultValueType.FLOAT },

			{ null, "A", ResultValueType.FLOAT }, { null, "UNABLE TO CALCULATE", ResultValueType.FLOAT },
			{ null, "Uanble to Calculate", ResultValueType.FLOAT },
			{ null, "unable to perform", ResultValueType.FLOAT },

			{ null, null, ResultValueType.DATE }, { null, "NOT CALCULATED", ResultValueType.DATE },
			{ "2011/05/31", "2011/05/31 Extra", ResultValueType.DATE },
			{ null, "NOT CALCULATED", ResultValueType.DATE },
			{ null, "UANBLE TO CALCULATE", ResultValueType.DATE },

			{ null, null, ResultValueType.RANGE }, { "120/180", "120/180 DEP", ResultValueType.RANGE },
			{ "120/180", "<120/180", ResultValueType.RANGE },
			{ "120/180", "120/180 extended", ResultValueType.RANGE },
			{ "120/180", "120/180 venouse", ResultValueType.RANGE },
			{ null, "unable to perform", ResultValueType.RANGE },

			{ null, null, ResultValueType.TEXT }, { "Some Text", "Some Text", ResultValueType.TEXT }
		});
	}

	@Test
	public void cleans_as_expected() {
        System.out.println(reason);
		assertThat(reason, sut.cleanIncomingValues(dataWarehouseValue, resultValueType), equalTo(expected));
	}

}
