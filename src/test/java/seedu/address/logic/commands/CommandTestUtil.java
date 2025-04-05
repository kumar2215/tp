package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CLIENT_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DESCRIPTION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PRICE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PROPERTY_NAME;
import static seedu.address.testutil.Assert.assertThrows;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.client.EditClientCommand.EditClientDescriptor;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.commands.property.EditPropertyCommand.EditPropertyDescriptor;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.client.Client;
import seedu.address.model.client.predicates.ClientNameContainsKeywordsPredicate;
import seedu.address.model.deal.Deal;
import seedu.address.model.deal.predicates.DealPropertyNameContainsPredicate;
import seedu.address.model.event.Event;
import seedu.address.model.event.predicates.EventAboutPropertyPredicate;
import seedu.address.model.property.Property;
import seedu.address.model.property.PropertyName;
import seedu.address.model.property.predicates.PropertyNameContainsKeywordsPredicate;
import seedu.address.testutil.EditClientDescriptorBuilder;
import seedu.address.testutil.EditPropertyDescriptorBuilder;

/**
 * Contains helper methods for testing commands.
 */
public class CommandTestUtil {

    public static final String VALID_CLIENT_NAME_AMY = "Amy Bee";
    public static final String VALID_CLIENT_NAME_BOB = "Bob Choo";
    public static final String VALID_PHONE_AMY = "11111111";
    public static final String VALID_PHONE_BOB = "22222222";
    public static final String VALID_EMAIL_AMY = "amy@example.com";
    public static final String VALID_EMAIL_BOB = "bob@example.com";
    public static final String VALID_ADDRESS_AMY = "Block 312, Amy Street 1";
    public static final String VALID_ADDRESS_BOB = "Block 123, Bobby Street 3";

    public static final String CLIENT_NAME_DESC_AMY = " " + PREFIX_CLIENT_NAME + VALID_CLIENT_NAME_AMY;
    public static final String CLIENT_NAME_DESC_BOB = " " + PREFIX_CLIENT_NAME + VALID_CLIENT_NAME_BOB;
    public static final String PHONE_DESC_AMY = " " + PREFIX_PHONE + VALID_PHONE_AMY;
    public static final String PHONE_DESC_BOB = " " + PREFIX_PHONE + VALID_PHONE_BOB;
    public static final String EMAIL_DESC_AMY = " " + PREFIX_EMAIL + VALID_EMAIL_AMY;
    public static final String EMAIL_DESC_BOB = " " + PREFIX_EMAIL + VALID_EMAIL_BOB;
    public static final String ADDRESS_DESC_AMY = " " + PREFIX_ADDRESS + VALID_ADDRESS_AMY;
    public static final String ADDRESS_DESC_BOB = " " + PREFIX_ADDRESS + VALID_ADDRESS_BOB;

    public static final String VALID_PROPERTY_NAME_MAPLE = "Maple Villa Condominium";
    public static final String VALID_PROPERTY_NAME_ORCHID = "Orchid Gardens Condominium";
    public static final String VALID_ADDRESS_MAPLE = "123 Maple Street";
    public static final String VALID_ADDRESS_ORCHID = "234 Orchid Street";
    public static final Long VALID_PRICE_MAPLE = 2400L;
    public static final Long VALID_PRICE_ORCHID = 1200L;
    public static final String VALID_SIZE_MAPLE = "1000";
    public static final String VALID_SIZE_ORCHID = "500";
    public static final String VALID_DESCRIPTION_MAPLE = "Spacious 4-bedroom home";
    public static final String VALID_DESCRIPTION_ORCHID = "Spacious 2-bedroom home";
    public static final String VALID_OWNER_MAPLE = "Amy Bee";
    public static final String VALID_OWNER_ORCHID = "Bob Choo";

    // '&' not allowed in clientNames
    public static final String INVALID_CLIENT_NAME_DESC = " " + PREFIX_CLIENT_NAME + "James&";
    public static final String INVALID_PHONE_DESC = " " + PREFIX_PHONE + "911a"; // 'a' not allowed in phones
    public static final String INVALID_EMAIL_DESC = " " + PREFIX_EMAIL + "bob!yahoo"; // missing '@' symbol
    public static final String INVALID_ADDRESS_DESC = " " + PREFIX_ADDRESS; // empty string not allowed for addresses
    public static final String INVALID_PROPERTY_NAME_DESC = " " + PREFIX_PROPERTY_NAME + "Maple&";
    public static final String INVALID_DESCRIPTION_DESC = " " + PREFIX_DESCRIPTION
            + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"; // exceed 50 chars
    public static final String INVALID_PRICE_DESC = " " + PREFIX_PRICE + -100L; // cannot be negative number
    public static final String PREAMBLE_WHITESPACE = "\t  \r  \n";
    public static final String PREAMBLE_NON_EMPTY = "NonEmptyPreamble";

    public static final EditClientDescriptor DESC_AMY;
    public static final EditClientDescriptor DESC_BOB;
    public static final EditPropertyDescriptor DESC_MAPLE;
    public static final EditPropertyDescriptor DESC_ORCHID;

    static {
        DESC_AMY = new EditClientDescriptorBuilder().withClientName(VALID_CLIENT_NAME_AMY)
                .withPhone(VALID_PHONE_AMY).withEmail(VALID_EMAIL_AMY).withAddress(VALID_ADDRESS_AMY)
                .build();
        DESC_BOB = new EditClientDescriptorBuilder().withClientName(VALID_CLIENT_NAME_BOB)
                .withPhone(VALID_PHONE_BOB).withEmail(VALID_EMAIL_BOB).withAddress(VALID_ADDRESS_BOB)
                .build();
        DESC_MAPLE = new EditPropertyDescriptorBuilder().withPropertyName(VALID_PROPERTY_NAME_MAPLE)
                .withAddress(VALID_ADDRESS_MAPLE).withPrice(VALID_PRICE_MAPLE).withSize(VALID_SIZE_MAPLE)
                .withDescription(VALID_DESCRIPTION_MAPLE).build();
        DESC_ORCHID = new EditPropertyDescriptorBuilder().withPropertyName(VALID_PROPERTY_NAME_ORCHID)
                .withAddress(VALID_ADDRESS_ORCHID).withPrice(VALID_PRICE_ORCHID).withSize(VALID_SIZE_ORCHID)
                .withDescription(VALID_DESCRIPTION_ORCHID).build();
    }

    /**
     * Executes the given {@code command}, confirms that <br>
     * - the returned {@link CommandResult} matches {@code expectedCommandResult} <br>
     * - the {@code actualModel} matches {@code expectedModel}
     */
    public static void assertCommandSuccess(Command command, Model actualModel, CommandResult expectedCommandResult,
            Model expectedModel) {
        try {
            CommandResult result = command.execute(actualModel);
            assertEquals(expectedCommandResult, result);
            assertEquals(expectedModel, actualModel);
        } catch (CommandException ce) {
            throw new AssertionError("Execution of command should not fail.", ce);
        }
    }

    /**
     * Convenience wrapper to {@link #assertCommandSuccess(Command, Model, CommandResult, Model)}
     * that takes a string {@code expectedMessage}.
     */
    public static void assertCommandSuccess(Command command, Model actualModel, String expectedMessage,
            Model expectedModel) {
        CommandResult expectedCommandResult = new CommandResult(expectedMessage);
        assertCommandSuccess(command, actualModel, expectedCommandResult, expectedModel);
    }

    /**
     * Executes the given {@code command}, confirms that <br>
     * - a {@code CommandException} is thrown <br>
     * - the CommandException message matches {@code expectedMessage} <br>
     * - the address book, filtered client list and selected client in {@code actualModel} remain unchanged
     */
    public static void assertCommandFailure(Command command, Model actualModel, String expectedMessage) {
        // we are unable to defensively copy the model for comparison later, so we can
        // only do so by copying its components.
        AddressBook expectedAddressBook = new AddressBook(actualModel.getAddressBook());
        List<Client> expectedFilteredList = new ArrayList<>(actualModel.getFilteredClientList());

        assertThrows(CommandException.class, expectedMessage, () -> command.execute(actualModel));
        assertEquals(expectedAddressBook, actualModel.getAddressBook());
        assertEquals(expectedFilteredList, actualModel.getFilteredClientList());
    }

    /**
     * Updates {@code model}'s filtered list to show only the client at the given {@code targetIndex} in the
     * {@code model}'s address book.
     */
    public static void showClientAtIndex(Model model, Index targetIndex) {
        assertTrue(targetIndex.getZeroBased() < model.getFilteredClientList().size());

        Client client = model.getFilteredClientList().get(targetIndex.getZeroBased());
        final String[] splitClientName = client.getFullName().fullName.split("\\s+");
        model.updateFilteredClientList(new ClientNameContainsKeywordsPredicate(Arrays.asList(splitClientName[0])));

        assertEquals(1, model.getFilteredClientList().size());
    }

    /**
     * Updates {@code model}'s filtered list to show only the property at the given {@code targetIndex} in the
     * {@code model}'s address book.
     */
    public static void showPropertyAtIndex(Model model, Index targetIndex) {
        assertTrue(targetIndex.getZeroBased() < model.getFilteredPropertyList().size());

        Property property = model.getFilteredPropertyList().get(targetIndex.getZeroBased());
        final String[] splitPropertyName = property.getFullName().fullName.split("\\s+");
        model.updateFilteredPropertyList(new PropertyNameContainsKeywordsPredicate(Arrays
                .asList(splitPropertyName[0])));

        assertEquals(1, model.getFilteredPropertyList().size());
    }

    /**
     * Updates {@code model}'s filtered list to show only the Deal at the given {@code targetIndex} in the
     * {@code model}'s address book.
     */
    public static void showDealAtIndex(Model model, Index targetIndex) {
        assertTrue(targetIndex.getZeroBased() < model.getFilteredDealList().size());

        Deal deal = model.getFilteredDealList().get(targetIndex.getZeroBased());
        model.updateFilteredDealList(new DealPropertyNameContainsPredicate(deal.getPropertyName()));

        assertEquals(1, model.getFilteredDealList().size());
    }

    /**
     * Updates {@code model}'s filtered list to show only the event at the given {@code targetIndex} in the
     * {@code model}'s address book.
     */
    public static void showEventAtIndex(Model model, Index targetIndex) {
        assertTrue(targetIndex.getZeroBased() < model.getFilteredEventList().size());

        Event event = model.getFilteredEventList().get(targetIndex.getZeroBased());
        final String[] splitEventName = event.getPropertyName().fullName.split("\\s+");
        model.updateFilteredEventList(new EventAboutPropertyPredicate(
            new PropertyName(splitEventName[0])));

        assertEquals(1, model.getFilteredPropertyList().size());
    }
}
