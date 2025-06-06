package com.amit_g.helper.inputValidators;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.EditText;

import java.time.LocalDate;

import com.amit_g.helper.DateUtil;

public class DateRule extends Rule{
    private static LocalDate lowBound;
    private static LocalDate upBound;

    public DateRule(View view, RuleOperation operation, String message, LocalDate lowBound, LocalDate upBound) {
        super(view, operation, message);

        DateRule.lowBound = lowBound;
        DateRule.upBound = upBound;
    }

    public LocalDate getLowBound(){
        return lowBound;
    }

    public LocalDate getUpBound(){
        return upBound;
    }

    @SuppressLint("NewApi")
    public static boolean validate(DateRule rule) {
        if (Validator.requiredValidator(rule)) {
            String input = ((EditText) rule.view).getText().toString();
            LocalDate dateToCheck = DateUtil.stringToLocalDate(input);

            if (dateToCheck == null) {
                rule.isValid = false;
                rule.setMessage("Invalid date format (use dd/MM/yyyy)");
                return false;
            }

            // Check bounds if set
            if (rule.lowBound != null && dateToCheck.isBefore(rule.lowBound)) {
                rule.isValid = false;
                rule.setMessage("Date must not be before " + rule.lowBound.format(DateUtil.getFormatter()));
                return false;
            }

            if (rule.upBound != null && dateToCheck.isAfter(rule.upBound)) {
                rule.isValid = false;
                rule.setMessage("Date must not be after " + rule.upBound.format(DateUtil.getFormatter()));
                return false;
            }

            rule.isValid = true;
        } else {
            rule.isValid = false;
        }

        return rule.isValid;
    }


}
