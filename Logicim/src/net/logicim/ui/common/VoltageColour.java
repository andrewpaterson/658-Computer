package net.logicim.ui.common;

import net.logicim.domain.common.port.Uniport;
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

  public static Color getColorForTrace(Colours colours, TraceNet trace)
  {
    if (trace == null)
    {
      return colours.getDisconnectedTrace();
    }
    else if (trace.isDriven())
    {
      float voltage = trace.getDrivenVoltage();
      return VoltageColour.getColorForVoltage(colours, voltage);
    }
    else
    {
      return colours.getTraceUndriven();
    }
  }

  public static Color getColorForUniport(Colours colours, Uniport uniport)
  {
    TraceNet trace = uniport.getTrace();
    if (trace == null)
    {
      if (uniport.isDriven())
      {
        float voltage = uniport.getDrivenVoltage();
        return VoltageColour.getColorForVoltage(colours, voltage);
      }
      else
      {
        return colours.getTraceUndriven();
      }
    }
    else
    {
      return getColorForTrace(colours, trace);
    }
  }
}

