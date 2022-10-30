package net.logicim.ui.common;

import net.logicim.ui.colour.ColourGradient;

import java.awt.*;

public class Colours
{
  protected Color background;

  protected Color smallGridDotColor;
  protected Color largeGridDotColor;

  protected Color traceUndriven;
  protected Color traceUnsettled;
  protected Color traceError;

  Color[] voltageColours;

  protected Color shapeBorder;
  protected Color shapeFill;

  protected Color portHover;
  protected Color viewHover;

  public Colours()
  {
    background = Color.WHITE;
    smallGridDotColor = new Color(0xCBCBCB);
    largeGridDotColor = new Color(0xABABAB);
    traceUnsettled = new Color(70, 160, 170);
    traceUndriven = new Color(70, 70, 70);
    traceError = new Color(255, 0, 0);

    voltageColours = new Color[71];
    ColourGradient.generate(voltageColours,
                            new Color(0, 50, 0), 0,
                            new Color(0, 180, 0), 33,
                            new Color(100, 255, 100), 50,
                            new Color(255, 253, 75, 255), 70);

     shapeBorder = Color.BLACK;
     shapeFill = new Color(232, 232, 232);

    portHover = new Color(0, 255, 0);
    viewHover = new Color(0, 255, 0);
  }

  public Color getSmallGridDotColor()
  {
    return smallGridDotColor;
  }

  public Color getLargeGridDotColor()
  {
    return largeGridDotColor;
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

  public Color getBackground()
  {
    return background;
  }

  public Color getShapeBorder()
  {
    return shapeBorder;
  }

  public Color getShapeFill()
  {
    return shapeFill;
  }

  public Color getTraceVoltage(float voltage)
  {
    int index = (int) (voltage * 10);
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

  public Color getPortHover()
  {
    return portHover;
  }

  public Color getViewHover()
  {
    return viewHover;
  }
}

