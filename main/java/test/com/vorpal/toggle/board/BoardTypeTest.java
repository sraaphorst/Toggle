// BoardTypeTest.java
//
// By Sebastian Raaphorst, 2018.

package com.vorpal.toggle.board;

import com.vorpal.utils.Coordinates;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test the BoardType.convert function to make sure it is returning the precalculated values in the PNG table in the
 * base documentation directory, board_calculations.
 */
class BoardTypeTest {
    private boolean inBounds(int v) {
        return v >= 0 && v < 4;
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private void check(final Optional<Coordinates> c, final int xVal, final int yVal) {
        assertTrue(c.filter(xy -> xy.first == xVal && xy.second == yVal).isPresent());
    }

    @Test
    @DisplayName("Test GRID")
    void testGrid() {
        for (var x = -1; x <= 4; ++x)
            for (var y = -1; y <= 4; ++y) {
                final var c = BoardType.GRID.convert(4, 4, x, y);
                if (inBounds(x) && inBounds(y)) {
                    final var e = new Coordinates(x, y);
                    assertTrue(c.map(e::equals).orElse(false));
                } else
                    assertFalse(c.isPresent());
            }
    }

    @Test
    @DisplayName("Test X_CYLINDER")
    void testXCylinder() {
        for (var x = -1; x <= 4; ++x)
            for (var y = -1; y <= 4; ++y) {
                final var c = BoardType.X_CYLINDER.convert(4, 4, x, y);
                if (inBounds(y)) {
                    final var e = new Coordinates((x + 4) % 4, y);
                    assertTrue(c.map(e::equals).orElse(false));
                } else
                    assertFalse(c.isPresent());
            }
    }

    @Test
    @DisplayName("Test Y_CYLINDER")
    void testYCylinder() {
        for (var x = -1; x <= 4; ++x)
            for (var y = -1; y <= 4; ++y) {
                final var c = BoardType.Y_CYLINDER.convert(4, 4, x, y);
                if (inBounds(x)) {
                    final var e = new Coordinates(x, (y + 4) % 4);
                    assertTrue(c.map(e::equals).orElse(false));
                } else
                    assertFalse(c.isPresent());
            }
    }

    @Test
    @DisplayName("Test X_MOBIUS_STRIP")
    void testXMobiusStrip() {
        for (var x = -1; x <= 4; ++x)
            for (var y = -1; y <= 4; ++y) {
                final var c = BoardType.X_MOBIUS_STRIP.convert(4, 4, x, y);
                if (inBounds(y)) {
                    if (x == -1 && y == 0) check(c, 3, 3);
                    if (x == -1 && y == 1) check(c, 3, 2);
                    if (x == -1 && y == 2) check(c, 3, 1);
                    if (x == -1 && y == 3) check(c, 3, 0);
                    if (x ==  4 && y == 0) check(c, 0, 3);
                    if (x ==  4 && y == 1) check(c, 0, 2);
                    if (x ==  4 && y == 2) check(c, 0, 1);
                    if (x ==  4 && y == 3) check(c, 0, 0);
                } else
                    assertFalse(c.isPresent());
            }
    }

    @Test
    @DisplayName("Test Y_MOBIUS_STRIP")
    void testYMobiusStrip() {
        for (var x = -1; x <= 4; ++x)
            for (var y = -1; y <= 4; ++y) {
                final var c = BoardType.Y_MOBIUS_STRIP.convert(4, 4, x, y);
                if (inBounds(x)) {
                    if (x == 0 && y == -1) check(c, 3, 3);
                    if (x == 1 && y == -1) check(c, 2, 3);
                    if (x == 2 && y == -1) check(c, 1, 3);
                    if (x == 3 && y == -1) check(c, 0, 3);
                    if (x == 0 && y ==  4) check(c, 3, 0);
                    if (x == 1 && y ==  4) check(c, 2, 0);
                    if (x == 2 && y ==  4) check(c, 1, 0);
                    if (x == 3 && y ==  4) check(c, 0, 0);
                } else
                    assertFalse(c.isPresent());
            }
    }

    @Test
    @DisplayName("Test TORUS")
    void testTorus() {
        for (var x = -1; x <= 4; ++x)
            for (var y = -1; y <= 4; ++y) {
                final var c = BoardType.TORUS.convert(4, 4, x, y);
                if (x ==  0 && y == -1) check(c, 0, 3);
                if (x ==  1 && y == -1) check(c, 1, 3);
                if (x ==  2 && y == -1) check(c, 2, 3);
                if (x ==  3 && y == -1) check(c, 3, 3);

                if (x ==  0 && y ==  4) check(c, 0, 0);
                if (x ==  1 && y ==  4) check(c, 1, 0);
                if (x ==  2 && y ==  4) check(c, 2, 0);
                if (x ==  3 && y ==  4) check(c, 3, 0);

                if (x == -1 && y ==  0) check(c, 3, 0);
                if (x == -1 && y ==  1) check(c, 3, 1);
                if (x == -1 && y ==  2) check(c, 3, 2);
                if (x == -1 && y ==  3) check(c, 3, 3);

                if (x ==  4 && y ==  0) check(c, 0, 0);
                if (x ==  4 && y ==  1) check(c, 0, 1);
                if (x ==  4 && y ==  2) check(c, 0, 2);
                if (x ==  4 && y ==  3) check(c, 0, 3);

                if (x == -1 && y == -1) check(c, 3, 3);
                if (x ==  4 && y == -1) check(c, 0, 3);
                if (x == -1 && y ==  4) check(c, 3, 0);
                if (x ==  4 && y ==  4) check(c, 0, 0);
            }
    }

    @Test
    @DisplayName("Test X_KLEIN_BOTTLE")
    void testXKleinBottle() {
        for (var x = -1; x <= 4; ++x)
            for (var y = -1; y <= 4; ++y) {
                final var c = BoardType.X_KLEIN_BOTTLE.convert(4, 4, x, y);
                if (x ==  0 && y == -1) check(c, 0, 3);
                if (x ==  1 && y == -1) check(c, 1, 3);
                if (x ==  2 && y == -1) check(c, 2, 3);
                if (x ==  3 && y == -1) check(c, 3, 3);

                if (x ==  0 && y ==  4) check(c, 0, 0);
                if (x ==  1 && y ==  4) check(c, 1, 0);
                if (x ==  2 && y ==  4) check(c, 2, 0);
                if (x ==  3 && y ==  4) check(c, 3, 0);

                if (x == -1 && y ==  0) check(c, 3, 3);
                if (x == -1 && y ==  1) check(c, 3, 2);
                if (x == -1 && y ==  2) check(c, 3, 1);
                if (x == -1 && y ==  3) check(c, 3, 0);

                if (x ==  4 && y ==  0) check(c, 0, 3);
                if (x ==  4 && y ==  1) check(c, 0, 2);
                if (x ==  4 && y ==  2) check(c, 0, 1);
                if (x ==  4 && y ==  3) check(c, 0, 0);

                if (x == -1 && y == -1) check(c, 3, 0);
                if (x ==  4 && y == -1) check(c, 0, 0);
                if (x == -1 && y ==  4) check(c, 3, 3);
                if (x ==  4 && y ==  4) check(c, 0, 3);
            }
    }

    @Test
    @DisplayName("Test Y_KLEIN_BOTTLE")
    void testYKleinBottle() {
        for (var x = -1; x <= 4; ++x)
            for (var y = -1; y <= 4; ++y) {
                final var c = BoardType.Y_KLEIN_BOTTLE.convert(4, 4, x, y);
                if (x ==  0 && y == -1) check(c, 3, 3);
                if (x ==  1 && y == -1) check(c, 2, 3);
                if (x ==  2 && y == -1) check(c, 1, 3);
                if (x ==  3 && y == -1) check(c, 0, 3);

                if (x ==  0 && y ==  4) check(c, 3, 0);
                if (x ==  1 && y ==  4) check(c, 2, 0);
                if (x ==  2 && y ==  4) check(c, 1, 0);
                if (x ==  3 && y ==  4) check(c, 0, 0);

                if (x == -1 && y ==  0) check(c, 3, 0);
                if (x == -1 && y ==  1) check(c, 3, 1);
                if (x == -1 && y ==  2) check(c, 3, 2);
                if (x == -1 && y ==  3) check(c, 3, 3);

                if (x ==  4 && y ==  0) check(c, 0, 0);
                if (x ==  4 && y ==  1) check(c, 0, 1);
                if (x ==  4 && y ==  2) check(c, 0, 2);
                if (x ==  4 && y ==  3) check(c, 0, 3);

                if (x == -1 && y == -1) check(c, 0, 3);
                if (x ==  4 && y == -1) check(c, 3, 3);
                if (x == -1 && y ==  4) check(c, 0, 0);
                if (x ==  4 && y ==  4) check(c, 3, 0);
            }
    }

    @Test
    @DisplayName("Test PROJECTIVE_PLANE")
    void testProjectivePlane() {
        for (var x = -1; x <= 4; ++x)
            for (var y = -1; y <= 4; ++y) {
                final var c = BoardType.PROJECTIVE_PLANE.convert(4, 4, x, y);
                if (x ==  0 && y == -1) check(c, 3, 3);
                if (x ==  1 && y == -1) check(c, 2, 3);
                if (x ==  2 && y == -1) check(c, 1, 3);
                if (x ==  3 && y == -1) check(c, 0, 3);

                if (x ==  0 && y ==  4) check(c, 3, 0);
                if (x ==  1 && y ==  4) check(c, 2, 0);
                if (x ==  2 && y ==  4) check(c, 1, 0);
                if (x ==  3 && y ==  4) check(c, 0, 0);

                if (x == -1 && y ==  0) check(c, 3, 3);
                if (x == -1 && y ==  1) check(c, 3, 2);
                if (x == -1 && y ==  2) check(c, 3, 1);
                if (x == -1 && y ==  3) check(c, 3, 0);

                if (x ==  4 && y ==  0) check(c, 0, 3);
                if (x ==  4 && y ==  1) check(c, 0, 2);
                if (x ==  4 && y ==  2) check(c, 0, 1);
                if (x ==  4 && y ==  3) check(c, 0, 0);

                if (x == -1 && y == -1) check(c, 0, 0);
                if (x ==  4 && y == -1) check(c, 3, 0);
                if (x == -1 && y ==  4) check(c, 0, 3);
                if (x ==  4 && y ==  4) check(c, 3, 3);
            }
    }
}
