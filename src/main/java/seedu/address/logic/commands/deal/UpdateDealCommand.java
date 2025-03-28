package seedu.address.logic.commands.deal;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_BUYER;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PRICE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PROPERTY_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SELLER;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STATUS;

import java.util.List;
import java.util.Optional;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.CollectionUtil;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.EditDescriptor;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.client.ClientName;
import seedu.address.model.commons.Price;
import seedu.address.model.deal.Deal;
import seedu.address.model.deal.DealStatus;
import seedu.address.model.property.PropertyName;

/**
 * Updates the details of an existing deal in the address book.
 */
public class UpdateDealCommand extends EditCommand<Deal> {

    public static final String COMMAND_WORD = "update_deal";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Updates a property deal identified "
            + "by the index number used in the displayed deal list. "
            + "Existing values will be overwritten by the input values.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + "[" + PREFIX_PROPERTY_NAME + "PROPERTY_NAME] "
            + "[" + PREFIX_BUYER + "BUYER] "
            + "[" + PREFIX_SELLER + "SELLER] "
            + "[" + PREFIX_PRICE + "PRICE] "
            + "[" + PREFIX_STATUS + "STATUS]\n"
            + "Example: " + COMMAND_WORD + " 3 "
            + PREFIX_STATUS + "CLOSED";

    public static final String MESSAGE_UPDATE_DEAL_SUCCESS = "Deal updated successfully";
    public static final String MESSAGE_NO_CHANGES = "At least one field to update must be provided";
    public static final String MESSAGE_INVALID_DEAL_ID = "Invalid deal ID";
    public static final String MESSAGE_SAME_BUYER_SELLER = "Buyer and seller cannot be the same person";
    public static final String MESSAGE_DUPLICATE_DEAL = "This deal already exists in the address book";

    private final UpdateDealDescriptor updateDealDescriptor;

    /**
     * Creates an UpdateDealCommand to update the specified {@code Deal}
     *
     * @param index of the deal in the filtered deal list to edit
     * @param updateDealDescriptor details to update the deal with
     */
    public UpdateDealCommand(Index index, UpdateDealDescriptor updateDealDescriptor) {
        super(index, updateDealDescriptor);
        this.updateDealDescriptor = new UpdateDealDescriptor(updateDealDescriptor);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Deal> lastShownList = model.getFilteredDealList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(MESSAGE_INVALID_DEAL_ID);
        }

        Deal dealToUpdate = lastShownList.get(index.getZeroBased());

        // If no fields are edited, throw an exception
        if (!updateDealDescriptor.isAnyFieldEdited()) {
            throw new CommandException(MESSAGE_NO_CHANGES);
        }

        Deal updatedDeal = createUpdatedDeal(dealToUpdate, model, updateDealDescriptor);

        if (!dealToUpdate.isSameDeal(updatedDeal) && model.hasDeal(updatedDeal)) {
            throw new CommandException(MESSAGE_DUPLICATE_DEAL);
        }

        model.setDeal(dealToUpdate, updatedDeal);
        model.updateFilteredDealList(Model.PREDICATE_SHOW_ALL_DEALS);
        return new CommandResult(String.format(MESSAGE_UPDATE_DEAL_SUCCESS, updatedDeal));
    }

    /**
     * Creates and returns a {@code Deal} with the details of {@code dealToUpdate}
     * updated with {@code updateDealDescriptor}. Fields not provided in the descriptor
     * will retain their original values.
     */
    private static Deal createUpdatedDeal(Deal dealToUpdate, Model model, UpdateDealDescriptor updateDealDescriptor)
            throws CommandException {
        assert dealToUpdate != null;
        PropertyName updatedPropertyName = updateDealDescriptor.getPropertyName()
                .orElse(dealToUpdate.getPropertyName());
        ClientName updatedBuyer = updateDealDescriptor.getBuyer()
                .map(index -> model.getFilteredClientList().get(index.getZeroBased()).getClientName())
                .orElse(dealToUpdate.getBuyer());
        ClientName updatedSeller = updateDealDescriptor.getSeller()
                .map(index -> model.getFilteredClientList().get(index.getZeroBased()).getClientName())
                .orElse(dealToUpdate.getSeller());

        // Validate that buyer and seller are not the same person
        if (updatedBuyer.equals(updatedSeller)) {
            throw new CommandException(MESSAGE_SAME_BUYER_SELLER);
        }

        Price updatedPrice = updateDealDescriptor.getPrice().orElse(dealToUpdate.getPrice());

        // Check if price exceeds limit
        if (!Price.isValidPrice(updatedPrice.value)) {
            throw new CommandException(Price.MESSAGE_CONSTRAINTS);
        }

        DealStatus updatedStatus = updateDealDescriptor.getStatus().orElse(dealToUpdate.getStatus());
        return new Deal(updatedPropertyName, updatedBuyer, updatedSeller, updatedPrice, updatedStatus);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof UpdateDealCommand)) {
            return false;
        }

        UpdateDealCommand otherUpdateCommand = (UpdateDealCommand) other;
        return index.equals(otherUpdateCommand.index)
            && updateDealDescriptor.equals(otherUpdateCommand.updateDealDescriptor);
    }

    /**
     * Stores the details to edit the deal with. Each non-empty field value will replace the corresponding field.
     */
    public static class UpdateDealDescriptor extends EditDescriptor<Deal> {
        private PropertyName propertyName;
        private Index buyer;
        private Index seller;
        private Price price;
        private DealStatus status;

        public UpdateDealDescriptor() {}

        /**
         * Copy constructor.
         *
         */
        public UpdateDealDescriptor(UpdateDealDescriptor toCopy) {
            setPropertyName(toCopy.propertyName);
            setBuyer(toCopy.buyer);
            setSeller(toCopy.seller);
            setPrice(toCopy.price);
            setStatus(toCopy.status);
        }

        @Override
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(propertyName, buyer, seller, price, status);
        }

        public void setPropertyName(PropertyName propertyName) {
            this.propertyName = propertyName;
        }
        public Optional<PropertyName> getPropertyName() {
            return Optional.ofNullable(propertyName);
        }

        public void setBuyer(Index buyer) {
            this.buyer = buyer;
        }
        public Optional<Index> getBuyer() {
            return Optional.ofNullable(buyer);
        }

        public void setSeller(Index seller) {
            this.seller = seller;
        }
        public Optional<Index> getSeller() {
            return Optional.ofNullable(seller);
        }

        public void setPrice(Price price) {
            this.price = price;
        }
        public Optional<Price> getPrice() {
            return Optional.ofNullable(price);
        }

        public void setStatus(DealStatus status) {
            this.status = status;
        }
        public Optional<DealStatus> getStatus() {
            return Optional.ofNullable(status);
        }

        @Override
        public boolean equals(Object other) {
            if (other == this) {
                return true;
            }

            if (!(other instanceof UpdateDealDescriptor otherUpdateDealDescriptor)) {
                return false;
            }

            return getPropertyName().equals(otherUpdateDealDescriptor.getPropertyName())
                    && getBuyer().equals(otherUpdateDealDescriptor.getBuyer())
                    && getSeller().equals(otherUpdateDealDescriptor.getSeller())
                    && getPrice().equals(otherUpdateDealDescriptor.getPrice())
                    && getStatus().equals(otherUpdateDealDescriptor.getStatus());
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this)
                    .add("propertyName", propertyName)
                    .add("buyer", buyer)
                    .add("seller", seller)
                    .add("price", price)
                    .add("status", status)
                    .toString();
        }
    }
}

