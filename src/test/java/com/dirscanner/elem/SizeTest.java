package com.dirscanner.elem;

import com.dirscanner.elem.DirectoryElement.Size;
import org.assertj.core.api.Assertions;
import org.junit.Test;

/**
 * Created by Sarah Micallef on 13/06/2017.
 */
public class SizeTest {

    @Test
    public void toString_integerValue_shouldConcatenateValueAndUnits() {

        final Size size = Size.builder().value(100).unit(FileSizeUnit.KB).build();

        Assertions.assertThat(size.toString()).isEqualTo("100KB");
    }

    @Test
    public void toString_decimalValue_shouldConcatenateRoundedValueAndunit() {

        final Size sizeToBeRoundedUp = Size.builder().value(100.5).unit(FileSizeUnit.KB).build();
        Assertions.assertThat(sizeToBeRoundedUp.toString()).isEqualTo("101KB");

        final Size sizeToBeRoundedDown = Size.builder().value(100.3).unit(FileSizeUnit.KB).build();
        Assertions.assertThat(sizeToBeRoundedDown.toString()).isEqualTo("100KB");
    }

}