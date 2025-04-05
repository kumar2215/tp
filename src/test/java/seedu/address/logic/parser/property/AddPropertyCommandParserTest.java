package seedu.address.logic.parser.property;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.PREAMBLE_NON_EMPTY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_MAPLE;
import static seedu.address.logic.commands.CommandTestUtil.VALID_DESCRIPTION_MAPLE;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PRICE_MAPLE;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PROPERTY_NAME_MAPLE;
import static seedu.address.logic.commands.CommandTestUtil.VALID_SIZE_MAPLE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CLIENT_ID;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DESCRIPTION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PRICE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PROPERTY_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SIZE;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.property.AddPropertyCommand;
import seedu.address.model.property.Description;
import seedu.address.model.property.Property;
import seedu.address.model.property.PropertyName;
import seedu.address.model.property.Size;
import seedu.address.testutil.PropertyBuilder;

public class AddPropertyCommandParserTest {

    private AddPropertyCommandParser parser = new AddPropertyCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        // Build the Property we expect after parsing
        Property expectedProperty = new PropertyBuilder()
                .withPropertyName(VALID_PROPERTY_NAME_MAPLE)
                .withAddress(VALID_ADDRESS_MAPLE)
                .withPrice(VALID_PRICE_MAPLE)
                .withSize(VALID_SIZE_MAPLE)
                .withDescription(VALID_DESCRIPTION_MAPLE)
                .build();

        // Provide the user input including client index
        String userInput = " "
                + PREFIX_PROPERTY_NAME + VALID_PROPERTY_NAME_MAPLE + " "
                + PREFIX_ADDRESS + VALID_ADDRESS_MAPLE + " "
                + PREFIX_PRICE + VALID_PRICE_MAPLE + " "
                + PREFIX_SIZE + VALID_SIZE_MAPLE + " "
                + PREFIX_DESCRIPTION + VALID_DESCRIPTION_MAPLE + " "
                + PREFIX_CLIENT_ID + "1"; // Required by parser

        // The parser should produce the AddPropertyCommand with the same fields + Index.fromOneBased(1)
        assertParseSuccess(parser, userInput,
                new AddPropertyCommand(
                        expectedProperty.getFullName(),
                        expectedProperty.getAddress(),
                        expectedProperty.getPrice(),
                        expectedProperty.getSize(),
                        expectedProperty.getDescription(),
                        Index.fromOneBased(1)
                )
        );
    }

    @Test
    public void parse_missingCompulsoryField_failure() {
        String expectedMessage = String.format(
                MESSAGE_INVALID_COMMAND_FORMAT, AddPropertyCommand.MESSAGE_USAGE);

        // Missing property name prefix
        String userInput = VALID_PROPERTY_NAME_MAPLE
                + " " + PREFIX_ADDRESS + VALID_ADDRESS_MAPLE
                + " " + PREFIX_PRICE + VALID_PRICE_MAPLE
                + " " + PREFIX_CLIENT_ID + "1"; // Suppose we have the client ID, but no pn/ prefix
        assertParseFailure(parser, userInput, expectedMessage);

        // Missing client id prefix
        userInput = " "
                + PREFIX_PROPERTY_NAME + VALID_PROPERTY_NAME_MAPLE + " "
                + PREFIX_ADDRESS + VALID_ADDRESS_MAPLE + " "
                + PREFIX_PRICE + VALID_PRICE_MAPLE;
        assertParseFailure(parser, userInput, expectedMessage);
    }

    @Test
    public void parse_invalidValue_failure() {
        // invalid property name (& character)
        String userInput = " "
                + PREFIX_PROPERTY_NAME + "Maple&"
                + " " + PREFIX_ADDRESS + VALID_ADDRESS_MAPLE
                + " " + PREFIX_PRICE + VALID_PRICE_MAPLE
                + " " + PREFIX_CLIENT_ID + "1";
        assertParseFailure(parser, userInput, PropertyName.MESSAGE_CONSTRAINTS);

        // invalid price (negative, or containing special chars)
        userInput = " "
                + PREFIX_PROPERTY_NAME + VALID_PROPERTY_NAME_MAPLE
                + " " + PREFIX_ADDRESS + VALID_ADDRESS_MAPLE
                + " " + PREFIX_PRICE + "-500"
                + " " + PREFIX_CLIENT_ID + "1";
        assertParseFailure(parser, userInput,
                "Price must be an integer and should not contain any special characters!");

        // invalid size (below your valid range or wrong format)
        userInput = " "
                + PREFIX_PROPERTY_NAME + VALID_PROPERTY_NAME_MAPLE
                + " " + PREFIX_ADDRESS + VALID_ADDRESS_MAPLE
                + " " + PREFIX_PRICE + VALID_PRICE_MAPLE
                + " " + PREFIX_SIZE + "99" // Suppose 99 is invalid if min is 100
                + " " + PREFIX_CLIENT_ID + "1";
        assertParseFailure(parser, userInput, Size.MESSAGE_CONSTRAINTS);

        // invalid description (too long, for example)
        userInput = " "
                + PREFIX_PROPERTY_NAME + VALID_PROPERTY_NAME_MAPLE
                + " " + PREFIX_ADDRESS + VALID_ADDRESS_MAPLE
                + " " + PREFIX_PRICE + VALID_PRICE_MAPLE
                + " " + PREFIX_DESCRIPTION + "x".repeat(60) // 60 chars if your max is < 60
                + " " + PREFIX_CLIENT_ID + "1";
        assertParseFailure(parser, userInput, Description.MESSAGE_CONSTRAINTS);

        // invalid client index (non-integer)
        userInput = " "
                + PREFIX_PROPERTY_NAME + VALID_PROPERTY_NAME_MAPLE
                + " " + PREFIX_ADDRESS + VALID_ADDRESS_MAPLE
                + " " + PREFIX_PRICE + VALID_PRICE_MAPLE
                + " " + PREFIX_CLIENT_ID + "one"; // invalid
        assertParseFailure(parser, userInput, "Invalid client ID: Index is not a positive integer.");

        // invalid client index (zero or negative)
        userInput = " "
                + PREFIX_PROPERTY_NAME + VALID_PROPERTY_NAME_MAPLE
                + " " + PREFIX_ADDRESS + VALID_ADDRESS_MAPLE
                + " " + PREFIX_PRICE + VALID_PRICE_MAPLE
                + " " + PREFIX_CLIENT_ID + "0";
        assertParseFailure(parser, userInput, "Invalid client ID: Index is not a positive integer.");
    }

    @Test
    public void parse_nonEmptyPreamble_failure() {
        String userInput = PREAMBLE_NON_EMPTY
                + PREFIX_PROPERTY_NAME + VALID_PROPERTY_NAME_MAPLE + " "
                + PREFIX_ADDRESS + VALID_ADDRESS_MAPLE + " "
                + PREFIX_PRICE + VALID_PRICE_MAPLE + " "
                + PREFIX_CLIENT_ID + "1";

        assertParseFailure(parser, userInput,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddPropertyCommand.MESSAGE_USAGE));
    }
}
