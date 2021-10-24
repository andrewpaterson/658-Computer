package net.logisim.common;

import com.cburch.logisim.circuit.Circuit;
import com.cburch.logisim.circuit.CircuitState;
import com.cburch.logisim.comp.Component;
import com.cburch.logisim.data.Attribute;
import com.cburch.logisim.data.Bounds;
import com.cburch.logisim.data.Direction;
import com.cburch.logisim.instance.*;
import com.cburch.logisim.util.GraphicsUtil;
import com.cburch.logisim.util.StringGetter;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

@SuppressWarnings("unchecked")
public abstract class LogisimFactory<T extends LogisimPins>
    extends InstanceFactory
{
  protected final Map<Integer, PortDescription> portMap;
  protected final ComponentDescription description;
  private PropagationListener propagationListener;

  @SuppressWarnings("rawtypes")
  public LogisimFactory(String name, ComponentDescription description)
  {
    super(name);
    Attribute[] attributesNames;
    Object[] attributeDefaults;
    if (description.canChangeDirection())
    {
      attributesNames = new Attribute[]{
          StdAttr.FACING,
          StdAttr.LABEL,
          StdAttr.LABEL_FONT,
          };
      attributeDefaults = new Object[]{
          Direction.EAST,
          "",
          StdAttr.DEFAULT_LABEL_FONT,
          };
    }
    else
    {
      attributesNames = new Attribute[]{
          StdAttr.LABEL,
          StdAttr.LABEL_FONT,
          };
      attributeDefaults = new Object[]{
          "",
          StdAttr.DEFAULT_LABEL_FONT,
          };
    }

    setAttributes(
        attributesNames,
        attributeDefaults);

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

  public void paintInstance(InstancePainter painter)
  {
    Graphics g = painter.getGraphics();
    if (g instanceof Graphics2D)
    {
      Graphics2D graphics2D = (Graphics2D) g;
      T instance = getOrCreateInstance(painter);
      paintPorts(painter, instance);

      Font oldFont = graphics2D.getFont();
      graphics2D.setFont(oldFont.deriveFont(Font.BOLD));
      Bounds bounds = painter.getBounds();

      AffineTransform oldTransform = graphics2D.getTransform();
      AffineTransform newTransform = (AffineTransform) oldTransform.clone();
      if (description.canChangeDirection())
      {
        Direction direction = painter.getAttributeValue(StdAttr.FACING);
        if (direction != null && direction != Direction.EAST)
        {
          var rotate = 0.0;
          rotate = -direction.toRadians();
          newTransform.rotate(rotate);
        }
      }

      newTransform.translate(bounds.getX() + bounds.getWidth() / 2.0, bounds.getY() + bounds.getHeight() / 2.0);
      graphics2D.setTransform(newTransform);
      GraphicsUtil.drawCenteredText(graphics2D, getName(), 0, description.getTopYPlusMargin());

      paint(instance, graphics2D);

      graphics2D.setTransform(oldTransform);
      graphics2D.setFont(oldFont);
    }
  }

  public T getOrCreateInstance(InstanceState instanceState)
  {
    T instance = (T) instanceState.getData();
    if (instance == null)
    {
      instance = createInstance();
      instanceState.setData(instance);

      propagationListener = new PropagationListener<>(instance);
      instanceState.getProject().getSimulator().addSimulatorListener(propagationListener);
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
    System.out.println("LogisimFactory.removeComponent");
  }

  protected abstract T createInstance();

  protected abstract void paint(T instance, Graphics2D graphics2D);
}

