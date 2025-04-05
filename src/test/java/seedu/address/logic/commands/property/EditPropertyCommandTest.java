package seedu.address.logic.commands.property;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.DESC_MAPLE;
import static seedu.address.logic.commands.CommandTestUtil.DESC_ORCHID;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PROPERTY_NAME_ORCHID;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showPropertyAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND;
import static seedu.address.testutil.TypicalProperties.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.EditCommandTest;
import seedu.address.logic.commands.HelpCommand;
import seedu.address.logic.commands.property.EditPropertyCommand.EditPropertyDescriptor;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.property.Property;
import seedu.address.testutil.EditPropertyDescriptorBuilder;
import seedu.address.testutil.PropertyBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for EditPropertyCommand.
 */
public class EditPropertyCommandTest extends EditCommandTest<Property> {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_someFieldsSpecifiedUnfilteredList_success() {
        Index indexLastProperty = Index.fromOneBased(model.getFilteredPropertyList().size());
        Property lastProperty = model.getFilteredPropertyList().get(indexLastProperty.getZeroBased());

        Property editedProperty = new PropertyBuilder(lastProperty)
                .withPropertyName(VALID_PROPERTY_NAME_ORCHID)
                .withPrice(2000L)
                .build();

        EditPropertyDescriptor descriptor = new EditPropertyDescriptorBuilder()
                .withPropertyName(VALID_PROPERTY_NAME_ORCHID)
                .withPrice(2000L)
                .build();
        EditPropertyCommand editCommand = new EditPropertyCommand(indexLastProperty, descriptor);

        String expectedMessage = String.format(EditPropertyCommand.MESSAGE_EDIT_PROPERTY_SUCCESS,
                Messages.formatProperty(editedProperty));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setProperty(lastProperty, editedProperty);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_noFieldSpecifiedUnfilteredList_success() {
        EditPropertyCommand editCommand = new EditPropertyCommand(INDEX_FIRST, new EditPropertyDescriptor());
        Property editedProperty = model.getFilteredPropertyList().get(INDEX_FIRST.getZeroBased());

        String expectedMessage = String.format(EditPropertyCommand.MESSAGE_EDIT_PROPERTY_SUCCESS,
                Messages.formatProperty(editedProperty));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidPropertyIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPropertyList().size() + 1);
        EditPropertyDescriptor descriptor = new EditPropertyDescriptorBuilder()
                .withPropertyName(VALID_PROPERTY_NAME_ORCHID).build();
        EditPropertyCommand editCommand = new EditPropertyCommand(outOfBoundIndex, descriptor);

        assertCommandFailure(editCommand, model, Messages.MESSAGE_INVALID_PROPERTY_DISPLAYED_INDEX);
    }

    @Test
    public void execute_invalidPropertyIndexFilteredList_failure() {
        showPropertyAtIndex(model, INDEX_FIRST);
        Index outOfBoundIndex = INDEX_SECOND;
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getPropertyList().size());

        EditPropertyCommand editCommand = new EditPropertyCommand(outOfBoundIndex,
                new EditPropertyDescriptorBuilder().withPropertyName(VALID_PROPERTY_NAME_ORCHID).build());

        assertCommandFailure(editCommand, model, Messages.MESSAGE_INVALID_PROPERTY_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        final EditPropertyCommand standardCommand = new EditPropertyCommand(INDEX_FIRST, DESC_MAPLE);

        // same values -> returns true
        EditPropertyDescriptor copyDescriptor = new EditPropertyDescriptor(DESC_MAPLE);
        EditPropertyCommand commandWithSameValues = new EditPropertyCommand(INDEX_FIRST, copyDescriptor);
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> returns false
        assertFalse(standardCommand.equals(new HelpCommand()));

        // different index -> returns false
        assertFalse(standardCommand.equals(new EditPropertyCommand(INDEX_SECOND, DESC_MAPLE)));

        // different descriptor -> returns false
        assertFalse(standardCommand.equals(new EditPropertyCommand(INDEX_FIRST, DESC_ORCHID)));
    }

    @Test
    public void toStringMethod() {
        Index index = Index.fromOneBased(1);
        EditPropertyDescriptor editPropertyDescriptor = new EditPropertyDescriptor();
        EditPropertyCommand editCommand = new EditPropertyCommand(index, editPropertyDescriptor);
        String expected = EditPropertyCommand.class.getCanonicalName() + "{index=" + index + ", editPropertyDescriptor="
                + editPropertyDescriptor + "}";
        assertEquals(expected, editCommand.toString());
    }
}
