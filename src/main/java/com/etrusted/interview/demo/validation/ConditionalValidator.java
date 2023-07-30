package com.etrusted.interview.demo.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import static org.apache.commons.lang3.StringUtils.isEmpty;

@Slf4j
public class ConditionalValidator implements ConstraintValidator<ConditionalValidation, Object> {

    private String selected;
    private String[] required;
    private String message;
    private String[] values;

    @Override
    public void initialize(ConditionalValidation requiredIfChecked) {
        selected = requiredIfChecked.selected();
        required = requiredIfChecked.required();
        message = requiredIfChecked.message();
        values = requiredIfChecked.values();
    }

    @Override
    public boolean isValid(Object object, ConstraintValidatorContext context) {
        try {
            Object checkedValue = BeanUtils.getProperty(object, selected);
            if (checkedValue != null && Arrays.asList(values).contains(checkedValue)) {
                for (String propName : required) {
                    Object requiredValue = BeanUtils.getProperty(object, propName);
                    if (requiredValue == null || isEmpty(requiredValue.toString())) {
                        String errorMessage = message.replace("{propName}", propName);
                        context.disableDefaultConstraintViolation();
                        context.buildConstraintViolationWithTemplate(errorMessage)
                                .addPropertyNode(propName)
                                .addConstraintViolation();
                        return false;
                    }
                }
            }
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            log.error("Exception occurred while accessing class : {}, exception : {}", object.getClass().getName(), e.getMessage());
            return false;
        }
        return true;
    }
}
