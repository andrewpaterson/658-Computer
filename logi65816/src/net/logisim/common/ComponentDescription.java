package net.logisim.common;

public class ComponentDescription
{
  protected final int pixelsPerPin;

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

  protected final PortDescription[] portDescriptions;

  public ComponentDescription(int width,
                              int height,
                              int verticalMargin,
                              PortDescription... portDescriptions)
  {
    this.portDescriptions = portDescriptions;
    this.pinsPerSide = portDescriptions.length / 2 + portDescriptions.length % 2;

    this.verticalMargin = verticalMargin;
    this.leftX = -(width / 2);
    this.rightX = width / 2;
    this.topY = -(height / 2) - verticalMargin;
    this.botY = (height / 2) + verticalMargin;

    this.pixelsPerPin = 20;

    this.pinTopY = ((pinsPerSide - 1) * pixelsPerPin / -2) - verticalMargin;
    this.pinBotY = ((pinsPerSide - 1) * pixelsPerPin / 2) + verticalMargin;

    this.pinStartY = pinTopY + verticalMargin;
    this.pinStopY = pinBotY - verticalMargin;
  }

  public PortDescription[] getPortDescriptions()
  {
    return portDescriptions;
  }

  public int getVerticalMargin()
  {
    return verticalMargin;
  }

  public int getPinsPerSide()
  {
    return pinsPerSide;
  }

  public int getPinTopY()
  {
    return pinTopY;
  }

  public int getPinBotY()
  {
    return pinBotY;
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

  public int getBotY()
  {
    return botY;
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
    return pixelsPerPin;
  }
}

