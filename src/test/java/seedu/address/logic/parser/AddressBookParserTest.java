package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.address.logic.parser.CliSyntax.PREFIX_KEYWORDS;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.ClearCommand;
import seedu.address.logic.commands.ExitCommand;
import seedu.address.logic.commands.HelpCommand;
import seedu.address.logic.commands.client.AddClientCommand;
import seedu.address.logic.commands.client.DeleteClientCommand;
import seedu.address.logic.commands.client.EditClientCommand;
import seedu.address.logic.commands.client.EditClientCommand.EditClientDescriptor;
import seedu.address.logic.commands.client.FindClientCommand;
import seedu.address.logic.commands.client.ListClientCommand;
import seedu.address.logic.commands.property.AddPropertyCommand;
import seedu.address.logic.commands.property.DeletePropertyCommand;
import seedu.address.logic.commands.property.EditPropertyCommand;
import seedu.address.logic.commands.property.FindPropertyCommand;
import seedu.address.logic.commands.property.ListPropertyCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.client.Client;
import seedu.address.model.client.predicates.ClientNameContainsKeywordsPredicate;
import seedu.address.model.property.Property;
import seedu.address.model.property.predicates.PropertyNameContainsKeywordsPredicate;
import seedu.address.testutil.ClientBuilder;
import seedu.address.testutil.ClientUtil;
import seedu.address.testutil.EditClientDescriptorBuilder;
import seedu.address.testutil.EditPropertyDescriptorBuilder;
import seedu.address.testutil.PropertyBuilder;
import seedu.address.testutil.PropertyUtil;

public class AddressBookParserTest {

    private final AddressBookParser parser = new AddressBookParser();

    @Test
    public void parseCommand_add() throws Exception {
        Client client = new ClientBuilder().build();
        AddClientCommand command = (AddClientCommand) parser.parseCommand(ClientUtil.getAddCommand(client));
        assertEquals(new AddClientCommand(client), command);
    }

    @Test
    public void parseCommand_clear() throws Exception {
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_WORD) instanceof ClearCommand);
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_WORD + " 3") instanceof ClearCommand);
    }

    @Test
    public void parseCommand_delete() throws Exception {
        DeleteClientCommand command = (DeleteClientCommand) parser.parseCommand(
                DeleteClientCommand.COMMAND_WORD + " " + INDEX_FIRST.getOneBased());
        assertEquals(new DeleteClientCommand(INDEX_FIRST), command);
    }

    @Test
    public void parseCommand_edit() throws Exception {
        Client client = new ClientBuilder().build();
        EditClientDescriptor descriptor = new EditClientDescriptorBuilder(client).build();
        EditClientCommand command = (EditClientCommand) parser.parseCommand(EditClientCommand.COMMAND_WORD + " "
                + INDEX_FIRST.getOneBased() + " " + ClientUtil.getEditClientDescriptorDetails(descriptor));
        assertEquals(new EditClientCommand(INDEX_FIRST, descriptor), command);
    }

    @Test
    public void parseCommand_exit() throws Exception {
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD) instanceof ExitCommand);
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD + " 3") instanceof ExitCommand);
    }

    @Test
    public void parseCommand_find() throws Exception {
        List<String> keywords = Arrays.asList("foo", "bar", "baz");
        FindClientCommand.addCommandWord();
        FindClientCommand command = (FindClientCommand) parser.parseCommand(
                FindClientCommand.COMMAND_WORD + " " + PREFIX_KEYWORDS + keywords.stream()
                        .collect(Collectors.joining(" ")));
        assertEquals(new FindClientCommand(new ClientNameContainsKeywordsPredicate(keywords)), command);
    }

    @Test
    public void parseCommand_help() throws Exception {
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD) instanceof HelpCommand);
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD + " 3") instanceof HelpCommand);
    }

    @Test
    public void parseCommand_list() throws Exception {
        assertTrue(parser.parseCommand(ListClientCommand.COMMAND_WORD) instanceof ListClientCommand);
        assertTrue(parser.parseCommand(ListClientCommand.COMMAND_WORD + " 3") instanceof ListClientCommand);
    }

    @Test
    public void parseCommand_unrecognisedInput_throwsParseException() {
        assertThrows(ParseException.class, String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE), ()
            -> parser.parseCommand(""));
    }

    @Test
    public void parseCommand_unknownCommand_throwsParseException() {
        assertThrows(ParseException.class, MESSAGE_UNKNOWN_COMMAND, () -> parser.parseCommand("unknownCommand"));
    }

    @Test
    public void parseCommand_addProperty() throws Exception {
        Property property = new PropertyBuilder().build();
        AddPropertyCommand command = (AddPropertyCommand) parser.parseCommand(PropertyUtil.getAddCommand(property));
        assertEquals(new AddPropertyCommand(property), command);
    }

    @Test
    public void parseCommand_editProperty() throws Exception {
        Property property = new PropertyBuilder().build();
        EditPropertyCommand.EditPropertyDescriptor descriptor = new EditPropertyDescriptorBuilder(property).build();
        String commandString = EditPropertyCommand.COMMAND_WORD + " " + INDEX_FIRST.getOneBased()
                + " " + PropertyUtil.getEditPropertyDescriptorDetails(descriptor);
        EditPropertyCommand command = (EditPropertyCommand) parser.parseCommand(commandString);
        assertEquals(new EditPropertyCommand(INDEX_FIRST, descriptor), command);
    }

    @Test
    public void parseCommand_deleteProperty() throws Exception {
        DeletePropertyCommand command = (DeletePropertyCommand) parser.parseCommand(
                DeletePropertyCommand.COMMAND_WORD + " " + INDEX_FIRST.getOneBased());
        assertEquals(new DeletePropertyCommand(INDEX_FIRST), command);
    }

    @Test
    public void parseCommand_findProperty() throws Exception {
        List<String> keywords = Arrays.asList("Maple", "Orchid");
        FindPropertyCommand command = (FindPropertyCommand) parser.parseCommand(
                FindPropertyCommand.COMMAND_WORD + " " + String.join(" ", keywords));
        assertEquals(new FindPropertyCommand(new PropertyNameContainsKeywordsPredicate(keywords)), command);
    }

    @Test
    public void parseCommand_listClient() throws Exception {
        assertTrue(parser.parseCommand(ListClientCommand.COMMAND_WORD) instanceof ListClientCommand);
    }

    @Test
    public void parseCommand_listProperty() throws Exception {
        assertTrue(parser.parseCommand(ListPropertyCommand.COMMAND_WORD) instanceof ListPropertyCommand);
    }
}
