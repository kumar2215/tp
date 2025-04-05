package seedu.address.logic.commands.property;

import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.AddCommandTest;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.ModelStub;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.client.Client;
import seedu.address.model.property.Property;
import seedu.address.testutil.ClientBuilder;
import seedu.address.testutil.PropertyBuilder;

/**
 * Contains unit tests for {@link AddPropertyCommand}.
 */
public class AddPropertyCommandTest extends AddCommandTest<Property> {

    @Test
    public void execute_propertyAcceptedByModel_addSuccessful() throws Exception {
        ModelStubAcceptingPropertyAdded modelStub = new ModelStubAcceptingPropertyAdded();
        // Build a valid property
        Property validProperty = new PropertyBuilder().build();

        // Create the AddPropertyCommand with an Index for the client
        AddPropertyCommand command = new AddPropertyCommand(
                validProperty.getFullName(),
                validProperty.getAddress(),
                validProperty.getPrice(),
                validProperty.getSize(),
                validProperty.getDescription(),
                Index.fromOneBased(1)
        );

        CommandResult commandResult = command.execute(modelStub);

        // Verify success message and that property was actually added
        assertEquals(String.format(AddPropertyCommand.MESSAGE_SUCCESS, Messages.formatProperty(validProperty)),
                commandResult.getFeedbackToUser());
        assertEquals(Arrays.asList(validProperty), modelStub.propertiesAdded);
    }

    @Test
    public void execute_duplicateProperty_throwsCommandException() {
        // Build a property and put it in the ModelStub
        Property validProperty = new PropertyBuilder().build();
        AddPropertyCommand command = new AddPropertyCommand(
            validProperty.getFullName(),
            validProperty.getAddress(),
            validProperty.getPrice(),
            validProperty.getSize(),
            validProperty.getDescription(),
            Index.fromOneBased(1)
        );
        ModelStub modelStub = new ModelStubWithProperty(validProperty);

        // Trying to add the same property should throw an exception
        assertThrows(CommandException.class, AddPropertyCommand.MESSAGE_DUPLICATE_PROPERTY, () -> {
            command.execute(modelStub);
        });
    }

    @Test
    public void equals() {
        // Two different properties
        Property maple = new PropertyBuilder().withPropertyName("Maple").build();
        Property orchid = new PropertyBuilder().withPropertyName("Orchid").build();

        // Two AddPropertyCommands using different property details
        AddPropertyCommand addMapleCommand = new AddPropertyCommand(
                maple.getFullName(),
                maple.getAddress(),
                maple.getPrice(),
                maple.getSize(),
                maple.getDescription(),
                Index.fromOneBased(1)
        );
        AddPropertyCommand addOrchidCommand = new AddPropertyCommand(
                orchid.getFullName(),
                orchid.getAddress(),
                orchid.getPrice(),
                orchid.getSize(),
                orchid.getDescription(),
                Index.fromOneBased(1)
        );

        // same object -> returns true
        assertTrue(addMapleCommand.equals(addMapleCommand));

        // same values -> returns true
        AddPropertyCommand addMapleCommandCopy = new AddPropertyCommand(
                maple.getFullName(),
                maple.getAddress(),
                maple.getPrice(),
                maple.getSize(),
                maple.getDescription(),
                Index.fromOneBased(1)
        );
        assertTrue(addMapleCommand.equals(addMapleCommandCopy));

        // different types -> returns false
        assertFalse(addMapleCommand.equals(1));

        // null -> returns false
        assertFalse(addMapleCommand.equals(null));

        // different property -> returns false
        assertFalse(addMapleCommand.equals(addOrchidCommand));
    }

    /**
     * A Model stub that contains a single property.
     */
    private class ModelStubWithProperty extends ModelStub {
        private final Property property;

        ModelStubWithProperty(Property property) {
            requireNonNull(property);
            this.property = property;
        }

        @Override
        public boolean hasProperty(Property property) {
            requireNonNull(property);
            return this.property.isSameProperty(property);
        }

        @Override
        public ObservableList<Client> getFilteredClientList() {
            // Return a single dummy Client in an observable list
            return FXCollections.observableArrayList(
                new ClientBuilder().withClientName("Amy Bee").build()
            );
        }
    }

    /**
     * A Model stub that always accepts the property being added.
     */
    private class ModelStubAcceptingPropertyAdded extends ModelStub {
        final ArrayList<Property> propertiesAdded = new ArrayList<>();

        @Override
        public boolean hasProperty(Property property) {
            requireNonNull(property);
            // Return true if this property was already added
            return propertiesAdded.stream().anyMatch(property::isSameProperty);
        }

        @Override
        public void addProperty(Property property) {
            requireNonNull(property);
            propertiesAdded.add(property);
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            return new AddressBook();
        }

        // Provide a dummy client list so Index.fromOneBased(1) won't be out of range
        @Override
        public ObservableList<Client> getFilteredClientList() {
            return FXCollections.observableArrayList(
                new ClientBuilder().withClientName("Amy Bee").build()
            );
        }
    }
}
