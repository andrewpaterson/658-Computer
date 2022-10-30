package net.logicim.ui.common;

import net.logicim.domain.common.trace.TraceNet;

import java.awt.*;

public abstract class VoltageColour
{
  public static Color getColorForVoltage(Colours colours, float voltage)
  {
    Color color;
    if (voltage == TraceNet.Unsettled)
    {
      color = colours.getTraceUnsettled();
    }
    else if (voltage == TraceNet.Undriven)
    {
      color = colours.getTraceUndriven();
    }
    else if ((voltage < 0.0f) || (voltage > 7.0f))
    {
      color = colours.getTraceError();
    }
    else
    {
      color = colours.getTraceVoltage(voltage);
    }
    return color;
  }
}

