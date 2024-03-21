package com.rkade;

import javax.swing.*;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;
import java.text.NumberFormat;
import java.text.ParseException;

public class IntegerFormatterFactory extends DefaultFormatterFactory {
    private final NumberFormatter formatter;

    public IntegerFormatterFactory(int min, int max) {
        NumberFormat format = NumberFormat.getIntegerInstance();
        format.setGroupingUsed(false); //no commas
        formatter = new NumberFormatter() {
            public Object stringToValue(String text) throws ParseException {
                if (text == null) {
                    return null;
                } else if (text.isEmpty()) {
                    return null;
                }
                return super.stringToValue(text);
            }
        };
        formatter.setFormat(format);
        formatter.setValueClass(Integer.class);
        formatter.setMinimum(min);
        formatter.setMaximum(max);
        formatter.setAllowsInvalid(false);
    }

    @Override
    public JFormattedTextField.AbstractFormatter getFormatter(JFormattedTextField tf) {
        return formatter;
    }
}
