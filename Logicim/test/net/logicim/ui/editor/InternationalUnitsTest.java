package net.logicim.ui.editor;

import static net.logicim.assertions.Validator.validate;

public class InternationalUnitsTest
{
  protected static void testToString()
  {
    validate("1.00 Hz", InternationalUnits.toString(1, "Hz"));
    validate("10.0 Hz", InternationalUnits.toString(10, "Hz"));
    validate("100 Hz", InternationalUnits.toString(100, "Hz"));
    validate("1.00 KHz", InternationalUnits.toString(1000, "Hz"));
    validate("10.0 KHz", InternationalUnits.toString(10000, "Hz"));
    validate("100 KHz", InternationalUnits.toString(100000, "Hz"));
    validate("1.00 MHz", InternationalUnits.toString(1000000, "Hz"));
    validate("10.0 MHz", InternationalUnits.toString(10000000, "Hz"));

    validate("5.99 MHz", InternationalUnits.toString(5993075d, "Hz"));
    validate("3.45 Hz", InternationalUnits.toString(3.45, "Hz"));
    validate("34.5 Hz", InternationalUnits.toString(34.5, "Hz"));
    validate("35.0 Hz", InternationalUnits.toString(34.9999, "Hz"));
    validate("35.1 GHz", InternationalUnits.toString(35050000000d, "Hz"));

    validate("100 mHz", InternationalUnits.toString(0.1, "Hz"));
    validate("10.0 mHz", InternationalUnits.toString(0.010005, "Hz"));
    validate("1.00 mHz", InternationalUnits.toString(0.001, "Hz"));
    validate("100 uHz", InternationalUnits.toString(0.00010005, "Hz"));
    validate("10.0 uHz", InternationalUnits.toString(0.000010005, "Hz"));
    validate("1.00 uHz", InternationalUnits.toString(0.0000010005, "Hz"));
    validate("100 nHz", InternationalUnits.toString(0.00000010005, "Hz"));
    validate("10.0 nHz", InternationalUnits.toString(0.000000010005, "Hz"));
    validate("1.00 nHz", InternationalUnits.toString(0.0000000010005, "Hz"));

    validate("100 mHz", InternationalUnits.toString(0.1, "Hz"));
    validate("99.0 mHz", InternationalUnits.toString(0.099, "Hz"));
    validate("10.0 mHz", InternationalUnits.toString(0.01, "Hz"));
    validate("9.90 mHz", InternationalUnits.toString(0.0099, "Hz"));
  }

  protected static void testParse()
  {
    validate(1e6d, InternationalUnits.parse("1 MHz", "Hz"));
    validate(1, InternationalUnits.parse("1 Hz", "Hz"));
    validate(0, InternationalUnits.parse("0 Hz", "Hz"));
    validate(1, InternationalUnits.parse("1", "Hz"));
    validate(1e6d, InternationalUnits.parse("1MHz", "Hz"));
    validate(1e6d, InternationalUnits.parse("1Mhz", "Hz"));
    validate(0.001, InternationalUnits.parse("1mhz", "Hz"));
  }

  public static void test()
  {
    testToString();
    testParse();
  }
}

