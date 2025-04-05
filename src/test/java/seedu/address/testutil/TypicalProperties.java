package seedu.address.testutil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seedu.address.model.AddressBook;
import seedu.address.model.property.Property;

/**
 * A utility class containing a list of {@code Property} objects to be used in tests.
 */
public class TypicalProperties {

    public static final Property MAPLE = new PropertyBuilder().withPropertyName("Maple Villa Condominium")
            .withAddress("123 Maple Street").withPrice(2400L)
            .withSize("1000")
            .withDescription("Spacious 4-bedroom home")
            .withOwner("Amy Bee").build();
    public static final Property ORCHID = new PropertyBuilder().withPropertyName("Orchid Gardens Condominium")
            .withAddress("234 Orchid Street")
            .withPrice(1200L).withSize("500")
            .withDescription("Spacious 2-bedroom home")
            .withOwner("Bob Choo").build();
    public static final Property JURONG = new PropertyBuilder().withPropertyName("Jurong Lake Gardens HDB")
            .withAddress("336 Tah Ching Rd")
            .withPrice(600L).withSize("600")
            .withDescription("Spacious 3-bedroom home")
            .withOwner("James Tee").build();
    public static final Property PUNGGOL = new PropertyBuilder().withPropertyName("Punggol Waterway Ridges HDB")
            .withAddress("670C Edgefield Plains")
            .withPrice(900L).withSize("1200")
            .withDescription("Spacious 5-bedroom home")
            .withOwner("John Walker").build();

    private TypicalProperties() {} // prevents instantiation

    /**
     * Returns an {@code AddressBook} with all the typical properties.
     */
    public static AddressBook getTypicalAddressBook() {
        AddressBook ab = new AddressBook();
        for (Property property : getTypicalProperties()) {
            ab.addProperty(property);
        }
        return ab;
    }

    public static List<Property> getTypicalProperties() {
        return new ArrayList<>(Arrays.asList(MAPLE, ORCHID, JURONG, PUNGGOL));
    }
}
