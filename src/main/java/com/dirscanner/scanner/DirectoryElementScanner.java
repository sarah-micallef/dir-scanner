package com.dirscanner.scanner;

import com.dirscanner.elem.DirectoryElemSizeComparator;
import com.dirscanner.elem.DirectoryElement;
import com.dirscanner.elem.DirectoryElement.Size;
import com.dirscanner.elem.FileSizeUnit;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.NotDirectoryException;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A {@link DirectoryElement} scanner which, given a {@link Path} will return a list of its {@link DirectoryElement}s.
 * <p>
 * <p>
 * ordered in descending order of size.
 * <p>
 * This scanner scans the top-level elements of the given path. However, in case such an element is another direcotyr,
 * then the size of this directory is actually the summation of the sizes of all its contents.
 */
public class DirectoryElementScanner {

    private final DirectoryElemSizeComparator fileSizeComparator = new DirectoryElemSizeComparator();

    /**
     * Scan the given path and return a list of {@link DirectoryElement}s sorted in descending order of their size.
     * <p>
     * This scanning will only return the top-level elements of the given path. However, in case such elements are
     * sub-directories, then their sizes is reported back as the summation of the sizes of all its contents.
     *
     * @param path the path to scan for elements. This must point to a directory
     * @return the top-level {@link DirectoryElement}s.
     * @throws NoSuchFileException   if the given path is not valid
     * @throws NotDirectoryException if the given path points to a file as opposed to a directory.
     */
    public List<DirectoryElement> scan(@Nonnull final Path path) throws NoSuchFileException, NotDirectoryException {

        final File file = path.toFile();

        if (!file.exists()) {
            throw new NoSuchFileException(file.getAbsolutePath());
        }

        if (file.isFile()) {
            throw new NotDirectoryException(file.getAbsolutePath());
        }

        try {
            return Files.list(path).map(this::toDirectoryElement).sorted(fileSizeComparator.reversed()).collect(Collectors.toList());
        } catch (final IOException e) {
            throw new IllegalStateException(String.format("Unexpected exception occurred when listing contents of file %s", file.getAbsoluteFile()), e);
        }

    }

    private DirectoryElement toDirectoryElement(@Nonnull final Path path) {

        final File file = path.toFile();

        return DirectoryElement.builder()
                .absPath(file.getAbsolutePath())
                .type(file.isFile() ? DirectoryElement.Type.FILE : DirectoryElement.Type.DIR)
                .size(Size.builder()
                        .value(FileSizeUnit.BYTES.convert(getSizeInBytes(path), FileSizeUnit.KB))
                        .unit(FileSizeUnit.KB)
                        .build())
                .build();
    }

    private long getSizeInBytes(@Nonnull final Path path) {

        final File file = path.toFile();

        final long size;
        try {
            size = Files.size(path);
        } catch (final IOException e) {
            throw new IllegalStateException("Unexpected exception occurred when reading file size", e);
        }

        if (file.isFile()) {
            return size;
        }

        final Stream<Path> nestedPaths;
        try {
            nestedPaths = Files.list(path);
        } catch (final IOException e) {
            throw new IllegalStateException("Unexpected exception occurred when listing files in path.", e);
        }

        return nestedPaths.mapToLong(this::getSizeInBytes).sum(); // Size of directory is not added, only its contents'
    }
}
