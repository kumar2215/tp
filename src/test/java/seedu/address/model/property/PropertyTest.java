package seedu.address.model.property;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_ORCHID;
import static seedu.address.logic.commands.CommandTestUtil.VALID_DESCRIPTION_ORCHID;
import static seedu.address.logic.commands.CommandTestUtil.VALID_OWNER_ORCHID;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PRICE_ORCHID;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PROPERTY_NAME_ORCHID;
import static seedu.address.logic.commands.CommandTestUtil.VALID_SIZE_ORCHID;
import static seedu.address.testutil.TypicalProperties.MAPLE;
import static seedu.address.testutil.TypicalProperties.ORCHID;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.PropertyBuilder;

public class PropertyTest {

    @Test
    public void isSameProperty() {
        // same object -> returns true
        assertTrue(MAPLE.isSameProperty(MAPLE));

        // null -> returns false
        assertFalse(MAPLE.isSameProperty(null));

        // same name, all other attributes different -> returns false
        Property editedMaple = new PropertyBuilder(MAPLE).withAddress(VALID_ADDRESS_ORCHID)
                .withPrice(VALID_PRICE_ORCHID).withSize(VALID_SIZE_ORCHID)
                .withDescription(VALID_DESCRIPTION_ORCHID).withOwner(VALID_OWNER_ORCHID).build();
        assertFalse(MAPLE.isSameProperty(editedMaple));

        // different name, all other attributes same -> returns false
        editedMaple = new PropertyBuilder(MAPLE).withPropertyName(VALID_PROPERTY_NAME_ORCHID).build();
        assertFalse(MAPLE.isSameProperty(editedMaple));

        // name differs in case, all other attributes same -> returns false
        Property editedOrchid = new PropertyBuilder(ORCHID).withPropertyName(VALID_PROPERTY_NAME_ORCHID.toLowerCase())
                .build();
        assertFalse(ORCHID.isSameProperty(editedOrchid));

        // name has trailing spaces, all other attributes same -> returns false
        String nameWithTrailingSpaces = VALID_PROPERTY_NAME_ORCHID + " ";
        editedOrchid = new PropertyBuilder(ORCHID).withPropertyName(nameWithTrailingSpaces).build();
        assertFalse(ORCHID.isSameProperty(editedOrchid));
    }

    @Test
    public void equals() {
        // same values -> returns true
        Property mapleCopy = new PropertyBuilder(MAPLE).build();
        assertTrue(MAPLE.equals(mapleCopy));

        // same object -> returns true
        assertTrue(MAPLE.equals(MAPLE));

        // null -> returns false
        assertFalse(MAPLE.equals(null));

        // different type -> returns false
        assertFalse(MAPLE.equals(5));

        // different client -> returns false
        assertFalse(MAPLE.equals(ORCHID));

        // different name -> returns false
        Property editedMaple = new PropertyBuilder(MAPLE).withPropertyName(VALID_PROPERTY_NAME_ORCHID).build();
        assertFalse(MAPLE.equals(editedMaple));

        // different price -> returns false
        editedMaple = new PropertyBuilder(MAPLE).withPrice(VALID_PRICE_ORCHID).build();
        assertFalse(MAPLE.equals(editedMaple));

        // different size -> returns false
        editedMaple = new PropertyBuilder(MAPLE).withSize(VALID_SIZE_ORCHID).build();
        assertFalse(MAPLE.equals(editedMaple));

        // different address -> returns false
        editedMaple = new PropertyBuilder(MAPLE).withAddress(VALID_ADDRESS_ORCHID).build();
        assertFalse(MAPLE.equals(editedMaple));

        // different description -> returns false
        editedMaple = new PropertyBuilder(MAPLE).withDescription(VALID_DESCRIPTION_ORCHID).build();
        assertFalse(MAPLE.equals(editedMaple));

        // different owner -> returns false
        editedMaple = new PropertyBuilder(MAPLE).withOwner(VALID_OWNER_ORCHID).build();
        assertFalse(MAPLE.equals(editedMaple));
    }

    @Test
    public void toStringMethod() {
        String expected = Property.class.getCanonicalName() + "{propertyName=" + MAPLE.getFullName()
                + ", owner=" + MAPLE.getOwner() + ", address=" + MAPLE.getAddress()
                + ", price=" + MAPLE.getPrice() + ", size=" + MAPLE.getSize() + ", description="
                + MAPLE.getDescription() + "}";
        assertEquals(expected, MAPLE.toString());
    }
}
