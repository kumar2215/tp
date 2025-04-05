package seedu.address.logic.commands.client;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CLIENT_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_CLIENTS;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.CollectionUtil;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.EditDescriptor;
import seedu.address.logic.commands.deal.UpdateDealCommand;
import seedu.address.logic.commands.deal.UpdateDealCommand.UpdateDealDescriptor;
import seedu.address.logic.commands.event.EditEventCommand;
import seedu.address.logic.commands.event.EditEventCommand.EditEventDescriptor;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.commands.property.EditPropertyCommand;
import seedu.address.logic.commands.property.EditPropertyCommand.EditPropertyDescriptor;
import seedu.address.logic.parser.Prefix;
import seedu.address.model.Model;
import seedu.address.model.client.Client;
import seedu.address.model.client.ClientName;
import seedu.address.model.client.Email;
import seedu.address.model.client.Phone;
import seedu.address.model.commons.Address;
import seedu.address.model.deal.Deal;
import seedu.address.model.event.Event;
import seedu.address.model.property.Property;

/**
 * Edits the details of an existing client in the address book.
 */
public class EditClientCommand extends EditCommand<Client> {

    public static final String COMMAND_WORD = "edit_client";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the details of the client identified "
            + "by the index number used in the displayed client list. "
            + "Existing values will be overwritten by the input values.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + "[" + PREFIX_CLIENT_NAME + "CLIENT_NAME] "
            + "[" + PREFIX_PHONE + "PHONE] "
            + "[" + PREFIX_EMAIL + "EMAIL] "
            + "[" + PREFIX_ADDRESS + "ADDRESS]\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_PHONE + "91234567 "
            + PREFIX_EMAIL + "johndoe@example.com";

    public static final String MESSAGE_EDIT_CLIENT_SUCCESS = "Edited Client: %1$s";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";
    public static final String MESSAGE_DUPLICATE_CLIENT = "This client already exists in the address book.";

    private static final Logger logger = LogsCenter.getLogger(EditClientCommand.class);

    private final EditClientDescriptor editClientDescriptor;

    /**
     * @param index of the client in the filtered client list to edit
     * @param editClientDescriptor details to edit the client with
     */
    public EditClientCommand(Index index, EditClientDescriptor editClientDescriptor) {
        super(index, editClientDescriptor);
        this.editClientDescriptor = new EditClientDescriptor(editClientDescriptor);
    }

    /**
     * Adds a command word and its associated prefixes to the command word map.
     */
    public static void addCommandWord() {
        Prefix[] prefixes = {
            PREFIX_CLIENT_NAME,
            PREFIX_PHONE,
            PREFIX_EMAIL,
            PREFIX_ADDRESS
        };
        initialiseCommandWord(COMMAND_WORD, prefixes);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Client> lastShownClientList = model.getFilteredClientList();
        List<Deal> lastShownDealList = model.getFilteredDealList();
        List<Event> lastShownEventList = model.getFilteredEventList();
        List<Property> lastShownPropertyList = model.getFilteredPropertyList();
        Optional<ClientName> optionalClientName = editClientDescriptor.getFullName();

        if (index.getZeroBased() >= lastShownClientList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_CLIENT_DISPLAYED_INDEX);
        }

        Client clientToEdit = lastShownClientList.get(index.getZeroBased());
        Client editedClient = createEditedClient(clientToEdit, editClientDescriptor);

        if (model.hasClient(editedClient, index)) {
            throw new CommandException(MESSAGE_DUPLICATE_CLIENT);
        }

        model.setClient(clientToEdit, editedClient);
        model.updateFilteredClientList(PREDICATE_SHOW_ALL_CLIENTS);

        if (optionalClientName.isPresent()) {
            ClientName oldClientName = clientToEdit.getFullName();

            updateClientNameInDeals(oldClientName, index, lastShownDealList, model);
            updateClientNameInEvents(oldClientName, index, lastShownEventList, model);
            updateClientNameInListings(oldClientName, index, lastShownPropertyList, model);
        }

        return new CommandResult(String.format(MESSAGE_EDIT_CLIENT_SUCCESS, Messages.formatClient(editedClient)));
    }

    private void updateClientNameInEvents(ClientName oldClientName, Index index,
            List<Event> eventList, Model model) {
        int eventPosition = 0;
        for (; eventPosition < eventList.size(); eventPosition++) {
            Event event = eventList.get(eventPosition);
            EditEventDescriptor descriptor = new EditEventDescriptor();
            if (oldClientName.equals(event.getClientName())) {
                descriptor.setClientId(index);
            }
            if (descriptor.isAnyFieldEdited()) {
                try {
                    new EditEventCommand(Index.fromZeroBased(eventPosition), descriptor).execute(model);
                } catch (CommandException e) {
                    logger.info("An error occurred while executing command to update client");
                }
            }
        }
    }

    private void updateClientNameInListings(ClientName oldClientName,
            Index index, List<Property> propertyList, Model model) {
        int listingPosition = 0;
        for (; listingPosition < propertyList.size(); listingPosition++) {
            Property property = propertyList.get(listingPosition);
            EditPropertyDescriptor descriptor = new EditPropertyDescriptor();
            if (oldClientName.equals(property.getOwner())) {
                descriptor.setOwner(index);
            }
            if (descriptor.isAnyFieldEdited()) {
                try {
                    new EditPropertyCommand(Index.fromZeroBased(listingPosition), descriptor).execute(model);
                } catch (CommandException e) {
                    logger.info("An error occurred while executing command to update client");
                }
            }
        }
    }

    private void updateClientNameInDeals(ClientName oldClientName, Index index,
            List<Deal> dealList, Model model) {
        int dealPosition = 0;
        for (; dealPosition < dealList.size(); dealPosition++) {
            Deal deal = dealList.get(dealPosition);
            UpdateDealDescriptor descriptor = new UpdateDealDescriptor();
            if (oldClientName.equals(deal.getBuyer())) {
                descriptor.setBuyer(index);
            }
            if (oldClientName.equals(deal.getSeller())) {
                descriptor.setSeller(index);
            }
            if (descriptor.isAnyFieldEdited()) {
                try {
                    new UpdateDealCommand(Index.fromZeroBased(dealPosition), descriptor).execute(model);
                } catch (CommandException e) {
                    logger.info("An error occurred while executing command to update client");
                }
            }
        }
    }

    /**
     * Creates and returns a {@code Client} with the details of {@code clientToEdit}
     * edited with {@code editClientDescriptor}.
     */
    private static Client createEditedClient(Client clientToEdit, EditClientDescriptor editClientDescriptor) {
        assert clientToEdit != null;

        ClientName updatedClientName = editClientDescriptor.getFullName().orElse(clientToEdit.getFullName());
        Phone updatedPhone = editClientDescriptor.getPhone().orElse(clientToEdit.getPhone());
        Email updatedEmail = editClientDescriptor.getEmail().orElse(clientToEdit.getEmail());
        Address updatedAddress = editClientDescriptor.getAddress().orElse(clientToEdit.getAddress());

        return new Client(updatedClientName, updatedPhone, updatedEmail, updatedAddress);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof EditClientCommand)) {
            return false;
        }

        EditClientCommand otherEditCommand = (EditClientCommand) other;
        return index.equals(otherEditCommand.index)
                && editClientDescriptor.equals(otherEditCommand.editClientDescriptor);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("index", index)
                .add("editClientDescriptor", editClientDescriptor)
                .toString();
    }

    /**
     * Stores the details to edit the client with. Each non-empty field value will replace the
     * corresponding field value of the client.
     */
    public static class EditClientDescriptor extends EditDescriptor<Client> {
        private ClientName clientName;
        private Phone phone;
        private Email email;
        private Address address;

        public EditClientDescriptor() {}

        /**
         * Copy constructor.
         *
         */
        public EditClientDescriptor(EditClientDescriptor toCopy) {
            setClientName(toCopy.clientName);
            setPhone(toCopy.phone);
            setEmail(toCopy.email);
            setAddress(toCopy.address);
        }

        /**
         * Returns true if at least one field is edited.
         */
        @Override
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(clientName, phone, email, address);
        }

        public void setClientName(ClientName clientName) {
            this.clientName = clientName;
        }

        public Optional<ClientName> getFullName() {
            return Optional.ofNullable(clientName);
        }

        public void setPhone(Phone phone) {
            this.phone = phone;
        }

        public Optional<Phone> getPhone() {
            return Optional.ofNullable(phone);
        }

        public void setEmail(Email email) {
            this.email = email;
        }

        public Optional<Email> getEmail() {
            return Optional.ofNullable(email);
        }

        public void setAddress(Address address) {
            this.address = address;
        }

        public Optional<Address> getAddress() {
            return Optional.ofNullable(address);
        }

        @Override
        public boolean equals(Object other) {
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof EditClientDescriptor)) {
                return false;
            }

            EditClientDescriptor otherEditClientDescriptor = (EditClientDescriptor) other;
            return Objects.equals(clientName, otherEditClientDescriptor.clientName)
                    && Objects.equals(phone, otherEditClientDescriptor.phone)
                    && Objects.equals(email, otherEditClientDescriptor.email)
                    && Objects.equals(address, otherEditClientDescriptor.address);
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this)
                    .add("clientName", clientName)
                    .add("phone", phone)
                    .add("email", email)
                    .add("address", address)
                    .toString();
        }
    }
}
