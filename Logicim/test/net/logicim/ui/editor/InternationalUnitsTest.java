package net.logicim.ui.editor;

import net.logicim.assertions.Validator;

public class InternationalUnitsTest
{
  public static void test()
  {
    Validator.validate("1.00 Hz", InternationalUnits.toString(1, "Hz"));
    Validator.validate("10.0 Hz", InternationalUnits.toString(10, "Hz"));
    Validator.validate("100 Hz", InternationalUnits.toString(100, "Hz"));
    Validator.validate("1.00 KHz", InternationalUnits.toString(1000, "Hz"));
    Validator.validate("10.0 KHz", InternationalUnits.toString(10000, "Hz"));
    Validator.validate("100 KHz", InternationalUnits.toString(100000, "Hz"));
    Validator.validate("1.00 MHz", InternationalUnits.toString(1000000, "Hz"));
    Validator.validate("10.0 MHz", InternationalUnits.toString(10000000, "Hz"));

    Validator.validate("5.99 MHz", InternationalUnits.toString(5993075d, "Hz"));
    Validator.validate("3.45 Hz", InternationalUnits.toString(3.45, "Hz"));
    Validator.validate("34.5 Hz", InternationalUnits.toString(34.5, "Hz"));
    Validator.validate("35.0 Hz", InternationalUnits.toString(34.9999, "Hz"));
    Validator.validate("35.1 GHz", InternationalUnits.toString(35050000000d, "Hz"));

    Validator.validate("100 mHz", InternationalUnits.toString(0.1, "Hz"));
    Validator.validate("10.0 mHz", InternationalUnits.toString(0.010005, "Hz"));
    Validator.validate("1.00 mHz", InternationalUnits.toString(0.001, "Hz"));
    Validator.validate("100 uHz", InternationalUnits.toString(0.00010005, "Hz"));
    Validator.validate("10.0 uHz", InternationalUnits.toString(0.000010005, "Hz"));
    Validator.validate("1.00 uHz", InternationalUnits.toString(0.0000010005, "Hz"));
    Validator.validate("100 nHz", InternationalUnits.toString(0.00000010005, "Hz"));
    Validator.validate("10.0 nHz", InternationalUnits.toString(0.000000010005, "Hz"));
    Validator.validate("1.00 nHz", InternationalUnits.toString(0.0000000010005, "Hz"));

    Validator.validate("100 mHz", InternationalUnits.toString(0.1, "Hz"));
    Validator.validate("99.0 mHz", InternationalUnits.toString(0.099, "Hz"));
    Validator.validate("10.0 mHz", InternationalUnits.toString(0.01, "Hz"));
    Validator.validate("9.90 mHz", InternationalUnits.toString(0.0099, "Hz"));
  }
}

