package seedu.address.testutil;

import java.util.Optional;

import seedu.address.logic.commands.property.EditPropertyCommand.EditPropertyDescriptor;
import seedu.address.model.commons.Address;
import seedu.address.model.commons.Price;
import seedu.address.model.property.Description;
import seedu.address.model.property.PropertyName;
import seedu.address.model.property.Size;

/**
 * A utility class to help with building EditPropertyDescriptor objects.
 */
public class EditPropertyDescriptorBuilder {

    private EditPropertyDescriptor descriptor;

    public EditPropertyDescriptorBuilder() {
        descriptor = new EditPropertyDescriptor();
    }

    public EditPropertyDescriptorBuilder(EditPropertyDescriptor descriptor) {
        this.descriptor = new EditPropertyDescriptor(descriptor);
    }

    /**
     * Sets the {@code PropertyName} of the {@code EditPropertyDescriptor} that we are building.
     */
    public EditPropertyDescriptorBuilder withPropertyName(String propertyName) {
        descriptor.setPropertyName(new PropertyName(propertyName));
        return this;
    }

    /**
     * Sets the {@code Address} of the {@code EditPropertyDescriptor} that we are building.
     */
    public EditPropertyDescriptorBuilder withAddress(String address) {
        descriptor.setAddress(new Address(address));
        return this;
    }

    /**
     * Sets the {@code Price} of the {@code EditPropertyDescriptor} that we are building.
     */
    public EditPropertyDescriptorBuilder withPrice(Long price) {
        descriptor.setPrice(new Price(price));
        return this;
    }

    /**
     * Sets the {@code Size} of the {@code EditPropertyDescriptor} that we are building.
     */
    public EditPropertyDescriptorBuilder withSize(String size) {
        descriptor.setSize(Optional.of(new Size(size)));
        return this;
    }

    /**
     * Sets the {@code Description} of the {@code EditPropertyDescriptor} that we are building.
     */
    public EditPropertyDescriptorBuilder withDescription(String description) {
        descriptor.setDescription(Optional.of(new Description(description)));
        return this;
    }

    public EditPropertyDescriptor build() {
        return descriptor;
    }
}
