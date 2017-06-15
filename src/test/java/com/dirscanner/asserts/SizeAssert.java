package com.dirscanner.asserts;

import com.dirscanner.elem.DirectoryElement.Size;
import com.dirscanner.elem.FileSizeUnit;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.ObjectAssert;

public class SizeAssert extends ObjectAssert<Size> {

    private SizeAssert(final Size actual) {
        super(actual);
    }

    public static SizeAssert assertThat(final Size actual) {
        return new SizeAssert(actual);
    }

    public SizeAssert hasValue(final double expectedValue) {
        Assertions.assertThat(actual.getValue()).as("Size Value").isEqualTo(expectedValue);
        return this;
    }

    public SizeAssert hasUnits(final FileSizeUnit expectedUnit) {
        Assertions.assertThat(actual.getUnit()).as("Size Units").isEqualTo(expectedUnit);
        return this;
    }
}
