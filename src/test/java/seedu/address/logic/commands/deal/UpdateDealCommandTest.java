package seedu.address.logic.commands.deal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static seedu.address.logic.commands.deal.UpdateDealCommand.UpdateDealDescriptor;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND;

import java.util.ArrayList;
import java.util.List;
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
import seedu.address.model.property.Property;
import seedu.address.model.property.PropertyName;
import seedu.address.testutil.DealBuilder;
import seedu.address.testutil.UpdateDealDescriptorBuilder;

public class UpdateDealCommandTest {

    private static final String VALID_PROPERTY_NAME = "Palm Spring";
    private static final String VALID_PROPERTY_NAME_2 = "Maple Villa";
    private static final long VALID_PRICE = 200L;
    private static final long VALID_PRICE_2 = 300L;
    private static final long PRICE_LIMIT = 999_999L;
    private static final DealStatus VALID_STATUS = DealStatus.PENDING;
    private static final DealStatus VALID_STATUS_2 = DealStatus.CLOSED;

    @Test
    public void execute_allFieldsSpecifiedUnfilteredList_success() {
        TestModelStub model = new TestModelStub();

        Client buyer = new Client(
                new ClientName("John"),
                new Phone("12345678"),
                new Email("john@example.com"),
                new Address("Blk 123"));
        Client seller = new Client(
                new ClientName("Jane"),
                new Phone("87654321"),
                new Email("jane@example.com"),
                new Address("Blk 456"));
        Property property = new Property(
                new PropertyName(VALID_PROPERTY_NAME),
                new Address("Blk 789"), new Price(VALID_PRICE), Optional.empty(),
                Optional.empty(), new ClientName("Owner")
        );

        model.addClient(buyer);
        model.addClient(seller);
        model.addProperty(property);

        ClientName buyerName = buyer.getFullName();
        ClientName sellerName = seller.getFullName();

        Deal testDeal = new DealBuilder()
                .withPropertyName(VALID_PROPERTY_NAME)
                .withBuyer(buyerName.toString())
                .withSeller(sellerName.toString())
                .withPrice(VALID_PRICE)
                .withStatus(VALID_STATUS)
                .build();
        model.addDeal(testDeal);

        // Get client & property index
        Index buyerIndex = INDEX_FIRST;
        Index sellerIndex = INDEX_SECOND;
        Index propertyIndex = INDEX_FIRST;

        UpdateDealDescriptor descriptor = new UpdateDealDescriptorBuilder()
                .withPropertyId(propertyIndex)
                .withBuyer(buyerIndex)
                .withSeller(sellerIndex)
                .withPrice(VALID_PRICE_2)
                .withStatus(VALID_STATUS_2)
                .build();
        UpdateDealCommand updateCommand = new UpdateDealCommand(INDEX_FIRST, descriptor);

        String expectedMessage = String.format(UpdateDealCommand.MESSAGE_UPDATE_DEAL_SUCCESS, testDeal);

        // Create a copy of expected deal after update
        Deal expectedDeal = new Deal(property.getFullName(), buyerName, property.getOwner(),
                new Price(VALID_PRICE_2), VALID_STATUS_2);

        // Execute command
        try {
            CommandResult result = updateCommand.execute(model);

            // Check success message
            assertTrue(expectedMessage.equals(result.getFeedbackToUser()));

            // Check the deal was updated correctly
            Deal updatedDeal = model.getFilteredDealList().get(INDEX_FIRST.getZeroBased());

            // Compare expected deal with actual updated deal
            assertTrue(expectedDeal.getPropertyName().equals(updatedDeal.getPropertyName()));
            assertTrue(expectedDeal.getBuyer().equals(updatedDeal.getBuyer()));
            assertTrue(expectedDeal.getSeller().equals(updatedDeal.getSeller()));
            assertTrue(expectedDeal.getPrice().equals(updatedDeal.getPrice()));
            assertTrue(expectedDeal.getStatus().equals(updatedDeal.getStatus()));
        } catch (CommandException e) {
            throw new AssertionError("Command should not fail: " + e.getMessage());
        }
    }

    @Test
    public void execute_statusOnlySpecified_success() {
        TestModelStub model = new TestModelStub();

        Client buyer = new Client(
                new ClientName("John"),
                new Phone("12345678"),
                new Email("john@example.com"),
                new Address("Blk 123"));
        Client seller = new Client(
                new ClientName("Jane"),
                new Phone("87654321"),
                new Email("jane@example.com"),
                new Address("Blk 456"));

        ClientName buyerName = buyer.getFullName();
        ClientName sellerName = seller.getFullName();

        Deal testDeal = new DealBuilder()
                .withPropertyName(VALID_PROPERTY_NAME)
                .withBuyer(buyerName.toString())
                .withSeller(sellerName.toString())
                .withPrice(VALID_PRICE)
                .withStatus(VALID_STATUS)
                .build();
        model.addDeal(testDeal);
        model.addClient(buyer);
        model.addClient(seller);

        Index dealIndex = INDEX_FIRST;
        UpdateDealDescriptor descriptor = new UpdateDealDescriptorBuilder()
                .withStatus(VALID_STATUS_2)
                .build();
        UpdateDealCommand updateCommand = new UpdateDealCommand(dealIndex, descriptor);

        String expectedMessage = String.format(UpdateDealCommand.MESSAGE_UPDATE_DEAL_SUCCESS, VALID_STATUS_2);

        Deal updatedDeal = new DealBuilder()
                .withPropertyName(VALID_PROPERTY_NAME)
                .withBuyer(buyerName.toString())
                .withSeller(sellerName.toString())
                .withPrice(VALID_PRICE)
                .withStatus(VALID_STATUS_2)
                .build();

        TestModelStub expectedModel = new TestModelStub();
        expectedModel.addDeal(updatedDeal);
        expectedModel.addClient(buyer);
        expectedModel.addClient(seller);

        // Create a modified assertCommandSuccess to avoid model comparison
        try {
            CommandResult result = updateCommand.execute(model);
            assertEquals(expectedMessage, result.getFeedbackToUser());
            assertEquals(FXCollections.observableList(expectedModel.getFilteredDealList()),
                        FXCollections.observableList(model.getFilteredDealList()));
        } catch (CommandException e) {
            fail("Command should not fail with: " + e.getMessage());
        }
    }

    @Test
    public void execute_noFieldsSpecified_throwsCommandException() {
        TestModelStub model = new TestModelStub();

        ClientName buyerName = new ClientName("John");
        ClientName sellerName = new ClientName("Jane");

        Deal testDeal = new DealBuilder()
                .withPropertyName(VALID_PROPERTY_NAME)
                .withBuyer(buyerName.toString())
                .withSeller(sellerName.toString())
                .withPrice(VALID_PRICE)
                .withStatus(VALID_STATUS)
                .build();
        model.addDeal(testDeal);

        UpdateDealDescriptor descriptor = new UpdateDealDescriptorBuilder().build();
        UpdateDealCommand updateCommand = new UpdateDealCommand(INDEX_FIRST, descriptor);

        assertThrows(CommandException.class, () -> updateCommand.execute(model),
                UpdateDealCommand.MESSAGE_NO_CHANGES);
    }

    @Test
    public void execute_invalidDealIndex_throwsCommandException() {
        TestModelStub model = new TestModelStub();

        ClientName buyerName = new ClientName("John");
        ClientName sellerName = new ClientName("Jane");

        Deal testDeal = new DealBuilder()
                .withPropertyName(VALID_PROPERTY_NAME)
                .withBuyer(buyerName.toString())
                .withSeller(sellerName.toString())
                .withPrice(VALID_PRICE)
                .withStatus(VALID_STATUS)
                .build();
        model.addDeal(testDeal);

        // Deal index not in model
        Index outOfBoundIndex = INDEX_SECOND;
        UpdateDealDescriptor descriptor = new UpdateDealDescriptorBuilder()
                .withPropertyId(INDEX_FIRST)
                .build();
        UpdateDealCommand updateCommand = new UpdateDealCommand(outOfBoundIndex, descriptor);

        assertThrows(CommandException.class, () -> updateCommand.execute(model),
                UpdateDealCommand.MESSAGE_INVALID_DEAL_ID);
    }

    @Test
    public void execute_priceExceedsLimit_throwsCommandException() {
        TestModelStub model = new TestModelStub();

        Client buyer = new Client(
                new ClientName("John"),
                new Phone("12345678"),
                new Email("john@example.com"),
                new Address("Blk 123"));
        Client seller = new Client(
                new ClientName("Jane"),
                new Phone("87654321"),
                new Email("jane@example.com"),
                new Address("Blk 456"));
        Property property = new Property(
                new PropertyName(VALID_PROPERTY_NAME),
                new Address("Blk 789"), new Price(VALID_PRICE), Optional.empty(),
                Optional.empty(), new ClientName("Owner")
        );

        model.addClient(buyer);
        model.addClient(seller);
        model.addProperty(property);

        ClientName buyerName = buyer.getFullName();
        ClientName sellerName = seller.getFullName();

        Deal testDeal = new DealBuilder()
                .withPropertyName(VALID_PROPERTY_NAME)
                .withBuyer(buyerName.toString())
                .withSeller(sellerName.toString())
                .withPrice(VALID_PRICE)
                .withStatus(VALID_STATUS)
                .build();
        model.addDeal(testDeal);

        // Create a price that exceeds the limit
        long invalidPrice = 10000000; // 10 million, should be invalid

        UpdateDealDescriptor descriptor = new UpdateDealDescriptorBuilder()
                .withPropertyId(INDEX_FIRST)
                .withPrice(invalidPrice)
                .build();
        UpdateDealCommand updateCommand = new UpdateDealCommand(INDEX_FIRST, descriptor);

        assertThrows(CommandException.class, () -> updateCommand.execute(model),
                Price.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void execute_buyerSellerSamePerson_throwsCommandException() {
        TestModelStubWithClients model = new TestModelStubWithClients();

        Client client = new Client(
                new ClientName("John"),
                new Phone("12345678"),
                new Email("john@example.com"),
                new Address("Blk 123"));
        model.addClient(client);

        ClientName clientName = client.getFullName();

        Deal testDeal = new DealBuilder()
                .withPropertyName(VALID_PROPERTY_NAME)
                .withBuyer(clientName.toString())
                .withSeller("Jane")
                .withPrice(VALID_PRICE)
                .withStatus(VALID_STATUS)
                .build();
        model.addDeal(testDeal);

        // Both buyer and seller are the same index (same client)
        UpdateDealDescriptor descriptor = new UpdateDealDescriptorBuilder()
                .withBuyer(INDEX_FIRST)
                .withSeller(INDEX_FIRST)
                .build();
        UpdateDealCommand updateCommand = new UpdateDealCommand(INDEX_FIRST, descriptor);

        assertThrows(CommandException.class, () -> updateCommand.execute(model),
                UpdateDealCommand.MESSAGE_SAME_BUYER_SELLER);
    }

    @Test
    public void execute_propertyIdOnlySpecified_success() {
        TestModelStub model = new TestModelStub();

        Client buyer = new Client(
                new ClientName("John"),
                new Phone("12345678"),
                new Email("john@example.com"),
                new Address("Blk 123"));
        Client seller = new Client(
                new ClientName("Jane"),
                new Phone("87654321"),
                new Email("jane@example.com"),
                new Address("Blk 456"));
        Property property = new Property(
                new PropertyName(VALID_PROPERTY_NAME),
                new Address("Blk 789"), new Price(VALID_PRICE), Optional.empty(),
                Optional.empty(), new ClientName("Owner")
        );

        model.addClient(buyer);
        model.addClient(seller);
        model.addProperty(property);

        ClientName buyerName = buyer.getFullName();
        ClientName sellerName = seller.getFullName();

        Deal testDeal = new DealBuilder()
                .withPropertyName(VALID_PROPERTY_NAME_2)
                .withBuyer(buyerName.toString())
                .withSeller(sellerName.toString())
                .withPrice(VALID_PRICE)
                .withStatus(VALID_STATUS)
                .build();
        model.addDeal(testDeal);

        // Get client & property index
        Index buyerIndex = INDEX_FIRST;
        Index propertyIndex = INDEX_FIRST;

        UpdateDealDescriptor descriptor = new UpdateDealDescriptorBuilder()
                .withPropertyId(propertyIndex)
                .build();
        UpdateDealCommand updateCommand = new UpdateDealCommand(INDEX_FIRST, descriptor);

        String expectedMessage = String.format(UpdateDealCommand.MESSAGE_UPDATE_DEAL_SUCCESS, testDeal);

        // Create a copy of expected deal after update
        Deal expectedDeal = new Deal(property.getFullName(), buyerName, property.getOwner(),
                new Price(VALID_PRICE), VALID_STATUS);

        // Execute command
        try {
            CommandResult result = updateCommand.execute(model);

            // Check success message
            assertTrue(expectedMessage.equals(result.getFeedbackToUser()));

            // Check the deal was updated correctly
            Deal updatedDeal = model.getFilteredDealList().get(INDEX_FIRST.getZeroBased());

            // Compare expected deal with actual updated deal
            assertTrue(expectedDeal.getPropertyName().equals(updatedDeal.getPropertyName()));
            assertTrue(expectedDeal.getBuyer().equals(updatedDeal.getBuyer()));
            assertTrue(expectedDeal.getSeller().equals(updatedDeal.getSeller()));
            assertTrue(expectedDeal.getPrice().equals(updatedDeal.getPrice()));
            assertTrue(expectedDeal.getStatus().equals(updatedDeal.getStatus()));
        } catch (CommandException e) {
            throw new AssertionError("Command should not fail: " + e.getMessage());
        }
    }

    @Test
    public void execute_multipleFieldsSpecified_success() {
        TestModelStub model = new TestModelStub();

        Client buyer = new Client(
                new ClientName("John"),
                new Phone("12345678"),
                new Email("john@example.com"),
                new Address("Blk 123"));
        Client seller = new Client(
                new ClientName("Jane"),
                new Phone("87654321"),
                new Email("jane@example.com"),
                new Address("Blk 456"));
        Property property = new Property(
                new PropertyName(VALID_PROPERTY_NAME),
                new Address("Blk 789"), new Price(VALID_PRICE), Optional.empty(),
                Optional.empty(), new ClientName("Owner")
        );

        model.addClient(buyer);
        model.addClient(seller);
        model.addProperty(property);

        ClientName buyerName = buyer.getFullName();
        ClientName sellerName = seller.getFullName();

        Deal testDeal = new DealBuilder()
                .withPropertyName(VALID_PROPERTY_NAME_2)
                .withBuyer(buyerName.toString())
                .withSeller(sellerName.toString())
                .withPrice(VALID_PRICE)
                .withStatus(VALID_STATUS)
                .build();
        model.addDeal(testDeal);

        // Get client & property index
        Index buyerIndex = INDEX_FIRST;
        Index propertyIndex = INDEX_FIRST;

        UpdateDealDescriptor descriptor = new UpdateDealDescriptorBuilder()
                .withPropertyId(propertyIndex)
                .withBuyer(buyerIndex)
                .withPrice(VALID_PRICE_2)
                .withStatus(VALID_STATUS_2)
                .build();
        UpdateDealCommand updateCommand = new UpdateDealCommand(INDEX_FIRST, descriptor);

        String expectedMessage = String.format(UpdateDealCommand.MESSAGE_UPDATE_DEAL_SUCCESS, testDeal);

        // Create a copy of expected deal after update
        Deal expectedDeal = new Deal(property.getFullName(), buyerName, property.getOwner(),
                new Price(VALID_PRICE_2), VALID_STATUS_2);

        // Execute command
        try {
            CommandResult result = updateCommand.execute(model);

            // Check success message
            assertTrue(expectedMessage.equals(result.getFeedbackToUser()));

            // Check the deal was updated correctly
            Deal updatedDeal = model.getFilteredDealList().get(INDEX_FIRST.getZeroBased());

            // Compare expected deal with actual updated deal
            assertTrue(expectedDeal.getPropertyName().equals(updatedDeal.getPropertyName()));
            assertTrue(expectedDeal.getBuyer().equals(updatedDeal.getBuyer()));
            assertTrue(expectedDeal.getSeller().equals(updatedDeal.getSeller()));
            assertTrue(expectedDeal.getPrice().equals(updatedDeal.getPrice()));
            assertTrue(expectedDeal.getStatus().equals(updatedDeal.getStatus()));
        } catch (CommandException e) {
            throw new AssertionError("Command should not fail: " + e.getMessage());
        }
    }

    @Test
    public void equals() {
        final UpdateDealCommand standardCommand = new UpdateDealCommand(INDEX_FIRST,
            new UpdateDealDescriptorBuilder().withPropertyId(INDEX_FIRST).build());

        // same values -> returns true
        UpdateDealDescriptor copyDescriptor = new UpdateDealDescriptor(
            new UpdateDealDescriptorBuilder().withPropertyId(INDEX_FIRST).build());
        UpdateDealCommand commandWithSameValues = new UpdateDealCommand(INDEX_FIRST, copyDescriptor);
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> returns false
        assertFalse(standardCommand.equals(new AddDealCommand(INDEX_FIRST, INDEX_FIRST,
            new Price(VALID_PRICE), VALID_STATUS)));

        // different index -> returns false
        assertFalse(standardCommand.equals(new UpdateDealCommand(INDEX_SECOND,
            new UpdateDealDescriptorBuilder().withPropertyId(INDEX_FIRST).build())));

        // different descriptor -> returns false
        assertFalse(standardCommand.equals(new UpdateDealCommand(INDEX_FIRST,
            new UpdateDealDescriptorBuilder().withPropertyId(INDEX_SECOND).build())));
    }

    /**
     * A Model stub that contains a single deal and supports adding and updating deals.
     */
    private static class TestModelStub extends ModelStub {
        private final List<Deal> deals = new ArrayList<>();
        private final List<Client> clients = new ArrayList<>();
        private final List<Property> properties = new ArrayList<>();

        @Override
        public boolean hasDeal(Deal deal) {
            return deals.contains(deal);
        }

        @Override
        public void addDeal(Deal deal) {
            deals.add(deal);
        }

        @Override
        public void setDeal(Deal target, Deal editedDeal) {
            int index = deals.indexOf(target);
            if (index >= 0) {
                deals.set(index, editedDeal);
            }
        }

        public Deal getDeal(Index index) {
            if (index.getZeroBased() >= deals.size()) {
                throw new IndexOutOfBoundsException();
            }
            return deals.get(index.getZeroBased());
        }

        @Override
        public ObservableList<Deal> getFilteredDealList() {
            return FXCollections.observableList(deals);
        }

        @Override
        public void updateFilteredDealList(java.util.function.Predicate<Deal> predicate) {
            // Do nothing since this is a stub
        }

        @Override
        public boolean hasClient(Client client) {
            return clients.contains(client);
        }

        @Override
        public void addClient(Client client) {
            clients.add(client);
        }

        @Override
        public ObservableList<Client> getFilteredClientList() {
            return FXCollections.observableList(clients);
        }

        public Client getClient(Index index) {
            if (index.getZeroBased() >= clients.size()) {
                throw new IndexOutOfBoundsException();
            }
            return clients.get(index.getZeroBased());
        }

        @Override
        public boolean hasProperty(Property property) {
            return properties.contains(property);
        }

        @Override
        public void addProperty(Property property) {
            properties.add(property);
        }

        @Override
        public ObservableList<Property> getFilteredPropertyList() {
            return FXCollections.observableList(properties);
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            AddressBook addressBook = new AddressBook();
            for (Client client : clients) {
                addressBook.addClient(client);
            }
            for (Deal deal : deals) {
                addressBook.addDeal(deal);
            }
            for (Property property : properties) {
                addressBook.addProperty(property);
            }
            return addressBook;
        }
    }

    /**
     * A Model stub with clients but no deals.
     */
    private static class TestModelStubWithClients extends TestModelStub {
    }
}

