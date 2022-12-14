package net.logicim.ui.common;

import net.logicim.domain.common.port.BasePort;
import net.logicim.domain.common.voltage.VoltageRepresentation;
import net.logicim.domain.common.port.Port;
import net.logicim.domain.common.trace.TraceNet;

import java.awt.*;

public abstract class VoltageColour
{
  public static Color getColourForVoltage(VoltageRepresentation colours, float voltage)
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

  public static Color getColourForTrace(VoltageRepresentation colours, TraceNet trace, long time)
  {
    if ((trace == null) || (time == -1))
    {
      return colours.getDisconnectedTrace();
    }
    else
    {
      float voltage = trace.getVoltage(time);
      return getColourForTrace(colours, trace, time, voltage);
    }
  }

  public static Color getColourForTrace(VoltageRepresentation colours, TraceNet trace, long time, float voltage)
  {
    if (!Float.isNaN(voltage))
    {
      float shortVoltage = trace.getShortVoltage(time);
      Color colorForVoltage = getColourForVoltage(colours, voltage);
      if (shortVoltage == 0)
      {
        return colorForVoltage;
      }
      else
      {
        Color colorForShort = getColorForShort(colours, shortVoltage);
        return new Color(clamp(colorForVoltage.getRed() + colorForShort.getRed()),
                         clamp(colorForVoltage.getGreen() + colorForShort.getGreen()),
                         clamp(colorForVoltage.getBlue() + colorForShort.getBlue()));
      }
    }
    else
    {
      return colours.getTraceUndriven();
    }
  }

  public static int clamp(int colourComponent)
  {
    if (colourComponent < 0)
    {
      return 0;
    }
    if (colourComponent > 255)
    {
      return 255;
    }
    else
    {
      return colourComponent;
    }
  }

  private static Color getColorForShort(VoltageRepresentation colours, float shortVoltage)
  {
    if (shortVoltage > 7 || (shortVoltage <= 0))
    {
      return colours.getTraceError();
    }
    return colours.getTraceShort(shortVoltage);
  }

  public static Color getColorForPort(VoltageRepresentation colours, BasePort port, long time)
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
        return VoltageColour.getColourForVoltage(colours, voltage);
      }
      else
      {
        return colours.getTraceUndriven();
      }
    }
    else
    {
      return getColourForTrace(colours, trace, time);
    }
  }
}

