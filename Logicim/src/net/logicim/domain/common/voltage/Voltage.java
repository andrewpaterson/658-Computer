package net.logicim.domain.common.voltage;

public interface Voltage
{
  float getVoltage(long time);

  String getDescription();

  static String toVoltageString(float voltage)
  {
    if (!Float.isNaN(voltage))
    {
      return String.format("%.1fV", voltage).replace(',', '.');
    }
    else
    {
      return "----";
    }
  }
}

