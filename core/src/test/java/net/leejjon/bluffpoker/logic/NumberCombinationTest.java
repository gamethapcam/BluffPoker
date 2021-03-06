package net.leejjon.bluffpoker.logic;

import org.junit.Test;

import static org.junit.Assert.*;

public class NumberCombinationTest {
    @Test
    public void testCompareToCalls() throws net.leejjon.bluffpoker.logic.InputValidationException {
        NumberCombination call643 = NumberCombination.validNumberCombinationFrom("643");
        NumberCombination call634 = NumberCombination.validNumberCombinationFrom("634");
        NumberCombination throw634 = new NumberCombination(6, 3, 4, true);
        NumberCombination throw643 = new NumberCombination(6, 4, 3, true);

        assertTrue(call643.equals(call643));
        assertFalse(call643.equals(call634));
        assertTrue(call643.isGreaterThan(call634));
        assertFalse(call643.isGreaterThan(call643));
        assertFalse(call634.isGreaterThan(call643));
        assertFalse(call643.isGreaterThan(throw634));
        assertFalse(throw643.isGreaterThan(throw634));

        try {
            NumberCombination.validNumberCombinationFrom("abc");
            fail("InputValidationException should have been thrown.");
        } catch (InputValidationException e) { }
    }

    @Test
    public void testValidateNumberCombinationInput() {
        assertTrue(NumberCombination.validateNumberCombinationInput("643"));
        assertFalse(NumberCombination.validateNumberCombinationInput("781"));
        assertFalse(NumberCombination.validateNumberCombinationInput("abc"));
    }

    @Test
    public void testValidateNumberCombinationConstructor() {
        try {
            new NumberCombination(7, 1, 1, false);
            fail("Can't have anything higher than 6.");
        } catch (IllegalArgumentException e) { }

        try {
            new NumberCombination(-1, 1, 1, false);
            fail("Can't have numbers lower than 0.");
        } catch (IllegalArgumentException e) { }
    }

    @Test
    public void testIncrementing() {
        NumberCombination call643 = new NumberCombination(6,4,3, true);

        NumberCombination incrementTo644 = call643.increment();
        assertEquals(new NumberCombination(6,4,4, true), incrementTo644);

        NumberCombination incrementTo661 = new NumberCombination(6,5,5, true).increment();
        assertEquals(new NumberCombination(6, 6, 1, true), incrementTo661);
    }
}
