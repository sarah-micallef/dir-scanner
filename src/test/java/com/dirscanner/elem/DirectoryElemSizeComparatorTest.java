package com.dirscanner.elem;

import com.dirscanner.elem.DirectoryElement.DirectoryElementBuilder;
import com.dirscanner.elem.DirectoryElement.Size;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for {@link DirectoryElemSizeComparator}
 */
public class DirectoryElemSizeComparatorTest {

    private DirectoryElementBuilder directoryElem1Builder;
    private DirectoryElementBuilder directoryElem2Builder;

    private DirectoryElemSizeComparator comparator;

    @Before
    public void setup() {
        directoryElem1Builder = DirectoryElement.builder().absPath("C:\\TEST_DU").type(DirectoryElement.Type.DIR);
        directoryElem2Builder = DirectoryElement.builder().absPath("C:\\TEST_DU\\somefile.bat").type(DirectoryElement.Type.FILE);

        comparator = new DirectoryElemSizeComparator();
    }

    @Test
    public void compare_firstElemIsNull_shouldThrowNullPointerException() {
        final DirectoryElement elem2 =
                directoryElem2Builder
                        .size(Size.builder()
                                .value(20)
                                .unit(FileSizeUnit.KB)
                                .build())
                        .build();

        Assertions.assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> comparator.compare(null, elem2));
    }

    @Test
    public void compare_secondElemIsNull_shouldThrowNullPointerException() {
        final DirectoryElement elem1 =
                directoryElem2Builder
                        .size(Size.builder()
                                .value(20)
                                .unit(FileSizeUnit.KB)
                                .build())
                        .build();

        Assertions.assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> comparator.compare(elem1, null));
    }

    @Test
    public void compare_firstDirectoryElemHasSmallerSize_shouldReturnNeg1() {

        final DirectoryElement elem1 =
                directoryElem1Builder
                        .size(Size.builder()
                                .value(10)
                                .unit(FileSizeUnit.KB)
                                .build())
                        .build();

        final DirectoryElement elem2 =
                directoryElem2Builder
                        .size(Size.builder()
                                .value(20)
                                .unit(FileSizeUnit.KB)
                                .build())
                        .build();

        Assertions.assertThat(comparator.compare(elem1, elem2)).isEqualTo(-1);
    }

    @Test
    public void compare_directoryElemsHaveSameSize_shouldReturn0() {
        final DirectoryElement elem1 =
                directoryElem1Builder
                        .size(Size.builder()
                                .value(10.15)
                                .unit(FileSizeUnit.KB)
                                .build())
                        .build();

        final DirectoryElement elem2 =
                directoryElem2Builder
                        .size(Size.builder()
                                .value(10.15)
                                .unit(FileSizeUnit.KB)
                                .build())
                        .build();

        Assertions.assertThat(comparator.compare(elem1, elem2)).isEqualTo(0);
    }

    @Test
    public void compare_firstDirectoryElemHasLargerSize_shouldReturn1() {
        final DirectoryElement elem1 =
                directoryElem1Builder
                        .size(Size.builder()
                                .value(50.25)
                                .unit(FileSizeUnit.KB)
                                .build())
                        .build();

        final DirectoryElement elem2 =
                directoryElem2Builder
                        .size(Size.builder()
                                .value(20)
                                .unit(FileSizeUnit.KB)
                                .build())
                        .build();

        Assertions.assertThat(comparator.compare(elem1, elem2)).isEqualTo(1);
    }

    @Test
    public void compare_shouldNotCompareRoundedSizes() {

        final DirectoryElement elem1 =
                directoryElem1Builder
                        .size(Size.builder()
                                .value(10.1)
                                .unit(FileSizeUnit.KB)
                                .build())
                        .build();

        final DirectoryElement elem2 =
                directoryElem2Builder
                        .size(Size.builder()
                                .value(10.2)
                                .unit(FileSizeUnit.KB)
                                .build())
                        .build();

        Assertions.assertThat(comparator.compare(elem1, elem2)).isEqualTo(-1);
    }

    @Test
    public void compare_elemsWithDifferentSizeUnit_shouldThrowIllegalStateException() {
        final DirectoryElement elem1 =
                directoryElem1Builder
                        .size(Size.builder()
                                .value(50.25)
                                .unit(FileSizeUnit.KB)
                                .build())
                        .build();

        final DirectoryElement elem2 =
                directoryElem2Builder
                        .size(Size.builder()
                                .value(20)
                                .unit(FileSizeUnit.BYTES)
                                .build())
                        .build();

        Assertions.assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(() -> comparator.compare(elem1, elem2));
    }

}