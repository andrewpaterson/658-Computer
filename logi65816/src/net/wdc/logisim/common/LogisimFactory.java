package net.wdc.logisim.common;

import com.cburch.logisim.data.Bounds;
import com.cburch.logisim.data.Direction;
import com.cburch.logisim.instance.InstanceFactory;
import com.cburch.logisim.instance.InstancePainter;
import com.cburch.logisim.instance.InstanceState;
import com.cburch.logisim.instance.Port;
import com.cburch.logisim.util.StringGetter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

@SuppressWarnings("unchecked")
public abstract class LogisimFactory<T extends LogisimPins>
    extends InstanceFactory
{
  protected final Map<Integer, PortDescription> portMap;
  protected final ComponentDescription description;

  public LogisimFactory(String name, ComponentDescription description)
  {
    super(name);
    this.description = description;
    this.portMap = new LinkedHashMap<>();
    for (PortDescription portDescription : description.getPortDescriptions())
    {
      this.portMap.put(portDescription.index, portDescription);
    }

    setOffsetBounds(Bounds.create(description.getLeft(),
                                  description.getTopY(),
                                  description.getWidth(),
                                  description.getHeight()));
    addPins(this.portMap);
  }

  @SuppressWarnings("unchecked")
  public T getOrCreateInstance(InstanceState instanceState)
  {
    T instance = (T) instanceState.getData();
    if (instance == null)
    {
      instance = createInstance();
      instanceState.setData(instance);
    }
    return instance;
  }

  protected void addPins(Map<Integer, PortDescription> portInfos)
  {
    ArrayList<Port> ports = new ArrayList<>();
    for (Integer index : portInfos.keySet())
    {
      PortDescription info = portInfos.get(index);
      if (info == null)
      {
        continue;
      }
      boolean isRightSide = index >= description.getPinsPerSide();
      int pinPerSide = isRightSide ? index - description.getPinsPerSide() : index;
      Port port;
      if (isRightSide)
      {
        port = new Port(description.getRight(), description.getPinStop() - pinPerSide * description.pixelsPerPin(), info.type, info.bitWidth, info.exclusive);
      }
      else
      {
        port = new Port(description.getLeft(), description.getPinStart() + pinPerSide * description.pixelsPerPin(), info.type, info.bitWidth, info.exclusive);
      }
      port.setToolTip(new StringGetter()
      {
        @Override
        public String toString()
        {
          return info.tooltip;
        }
      });
      ports.add(port);
    }
    setPorts(ports);
  }

  protected boolean isClockHigh(T instance)
  {
    return instance.isClockHigh();
  }

  protected void paintPorts(InstancePainter painter, LogisimPins instance)
  {
    boolean clock = isClockHigh((T) instance);
    painter.drawBounds();
    for (Integer index : portMap.keySet())
    {
      PortDescription portDescription = portMap.get(index);
      if (portDescription != null)
      {
        Direction dir = index < description.getPinsPerSide() ? Direction.EAST : Direction.WEST;
        painter.drawPort(portDescription.index, !clock ? portDescription.lowName : portDescription.highName, dir);
      }
    }
  }

  @Override
  public void propagate(InstanceState instanceState)
  {
    T instance = getOrCreateInstance(instanceState);
    instance.setInstanceState(instanceState);
    instance.tick(instanceState);
  }

  protected abstract T createInstance();
}

