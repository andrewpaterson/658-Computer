package net.logicim.domain.common.propagation;

import net.logicim.domain.common.voltage.Voltage;

import static net.logicim.assertions.Validator.validate;
import static net.logicim.assertions.Validator.validateNotNull;

public class FamilyVoltageConfigurationTest
{
  static void testHighVoltageIn()
  {
    FamilyVoltageConfigurationStore.getInstance();
    testHighVoltageIn("LVC", "0.0V -> 0.0V\n" +
                             "0.1V -> 0.1V\n" +
                             "0.2V -> 0.1V\n" +
                             "0.3V -> 0.2V\n" +
                             "0.4V -> 0.3V\n" +
                             "0.5V -> 0.3V\n" +
                             "0.6V -> 0.4V\n" +
                             "0.7V -> 0.5V\n" +
                             "0.8V -> 0.5V\n" +
                             "0.9V -> 0.6V\n" +
                             "1.0V -> 0.7V\n" +
                             "1.1V -> 0.7V\n" +
                             "1.2V -> 0.8V\n" +
                             "1.3V -> 0.9V\n" +
                             "1.4V -> 0.9V\n" +
                             "1.5V -> 1.0V\n" +
                             "1.6V -> 1.1V\n" +
                             "1.7V -> 1.1V\n" +
                             "1.8V -> 1.2V\n" +
                             "1.9V -> 1.3V\n" +
                             "2.0V -> 1.3V\n" +
                             "2.1V -> 1.4V\n" +
                             "2.2V -> 1.4V\n" +
                             "2.3V -> 1.5V\n" +
                             "2.4V -> 1.5V\n" +
                             "2.5V -> 1.6V\n" +
                             "2.6V -> 1.6V\n" +
                             "2.7V -> 1.7V\n" +
                             "2.8V -> 1.7V\n" +
                             "2.9V -> 1.8V\n" +
                             "3.0V -> 1.8V\n" +
                             "3.1V -> 1.9V\n" +
                             "3.2V -> 1.9V\n" +
                             "3.3V -> 2.0V\n" +
                             "3.4V -> 2.1V\n" +
                             "3.5V -> 2.1V\n" +
                             "3.6V -> 2.2V\n" +
                             "3.7V -> 2.2V\n" +
                             "3.8V -> 2.3V\n" +
                             "3.9V -> 2.4V\n" +
                             "4.0V -> 2.4V\n" +
                             "4.1V -> 2.5V\n" +
                             "4.2V -> 2.5V\n" +
                             "4.3V -> 2.6V\n" +
                             "4.4V -> 2.7V\n" +
                             "4.5V -> 2.7V\n" +
                             "4.6V -> 2.8V\n" +
                             "4.7V -> 2.8V\n" +
                             "4.8V -> 2.9V\n" +
                             "4.9V -> 3.0V\n" +
                             "5.0V -> 3.0V\n");

    testHighVoltageIn("LVX", "0.0V -> 0.0V\n" +
                             "0.1V -> 0.1V\n" +
                             "0.2V -> 0.2V\n" +
                             "0.3V -> 0.2V\n" +
                             "0.4V -> 0.3V\n" +
                             "0.5V -> 0.4V\n" +
                             "0.6V -> 0.5V\n" +
                             "0.7V -> 0.5V\n" +
                             "0.8V -> 0.6V\n" +
                             "0.9V -> 0.7V\n" +
                             "1.0V -> 0.8V\n" +
                             "1.1V -> 0.8V\n" +
                             "1.2V -> 0.9V\n" +
                             "1.3V -> 1.0V\n" +
                             "1.4V -> 1.1V\n" +
                             "1.5V -> 1.1V\n" +
                             "1.6V -> 1.2V\n" +
                             "1.7V -> 1.3V\n" +
                             "1.8V -> 1.4V\n" +
                             "1.9V -> 1.4V\n" +
                             "2.0V -> 1.5V\n" +
                             "2.1V -> 1.6V\n" +
                             "2.2V -> 1.6V\n" +
                             "2.3V -> 1.6V\n" +
                             "2.4V -> 1.7V\n" +
                             "2.5V -> 1.7V\n" +
                             "2.6V -> 1.8V\n" +
                             "2.7V -> 1.8V\n" +
                             "2.8V -> 1.9V\n" +
                             "2.9V -> 1.9V\n" +
                             "3.0V -> 2.0V\n" +
                             "3.1V -> 2.1V\n" +
                             "3.2V -> 2.1V\n" +
                             "3.3V -> 2.2V\n" +
                             "3.4V -> 2.3V\n" +
                             "3.5V -> 2.3V\n" +
                             "3.6V -> 2.4V\n" +
                             "3.7V -> 2.5V\n" +
                             "3.8V -> 2.5V\n" +
                             "3.9V -> 2.6V\n" +
                             "4.0V -> 2.7V\n" +
                             "4.1V -> 2.7V\n" +
                             "4.2V -> 2.8V\n" +
                             "4.3V -> 2.9V\n" +
                             "4.4V -> 2.9V\n" +
                             "4.5V -> 3.0V\n" +
                             "4.6V -> 3.1V\n" +
                             "4.7V -> 3.1V\n" +
                             "4.8V -> 3.2V\n" +
                             "4.9V -> 3.3V\n" +
                             "5.0V -> 3.3V\n");

    testHighVoltageIn("F", "0.0V -> 0.0V\n" +
                           "0.1V -> 0.0V\n" +
                           "0.2V -> 0.1V\n" +
                           "0.3V -> 0.1V\n" +
                           "0.4V -> 0.2V\n" +
                           "0.5V -> 0.2V\n" +
                           "0.6V -> 0.2V\n" +
                           "0.7V -> 0.3V\n" +
                           "0.8V -> 0.3V\n" +
                           "0.9V -> 0.4V\n" +
                           "1.0V -> 0.4V\n" +
                           "1.1V -> 0.4V\n" +
                           "1.2V -> 0.5V\n" +
                           "1.3V -> 0.5V\n" +
                           "1.4V -> 0.6V\n" +
                           "1.5V -> 0.6V\n" +
                           "1.6V -> 0.6V\n" +
                           "1.7V -> 0.7V\n" +
                           "1.8V -> 0.7V\n" +
                           "1.9V -> 0.8V\n" +
                           "2.0V -> 0.8V\n" +
                           "2.1V -> 0.8V\n" +
                           "2.2V -> 0.9V\n" +
                           "2.3V -> 0.9V\n" +
                           "2.4V -> 1.0V\n" +
                           "2.5V -> 1.0V\n" +
                           "2.6V -> 1.0V\n" +
                           "2.7V -> 1.1V\n" +
                           "2.8V -> 1.1V\n" +
                           "2.9V -> 1.2V\n" +
                           "3.0V -> 1.2V\n" +
                           "3.1V -> 1.2V\n" +
                           "3.2V -> 1.3V\n" +
                           "3.3V -> 1.3V\n" +
                           "3.4V -> 1.4V\n" +
                           "3.5V -> 1.4V\n" +
                           "3.6V -> 1.4V\n" +
                           "3.7V -> 1.5V\n" +
                           "3.8V -> 1.5V\n" +
                           "3.9V -> 1.6V\n" +
                           "4.0V -> 1.6V\n" +
                           "4.1V -> 1.6V\n" +
                           "4.2V -> 1.7V\n" +
                           "4.3V -> 1.7V\n" +
                           "4.4V -> 1.8V\n" +
                           "4.5V -> 1.8V\n" +
                           "4.6V -> 1.8V\n" +
                           "4.7V -> 1.9V\n" +
                           "4.8V -> 1.9V\n" +
                           "4.9V -> 2.0V\n" +
                           "5.0V -> 2.0V\n");
  }

  static void testHighVoltageIn(String family, String expected)
  {
    FamilyVoltageConfiguration voltageConfiguration = FamilyVoltageConfigurationStore.get(family);
    validateNotNull(voltageConfiguration);

    StringBuilder builder = new StringBuilder();
    for (float vcc = 0; vcc <= 5; vcc += 0.1f)
    {
      float voltage = voltageConfiguration.getHighVoltageIn(vcc);
      builder.append((Voltage.getVoltageString(vcc) + " -> " + Voltage.getVoltageString(voltage) + "\n"));
    }
    validate(expected, builder.toString());
  }

  static void testLowVoltageIn()
  {
    FamilyVoltageConfigurationStore.getInstance();
    testLowVoltageIn("LVC", "0.0V -> 0.0V\n" +
                            "0.1V -> 0.0V\n" +
                            "0.2V -> 0.1V\n" +
                            "0.3V -> 0.1V\n" +
                            "0.4V -> 0.1V\n" +
                            "0.5V -> 0.2V\n" +
                            "0.6V -> 0.2V\n" +
                            "0.7V -> 0.2V\n" +
                            "0.8V -> 0.3V\n" +
                            "0.9V -> 0.3V\n" +
                            "1.0V -> 0.3V\n" +
                            "1.1V -> 0.4V\n" +
                            "1.2V -> 0.4V\n" +
                            "1.3V -> 0.4V\n" +
                            "1.4V -> 0.5V\n" +
                            "1.5V -> 0.5V\n" +
                            "1.6V -> 0.5V\n" +
                            "1.7V -> 0.6V\n" +
                            "1.8V -> 0.6V\n" +
                            "1.9V -> 0.6V\n" +
                            "2.0V -> 0.6V\n" +
                            "2.1V -> 0.6V\n" +
                            "2.2V -> 0.6V\n" +
                            "2.3V -> 0.7V\n" +
                            "2.4V -> 0.7V\n" +
                            "2.5V -> 0.7V\n" +
                            "2.6V -> 0.7V\n" +
                            "2.7V -> 0.7V\n" +
                            "2.8V -> 0.7V\n" +
                            "2.9V -> 0.7V\n" +
                            "3.0V -> 0.7V\n" +
                            "3.1V -> 0.8V\n" +
                            "3.2V -> 0.8V\n" +
                            "3.3V -> 0.8V\n" +
                            "3.4V -> 0.8V\n" +
                            "3.5V -> 0.8V\n" +
                            "3.6V -> 0.9V\n" +
                            "3.7V -> 0.9V\n" +
                            "3.8V -> 0.9V\n" +
                            "3.9V -> 0.9V\n" +
                            "4.0V -> 1.0V\n" +
                            "4.1V -> 1.0V\n" +
                            "4.2V -> 1.0V\n" +
                            "4.3V -> 1.0V\n" +
                            "4.4V -> 1.1V\n" +
                            "4.5V -> 1.1V\n" +
                            "4.6V -> 1.1V\n" +
                            "4.7V -> 1.1V\n" +
                            "4.8V -> 1.2V\n" +
                            "4.9V -> 1.2V\n" +
                            "5.0V -> 1.2V\n");

    testLowVoltageIn("F", "0.0V -> 0.0V\n" +
                          "0.1V -> 0.0V\n" +
                          "0.2V -> 0.0V\n" +
                          "0.3V -> 0.0V\n" +
                          "0.4V -> 0.1V\n" +
                          "0.5V -> 0.1V\n" +
                          "0.6V -> 0.1V\n" +
                          "0.7V -> 0.1V\n" +
                          "0.8V -> 0.1V\n" +
                          "0.9V -> 0.1V\n" +
                          "1.0V -> 0.2V\n" +
                          "1.1V -> 0.2V\n" +
                          "1.2V -> 0.2V\n" +
                          "1.3V -> 0.2V\n" +
                          "1.4V -> 0.2V\n" +
                          "1.5V -> 0.2V\n" +
                          "1.6V -> 0.3V\n" +
                          "1.7V -> 0.3V\n" +
                          "1.8V -> 0.3V\n" +
                          "1.9V -> 0.3V\n" +
                          "2.0V -> 0.3V\n" +
                          "2.1V -> 0.3V\n" +
                          "2.2V -> 0.4V\n" +
                          "2.3V -> 0.4V\n" +
                          "2.4V -> 0.4V\n" +
                          "2.5V -> 0.4V\n" +
                          "2.6V -> 0.4V\n" +
                          "2.7V -> 0.4V\n" +
                          "2.8V -> 0.4V\n" +
                          "2.9V -> 0.5V\n" +
                          "3.0V -> 0.5V\n" +
                          "3.1V -> 0.5V\n" +
                          "3.2V -> 0.5V\n" +
                          "3.3V -> 0.5V\n" +
                          "3.4V -> 0.5V\n" +
                          "3.5V -> 0.6V\n" +
                          "3.6V -> 0.6V\n" +
                          "3.7V -> 0.6V\n" +
                          "3.8V -> 0.6V\n" +
                          "3.9V -> 0.6V\n" +
                          "4.0V -> 0.6V\n" +
                          "4.1V -> 0.7V\n" +
                          "4.2V -> 0.7V\n" +
                          "4.3V -> 0.7V\n" +
                          "4.4V -> 0.7V\n" +
                          "4.5V -> 0.7V\n" +
                          "4.6V -> 0.7V\n" +
                          "4.7V -> 0.8V\n" +
                          "4.8V -> 0.8V\n" +
                          "4.9V -> 0.8V\n" +
                          "5.0V -> 0.8V\n");
  }

  static void testLowVoltageIn(String family, String expected)
  {
    FamilyVoltageConfiguration voltageConfiguration = FamilyVoltageConfigurationStore.get(family);
    validateNotNull(voltageConfiguration);

    StringBuilder builder = new StringBuilder();
    for (float vcc = 0; vcc <= 5; vcc += 0.1f)
    {
      float voltage = voltageConfiguration.getLowVoltageIn(vcc);
      builder.append((Voltage.getVoltageString(vcc) + " -> " + Voltage.getVoltageString(voltage) + "\n"));
    }
    validate(expected, builder.toString());
  }

  static void testMidVoltageOut()
  {
    FamilyVoltageConfigurationStore.getInstance();
    testMidVoltageOut("LVC", "0.0V -> 0.0V\n" +
                             "0.1V -> 0.1V\n" +
                             "0.2V -> 0.1V\n" +
                             "0.3V -> 0.2V\n" +
                             "0.4V -> 0.2V\n" +
                             "0.5V -> 0.3V\n" +
                             "0.6V -> 0.3V\n" +
                             "0.7V -> 0.4V\n" +
                             "0.8V -> 0.4V\n" +
                             "0.9V -> 0.5V\n" +
                             "1.0V -> 0.5V\n" +
                             "1.1V -> 0.6V\n" +
                             "1.2V -> 0.6V\n" +
                             "1.3V -> 0.7V\n" +
                             "1.4V -> 0.7V\n" +
                             "1.5V -> 0.8V\n" +
                             "1.6V -> 0.8V\n" +
                             "1.7V -> 0.9V\n" +
                             "1.8V -> 0.9V\n" +
                             "1.9V -> 1.0V\n" +
                             "2.0V -> 1.0V\n" +
                             "2.1V -> 1.1V\n" +
                             "2.2V -> 1.1V\n" +
                             "2.3V -> 1.1V\n" +
                             "2.4V -> 1.2V\n" +
                             "2.5V -> 1.2V\n" +
                             "2.6V -> 1.3V\n" +
                             "2.7V -> 1.3V\n" +
                             "2.8V -> 1.4V\n" +
                             "2.9V -> 1.4V\n" +
                             "3.0V -> 1.5V\n" +
                             "3.1V -> 1.5V\n" +
                             "3.2V -> 1.6V\n" +
                             "3.3V -> 1.6V\n" +
                             "3.4V -> 1.7V\n" +
                             "3.5V -> 1.7V\n" +
                             "3.6V -> 1.8V\n" +
                             "3.7V -> 1.8V\n" +
                             "3.8V -> 1.9V\n" +
                             "3.9V -> 1.9V\n" +
                             "4.0V -> 2.0V\n" +
                             "4.1V -> 2.0V\n" +
                             "4.2V -> 2.1V\n" +
                             "4.3V -> 2.1V\n" +
                             "4.4V -> 2.2V\n" +
                             "4.5V -> 2.2V\n" +
                             "4.6V -> 2.3V\n" +
                             "4.7V -> 2.3V\n" +
                             "4.8V -> 2.4V\n" +
                             "4.9V -> 2.4V\n" +
                             "5.0V -> 2.5V\n");

    testMidVoltageOut("F", "0.0V -> 0.0V\n" +
                           "0.1V -> 0.0V\n" +
                           "0.2V -> 0.1V\n" +
                           "0.3V -> 0.1V\n" +
                           "0.4V -> 0.1V\n" +
                           "0.5V -> 0.2V\n" +
                           "0.6V -> 0.2V\n" +
                           "0.7V -> 0.3V\n" +
                           "0.8V -> 0.3V\n" +
                           "0.9V -> 0.3V\n" +
                           "1.0V -> 0.4V\n" +
                           "1.1V -> 0.4V\n" +
                           "1.2V -> 0.4V\n" +
                           "1.3V -> 0.5V\n" +
                           "1.4V -> 0.5V\n" +
                           "1.5V -> 0.6V\n" +
                           "1.6V -> 0.6V\n" +
                           "1.7V -> 0.6V\n" +
                           "1.8V -> 0.7V\n" +
                           "1.9V -> 0.7V\n" +
                           "2.0V -> 0.7V\n" +
                           "2.1V -> 0.8V\n" +
                           "2.2V -> 0.8V\n" +
                           "2.3V -> 0.9V\n" +
                           "2.4V -> 0.9V\n" +
                           "2.5V -> 0.9V\n" +
                           "2.6V -> 1.0V\n" +
                           "2.7V -> 1.0V\n" +
                           "2.8V -> 1.0V\n" +
                           "2.9V -> 1.1V\n" +
                           "3.0V -> 1.1V\n" +
                           "3.1V -> 1.1V\n" +
                           "3.2V -> 1.2V\n" +
                           "3.3V -> 1.2V\n" +
                           "3.4V -> 1.3V\n" +
                           "3.5V -> 1.3V\n" +
                           "3.6V -> 1.3V\n" +
                           "3.7V -> 1.4V\n" +
                           "3.8V -> 1.4V\n" +
                           "3.9V -> 1.4V\n" +
                           "4.0V -> 1.5V\n" +
                           "4.1V -> 1.5V\n" +
                           "4.2V -> 1.6V\n" +
                           "4.3V -> 1.6V\n" +
                           "4.4V -> 1.6V\n" +
                           "4.5V -> 1.7V\n" +
                           "4.6V -> 1.7V\n" +
                           "4.7V -> 1.7V\n" +
                           "4.8V -> 1.8V\n" +
                           "4.9V -> 1.8V\n" +
                           "5.0V -> 1.8V\n");
  }

  static void testMidVoltageOut(String family, String expected)
  {
    FamilyVoltageConfiguration voltageConfiguration = FamilyVoltageConfigurationStore.get(family);
    validateNotNull(voltageConfiguration);

    StringBuilder builder = new StringBuilder();
    for (float vcc = 0; vcc <= 5; vcc += 0.1f)
    {
      float voltage = voltageConfiguration.getMidVoltageOut(vcc);
      builder.append((Voltage.getVoltageString(vcc) + " -> " + Voltage.getVoltageString(voltage) + "\n"));
    }
    validate(expected, builder.toString());
  }

  static void testHighVoltageOut()
  {
    FamilyVoltageConfigurationStore.getInstance();
    testHighVoltageOut("LVC", "0.0V -> 0.0V\n" +
                              "0.1V -> 0.1V\n" +
                              "0.2V -> 0.2V\n" +
                              "0.3V -> 0.3V\n" +
                              "0.4V -> 0.4V\n" +
                              "0.5V -> 0.4V\n" +
                              "0.6V -> 0.5V\n" +
                              "0.7V -> 0.6V\n" +
                              "0.8V -> 0.7V\n" +
                              "0.9V -> 0.8V\n" +
                              "1.0V -> 0.9V\n" +
                              "1.1V -> 1.0V\n" +
                              "1.2V -> 1.1V\n" +
                              "1.3V -> 1.2V\n" +
                              "1.4V -> 1.2V\n" +
                              "1.5V -> 1.3V\n" +
                              "1.6V -> 1.4V\n" +
                              "1.7V -> 1.5V\n" +
                              "1.8V -> 1.6V\n" +
                              "1.9V -> 1.7V\n" +
                              "2.0V -> 1.8V\n" +
                              "2.1V -> 1.9V\n" +
                              "2.2V -> 2.0V\n" +
                              "2.3V -> 2.1V\n" +
                              "2.4V -> 2.2V\n" +
                              "2.5V -> 2.3V\n" +
                              "2.6V -> 2.4V\n" +
                              "2.7V -> 2.5V\n" +
                              "2.8V -> 2.6V\n" +
                              "2.9V -> 2.7V\n" +
                              "3.0V -> 2.8V\n" +
                              "3.1V -> 2.9V\n" +
                              "3.2V -> 3.0V\n" +
                              "3.3V -> 3.1V\n" +
                              "3.4V -> 3.2V\n" +
                              "3.5V -> 3.3V\n" +
                              "3.6V -> 3.4V\n" +
                              "3.7V -> 3.5V\n" +
                              "3.8V -> 3.6V\n" +
                              "3.9V -> 3.7V\n" +
                              "4.0V -> 3.8V\n" +
                              "4.1V -> 3.9V\n" +
                              "4.2V -> 3.9V\n" +
                              "4.3V -> 4.0V\n" +
                              "4.4V -> 4.1V\n" +
                              "4.5V -> 4.2V\n" +
                              "4.6V -> 4.3V\n" +
                              "4.7V -> 4.4V\n" +
                              "4.8V -> 4.5V\n" +
                              "4.9V -> 4.6V\n" +
                              "5.0V -> 4.7V\n");

    testHighVoltageOut("F", "0.0V -> 0.0V\n" +
                            "0.1V -> 0.1V\n" +
                            "0.2V -> 0.1V\n" +
                            "0.3V -> 0.2V\n" +
                            "0.4V -> 0.3V\n" +
                            "0.5V -> 0.3V\n" +
                            "0.6V -> 0.4V\n" +
                            "0.7V -> 0.5V\n" +
                            "0.8V -> 0.5V\n" +
                            "0.9V -> 0.6V\n" +
                            "1.0V -> 0.7V\n" +
                            "1.1V -> 0.7V\n" +
                            "1.2V -> 0.8V\n" +
                            "1.3V -> 0.9V\n" +
                            "1.4V -> 0.9V\n" +
                            "1.5V -> 1.0V\n" +
                            "1.6V -> 1.1V\n" +
                            "1.7V -> 1.1V\n" +
                            "1.8V -> 1.2V\n" +
                            "1.9V -> 1.3V\n" +
                            "2.0V -> 1.3V\n" +
                            "2.1V -> 1.4V\n" +
                            "2.2V -> 1.5V\n" +
                            "2.3V -> 1.5V\n" +
                            "2.4V -> 1.6V\n" +
                            "2.5V -> 1.6V\n" +
                            "2.6V -> 1.7V\n" +
                            "2.7V -> 1.8V\n" +
                            "2.8V -> 1.8V\n" +
                            "2.9V -> 1.9V\n" +
                            "3.0V -> 2.0V\n" +
                            "3.1V -> 2.0V\n" +
                            "3.2V -> 2.1V\n" +
                            "3.3V -> 2.2V\n" +
                            "3.4V -> 2.2V\n" +
                            "3.5V -> 2.3V\n" +
                            "3.6V -> 2.4V\n" +
                            "3.7V -> 2.4V\n" +
                            "3.8V -> 2.5V\n" +
                            "3.9V -> 2.6V\n" +
                            "4.0V -> 2.6V\n" +
                            "4.1V -> 2.7V\n" +
                            "4.2V -> 2.8V\n" +
                            "4.3V -> 2.8V\n" +
                            "4.4V -> 2.9V\n" +
                            "4.5V -> 3.0V\n" +
                            "4.6V -> 3.0V\n" +
                            "4.7V -> 3.1V\n" +
                            "4.8V -> 3.2V\n" +
                            "4.9V -> 3.2V\n" +
                            "5.0V -> 3.3V\n");
  }

  static void testHighVoltageOut(String family, String expected)
  {
    FamilyVoltageConfiguration voltageConfiguration = FamilyVoltageConfigurationStore.get(family);
    validateNotNull(voltageConfiguration);

    StringBuilder builder = new StringBuilder();
    for (float vcc = 0; vcc <= 5; vcc += 0.1f)
    {
      float voltage = voltageConfiguration.getVoltageOut(true, vcc);
      builder.append((Voltage.getVoltageString(vcc) + " -> " + Voltage.getVoltageString(voltage) + "\n"));
    }
    validate(expected, builder.toString());
  }

  static void testLowVoltageOut()
  {
    FamilyVoltageConfigurationStore.getInstance();
    testLowVoltageOut("LVC", "0.0V -> 0.0V\n" +
                             "0.1V -> 0.0V\n" +
                             "0.2V -> 0.0V\n" +
                             "0.3V -> 0.0V\n" +
                             "0.4V -> 0.0V\n" +
                             "0.5V -> 0.1V\n" +
                             "0.6V -> 0.1V\n" +
                             "0.7V -> 0.1V\n" +
                             "0.8V -> 0.1V\n" +
                             "0.9V -> 0.1V\n" +
                             "1.0V -> 0.1V\n" +
                             "1.1V -> 0.1V\n" +
                             "1.2V -> 0.1V\n" +
                             "1.3V -> 0.1V\n" +
                             "1.4V -> 0.2V\n" +
                             "1.5V -> 0.2V\n" +
                             "1.6V -> 0.2V\n" +
                             "1.7V -> 0.2V\n" +
                             "1.8V -> 0.2V\n" +
                             "1.9V -> 0.2V\n" +
                             "2.0V -> 0.2V\n" +
                             "2.1V -> 0.2V\n" +
                             "2.2V -> 0.2V\n" +
                             "2.3V -> 0.2V\n" +
                             "2.4V -> 0.2V\n" +
                             "2.5V -> 0.2V\n" +
                             "2.6V -> 0.2V\n" +
                             "2.7V -> 0.2V\n" +
                             "2.8V -> 0.2V\n" +
                             "2.9V -> 0.2V\n" +
                             "3.0V -> 0.2V\n" +
                             "3.1V -> 0.2V\n" +
                             "3.2V -> 0.2V\n" +
                             "3.3V -> 0.2V\n" +
                             "3.4V -> 0.2V\n" +
                             "3.5V -> 0.2V\n" +
                             "3.6V -> 0.2V\n" +
                             "3.7V -> 0.2V\n" +
                             "3.8V -> 0.2V\n" +
                             "3.9V -> 0.2V\n" +
                             "4.0V -> 0.2V\n" +
                             "4.1V -> 0.2V\n" +
                             "4.2V -> 0.3V\n" +
                             "4.3V -> 0.3V\n" +
                             "4.4V -> 0.3V\n" +
                             "4.5V -> 0.3V\n" +
                             "4.6V -> 0.3V\n" +
                             "4.7V -> 0.3V\n" +
                             "4.8V -> 0.3V\n" +
                             "4.9V -> 0.3V\n" +
                             "5.0V -> 0.3V\n");

    testLowVoltageOut("F", "0.0V -> 0.0V\n" +
                           "0.1V -> 0.0V\n" +
                           "0.2V -> 0.0V\n" +
                           "0.3V -> 0.0V\n" +
                           "0.4V -> 0.0V\n" +
                           "0.5V -> 0.0V\n" +
                           "0.6V -> 0.0V\n" +
                           "0.7V -> 0.1V\n" +
                           "0.8V -> 0.1V\n" +
                           "0.9V -> 0.1V\n" +
                           "1.0V -> 0.1V\n" +
                           "1.1V -> 0.1V\n" +
                           "1.2V -> 0.1V\n" +
                           "1.3V -> 0.1V\n" +
                           "1.4V -> 0.1V\n" +
                           "1.5V -> 0.1V\n" +
                           "1.6V -> 0.1V\n" +
                           "1.7V -> 0.1V\n" +
                           "1.8V -> 0.1V\n" +
                           "1.9V -> 0.2V\n" +
                           "2.0V -> 0.2V\n" +
                           "2.1V -> 0.2V\n" +
                           "2.2V -> 0.2V\n" +
                           "2.3V -> 0.2V\n" +
                           "2.4V -> 0.2V\n" +
                           "2.5V -> 0.2V\n" +
                           "2.6V -> 0.2V\n" +
                           "2.7V -> 0.2V\n" +
                           "2.8V -> 0.2V\n" +
                           "2.9V -> 0.2V\n" +
                           "3.0V -> 0.2V\n" +
                           "3.1V -> 0.2V\n" +
                           "3.2V -> 0.3V\n" +
                           "3.3V -> 0.3V\n" +
                           "3.4V -> 0.3V\n" +
                           "3.5V -> 0.3V\n" +
                           "3.6V -> 0.3V\n" +
                           "3.7V -> 0.3V\n" +
                           "3.8V -> 0.3V\n" +
                           "3.9V -> 0.3V\n" +
                           "4.0V -> 0.3V\n" +
                           "4.1V -> 0.3V\n" +
                           "4.2V -> 0.3V\n" +
                           "4.3V -> 0.3V\n" +
                           "4.4V -> 0.4V\n" +
                           "4.5V -> 0.4V\n" +
                           "4.6V -> 0.4V\n" +
                           "4.7V -> 0.4V\n" +
                           "4.8V -> 0.4V\n" +
                           "4.9V -> 0.4V\n" +
                           "5.0V -> 0.4V\n");
  }

  static void testLowVoltageOut(String family, String expected)
  {
    FamilyVoltageConfiguration voltageConfiguration = FamilyVoltageConfigurationStore.get(family);
    validateNotNull(voltageConfiguration);

    StringBuilder builder = new StringBuilder();
    for (float vcc = 0; vcc <= 5; vcc += 0.1f)
    {
      float voltage = voltageConfiguration.getVoltageOut(false, vcc);
      builder.append((Voltage.getVoltageString(vcc) + " -> " + Voltage.getVoltageString(voltage) + "\n"));
    }
    validate(expected, builder.toString());
  }

  public static void test()
  {
    testHighVoltageIn();
    testLowVoltageIn();
    testMidVoltageOut();
    testHighVoltageOut();
    testLowVoltageOut();
  }
}

