package seedu.address.model.property;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Objects;
import java.util.Optional;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.client.ClientName;
import seedu.address.model.commons.Address;
import seedu.address.model.commons.Nameable;
import seedu.address.model.commons.Price;

/**
 * Represents a Property in the address book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Property implements Nameable<Property> {
    private final PropertyName propertyName;
    private final Address address;
    private final Price price;
    private final Optional<Size> size;
    private final Optional<Description> description;
    private final ClientName owner;

    /**
     * Every field must be present and not null.
     */
    public Property(PropertyName propertyName, Address address, Price price,
                    Optional<Size> size, Optional<Description> description, ClientName owner) {
        requireAllNonNull(propertyName, address, price, size, description, owner);
        this.propertyName = propertyName;
        this.address = address;
        this.price = price;
        this.size = size;
        this.description = description;
        this.owner = owner;
    }

    public PropertyName getFullName() {
        return propertyName;
    }

    public Address getAddress() {
        return address;
    }

    public Price getPrice() {
        return price;
    }

    public Optional<Size> getSize() {
        return size;
    }

    public Optional<Description> getDescription() {
        return description;
    }
    public ClientName getOwner() {
        return owner;
    }

    /**
     * Returns true if both properties have the same propertyName and address.
     * This defines a weaker notion of equality between two properties.
     */
    public boolean isSameProperty(Property otherProperty) {
        if (otherProperty == this) {
            return true;
        }

        return otherProperty != null
                && otherProperty.getFullName().equals(getFullName())
                && otherProperty.getAddress().equals(getAddress());
    }

    /**
     * Returns true if both properties have the same identity and data fields.
     * This defines a stronger notion of equality between two properties.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Property otherProperty)) {
            return false;
        }

        return propertyName.equals(otherProperty.propertyName)
                && address.equals(otherProperty.address)
                && price.equals(otherProperty.price)
                && size.equals(otherProperty.size)
                && description.equals(otherProperty.description)
                && owner.equals(otherProperty.owner);
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(propertyName, address, price, size, description, owner);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("propertyName", propertyName)
                .add("owner", owner)
                .add("address", address)
                .add("price", price)
                .add("size", size)
                .add("description", description)
                .toString();
    }
}
