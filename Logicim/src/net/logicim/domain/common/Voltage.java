package net.logicim.domain.common;

public class Voltage
{
  public static String getVoltageString(float voltage)
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

