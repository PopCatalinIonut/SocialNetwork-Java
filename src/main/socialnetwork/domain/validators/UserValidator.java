package main.socialnetwork.domain.validators;

import main.socialnetwork.domain.User;

public class UserValidator implements Validator<User> {
    @Override
    public void validate(User entity) throws ValidationException {
        String errorMessage = "";
        if (entity.getFirstName() == null || entity.getFirstName().equals(""))
            errorMessage += "Numele nu poate fi null!\n";
        if (entity.getLastName() == null || entity.getLastName().equals(""))
            errorMessage += "Prenumele nu poate fi null!\n";

        if (!errorMessage.equals(""))
            throw new ValidationException(errorMessage);

        if (!entity.getFirstName().matches("^[A-Z][a-z]+$"))
            errorMessage += "Numele trebuie sa contina doar litere si sa inceapa cu majuscula!\n";
        if (!entity.getLastName().matches("^[A-Z][a-z]+$"))
            errorMessage += "Prenumele trebuie sa contina doar litere si sa inceapa cu majuscula!";

        if (!errorMessage.equals(""))
            throw new ValidationException(errorMessage);
    }

}
