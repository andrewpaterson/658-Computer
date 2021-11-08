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

import static net.logisim.common.ComponentDescription.PIXELS_PER_PIN;
import static net.logisim.common.PortPosition.LEFT;
import static net.logisim.common.PortPosition.RIGHT;

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
  public LogisimFactory(ComponentDescription description)
  {
    super(description.getType() + " (" + description.getName() + ")");
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
    setPorts(createPorts());
  }

  protected List<Port> createPorts()
  {
    ArrayList<Port> ports = new ArrayList<>();

    createVerticalPorts(ports, LEFT);
    createVerticalPorts(ports, RIGHT);

    return ports;
  }

  private void createVerticalPorts(ArrayList<Port> ports, PortPosition position)
  {
    for (PortDescription portDescription : description.getPorts(position))
    {
      if (portDescription.notBlank())
      {
        ports.add(createPort(portDescription));
      }
    }
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

  public int getTopOffset(int offset)
  {
    return TOP_OFFSET + offset * PIXELS_PER_PIN;
  }
}

