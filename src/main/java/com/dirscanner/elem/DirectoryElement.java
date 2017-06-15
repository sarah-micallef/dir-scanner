package com.dirscanner.elem;

import lombok.Builder;
import lombok.Getter;

/**
 *
 * A representation of a Directory Element, which can either be a file or a directory itself.
 */
@Builder()
public class DirectoryElement {

    @Getter
    private Type type;

    @Getter
    private String absPath;

    @Getter
    private Size size;

    @Override
    public String toString() {
        return String.format("%s %s %s", type.name(), absPath, size);
    }

    public enum Type {
        DIR,
        FILE;
    }

    @lombok.Builder
    public static class Size {

        @Getter
        private double value;

        @Getter
        private FileSizeUnit unit;

        @Override
        public String toString() {
            return String.format("%.0f%s", value, unit);
        }
    }

}