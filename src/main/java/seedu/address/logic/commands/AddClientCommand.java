package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MAKE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MODEL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_VIN;
import static seedu.address.logic.parser.CliSyntax.PREFIX_VRN;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

/**
 * Adds a person to the MATER address book.
 */
public class AddClientCommand extends Command {

    public static final String COMMAND_WORD = "add-client";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a client to the address book. "
            + "Parameters: "
            + PREFIX_NAME + "NAME "
            + PREFIX_PHONE + "PHONE "
            + PREFIX_EMAIL + "EMAIL "
            + PREFIX_ADDRESS + "ADDRESS " // + "[" + PREFIX_TAG + "TAG]...\n"
            + PREFIX_VRN + " VEHICLE REGISTRATION NUMBER (OPTIONAL) "
            + PREFIX_VIN + " VEHICLE IDENTIFICATION NUMBER (OPTIONAL) "
            + PREFIX_MAKE + " VEHICLE MAKE (OPTIONAL) "
            + PREFIX_MODEL + " VEHICLE MODEL (OPTIONAL) \n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_NAME + "John Doe "
            + PREFIX_PHONE + "98765432 "
            + PREFIX_EMAIL + "johnd@example.com "
            + PREFIX_ADDRESS + "311, Clementi Ave 2, #02-25 "
            + PREFIX_VRN + "SGX1234B "
            + PREFIX_VIN + "KMHGH4JH3EU073801 "
            + PREFIX_MAKE + "Toyota "
            + PREFIX_MODEL + "Corolla ";

    public static final String VEHICLE_DETAILS_MISSING =
            "Vehicle details are missing. Please provide all vehicle details. \n" + MESSAGE_USAGE;
    public static final String MESSAGE_SUCCESS = "New person added: %1$s";
    public static final String MESSAGE_SUCCESS_WITH_CAR = "with VIN: ";
    public static final String MESSAGE_DUPLICATE_PERSON = "This person already exists in the address book";
    public static final String MESSAGE_NO_CAR_TO_ADD_ISSUES = "This person does not have a car to add issues to";

    private final Person toAdd;

    /**
     * Creates an AddClientCommand to add the specified {@code Person}
     */
    public AddClientCommand(Person person) {
        requireNonNull(person);
        System.out.println(person);
        toAdd = person;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (model.hasPerson(toAdd)) {
            throw new CommandException(MESSAGE_DUPLICATE_PERSON);
        }

        if (model.hasCar(toAdd.getCar())) {
            throw new CommandException("This car already exists in the address book");
        }

        // check if there is a car to add issues to
        if (toAdd.getCar() == null && toAdd.getIssues().size() > 0) {
            throw new CommandException(MESSAGE_NO_CAR_TO_ADD_ISSUES);
        }

        model.addPerson(toAdd);

        String message = String.format(MESSAGE_SUCCESS, Messages.format(toAdd));
        if (toAdd.getCar() != null) {
            message += MESSAGE_SUCCESS_WITH_CAR + Messages.formatCar(toAdd);
        }

        return new CommandResult(message);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AddClientCommand)) {
            return false;
        }

        AddClientCommand otherAddClientCommand = (AddClientCommand) other;
        return toAdd.equals(otherAddClientCommand.toAdd);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("toAdd", toAdd)
                .toString();
    }
}
