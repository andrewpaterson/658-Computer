package net.logisim.common;

import com.cburch.logisim.circuit.Circuit;
import com.cburch.logisim.circuit.CircuitState;
import com.cburch.logisim.comp.Component;
import com.cburch.logisim.data.Attribute;
import com.cburch.logisim.data.Bounds;
import com.cburch.logisim.instance.*;
import com.cburch.logisim.util.StringGetter;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unchecked")
public abstract class LogisimFactory<T extends LogisimPins<?, ?, ?>>
    extends InstanceFactory
    implements LogisimPainter<T>

{
  public static final int TOP_OFFSET = 30;
  public static final int WIDTH_8BIT = 38;
  public static final int WIDTH_16BIT = 52;
  public static final int WIDTH_24BIT = 70;

  protected final ComponentDescription description;

  protected PropagationListener<T> propagationListener;

  @SuppressWarnings("rawtypes")
  public LogisimFactory(String name, ComponentDescription description)
  {
    super(name);
    Attribute[] attributesNames;
    Object[] attributeDefaults;
    attributesNames = new Attribute[]{
        StdAttr.LABEL,
        StdAttr.LABEL_FONT,
        };
    attributeDefaults = new Object[]{
        "",
        StdAttr.DEFAULT_LABEL_FONT,
        };

    setAttributes(
        attributesNames,
        attributeDefaults);

    this.description = description;

    setOffsetBounds(Bounds.create(description.getLeft(),
                                  description.getTopY(),
                                  description.getWidth(),
                                  description.getHeight()));
    setPorts(createPorts(this.description));
  }

  protected List<Port> createPorts(ComponentDescription description)
  {
    ArrayList<Port> ports = new ArrayList<>();
    List<PortDescription> portInfos = description.getPorts();
    for (int index = 0; index < portInfos.size(); index++)
    {
      PortDescription portDescription = portInfos.get(index);
      if (portDescription.mustDraw)
      {
        boolean isRightSide = index >= description.getPinsPerSide();
        int pinPerSide = isRightSide ? index - description.getPinsPerSide() : index;
        Port port;
        if (isRightSide)
        {
          port = createRightSidePort(description, portDescription, pinPerSide);
        }
        else
        {
          port = createLeftSidePort(description, portDescription, pinPerSide);
        }
        port.setToolTip(new StringGetter()
        {
          @Override
          public String toString()
          {
            return portDescription.tooltip;
          }
        });
        ports.add(port);
      }
    }
    return ports;
  }

  protected Port createLeftSidePort(ComponentDescription description, PortDescription portDescription, int pinPerSide)
  {
    return new Port(description.getLeft(), description.getPinStart() + pinPerSide * description.pixelsPerPin(), portDescription.type, portDescription.bitWidth, portDescription.exclusive);
  }

  protected Port createRightSidePort(ComponentDescription description, PortDescription portDescription, int pinPerSide)
  {
    return new Port(description.getRight(), description.getPinStop() - pinPerSide * description.pixelsPerPin(), portDescription.type, portDescription.bitWidth, portDescription.exclusive);
  }

  @Override
  public void propagate(InstanceState instanceState)
  {
    T instance = getOrCreateInstance(instanceState);
    instance.setInstanceState(instanceState);
    instance.propagate();
  }

  public void paintInstance(InstancePainter painter)
  {
    T pins = getOrCreateInstance(painter);
    paintInstance(painter, pins);
  }

  public T getOrCreateInstance(InstanceState instanceState)
  {
    T instance = (T) instanceState.getData();
    if (instance == null)
    {
      instance = createInstance();
      instanceState.setData(instance);

      if (instance.requiresPropagationListener())
      {
        propagationListener = new PropagationListener<>(instance);
        instanceState.getProject().getSimulator().addSimulatorListener(propagationListener);
      }
    }
    return instance;
  }

  @Override
  public void removeComponent(Circuit circ, Component c, CircuitState state)
  {
    super.removeComponent(circ, c, state);
    if (propagationListener != null)
    {
      state.getProject().getSimulator().removeSimulatorListener(propagationListener);
    }
  }

  public ComponentDescription getDescription()
  {
    return description;
  }

  protected abstract T createInstance();
}

