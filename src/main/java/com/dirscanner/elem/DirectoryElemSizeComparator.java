package com.dirscanner.elem;

import com.dirscanner.elem.DirectoryElement;
import lombok.NonNull;

import java.util.Comparator;

/**
 * A {@link Comparator} that compares two {@link DirectoryElement}s on the basis of their size.
 *
 * Such comparison is not supported if the given elems have different file size units.
 *
 * Also note that this comparator does not support null arguments.
 */
public class DirectoryElemSizeComparator implements Comparator<DirectoryElement> {

    @Override
    public int compare(@NonNull final DirectoryElement o1, @NonNull final DirectoryElement o2) {

        if (!o1.getSize().getUnit().equals(o2.getSize().getUnit())) {
            throw new IllegalStateException(
                    String.format("Cannot compare directory elements %s and %s due to different file size units.", o1, o2));
        }

        return Double.compare(o1.getSize().getValue(), o2.getSize().getValue());
    }
}
