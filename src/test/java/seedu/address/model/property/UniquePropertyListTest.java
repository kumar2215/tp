package seedu.address.model.property;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import seedu.address.model.client.ClientName;
import seedu.address.model.commons.Address;
import seedu.address.model.commons.Price;
import seedu.address.model.property.exceptions.DuplicatePropertyException;
import seedu.address.model.property.exceptions.PropertyNotFoundException;

public class UniquePropertyListTest {
    private final UniquePropertyList uniquePropertyList = new UniquePropertyList();
    private final Property property1 = new Property(new PropertyName("Maple Villa"), new Address("123 Maple St"),
            new Price(2400L), Optional.of(new Size("1000")), Optional.of(new Description("Spacious 3-bedroom")),
            new ClientName("Amy Bee")
    );
    private final Property property2 = new Property(new PropertyName("Orchid Gardens"), new Address("234 Orchid St"),
            new Price(1500L), Optional.of(new Size("800")), Optional.of(new Description("2-bedroom apartment")),
            new ClientName("Bob Choo")
    );

    @Test
    public void add_uniqueProperty_success() {
        uniquePropertyList.add(property1);

        assertEquals(1, uniquePropertyList.asUnmodifiableObservableList().size());
        assertEquals(property1, uniquePropertyList.asUnmodifiableObservableList().get(0));
    }

    @Test
    public void add_duplicateProperty_throwsDuplicatePropertyException() {
        uniquePropertyList.add(property1);

        assertThrows(DuplicatePropertyException.class, () -> uniquePropertyList.add(property1));
    }

    @Test
    public void remove_property_success() {
        uniquePropertyList.add(property1);
        uniquePropertyList.remove(property1);

        assertEquals(0, uniquePropertyList.asUnmodifiableObservableList().size());
    }

    @Test
    public void remove_nonExistingProperty_throwsPropertyNotFoundException() {
        // removing a property that does not exist in the list
        assertThrows(PropertyNotFoundException.class, () -> uniquePropertyList.remove(property2));
    }

    @Test
    public void setProperty_replaceProperty_success() {
        uniquePropertyList.add(property1);

        // replace the property with a new one
        uniquePropertyList.setProperty(property1, property2);

        assertEquals(1, uniquePropertyList.asUnmodifiableObservableList().size());
        assertEquals(property2, uniquePropertyList.asUnmodifiableObservableList().get(0));
    }

    @Test
    public void setProperty_duplicateProperty_throwsDuplicatePropertyException() {
        uniquePropertyList.add(property1);
        uniquePropertyList.add(property2);

        assertThrows(DuplicatePropertyException.class, () -> uniquePropertyList.setProperty(property1, property2));
    }

    @Test
    public void setProperties_listContainsDuplicates_throwsDuplicatePropertyException() {
        assertThrows(DuplicatePropertyException.class, () -> uniquePropertyList
                .setProperties(List.of(property1, property1)));
    }
}
