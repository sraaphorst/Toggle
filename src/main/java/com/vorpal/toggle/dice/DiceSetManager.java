// DiceSetManager.java
//
// By Sebastian Raaphorst, 2018.

package com.vorpal.toggle.dice;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Objects;

/**
 * This manages the dice sets to be used.
 * By default, it must include one set, so if an attempt to get it is made and it is empty, it will include the
 * DefaultDiceSet as defined in:
 * @see DefaultDiceSets
 *
 * As we intend to use this directly with a JavaFX ComboBox, we manage dice using an ObservableList.
 */
public final class DiceSetManager {
    /**
     * The collection of dice sets to offer. By default, this should be configured by the application, but dice sets
     * are offered in DefaultDiceSets.
     */
    private static ObservableList<DiceSet> diceSets = FXCollections.observableArrayList();

    /**
     * This should only be used statically.
     */
    private DiceSetManager() {}

    /**
     * Register a dice set with the list.
     * @param diceSet the dice set to register
     */
    public static void registerDiceSet(final DiceSet diceSet) {
        Objects.requireNonNull(diceSet);
        if (!diceSets.contains(diceSet))
            diceSets.add(diceSet);
    }

    /**
     * Unregister a dice set from the list
     * @param diceSet the dice set to unregister
     * @return true if removed, and false otherwise
     */
    public static boolean unregisterDiceSet(final DiceSet diceSet) {
        if (diceSet == null)
            return false;
        return diceSets.remove(diceSet);
    }

    /**
     * Return an immutable view to this observable list.
     * @return immutable view to list
     */
    public static ObservableList<DiceSet> getDiceSets() {
        if (diceSets.isEmpty())
            diceSets.add(DefaultDiceSets.DEFAULT_16_DICE_SET);
        return FXCollections.unmodifiableObservableList(diceSets);
    }
}
