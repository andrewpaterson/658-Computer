package net.logicim.domain.common.port;

public class OutputPortHelper
{
  public static float getPortOutputVoltage(Port port, long time)
  {
    if (port.isLogicPort())
    {
      return ((LogicPort) port).getVoltageOut(time);
    }
    else if (port.isPowerOut())
    {
      return ((PowerOutPort) port).getVoltageOut(time);
    }
    else
    {
      return Float.NaN;
    }
  }
}
