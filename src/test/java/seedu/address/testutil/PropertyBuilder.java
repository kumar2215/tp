package seedu.address.testutil;

import java.util.Optional;

import seedu.address.model.client.ClientName;
import seedu.address.model.commons.Address;
import seedu.address.model.commons.Price;
import seedu.address.model.property.Description;
import seedu.address.model.property.Property;
import seedu.address.model.property.PropertyName;
import seedu.address.model.property.Size;


/**
 * A utility class to help with building Client objects.
 */
public class PropertyBuilder {

    public static final String DEFAULT_PROPERTY_NAME = "Maple Villa Condominium";
    public static final String DEFAULT_ADDRESS = "123 Maple Street";
    public static final Long DEFAULT_PRICE = 2400L;
    public static final String DEFAULT_SIZE = "1000";
    public static final String DEFAULT_DESCRIPTION = "Spacious 4-bedroom home";
    public static final String DEFAULT_OWNER = "Amy Bee";

    private PropertyName propertyName;
    private ClientName owner;
    private Address address;
    private Price price;
    private Optional<Size> size;
    private Optional<Description> description;

    /**
     * Creates a {@code PropertyBuilder} with the default details.
     */
    public PropertyBuilder() {
        propertyName = new PropertyName(DEFAULT_PROPERTY_NAME);
        owner = new ClientName(DEFAULT_OWNER);
        address = new Address(DEFAULT_ADDRESS);
        price = new Price(DEFAULT_PRICE);
        size = Optional.of(new Size(DEFAULT_SIZE));
        description = Optional.of(new Description(DEFAULT_DESCRIPTION));
    }

    /**
     * Initializes the PropertyBuilder with the data of {@code propertyToCopy}.
     */
    public PropertyBuilder(Property propertyToCopy) {
        propertyName = propertyToCopy.getFullName();
        owner = propertyToCopy.getOwner();
        address = propertyToCopy.getAddress();
        price = propertyToCopy.getPrice();
        size = propertyToCopy.getSize();
        description = propertyToCopy.getDescription();
    }

    /**
     * Sets the {@code PropertyName} of the {@code Property} that we are building.
     */
    public PropertyBuilder withPropertyName(String propertyName) {
        this.propertyName = new PropertyName(propertyName);
        return this;
    }

    /**
     * Sets the {@code Owner} of the {@code Property} that we are building.
     */
    public PropertyBuilder withOwner(String owner) {
        this.owner = new ClientName(owner);
        return this;
    }

    /**
     * Sets the {@code Address} of the {@code Property} that we are building.
     */
    public PropertyBuilder withAddress(String address) {
        this.address = new Address(address);
        return this;
    }

    /**
     * Sets the {@code Price} of the {@code Property} that we are building.
     */
    public PropertyBuilder withPrice(Long price) {
        this.price = new Price(price);
        return this;
    }

    /**
     * Sets the {@code Size} of the {@code Property} that we are building.
     */
    public PropertyBuilder withSize(String size) {
        this.size = Optional.of(new Size(size));
        return this;
    }

    /**
     * Sets the {@code Description} of the {@code Property} that we are building.
     */
    public PropertyBuilder withDescription(String description) {
        this.description = Optional.of(new Description(description));
        return this;
    }

    public Property build() {
        return new Property(propertyName, address, price, size, description, owner);
    }
}
