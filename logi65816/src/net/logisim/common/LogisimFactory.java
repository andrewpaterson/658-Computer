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
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("unchecked")
public abstract class LogisimFactory<T extends LogisimPins>
    extends InstanceFactory
{
  public static final int TOP_OFFSET = 30;
  public static final int WIDTH_8BIT = 38;
  public static final int WIDTH_16BIT = 52;
  public static final int WIDTH_24BIT = 70;

  protected final List<PortDescription> portMap;
  protected final ComponentDescription description;
  protected PropagationListener<T> propagationListener;

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
    this.portMap = Arrays.asList(description.getPortDescriptions());

    setOffsetBounds(Bounds.create(description.getLeft(),
                                  description.getTopY(),
                                  description.getWidth(),
                                  description.getHeight()));
    addPins(this.portMap);
  }

  protected void addPins(List<PortDescription> portInfos)
  {
    ArrayList<Port> ports = new ArrayList<>();
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
          port = new Port(description.getRight(), description.getPinStop() - pinPerSide * description.pixelsPerPin(), portDescription.type, portDescription.bitWidth, portDescription.exclusive);
        }
        else
        {
          port = new Port(description.getLeft(), description.getPinStart() + pinPerSide * description.pixelsPerPin(), portDescription.type, portDescription.bitWidth, portDescription.exclusive);
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
    setPorts(ports);
  }

  protected boolean isClockHigh(T instance)
  {
    return instance.isClockHigh();
  }

  protected void paintPorts(InstancePainter painter, LogisimPins instance)
  {
    boolean clock = !isClockHigh((T) instance);
    painter.drawBounds();
    for (int index = 0; index < portMap.size(); index++)
    {
      PortDescription portDescription = portMap.get(index);
      if (portDescription.mustDraw)
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
    instance.propagate();
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
  }

  protected Color setColour(Graphics g, boolean black)
  {
    Color oldColour = g.getColor();
    if (black)
    {
      g.setColor(Color.black);
    }
    else
    {
      g.setColor(Color.lightGray);
    }
    return oldColour;
  }

  protected void drawField(Graphics g, int topOffset, int rectangleWidth, String label, String value, boolean black)
  {
    int y = description.getTopYPlusMargin() + topOffset;
    g.drawRect(5, y - 5, rectangleWidth, 15);
    GraphicsUtil.drawText(g, label, -10, y, GraphicsUtil.H_RIGHT, GraphicsUtil.V_CENTER);
    Color oldColour = setColour(g, black);
    GraphicsUtil.drawText(g, value, 10, y, GraphicsUtil.H_LEFT, GraphicsUtil.V_CENTER);
    g.setColor(oldColour);
  }

  protected abstract T createInstance();

  protected abstract void paint(T instance, Graphics2D graphics2D);
}

