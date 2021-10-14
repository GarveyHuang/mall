package com.shura.mall.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

/**
 * @Author: Garvey
 * @Created: 2021/10/14
 * @Description: 状态标记校验器
 */
public class MyFlagValidator implements ConstraintValidator<FlagValidator, Integer> {

    private String[] values;

    @Override
    public void initialize(FlagValidator flagValidator) {
        this.values = flagValidator.values();
    }

    @Override
    public boolean isValid(Integer status, ConstraintValidatorContext constraintValidatorContext) {
        boolean isValid = false;

        if (Objects.isNull(status)) {
            // 当状态为空时，使用默认值
            return true;
        }

        for (String value : values) {
            if (Objects.equals(value, String.valueOf(status))) {
                isValid = true;
                break;
            }
        }

        return isValid;
    }
}
