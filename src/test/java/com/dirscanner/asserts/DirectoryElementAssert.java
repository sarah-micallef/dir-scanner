package com.dirscanner.asserts;

import com.dirscanner.elem.DirectoryElement;
import com.dirscanner.elem.FileSizeUnit;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.ObjectAssert;

public class DirectoryElementAssert extends ObjectAssert<DirectoryElement> {

    private DirectoryElementAssert(final DirectoryElement actual) {
        super(actual);
    }

    public static DirectoryElementAssert assertThat(final DirectoryElement actual) {
        return new DirectoryElementAssert(actual);
    }

    public DirectoryElementAssert hasType(final DirectoryElement.Type expectedType) {
        Assertions.assertThat(actual.getType()).as("Type").isEqualTo(expectedType);
        return this;
    }

    public DirectoryElementAssert hasAbsPath(final String expectedAbsPath) {
        Assertions.assertThat(actual.getAbsPath()).as("Abs Path").isEqualTo(expectedAbsPath);
        return this;
    }

    public DirectoryElementAssert hasSize(final double expectedValue, final FileSizeUnit expectedUnit) {
        SizeAssert.assertThat(actual.getSize())
                .hasValue(expectedValue)
                .hasUnits(expectedUnit);

        return this;
    }

    public DirectoryElementAssert hasSizeAtLeast(final double expectedValue, final FileSizeUnit expectedUnit) {

        Assertions.assertThat(actual.getSize().getValue()).isGreaterThanOrEqualTo(expectedValue);
        SizeAssert.assertThat(actual.getSize()).hasUnits(expectedUnit);

        return this;
    }

}
