// BoardType.java
//
// By Sebastian Raaphorst, 2018.

package com.vorpal.toggle.board;

import com.vorpal.utils.Coordinates;
import com.vorpal.utils.Dimensions;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * Defines the characteristics necessary for different types of boards: this consists of the adjacencies
 * for a given cell.
 */
public enum BoardType {
    GRID(AxisAlignment.NONE, AxisAlignment.NONE) {
        @Override public String typeName() {
            return "Grid";
        }
        @Override public String typeDescription() {
            return "A standard board that can be embedded in a plane.";
        }
    },
    X_CYLINDER(AxisAlignment.LINKED, AxisAlignment.NONE) {
        @Override public String typeName() {
            return "Horizontal cylinder";
        }
        @Override public String typeDescription() {
            return "A board that can be embedded on a horizontal cylinder.";
        }
    },
    Y_CYLINDER(AxisAlignment.NONE, AxisAlignment.LINKED) {
        @Override public String typeName() {
            return "Vertical cylinder";
        }
        @Override public String typeDescription() {
            return "A board that can be embedded on a vertical cylinder.";
        }
    },
    X_MOBIUS_STRIP(AxisAlignment.REVERSE_LINKED, AxisAlignment.NONE) {
        @Override public String typeName() {
            return "Horizontal Mobius strip";
        }
        @Override public String typeDescription() {
            return "A board that can be embedded on a horizontal Mobius strip.";
        }
    },
    Y_MOBIUS_STRIP(AxisAlignment.NONE, AxisAlignment.REVERSE_LINKED) {
        @Override public String typeName() {
            return "Vertical Mobius strip";
        }
        @Override public String typeDescription() {
            return "A board that can be embedded on a vertical Mobius strip.";
        }
    },
    TORUS(AxisAlignment.LINKED, AxisAlignment.LINKED) {
        @Override public String typeName() {
            return "Torus";
        }
        @Override public String typeDescription() {
            return "A board that can be embedded on a torus. (RECOMMENDED)";
        }
    },
    X_KLEIN_BOTTLE(AxisAlignment.REVERSE_LINKED, AxisAlignment.LINKED) {
        @Override public String typeName() {
            return "Horizontal Klein bottle";
        }
        @Override public String typeDescription() {
            return "A board that can be embedded on a horizonal Klein bottle.";
        }
    },
    Y_KLEIN_BOTTLE(AxisAlignment.LINKED, AxisAlignment.REVERSE_LINKED) {
        @Override public String typeName() {
            return "Vertical Klein bottle";
        }
        @Override public String typeDescription() {
            return "A board that can be embedded on a vertical Klein bottle.";
        }
    },
    PROJECTIVE_PLANE(AxisAlignment.REVERSE_LINKED, AxisAlignment.REVERSE_LINKED) {
        @Override public String typeName() {
            return "Projective plane";
        }
        @Override public String typeDescription() {
            return "A board that can beb embedded on a projective plane.";
        }
    };

    private final AxisAlignment xAlign;
    private final AxisAlignment yAlign;

    BoardType(final AxisAlignment xAlign, final AxisAlignment yAlign) {
        this.xAlign = xAlign;
        this.yAlign = yAlign;
    }

    public Optional<Coordinates> convert(final int w, final int h, final int newx, final int newy) {
        // We will look at all four cases:
        // 1. x in bounds, y in bounds.
        if (newx >= 0 && newy >= 0 && newx < w && newy < h)
            return Optional.of(new Coordinates(newx, newy));

            // 2. x out of bounds, y in bounds.
        else if ((newx == -1 || newx == w) && (newy >= 0 && newy < h) && xAlign != AxisAlignment.NONE)
            // Loop x, and flip y if necessary.
            return Optional.of(new Coordinates((newx + w) % w, xAlign == AxisAlignment.LINKED ? newy : h - newy - 1));

            // 3. x in bounds, y out of bounds.
        else if ((newx >= 0 && newx < h) && (newy == -1 || newy == h) && yAlign != AxisAlignment.NONE)
            // Loop y, and flip x if necessary.
            return Optional.of(new Coordinates(yAlign == AxisAlignment.LINKED ? newx : w - newx - 1, (newy + h) % h));

            // 4. x out of bounds, y out of bounds.
        else if ((newx == -1 || newx == w) && (newy == -1 || newy == h)
                && xAlign != AxisAlignment.NONE && yAlign != AxisAlignment.NONE) {
            // This is the most complicated case, worked out painfully by hand.
            // -1 goes to 0
            // 4 goes to 3
            int adjx = xAlign == AxisAlignment.LINKED ?
                    (newx + w) % w :
                    Math.abs(newx) - 1;
            int adjy = yAlign == AxisAlignment.LINKED ?
                    (newy + h) % h :
                    Math.abs(newy) - 1;
            return Optional.of(new Coordinates(adjx, adjy));
        }

        return Optional.empty();
    }

    public Set<Coordinates> adjacencies(final Dimensions d, final Coordinates c) {
        final int h = d.first;
        final int w = d.second;
        final int x = c.first;
        final int y = c.second;

        if (x < 0 || x >= w || y < 0 || y >= h)
            throw new IllegalArgumentException("Illegal coordinates: " + c);

        final var nbrs = new HashSet<Coordinates>();

        // This requires careful handling, so handle each case individually.
        for (var xoffset = -1; xoffset <= 1; ++xoffset) {
            for (var yoffset = -1; yoffset <= 1; ++yoffset) {
                final int newx = x + xoffset;
                final int newy = y + yoffset;

                final var nbr = convert(w, h, newx, newy);
                nbr.ifPresent(nbrs::add);
            }
        }

        return nbrs;
    }

    public abstract String typeName();
    public abstract String typeDescription();
}
