package net.logicim.ui.common;

import net.logicim.domain.common.voltage.VoltageRepresentation;
import net.logicim.ui.colour.ColourGradient;

import java.awt.*;

public class Colours
    implements VoltageRepresentation
{
  protected Color background;

  protected Color smallGridDotColor;
  protected Color largeGridDotColor;

  protected Color traceUndriven;
  protected Color traceError;

  protected Color[] voltageColours;
  protected Color[] shortColours;

  protected Color shapeBorder;
  protected Color shapeFill;
  protected Color darkShapeFill;

  protected Color portHover;
  protected Color viewHover;
  protected Color selected;

  protected Color disconnectedTrace;
  protected Color differingBusTrace;

  protected Color text;
  protected Color commentText;

  protected Color infoBackground;
  protected Color infoBorder;

  protected static Colours instance;

  public Colours()
  {
    background = Color.WHITE;
    smallGridDotColor = new Color(0xCBCBCB);
    largeGridDotColor = new Color(0xABABAB);
    traceUndriven = new Color(110, 90, 90);
    traceError = new Color(255, 0, 100);

    voltageColours = new Color[71];
    ColourGradient.generate(voltageColours,
                            new Color(0, 50, 0), 0,
                            new Color(0, 180, 0), 33,
                            new Color(100, 255, 100), 50,
                            new Color(255, 253, 75), 70);
    shortColours = new Color[71];
    ColourGradient.generate(shortColours,
                            new Color(0, 0, 0), 0,
                            new Color(200, 0, 0), 33,
                            new Color(255, 255, 100), 50,
                            new Color(255, 255, 200), 70);

    shapeBorder = Color.BLACK;
    shapeFill = new Color(232, 232, 232);
    darkShapeFill = new Color(170, 170, 170);

    portHover = new Color(0, 255, 0);
    viewHover = new Color(0, 255, 0);
    selected = new Color(255, 255, 255);

    disconnectedTrace = new Color(0, 170, 250);
    differingBusTrace = new Color(20, 230, 180);

    text = new Color(0, 0, 0);
    commentText = new Color(64, 64, 64);

    infoBackground = new Color(220, 220, 220);
    infoBorder = new Color(128, 128, 128);
  }

  public static Colours getInstance()
  {
    if (instance == null)
    {
      instance = new Colours();
    }
    return instance;
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

  public Color getDarkShapeFill()
  {
    return darkShapeFill;
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

  public Color getDisconnectedTrace()
  {
    return disconnectedTrace;
  }

  public Color getDifferingBusTrace()
  {
    return differingBusTrace;
  }

  public Color getTraceShort(float shortVoltage)
  {
    int index = (int) (shortVoltage * 10);
    if (index >= shortColours.length)
    {
      index = shortColours.length - 1;
    }
    if (index < 0)
    {
      index = 0;
    }
    return shortColours[index];
  }

  public Color getSelected()
  {
    return selected;
  }

  public Color getText()
  {
    return text;
  }

  public Color getCommentText()
  {
    return commentText;
  }

  public Color getInfoBackground()
  {
    return infoBackground;
  }

  public Color getInfoBorder()
  {
    return infoBorder;
  }
}

