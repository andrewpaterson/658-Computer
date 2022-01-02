package net.logisim.common;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

import static net.logisim.common.PortPosition.LEFT;
import static net.logisim.common.PortPosition.RIGHT;

public class ComponentDescription
{
  public static final int PIXELS_PER_GRID = 10;
  public static final int PIXELS_PER_PIN = PIXELS_PER_GRID * 2;

  protected final int verticalMargin;
  protected final int pinsPerSide;
  protected final int pinTopY;
  protected final int pinBotY;

  protected final int leftX;
  protected final int rightX;
  protected final int topY;
  protected final int botY;
  protected final int pinStartY;

  protected final String name;
  protected final String type;
  protected final List<PortDescription> ports;
  protected final List<String> commonPortNames;

  public ComponentDescription(String name,
                              String type,
                              int width,
                              List<PortDescription> ports,
                              List<String> commonPortNames)
  {
    this(name,
         type,
         width,
         height(Math.max(getPorts(ports, LEFT).size(),
                         getPorts(ports, RIGHT).size())),
         ports,
         commonPortNames);
  }

  public ComponentDescription(String name,
                              String type,
                              int width,
                              int height,
                              List<PortDescription> ports,
                              List<String> commonPortNames)
  {
    this.ports = ports;
    this.commonPortNames = commonPortNames;
    List<PortDescription> leftPorts = getPorts(LEFT);
    List<PortDescription> rightPorts = getPorts(RIGHT);
    this.pinsPerSide = Math.max(leftPorts.size(), rightPorts.size());

    this.name = name;
    this.type = type;

    this.verticalMargin = 10;
    this.leftX = -(width / 2);
    this.rightX = width / 2;
    this.topY = -(height / 2) - verticalMargin;
    this.botY = (height / 2) + verticalMargin;

    this.pinTopY = ((pinsPerSide - 1) * PIXELS_PER_PIN / -2) - verticalMargin;
    this.pinBotY = ((pinsPerSide - 1) * PIXELS_PER_PIN / 2) + verticalMargin;

    this.pinStartY = pinTopY + verticalMargin;

    setPortOffsets(leftPorts);
    setPortOffsets(rightPorts);
  }

  private void setPortOffsets(List<PortDescription> ports)
  {
    int offset = (pinsPerSide - ports.size()) / 2;
    int index = 0;
    for (PortDescription port : ports)
    {
      port.setOffset(offset + index);
      index++;
    }
  }

  protected List<PortDescription> getPorts(PortPosition position)
  {
    return getPorts(this.ports, position);
  }

  private static List<PortDescription> getPorts(List<PortDescription> ports, PortPosition position)
  {
    List<PortDescription> list = new ArrayList<>();
    for (PortDescription port : ports)
    {
      if (port.isPosition(position))
      {
        list.add(port);
      }
    }
    return list;
  }

  public static int height(int pinsPerSide)
  {
    return PIXELS_PER_PIN * pinsPerSide;
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

  public int getPinStartY()
  {
    return pinStartY;
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

  public int getBottomY()
  {
    return botY;
  }

  public int pixelsPerPin()
  {
    return PIXELS_PER_PIN;
  }

  public List<PortDescription> getPorts()
  {
    return ports;
  }

  public List<String> getCommonPortNames()
  {
    return commonPortNames;
  }

  public String getName()
  {
    return name;
  }

  public int getPortX(PortDescription portDescription, boolean allowInverting)
  {
    if (portDescription.isPosition(LEFT))
    {
      int x = getLeft();
      if (portDescription.isInverting() && allowInverting)
      {
        x -= PIXELS_PER_GRID;
      }
      return x;
    }
    else if (portDescription.isPosition(RIGHT))
    {
      int x = getRight();
      if (portDescription.isInverting() && allowInverting)
      {
        x += PIXELS_PER_GRID;
      }
      return x;
    }
    else
    {
      return 0;
    }
  }

  public int getPortY(PortDescription portDescription)
  {
    return getPinStartY() + portDescription.getOffset() * pixelsPerPin();
  }

  public String getType()
  {
    return type;
  }
}

