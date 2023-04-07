package net.logicim.ui.common;

import net.logicim.domain.common.port.OutputPortHelper;
import net.logicim.domain.common.port.Port;
import net.logicim.domain.common.voltage.VoltageRepresentation;
import net.logicim.domain.common.wire.Trace;

import java.awt.*;
import java.util.List;

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

  public static Color getColourForTrace(VoltageRepresentation colours, Trace trace, long time)
  {
    if ((trace == null) || (time == -1))
    {
      return colours.getDisconnectedTrace();
    }

    float voltage = trace.getVoltage(time);
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

  public static Color getColourForTraces(VoltageRepresentation colours, List<Trace> traces, long time)
  {
    if ((traces == null) || (traces.isEmpty()) || (time == -1))
    {
      return colours.getDisconnectedTrace();
    }

    float averageVoltage = 0;
    int undrivenCount = 0;
    float maximumShortVoltage = 0;
    for (Trace trace : traces)
    {
      float voltage = trace.getVoltage(time);
      if (!Float.isNaN(voltage))
      {
        float shortVoltage = trace.getShortVoltage(time);
        averageVoltage += voltage;
        if (shortVoltage != 0)
        {
          if (shortVoltage > maximumShortVoltage)
          {
            maximumShortVoltage = shortVoltage;
          }
        }
      }
      else
      {
        undrivenCount++;
      }
    }

    if (undrivenCount == traces.size())
    {
      return colours.getTraceUndriven();
    }
    else
    {
      averageVoltage /= (traces.size() - undrivenCount);
      Color colorForVoltage = getColourForVoltage(colours, averageVoltage);
      if (maximumShortVoltage == 0)
      {
        return colorForVoltage;
      }
      else
      {
        Color colorForShort = getColorForShort(colours, maximumShortVoltage);
        return new Color(clamp(colorForVoltage.getRed() + colorForShort.getRed()),
                         clamp(colorForVoltage.getGreen() + colorForShort.getGreen()),
                         clamp(colorForVoltage.getBlue() + colorForShort.getBlue()));
      }
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

  public static Color getColorForPorts(VoltageRepresentation colours, List<? extends Port> ports, long time)
  {
    if (time == -1)
    {
      return colours.getDisconnectedTrace();
    }

    float averageVoltageOut = 0;
    int drivenPorts = 0;
    for (Port port : ports)
    {
      float voltageOut = OutputPortHelper.getPortOutputVoltage(port, time);

      if (!Float.isNaN(voltageOut))
      {
        averageVoltageOut += voltageOut;
        drivenPorts++;
      }
    }

    if (drivenPorts == 0)
    {
      return colours.getTraceUndriven();
    }
    else
    {
      averageVoltageOut /= drivenPorts;
      return getColourForVoltage(colours, averageVoltageOut);
    }
  }
}

