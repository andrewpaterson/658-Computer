package net.logicim.ui.common;

import net.logicim.ui.colour.ColourGradient;

import java.awt.*;

public class Colours
{
  protected Color smallGridDotColor;
  protected Color largeGridDotColor;
  protected Color chipBorderColor;

  protected Color traceUndriven;
  protected Color traceUnsettled;
  protected Color traceError;

  Color[] voltageColours;

  public Colours()
  {
    smallGridDotColor = new Color(0xCBCBCB);
    largeGridDotColor = new Color(0xABABAB);
    chipBorderColor = Color.BLACK;
    traceUnsettled = new Color(70, 160, 170);
    traceUndriven = new Color(70, 70, 70);
    traceError = new Color(255, 0, 0);

    voltageColours = new Color[71];
    ColourGradient.generate(voltageColours,
                            new Color(0, 50, 0), 0,
                            new Color(0, 180, 0), 33,
                            new Color(100, 255, 100), 50,
                            new Color(255, 253, 75, 255), 70);
  }

  public Color getSmallGridDotColor()
  {
    return smallGridDotColor;
  }

  public Color getLargeGridDotColor()
  {
    return largeGridDotColor;
  }

  public Color getChipBorderColor()
  {
    return chipBorderColor;
  }

  public Color getTraceUndriven()
  {
    return traceUndriven;
  }

  public Color getTraceUnsettled()
  {
    return traceUnsettled;
  }

  public Color getTraceError()
  {
    return traceError;
  }

  public Color getTraceVoltage(float voltage)
  {
    int index = (int) (voltage * 100);
    if (index >= voltageColours.length)
    {
      index = voltageColours.length - 1;
    }
    if (index < 0)
    {
      index = 0;
    }
    return voltageColours[index];
  }
}

