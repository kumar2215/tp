package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.Locale;
import java.util.Optional;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.StringUtil;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.client.ClientName;
import seedu.address.model.client.Email;
import seedu.address.model.client.Phone;
import seedu.address.model.commons.Address;
import seedu.address.model.commons.Price;
import seedu.address.model.deal.DealStatus;
import seedu.address.model.event.EventType;
import seedu.address.model.event.Note;
import seedu.address.model.property.Description;
import seedu.address.model.property.PropertyName;
import seedu.address.model.property.Size;

/**
 * Contains utility methods used for parsing strings in the various *Parser classes.
 */
public class ParserUtil {
    public static final String MESSAGE_INVALID_DATETIME =
            "Invalid date: %s%nUse dd-mm-yyyy hhmm (e.g. 30-04-2025 1700).";
    public static final String MESSAGE_INVALID_INDEX = "Index is not a positive integer.";

    public static final DateTimeFormatter DATE_FORMAT_TEXT = DateTimeFormatter
            .ofPattern("dd-MM-yyyy HHmm", Locale.ENGLISH)
            .withResolverStyle(ResolverStyle.SMART);

    /**
     * Parses a string representation of date and time into an {@code LocalDateTime}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code dateTime} is invalid.
     */
    public static LocalDateTime parseDateTime(String dateTime) throws ParseException {
        try {
            LocalDateTime localDateTime = LocalDateTime.parse(dateTime, DATE_FORMAT_TEXT);

            // This step filters out nonsense dates like 29-02-2025 (accepts 29-02-2024)
            if (!DATE_FORMAT_TEXT.format(localDateTime).equals(dateTime)) {
                throw new ParseException(String.format(MESSAGE_INVALID_DATETIME, dateTime));
            }
            return localDateTime;
        } catch (DateTimeParseException e) {
            throw new ParseException(String.format(MESSAGE_INVALID_DATETIME, dateTime));
        }
    }

    /**
     * Parses {@code oneBasedIndex} into an {@code Index} and returns it. Leading and trailing whitespaces will be
     * trimmed.
     * @throws ParseException if the specified index is invalid (not non-zero unsigned integer).
     */
    public static Index parseIndex(String oneBasedIndex) throws ParseException {
        String trimmedIndex = oneBasedIndex.trim();
        if (!StringUtil.isNonZeroUnsignedInteger(trimmedIndex)) {
            throw new ParseException(MESSAGE_INVALID_INDEX);
        }
        return Index.fromOneBased(Integer.parseInt(trimmedIndex));
    }

    /**
     * Parses a {@code String clientName} into a {@code ClientName}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code clientName} is invalid.
     */
    public static ClientName parseClientName(String clientName) throws ParseException {
        requireNonNull(clientName);
        String trimmedClientName = clientName.trim();
        if (!ClientName.isValidClientName(trimmedClientName)) {
            throw new ParseException(ClientName.MESSAGE_CONSTRAINTS);
        }
        return new ClientName(trimmedClientName);
    }

    /**
     * Parses a {@code String phone} into a {@code Phone}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code phone} is invalid.
     */
    public static Phone parsePhone(String phone) throws ParseException {
        requireNonNull(phone);
        String trimmedPhone = phone.trim();
        if (!Phone.isValidPhone(trimmedPhone)) {
            throw new ParseException(Phone.MESSAGE_CONSTRAINTS);
        }
        return new Phone(trimmedPhone);
    }

    /**
     * Parses a {@code String address} into an {@code Address}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code address} is invalid.
     */
    public static Address parseAddress(String address) throws ParseException {
        requireNonNull(address);
        String trimmedAddress = address.trim();
        if (!Address.isValidAddress(trimmedAddress)) {
            throw new ParseException(Address.MESSAGE_CONSTRAINTS);
        }
        return new Address(trimmedAddress);
    }

    /**
     * Parses a {@code String email} into an {@code Email}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code email} is invalid.
     */
    public static Email parseEmail(String email) throws ParseException {
        requireNonNull(email);
        String trimmedEmail = email.trim();
        if (!Email.isValidEmail(trimmedEmail)) {
            throw new ParseException(Email.MESSAGE_CONSTRAINTS);
        }
        return new Email(trimmedEmail);
    }

    /**
     * Parses a {@code String propertyName} into a {@code propertyName}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code propertyName} is invalid.
     */
    public static PropertyName parsePropertyName(String propertyName) throws ParseException {
        requireNonNull(propertyName);
        String trimmedPropertyName = propertyName.trim();
        if (!PropertyName.isValidPropertyName(trimmedPropertyName)) {
            throw new ParseException(PropertyName.MESSAGE_CONSTRAINTS);
        }
        return new PropertyName(trimmedPropertyName);
    }

    /**
     * Parses a {@code String price} into a {@code Price}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code price} is invalid.
     */
    public static Price parsePrice(Long price) throws ParseException {
        requireNonNull(price);
        if (!Price.isValidPrice(price)) {
            throw new ParseException(Price.MESSAGE_CONSTRAINTS);
        }
        return new Price(price);
    }

    /**
     * Parses a {@code String size} into a {@code size}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code size} is invalid.
     */
    public static Optional<Size> parseSize(String size) throws ParseException {
        if (size == null) {
            throw new ParseException(Size.MESSAGE_CONSTRAINTS);
        }

        if (size.isBlank()) {
            return Optional.empty();
        }
        if (!Size.isValidSize(size)) {
            throw new ParseException(Size.MESSAGE_CONSTRAINTS);
        }

        return Optional.of(new Size(size));
    }

    /**
     * Parses a {@code String description} into an {@code Description}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code description} is invalid.
     */
    public static Optional<Description> parseDescription(String description) throws ParseException {
        if (description == null) {
            throw new ParseException(Description.MESSAGE_CONSTRAINTS);
        }

        if (description.isBlank()) {
            return Optional.empty();
        }

        String trimmedDescription = description.trim();
        if (!Description.isValidDescription(trimmedDescription)) {
            throw new ParseException(Description.MESSAGE_CONSTRAINTS);
        }

        return Optional.of(new Description(trimmedDescription));
    }

    /**
     * Parses a {@code String owner} into a {@code ClientName}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code owner} is invalid.
     */
    public static ClientName parseOwner(String owner) throws ParseException {
        requireNonNull(owner);
        if (!ClientName.isValidClientName(owner)) {
            throw new ParseException(ClientName.MESSAGE_CONSTRAINTS);
        }
        return new ClientName(owner);
    }

    /**
     * Parses a {@code String eventType} into an {@code EventType}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code eventType} is invalid.
     */
    public static EventType parseEventType(String eventType) throws ParseException {
        requireNonNull(eventType);
        String trimmedEventType = eventType.trim();
        try {
            return EventType.valueOf(trimmedEventType.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ParseException(EventType.MESSAGE_CONSTRAINTS);
        }
    }

    /**
     * Parses a {@code String dealStatus} into an {@code DealStatus}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code eventType} is invalid.
     */
    public static DealStatus parseDealStatus(String dealStatus) throws ParseException {
        requireNonNull(dealStatus);
        String trimmedDealStatus = dealStatus.trim();
        try {
            return DealStatus.valueOf(trimmedDealStatus.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ParseException(DealStatus.MESSAGE_CONSTRAINTS);
        }
    }

    /**
     * Parses a {@code String note} into an {@code Note}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code note} is invalid.
     */
    public static Note parseNote(String note) throws ParseException {
        requireNonNull(note);
        String trimmedNote = note.trim();
        if (trimmedNote.isEmpty()) {
            throw new ParseException(Note.MESSAGE_CONSTRAINTS);
        }
        return new Note(trimmedNote);
    }
}
