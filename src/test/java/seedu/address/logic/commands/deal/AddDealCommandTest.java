package seedu.address.logic.commands.deal;

import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.ModelStub;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.client.Client;
import seedu.address.model.client.ClientName;
import seedu.address.model.client.Email;
import seedu.address.model.client.Phone;
import seedu.address.model.commons.Address;
import seedu.address.model.commons.Price;
import seedu.address.model.deal.Deal;
import seedu.address.model.deal.DealStatus;
import seedu.address.model.property.Description;
import seedu.address.model.property.Property;
import seedu.address.model.property.PropertyName;
import seedu.address.model.property.Size;

public class AddDealCommandTest {

    @Test
    public void constructor_nullDeal_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new AddDealCommand(null, null,
                null, null));
    }

    @Test
    public void execute_dealAcceptedByModel_addSuccessful() throws Exception {
        ModelStubAcceptingDealAdded modelStub = new ModelStubAcceptingDealAdded();
        PropertyName propertyName = new PropertyName("Test Property");
        ClientName ownerName = new ClientName("Jane Smith");

        // Create property and add to model
        Property testProperty = new Property(
            propertyName,
                new Address("123 Test St"), new Price(500000L), Optional.of(new Size("1000")),
                Optional.of(new Description("Test description")), ownerName
        );
        modelStub.addProperty(testProperty);

        // Property ID
        Index propertyId = Index.fromOneBased(1);

        // Create client objects
        ClientName buyerName = new ClientName("John Doe");
        Client buyer = new Client(buyerName, new Phone("91234567"),
                new Email("john@example.com"), new Address("123 Buyer St"));

        // Use buyer index
        Index buyerId = Index.fromOneBased(1);

        // Add client to model
        modelStub.addClient(buyer);

        Price price = new Price(500000L);
        DealStatus status = DealStatus.PENDING;

        Deal validDeal = new Deal(propertyName, buyerName, ownerName, price, status);
        CommandResult commandResult = new AddDealCommand(propertyId, buyerId, price, status)
                .execute(modelStub);

        assertEquals(String.format(AddDealCommand.MESSAGE_SUCCESS,
                propertyName,
                buyerName,
                ownerName,
                price.value,
                status),
                commandResult.getFeedbackToUser());
        assertEquals(1, modelStub.dealsAdded.size());
        assertTrue(modelStub.dealsAdded.get(0).isSameDeal(validDeal));
    }

    @Test
    public void execute_duplicateDeal_throwsCommandException() {
        PropertyName propertyName = new PropertyName("Test Property");
        ClientName ownerName = new ClientName("Jane Smith");

        // Create property and add to model
        Property testProperty = new Property(
            propertyName,
                new Address("123 Test St"), new Price(500000L), Optional.of(new Size("1000")),
                Optional.of(new Description("Test description")), ownerName
        );

        ClientName buyerName = new ClientName("John Doe");
        Client buyer = new Client(buyerName, new Phone("91234567"),
                new Email("john@example.com"), new Address("123 Buyer St"));

        // Use indices
        Index propertyId = Index.fromOneBased(1);
        Index buyerId = Index.fromOneBased(1);

        Price price = new Price(500000L);
        DealStatus status = DealStatus.PENDING;

        Deal validDeal = new Deal(propertyName, buyerName, ownerName, price, status);
        AddDealCommand addDealCommand = new AddDealCommand(propertyId, buyerId, price, status);
        ModelStubWithDeal modelStub = new ModelStubWithDeal(validDeal);
        modelStub.addProperty(testProperty);
        modelStub.addClient(buyer);

        CommandException exception = assertThrows(CommandException.class, (
                ) -> addDealCommand.execute(modelStub));
        assertEquals(AddDealCommand.MESSAGE_DUPLICATE_DEAL, exception.getMessage());
    }

    @Test
    public void execute_sameBuyerAndSeller_throwsCommandException() {
        String personName = "Same Person";
        PropertyName propertyName = new PropertyName("Test Property");
        ClientName ownerName = new ClientName(personName);

        // Create property with owner same as buyer
        Property testProperty = new Property(
            propertyName,
                new Address("123 Test St"), new Price(500000L), Optional.of(new Size("1000")),
                Optional.of(new Description("Test description")), ownerName
        );

        Client person = new Client(ownerName, new Phone("91234567"),
                new Email("same@example.com"), new Address("123 Same St"));

        // Use indices for property and client
        Index propertyId = Index.fromOneBased(1);
        Index personId = Index.fromOneBased(1);

        Price price = new Price(500000L);
        DealStatus status = DealStatus.PENDING;

        AddDealCommand addDealCommand = new AddDealCommand(propertyId, personId, price, status);
        ModelStubWithClients modelStub = new ModelStubWithClients();
        modelStub.addProperty(testProperty);
        modelStub.addClient(person);

        CommandException exception = assertThrows(CommandException.class, (
                ) -> addDealCommand.execute(modelStub));
        assertEquals(AddDealCommand.MESSAGE_SAME_BUYER_SELLER, exception.getMessage());
    }

    @Test
    public void execute_propertyAlreadyInDeal_throwsCommandException() {
        PropertyName propertyName = new PropertyName("Test Property");
        ClientName ownerName = new ClientName("Jane Smith");

        // Create property and add to model
        Property testProperty = new Property(
            propertyName,
                new Address("123 Test St"), new Price(500000L), Optional.of(new Size("1000")),
                Optional.of(new Description("Test description")), ownerName
        );

        // First buyer
        ClientName buyer1Name = new ClientName("John Doe");
        Client buyer1 = new Client(buyer1Name, new Phone("91234567"),
                new Email("john@example.com"), new Address("123 Buyer St"));

        // Second buyer
        ClientName buyer2Name = new ClientName("Alice Brown");
        Client buyer2 = new Client(buyer2Name, new Phone("91234569"),
                new Email("alice@example.com"), new Address("789 Buyer St"));

        // Use indices
        Index propertyId = Index.fromOneBased(1);
        Index buyer1Id = Index.fromOneBased(1);
        Index buyer2Id = Index.fromOneBased(2);

        Price price = new Price(500000L);
        DealStatus status = DealStatus.PENDING;

        Deal existingDeal = new Deal(propertyName, buyer1Name, ownerName, price, status);
        AddDealCommand addDealCommand = new AddDealCommand(propertyId, buyer2Id, price, status);
        ModelStubWithDealAndClients modelStub = new ModelStubWithDealAndClients(existingDeal);
        modelStub.addProperty(testProperty);
        modelStub.addClient(buyer1);
        modelStub.addClient(buyer2);

        CommandException exception = assertThrows(CommandException.class, (
                ) -> addDealCommand.execute(modelStub));
        assertEquals(AddDealCommand.MESSAGE_PROPERTY_ALREADY_IN_DEAL, exception.getMessage());
    }

    @Test
    public void equals() {
        Index property1Id = Index.fromOneBased(1);
        Index property2Id = Index.fromOneBased(2);

        // Use indices for clients
        Index buyer1Id = Index.fromOneBased(1);
        Index buyer2Id = Index.fromOneBased(2);
        Price price1 = new Price(500000L);
        Price price2 = new Price(600000L);
        DealStatus status1 = DealStatus.PENDING;
        DealStatus status2 = DealStatus.CLOSED;

        AddDealCommand addFirstCommand = new AddDealCommand(property1Id, buyer1Id, price1, status1);
        AddDealCommand addSecondCommand = new AddDealCommand(property2Id, buyer2Id, price2, status2);

        // same object -> returns true
        assertTrue(addFirstCommand.equals(addFirstCommand));

        // same values -> returns true
        AddDealCommand addFirstCommandCopy = new AddDealCommand(property1Id, buyer1Id, price1, status1);
        assertTrue(addFirstCommand.equals(addFirstCommandCopy));

        // different types -> returns false
        assertFalse(addFirstCommand.equals(1));

        // null -> returns false
        assertFalse(addFirstCommand.equals(null));

        // different deal -> returns false
        assertFalse(addFirstCommand.equals(addSecondCommand));
    }

    /**
     * A Model stub that contains a single deal.
     */
    private static class ModelStubWithDeal extends ModelStub {
        private final Deal deal;
        private final ArrayList<Client> clients = new ArrayList<>();
        private final ArrayList<Property> properties = new ArrayList<>();

        ModelStubWithDeal(Deal deal) {
            requireNonNull(deal);
            this.deal = deal;
        }

        @Override
        public boolean hasDeal(Deal deal) {
            requireNonNull(deal);
            return this.deal.isSameDeal(deal);
        }

        @Override
        public ObservableList<Deal> getFilteredDealList() {
            ArrayList<Deal> deals = new ArrayList<>();
            deals.add(deal);
            return FXCollections.observableList(deals);
        }

        public void addClient(Client client) {
            clients.add(client);
        }

        public void addProperty(Property property) {
            properties.add(property);
        }

        @Override
        public ObservableList<Property> getFilteredPropertyList() {
            return FXCollections.observableList(properties);
        }

        @Override
        public ObservableList<Client> getFilteredClientList() {
            return FXCollections.observableList(clients);
        }
    }

    /**
     * A Model stub that contains clients but no deals
     */
    private static class ModelStubWithClients extends ModelStub {
        private final ArrayList<Client> clients = new ArrayList<>();
        private final ArrayList<Property> properties = new ArrayList<>();

        public void addClient(Client client) {
            clients.add(client);
        }

        public void addProperty(Property property) {
            properties.add(property);
        }

        @Override
        public ObservableList<Deal> getFilteredDealList() {
            return FXCollections.observableList(new ArrayList<>());
        }

        @Override
        public ObservableList<Property> getFilteredPropertyList() {
            return FXCollections.observableList(properties);
        }

        @Override
        public ObservableList<Client> getFilteredClientList() {
            return FXCollections.observableList(clients);
        }
    }

    /**
     * A Model stub that contains a deal and clients
     */
    private static class ModelStubWithDealAndClients extends ModelStub {
        private final Deal deal;
        private final ArrayList<Client> clients = new ArrayList<>();
        private final ArrayList<Property> properties = new ArrayList<>();

        ModelStubWithDealAndClients(Deal deal) {
            requireNonNull(deal);
            this.deal = deal;
        }

        public void addClient(Client client) {
            clients.add(client);
        }

        public void addProperty(Property property) {
            properties.add(property);
        }

        @Override
        public boolean hasDeal(Deal deal) {
            requireNonNull(deal);
            return this.deal.isSameDeal(deal);
        }

        @Override
        public ObservableList<Deal> getFilteredDealList() {
            ArrayList<Deal> deals = new ArrayList<>();
            deals.add(deal);
            return FXCollections.observableList(deals);
        }

        @Override
        public ObservableList<Property> getFilteredPropertyList() {
            return FXCollections.observableList(properties);
        }

        @Override
        public ObservableList<Client> getFilteredClientList() {
            return FXCollections.observableList(clients);
        }
    }

    /**
     * A Model stub that always accept the deal being added.
     */
    private static class ModelStubAcceptingDealAdded extends ModelStub {
        final ArrayList<Deal> dealsAdded = new ArrayList<>();
        final ArrayList<Client> clients = new ArrayList<>();
        final ArrayList<Property> properties = new ArrayList<>();

        @Override
        public boolean hasDeal(Deal deal) {
            requireNonNull(deal);
            return dealsAdded.stream().anyMatch(d -> d.isSameDeal(deal));
        }

        @Override
        public void addDeal(Deal deal) {
            requireNonNull(deal);
            dealsAdded.add(deal);
        }

        public void addClient(Client client) {
            clients.add(client);
        }

        public void addProperty(Property property) {
            properties.add(property);
        }

        @Override
        public ObservableList<Deal> getFilteredDealList() {
            return FXCollections.observableList(dealsAdded);
        }

        @Override
        public ObservableList<Property> getFilteredPropertyList() {
            return FXCollections.observableList(properties);
        }

        @Override
        public ObservableList<Client> getFilteredClientList() {
            return FXCollections.observableList(clients);
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            return new AddressBook();
        }
    }
}
