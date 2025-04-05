package seedu.address.logic.commands.property;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CLIENT_ID;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DESCRIPTION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PRICE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PROPERTY_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SIZE;

import java.util.List;
import java.util.Optional;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.Prefix;
import seedu.address.model.Model;
import seedu.address.model.client.Client;
import seedu.address.model.client.ClientName;
import seedu.address.model.commons.Address;
import seedu.address.model.commons.Price;
import seedu.address.model.property.Description;
import seedu.address.model.property.Property;
import seedu.address.model.property.PropertyName;
import seedu.address.model.property.Size;

/**
 * Adds a property to the address book.
 */
public class AddPropertyCommand extends Command {

    public static final String COMMAND_WORD = "add_property";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a property to the address book. "
            + "Parameters: "
            + PREFIX_PROPERTY_NAME + "PROPERTY_NAME "
            + PREFIX_CLIENT_ID + "OWNER "
            + PREFIX_ADDRESS + "ADDRESS "
            + PREFIX_PRICE + "PRICE (in S$ thousands) "
            + "[" + PREFIX_SIZE + "SIZE (in square feet)] "
            + "[" + PREFIX_DESCRIPTION + "DESCRIPTION]\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_PROPERTY_NAME + "Maple Villa Condominium "
            + PREFIX_CLIENT_ID + "Amy Bee "
            + PREFIX_ADDRESS + "123 Maple Street "
            + PREFIX_PRICE + "2400 "
            + PREFIX_SIZE + "1000 "
            + PREFIX_DESCRIPTION + "Spacious 4-bedroom home ";

    public static final String MESSAGE_SUCCESS = "New property added: %1$s";
    public static final String MESSAGE_DUPLICATE_PROPERTY = "This property already exists in the address book";
    public static final String MESSAGE_INVALID_CLIENT_ID = "Invalid client ID.";

    private final PropertyName propertyName;
    private final Address address;
    private final Price price;
    private final Optional<Size> size;
    private final Optional<Description> description;
    private final Index clientId;

    /**
     * Creates an AddCommand to add the specified {@code Property}
     */
    public AddPropertyCommand(PropertyName propertyName, Address address, Price price,
        Optional<Size> size, Optional<Description> description, Index clientId) {
        requireNonNull(propertyName);
        requireNonNull(address);
        requireNonNull(price);
        requireNonNull(size);
        requireNonNull(description);
        requireNonNull(clientId);
        this.propertyName = propertyName;
        this.address = address;
        this.price = price;
        this.size = size;
        this.description = description;
        this.clientId = clientId;
    }

    /**
     * Adds a command word and its associated prefixes to the command word map.
     */
    public static void addCommandWord() {
        Prefix[] parameterPrefixes = {
            PREFIX_PROPERTY_NAME,
            PREFIX_CLIENT_ID,
            PREFIX_ADDRESS,
            PREFIX_PRICE,
            PREFIX_SIZE,
            PREFIX_DESCRIPTION
        };
        initialiseCommandWord(COMMAND_WORD, parameterPrefixes);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        // Fetch clients by index
        List<Client> clientList = model.getFilteredClientList();
        int clientIdZeroBased = clientId.getZeroBased();
        if (clientIdZeroBased >= clientList.size()) {
            throw new CommandException(MESSAGE_INVALID_CLIENT_ID);
        }
        Client client = clientList.get(clientIdZeroBased);
        ClientName owner = client.getFullName();
        Property toAdd = new Property(propertyName, address, price, size, description, owner);

        if (model.hasProperty(toAdd)) {
            throw new CommandException(MESSAGE_DUPLICATE_PROPERTY);
        }

        model.addProperty(toAdd);
        return new CommandResult(String.format(MESSAGE_SUCCESS, Messages.formatProperty(toAdd)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AddPropertyCommand otherAddCommand)) {
            return false;
        }

        return this.propertyName.equals(otherAddCommand.propertyName)
                && this.address.equals(otherAddCommand.address)
                && this.price.equals(otherAddCommand.price)
                && this.size.equals(otherAddCommand.size)
                && this.description.equals(otherAddCommand.description)
                && this.clientId.equals(otherAddCommand.clientId);
    }
}
