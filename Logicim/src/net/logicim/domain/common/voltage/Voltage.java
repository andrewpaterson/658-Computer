package net.logicim.domain.common.voltage;

public interface Voltage
{
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

  static String toVoltageString(float voltage, boolean includeV)
  {
    if (includeV)
    {
      return toVoltageString(voltage);
    }
    else
    {
      if (!Float.isNaN(voltage))
      {
        return String.format("%.1f", voltage).replace(',', '.');
      }
      else
      {
        return "---";
      }
    }
  }
}

