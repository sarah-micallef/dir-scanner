package com.dirscanner.elem;

import org.assertj.core.api.Assertions;
import org.junit.Test;

/**
 * Tests for {@link FileSizeUnit}
 */
public class FileSizeUnitTest {

    @Test
    public void convert_toIsNull_shouldThrowNullPointerException() {
        Assertions.assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> FileSizeUnit.BYTES.convert(0, null));
    }

    @Test
    public void convert_bytesToKB_shouldApply1024BytesIs1KBConversion() {
        Assertions.assertThat(FileSizeUnit.BYTES.convert(1024, FileSizeUnit.KB)).isEqualTo(1);
        Assertions.assertThat(FileSizeUnit.BYTES.convert(1, FileSizeUnit.KB)).isEqualTo((double) 1 / 1024);
    }

    @Test
    public void convert_byteToBytes_shouldReturnGivenValue() {
        Assertions.assertThat(FileSizeUnit.BYTES.convert(123, FileSizeUnit.BYTES)).isEqualTo(123);
    }

    @Test
    public void convert_kbToBytes_shouldThrowUnsupportedOperationException() {
        Assertions.assertThatExceptionOfType(UnsupportedOperationException.class).isThrownBy(() -> FileSizeUnit.KB.convert(1, FileSizeUnit.BYTES));
    }

    @Test
    public void convert_kbToKb_shouldReturnGivenValue() {
        Assertions.assertThat(FileSizeUnit.KB.convert(1, FileSizeUnit.KB)).isEqualTo(1);
    }

}