package seedu.address.logic.parser.property;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_PRICE_DESC;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_MAPLE;
import static seedu.address.logic.commands.CommandTestUtil.VALID_DESCRIPTION_MAPLE;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PRICE_MAPLE;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PROPERTY_NAME_MAPLE;
import static seedu.address.logic.commands.CommandTestUtil.VALID_SIZE_MAPLE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DESCRIPTION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PRICE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PROPERTY_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SIZE;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.property.EditPropertyCommand;
import seedu.address.logic.commands.property.EditPropertyCommand.EditPropertyDescriptor;
import seedu.address.testutil.EditPropertyDescriptorBuilder;

public class EditPropertyCommandParserTest {

    private static final String MESSAGE_INVALID_FORMAT =
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditPropertyCommand.MESSAGE_USAGE);

    private final EditPropertyCommandParser parser = new EditPropertyCommandParser();

    @Test
    public void parse_missingInputs_failure() {
        String args = " " + PREFIX_PROPERTY_NAME + VALID_PROPERTY_NAME_MAPLE;

        assertParseFailure(parser, args, MESSAGE_INVALID_FORMAT);
        assertParseFailure(parser, "1", EditPropertyCommand.MESSAGE_NOT_EDITED);
        assertParseFailure(parser, "", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidValue_failure() {
        String userInput = "1" + INVALID_PRICE_DESC;
        assertParseFailure(parser, userInput, "Price should only contain positive numbers "
                + "(in S$ thousands) between 3 to 6 digits");
    }

    @Test
    public void parse_fieldsSpecified_success() {
        Index targetIndex = Index.fromOneBased(1);
        String userInput = targetIndex.getOneBased()
                + " " + PREFIX_PROPERTY_NAME + VALID_PROPERTY_NAME_MAPLE
                + " " + PREFIX_ADDRESS + VALID_ADDRESS_MAPLE
                + " " + PREFIX_PRICE + VALID_PRICE_MAPLE
                + " " + PREFIX_SIZE + VALID_SIZE_MAPLE
                + " " + PREFIX_DESCRIPTION + VALID_DESCRIPTION_MAPLE;

        EditPropertyDescriptor descriptor = new EditPropertyDescriptorBuilder()
                .withPropertyName(VALID_PROPERTY_NAME_MAPLE)
                .withAddress(VALID_ADDRESS_MAPLE)
                .withPrice(VALID_PRICE_MAPLE)
                .withSize(VALID_SIZE_MAPLE)
                .withDescription(VALID_DESCRIPTION_MAPLE)
                .build();

        EditPropertyCommand expectedCommand = new EditPropertyCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_someFieldsSpecified_success() {
        Index targetIndex = Index.fromOneBased(2);
        String userInput = targetIndex.getOneBased()
                + " " + PREFIX_PROPERTY_NAME + VALID_PROPERTY_NAME_MAPLE
                + " " + PREFIX_PRICE + VALID_PRICE_MAPLE;

        EditPropertyDescriptor descriptor = new EditPropertyDescriptorBuilder()
                .withPropertyName(VALID_PROPERTY_NAME_MAPLE)
                .withPrice(VALID_PRICE_MAPLE)
                .build();

        EditPropertyCommand expectedCommand = new EditPropertyCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);
    }
}
