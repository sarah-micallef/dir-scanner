package com.dirscanner;

import com.dirscanner.elem.DirectoryElement;
import com.dirscanner.asserts.DirectoryElementAssert;
import com.dirscanner.elem.FileSizeUnit;
import com.dirscanner.scanner.DirectoryElementScanner;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.NoSuchFileException;
import java.nio.file.NotDirectoryException;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Created by Sarah Micallef on 13/06/2017.
 */
public class DirectoryElementScannerTest {

    // Some OSes, such as OS X, adds .DS_STORE files to directories which we do not have control over.
    private final static Predicate<DirectoryElement> IS_NON_OS_FILE = elem -> !elem.getAbsPath().endsWith("/.DS_Store");

    private final static String TEST_RESOURCES_RELATIVE_PATH = "src/dirscanner/resources";

    private DirectoryElementScanner extractor;

    @Before
    public void setup() {
        this.extractor = new DirectoryElementScanner();
    }

    @Test
    public void scan_pathDoesNotExist_shouldThrowNoSuchFileException() {

        Assertions.assertThatExceptionOfType(NoSuchFileException.class)
                .isThrownBy(() -> extractor.scan(Paths.get(TEST_RESOURCES_RELATIVE_PATH + "/non-existent-path")));
    }

    @Test
    public void scan_pathToFile_shouldThrowNotDirectoryException() {
        Assertions.assertThatExceptionOfType(NotDirectoryException.class)
                .isThrownBy(() -> extractor.scan(Paths.get(TEST_RESOURCES_RELATIVE_PATH + "/test.txt")));
    }

    @Test
    public void scan_pathToEmptyDir_shouldReturnEmptyList() throws Exception {
        Assertions.assertThat(extractor.scan(Paths.get(TEST_RESOURCES_RELATIVE_PATH + "/empty-dir")))
                .isEmpty();
    }

    @Test
    public void scan_pathToDirContainingOnlyFiles_shouldReturnAllFileElementsSortedBySizeInDescOrder() throws Exception {

        final List<DirectoryElement> elems =
                extractor.scan(Paths.get(TEST_RESOURCES_RELATIVE_PATH + "/dir-of-files-only"))
                        .stream()
                        .filter(IS_NON_OS_FILE).collect(Collectors.toList());

        Assertions.assertThat(elems)
                .isNotNull()
                .hasSize(3);

        DirectoryElementAssert.assertThat(elems.get(0))
                .hasType(DirectoryElement.Type.FILE)
                .hasAbsPath(Paths.get(TEST_RESOURCES_RELATIVE_PATH + "/dir-of-files-only/large.txt").toFile().getAbsolutePath())
                .hasSize((double) 2911 / 1024, FileSizeUnit.KB);

        DirectoryElementAssert.assertThat(elems.get(1))
                .hasType(DirectoryElement.Type.FILE)
                .hasAbsPath(Paths.get(TEST_RESOURCES_RELATIVE_PATH + "/dir-of-files-only/medium.txt").toFile().getAbsolutePath())
                .hasSize((double) 1558 / 1024, FileSizeUnit.KB);

        DirectoryElementAssert.assertThat(elems.get(2))
                .hasType(DirectoryElement.Type.FILE)
                .hasAbsPath(Paths.get(TEST_RESOURCES_RELATIVE_PATH + "/dir-of-files-only/small.txt").toFile().getAbsolutePath())
                .hasSize((double) 5 / 1024, FileSizeUnit.KB);
    }

    @Test
    public void scan_pathToDirContainingFilesAndSubDirs_shouldReturnOnlyTopLevelElemsAndCalculateSubDirSizeByAddingSizesOfItsContents() throws Exception {

        final List<DirectoryElement> elems =
                extractor.scan(Paths.get(TEST_RESOURCES_RELATIVE_PATH + "/dir-of-files-and-sub-dirs"))
                        .stream()
                        .filter(IS_NON_OS_FILE)
                        .collect(Collectors.toList());

        Assertions.assertThat(elems)
                .isNotNull()
                .hasSize(3);

        DirectoryElementAssert.assertThat(elems.get(0))
                .hasType(DirectoryElement.Type.DIR)
                .hasAbsPath(Paths.get(TEST_RESOURCES_RELATIVE_PATH + "/dir-of-files-and-sub-dirs/bar").toFile().getAbsolutePath())
                .hasSizeAtLeast((double) 52216 / 1024, FileSizeUnit.KB); // The size cannot be compared with the exact value because certain OS might create hidden files, such as OS X which creates .ds_store files

        DirectoryElementAssert.assertThat(elems.get(1))
                .hasType(DirectoryElement.Type.DIR)
                .hasAbsPath(Paths.get(TEST_RESOURCES_RELATIVE_PATH + "/dir-of-files-and-sub-dirs/foo").toFile().getAbsolutePath())
                .hasSizeAtLeast((double) 2648 / 1024, FileSizeUnit.KB);

        DirectoryElementAssert.assertThat(elems.get(2))
                .hasType(DirectoryElement.Type.FILE)
                .hasAbsPath(Paths.get(TEST_RESOURCES_RELATIVE_PATH + "/dir-of-files-and-sub-dirs/somefile.txt").toFile().getAbsolutePath())
                .hasSize((double) 9 / 1024, FileSizeUnit.KB);
    }

}