package com.dirscanner.elem;

import com.dirscanner.elem.DirectoryElement.Size;
import org.assertj.core.api.Assertions;
import org.junit.Test;

/**
 * Tests for {@link DirectoryElement}.
 */
public class DirectoryElementTest {

    @Test
    public void toString_shouldConcatenateTypePathAndSizeSeparatedByWhitespace() {

        final DirectoryElement elem =
                DirectoryElement.builder()
                        .type(DirectoryElement.Type.DIR)
                        .absPath("C:\\TEST_DU\\FOO")
                        .size(Size.builder().value(100).unit(FileSizeUnit.KB).build())
                        .build();

        Assertions.assertThat(elem.toString()).isEqualTo("DIR C:\\TEST_DU\\FOO 100KB");
    }

}