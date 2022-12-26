package br.com.gubee.api.out.validation;

import javax.validation.*;
import java.util.Set;

public abstract class SelfValidation<T> {
    private Validator validator;

    private static ValidatorFactory factory = Validation.buildDefaultValidatorFactory();

    public SelfValidation() {
        validator = factory.getValidator();
    }

    /**
     * Evaluates all Bean Validations on the attributes of this
     * instance.
     */
    protected void validateSelf() {
        Set<ConstraintViolation<T>> violations = validator.validate((T) this);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }
}
