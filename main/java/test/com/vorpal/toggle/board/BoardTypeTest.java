// BoardTypeTest.java
//
// By Sebastian Raaphorst, 2018.

package com.vorpal.toggle.board;

import com.vorpal.utils.Coordinates;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test the BoardType.convert function to make sure it is returning the calculated values.
 */
class BoardTypeTest {
    private boolean inBounds(int v) {
        return v >= 0 && v < 4;
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
    void testXMobius() {
        for (var x = -1; x <= 4; ++x)
            for (var y = -1; y <= 4; ++y) {
                final var c = BoardType.X_MOBIUS_STRIP.convert(4, 4, x, y);
                if (inBounds(y)) {
                    if (x == -1 && y == 0) assertTrue(c.filter(xy -> xy.first == 3 && xy.second == 3).isPresent());
                    if (x == -1 && y == 1) assertTrue(c.filter(xy -> xy.first == 3 && xy.second == 2).isPresent());
                    if (x == -1 && y == 2) assertTrue(c.filter(xy -> xy.first == 3 && xy.second == 1).isPresent());
                    if (x == -1 && y == 3) assertTrue(c.filter(xy -> xy.first == 3 && xy.second == 0).isPresent());
                    if (x ==  4 && y == 0) assertTrue(c.filter(xy -> xy.first == 0 && xy.second == 3).isPresent());
                    if (x ==  4 && y == 1) assertTrue(c.filter(xy -> xy.first == 0 && xy.second == 2).isPresent());
                    if (x ==  4 && y == 2) assertTrue(c.filter(xy -> xy.first == 0 && xy.second == 1).isPresent());
                    if (x ==  4 && y == 3) assertTrue(c.filter(xy -> xy.first == 0 && xy.second == 0).isPresent());
                } else
                    assertFalse(c.isPresent());
            }
    }

    @Test
    @DisplayName("Test Y_MOBIUS_STRIP")
    void testYMobius() {
        for (var x = -1; x <= 4; ++x)
            for (var y = -1; y <= 4; ++y) {
                final var c = BoardType.Y_MOBIUS_STRIP.convert(4, 4, x, y);
                if (inBounds(x)) {
                    if (x == 0 && y == -1) assertTrue(c.filter(xy -> xy.first == 3 && xy.second == 3).isPresent());
                    if (x == 1 && y == -1) assertTrue(c.filter(xy -> xy.first == 2 && xy.second == 3).isPresent());
                    if (x == 2 && y == -1) assertTrue(c.filter(xy -> xy.first == 1 && xy.second == 3).isPresent());
                    if (x == 3 && y == -1) assertTrue(c.filter(xy -> xy.first == 0 && xy.second == 3).isPresent());
                    if (x == 0 && y ==  4) assertTrue(c.filter(xy -> xy.first == 3 && xy.second == 0).isPresent());
                    if (x == 1 && y ==  4) assertTrue(c.filter(xy -> xy.first == 2 && xy.second == 0).isPresent());
                    if (x == 2 && y ==  4) assertTrue(c.filter(xy -> xy.first == 1 && xy.second == 0).isPresent());
                    if (x == 3 && y ==  4) assertTrue(c.filter(xy -> xy.first == 0 && xy.second == 0).isPresent());
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
                if (x ==  0 && y == -1) assertTrue(c.filter(xy -> xy.first == 0 && xy.second == 3).isPresent());
                if (x ==  1 && y == -1) assertTrue(c.filter(xy -> xy.first == 1 && xy.second == 3).isPresent());
                if (x ==  2 && y == -1) assertTrue(c.filter(xy -> xy.first == 2 && xy.second == 3).isPresent());
                if (x ==  3 && y == -1) assertTrue(c.filter(xy -> xy.first == 3 && xy.second == 3).isPresent());

                if (x ==  0 && y ==  4) assertTrue(c.filter(xy -> xy.first == 0 && xy.second == 0).isPresent());
                if (x ==  1 && y ==  4) assertTrue(c.filter(xy -> xy.first == 1 && xy.second == 0).isPresent());
                if (x ==  2 && y ==  4) assertTrue(c.filter(xy -> xy.first == 2 && xy.second == 0).isPresent());
                if (x ==  3 && y ==  4) assertTrue(c.filter(xy -> xy.first == 3 && xy.second == 0).isPresent());

                if (x == -1 && y ==  0) assertTrue(c.filter(xy -> xy.first == 3 && xy.second == 0).isPresent());
                if (x == -1 && y ==  1) assertTrue(c.filter(xy -> xy.first == 3 && xy.second == 1).isPresent());
                if (x == -1 && y ==  2) assertTrue(c.filter(xy -> xy.first == 3 && xy.second == 2).isPresent());
                if (x == -1 && y ==  3) assertTrue(c.filter(xy -> xy.first == 3 && xy.second == 3).isPresent());

                if (x ==  4 && y ==  0) assertTrue(c.filter(xy -> xy.first == 0 && xy.second == 0).isPresent());
                if (x ==  4 && y ==  1) assertTrue(c.filter(xy -> xy.first == 0 && xy.second == 1).isPresent());
                if (x ==  4 && y ==  2) assertTrue(c.filter(xy -> xy.first == 0 && xy.second == 2).isPresent());
                if (x ==  4 && y ==  3) assertTrue(c.filter(xy -> xy.first == 0 && xy.second == 3).isPresent());

                if (x == -1 && y == -1) assertTrue(c.filter(xy -> xy.first == 3 && xy.second == 3).isPresent());
                if (x ==  4 && y == -1) assertTrue(c.filter(xy -> xy.first == 0 && xy.second == 3).isPresent());
                if (x == -1 && y ==  4) assertTrue(c.filter(xy -> xy.first == 3 && xy.second == 0).isPresent());
                if (x ==  4 && y ==  4) assertTrue(c.filter(xy -> xy.first == 0 && xy.second == 0).isPresent());
            }
    }
}
