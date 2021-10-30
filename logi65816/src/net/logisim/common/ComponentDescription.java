package net.logisim.common;

import java.util.Arrays;
import java.util.List;

import static net.logisim.common.LogisimFactory.TOP_OFFSET;

public class ComponentDescription
{
  public static final int PIXELS_PER_PIN = 20;

  protected final int verticalMargin;
  protected final int pinsPerSide;
  protected final int pinTopY;
  protected final int pinBotY;

  protected final int leftX;
  protected final int rightX;
  protected final int topY;
  protected final int botY;
  protected final int pinStartY;
  protected final int pinStopY;

  protected final List<PortDescription> ports;

  public ComponentDescription(int width,
                              int height,

                              PortDescription... ports)
  {
    this.ports = Arrays.asList(ports);
    this.pinsPerSide = ports.length / 2 + ports.length % 2;

    this.verticalMargin = 10;
    this.leftX = -(width / 2);
    this.rightX = width / 2;
    this.topY = -(height / 2) - verticalMargin;
    this.botY = (height / 2) + verticalMargin;

    this.pinTopY = ((pinsPerSide - 1) * PIXELS_PER_PIN / -2) - verticalMargin;
    this.pinBotY = ((pinsPerSide - 1) * PIXELS_PER_PIN / 2) + verticalMargin;

    this.pinStartY = pinTopY + verticalMargin;
    this.pinStopY = pinBotY - verticalMargin;
  }

  public static int height(int pinsPerSide)
  {
    return PIXELS_PER_PIN * pinsPerSide;
  }

  public int getPinsPerSide()
  {
    return pinsPerSide;
  }

  public int getLeft()
  {
    return leftX;
  }

  public int getRight()
  {
    return rightX;
  }

  public int getTopY()
  {
    return topY;
  }

  public int getPinStart()
  {
    return pinStartY;
  }

  public int getPinStop()
  {
    return pinStopY;
  }

  public int getWidth()
  {
    return rightX - leftX;
  }

  public int getHeight()
  {
    return botY - topY;
  }

  public int getTopYPlusMargin()
  {
    return topY + verticalMargin;
  }

  public int getBottomYMinusMargin()
  {
    return botY - verticalMargin - 5;
  }

  public int pixelsPerPin()
  {
    return PIXELS_PER_PIN;
  }

  public List<PortDescription> getPorts()
  {
    return ports;
  }
}

