package com.dirscanner.elem;

import lombok.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * An enumeration of the supported File Sizes.
 */
public enum FileSizeUnit {

    // IMP: Declare units from smallest to largest.
    BYTES,
    KB;

    private static final Units UNITS = new Units(FileSizeUnit.values());

    /**
     * Convert the given {@code value} specified in this {@link FileSizeUnit} to a value specified in the target {@link FileSizeUnit}.
     * <p>
     * Note that the only conversion currently supported is that from a small unit to a larger unit.
     *
     * @param value  a value specified in this {@link FileSizeUnit}
     * @param target the target {@link FileSizeUnit} in which the given value is to be converted.
     * @return the value specified in the target {@link FileSizeUnit}.
     * @throws UnsupportedOperationException if conversion is requested from a large {@link FileSizeUnit} to a smaller {@link FileSizeUnit}.
     */
    public double convert(final double value, @NonNull final FileSizeUnit target) {

        if (this == target) {
            return value;
        }

        final List<FileSizeUnit> nextUnits = UNITS.getNextUnitsInAscOrder(this);

        if (!nextUnits.contains(target)) {
            throw new UnsupportedOperationException(String.format("Conversion from %s to %s not supported.", this, target));
        }

        double convertedValue = value;
        for (int i = 0; i <= nextUnits.indexOf(target); i++) {
            convertedValue /= 1024;
        }

        return convertedValue;
    }

    private static final class Units {

        private final List<FileSizeUnit> unitsInAscOrder = new ArrayList<>();

        Units(final FileSizeUnit[] unitsInAscOrder) {
            this.unitsInAscOrder.addAll(Arrays.stream(unitsInAscOrder).collect(Collectors.toList()));
        }

        /**
         * @return a list of {@link FileSizeUnit}s, in ascending order, which are larger than the given {@link FileSizeUnit}
         */
        List<FileSizeUnit> getNextUnitsInAscOrder(final FileSizeUnit unit) {

            if (!unitsInAscOrder.contains(unit)) {
                return Collections.emptyList();
            }

            return Collections.unmodifiableList(unitsInAscOrder.stream().skip(unitsInAscOrder.indexOf(unit) + 1).collect(Collectors.toList()));
        }

    }
}
