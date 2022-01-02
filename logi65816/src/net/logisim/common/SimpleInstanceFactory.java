package net.logisim.common;

import com.cburch.logisim.data.Bounds;
import com.cburch.logisim.instance.InstanceFactory;
import com.cburch.logisim.instance.Port;
import com.cburch.logisim.util.StringGetter;

import java.util.ArrayList;
import java.util.List;

public abstract class SimpleInstanceFactory
    extends InstanceFactory
{
  protected final ComponentDescription description;

  public SimpleInstanceFactory(ComponentDescription description)
  {
    super(description.getType() + " (" + description.getName() + ")");
    this.description = description;

    setOffsetBounds(Bounds.create(description.getLeft(),
                                  description.getTopY(),
                                  description.getWidth(),
                                  description.getHeight()));
    setPorts(createPorts());
  }

  protected List<Port> createPorts()
  {
    ArrayList<Port> ports = new ArrayList<>();

    for (PortDescription portDescription : description.getPorts())
    {
      if (portDescription.notBlank())
      {
        ports.add(createPort(portDescription));
      }
    }
    return ports;
  }

  private Port createPort(PortDescription portDescription)
  {
    Port port = new Port(description.getPortX(portDescription, true),
                         description.getPortY(portDescription),
                         portDescription.getType(),
                         portDescription.getBitWidth(),
                         portDescription.getExclusive());
    port.setToolTip(new StringGetter()
    {
      @Override
      public String toString()
      {
        return portDescription.getTooltip();
      }
    });
    return port;
  }
}

