package net.logisim.common;

import java.util.ArrayList;
import java.util.List;

import static com.cburch.logisim.instance.Port.*;

public class PortFactory
{
  protected int index;
  protected List<PortDescription> ports;
  protected List<String> commonPortNames;

  public PortFactory()
  {
    index = 0;
    ports = new ArrayList<>();
    commonPortNames = new ArrayList<>();
  }

  public PortDescription outputShared(String name, PortPosition position)
  {
    return createPortDescription(name, 1, position, OUTPUT, SHARED, true);
  }

  public PortDescription outputShared(String name, int pins, PortPosition position)
  {
    return createPortDescription(name, pins, position, OUTPUT, SHARED, true);
  }

  public PortDescription outputExclusive(String name, PortPosition position)
  {
    return createPortDescription(name, 1, position, OUTPUT, EXCLUSIVE, true);
  }

  public PortDescription outputExclusive(String name, int pins, PortPosition position)
  {
    return createPortDescription(name, pins, position, OUTPUT, EXCLUSIVE, true);
  }

  public PortDescription inoutShared(String name, int pins, PortPosition position)
  {
    return createPortDescription(name, pins, position, INOUT, SHARED, true);
  }

  public PortDescription inputShared(String name, PortPosition position)
  {
    return createPortDescription(name, 1, position, INPUT, SHARED, true);
  }

  public PortDescription inputShared(String name, int pins, PortPosition position)
  {
    return createPortDescription(name, pins, position, INPUT, SHARED, true);
  }

  public PortDescription inoutShared(String name, PortPosition position)
  {
    return createPortDescription(name, 1, position, INOUT, SHARED, true);
  }

  public void blank(PortPosition position)
  {
    createPortDescription(null, 0, position, null, null, false);
  }

  private PortDescription createPortDescription(String name,
                                                int pins,
                                                PortPosition position,
                                                String output,
                                                String shared,
                                                boolean notBlank)
  {
    PortDescription port = new PortDescription(index,
                                               name,
                                               output,
                                               shared,
                                               pins,
                                               notBlank,
                                               position);
    ports.add(port);
    if (notBlank)
    {
      index++;
    }
    return port;
  }

  public List<PortDescription> getPorts()
  {
    return ports;
  }

  public List<String> getCommonPortNames()
  {
    return commonPortNames;
  }

  public PairedLogiBus pairedInputShared(String left, String common, String right, int pins, int propagationDelay)
  {
    commonPortNames.add(common);
    return new PairedLogiBus(inputShared(left, pins, PortPosition.LEFT).setTooltip(common + " " + left).createBus(propagationDelay),
                             inputShared(right, pins, PortPosition.RIGHT).setTooltip(common + " " + right).createBus(propagationDelay));
  }
}

