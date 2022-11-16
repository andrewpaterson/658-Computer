package net.logicim.ui.common;

import net.logicim.domain.common.port.Port;
import net.logicim.domain.common.trace.TraceNet;

import java.awt.*;

public abstract class VoltageColour
{
  public static Color getColorForVoltage(Colours colours, float voltage)
  {
    if ((voltage < 0.0f) || (voltage > 7.0f))
    {
      return colours.getTraceError();
    }
    else
    {
      return colours.getTraceVoltage(voltage);
    }
  }

  public static Color getColorForTrace(Colours colours, TraceNet trace, long time)
  {
    if ((trace == null) || (time == -1))
    {
      return colours.getDisconnectedTrace();
    }
    else
    {
      float voltage = trace.getVoltage(time);
      if (!Float.isNaN(voltage))
      {
        return VoltageColour.getColorForVoltage(colours, voltage);
      }

      else
      {
        return colours.getTraceUndriven();
      }
    }
  }

  public static Color getColorForPort(Colours colours, Port port, long time)
  {
    TraceNet trace = port.getTrace();
    if (time == -1)
    {
      return colours.getDisconnectedTrace();
    }
    else if (trace == null)
    {
      float voltage = port.getVoltage(time);
      if (!Float.isNaN(voltage))
      {
        return VoltageColour.getColorForVoltage(colours, voltage);
      }
      else
      {
        return colours.getTraceUndriven();
      }
    }
    else
    {
      return getColorForTrace(colours, trace, time);
    }
  }
}

