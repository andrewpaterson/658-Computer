package net.logicim.ui.simulation.component.passive.pin;

import net.logicim.common.type.Float2D;
import net.logicim.common.type.Int2D;
import net.logicim.data.integratedcircuit.common.PassiveData;
import net.logicim.data.passive.wire.PinData;
import net.logicim.domain.Simulation;
import net.logicim.domain.common.Circuit;
import net.logicim.domain.common.propagation.FamilyVoltageConfiguration;
import net.logicim.domain.common.propagation.FamilyVoltageConfigurationStore;
import net.logicim.domain.passive.wire.Pin;
import net.logicim.ui.circuit.SubcircuitView;
import net.logicim.ui.common.Colours;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.Viewport;
import net.logicim.ui.common.integratedcircuit.PassiveView;
import net.logicim.ui.common.port.PortView;
import net.logicim.ui.shape.circle.CircleView;
import net.logicim.ui.simulation.component.integratedcircuit.extra.FrameView;

import java.awt.*;

public class PinView
    extends PassiveView<Pin, PinProperties>
{
  public static long nextId = 1L;

  protected PortView port;
  protected long id;
  protected Int2D relativeSubcircuitPosition;
  protected FamilyVoltageConfiguration familyVoltageConfiguration;

  protected CircleView circleView;
  protected FrameView frameView;

  public PinView(SubcircuitView subcircuitView,
                 Circuit circuit,
                 Int2D position,
                 Rotation rotation,
                 PinProperties properties)
  {
    super(subcircuitView,
          circuit,
          position,
          rotation,
          properties);
    this.familyVoltageConfiguration = FamilyVoltageConfigurationStore.get(properties.family);
    id = nextId;
    nextId++;
    relativeSubcircuitPosition = new Int2D();

    createGraphics();
    finaliseView(circuit);
  }

  protected void createGraphics()
  {
    if (isSingleDigit())
    {
      circleView = new CircleView(this, new Float2D(0, 1), 1, true, true);
      frameView = null;
    }
    else
    {
      frameView = new FrameView(this, Colours.getInstance().getShapeFill(), 1, -1, 1, 0, 4);
      circleView = null;
    }
  }

  private boolean isSingleDigit()
  {
    return ((properties.radix == Radix.BINARY) &&
            (properties.bitWidth <= 1)) ||
        ((properties.radix == Radix.DECIMAL) &&
         (properties.bitWidth <= 3)) ||
        ((properties.radix == Radix.HEXADECIMAL) &&
         (properties.bitWidth <= 4));
  }

  @Override
  public String getType()
  {
    return "Pin";
  }

  @Override
  protected void createPortViews()
  {
    port = new PortView(this, passive.getPorts(), new Int2D());
  }

  @Override
  public PassiveData<?> save(boolean selected)
  {
    return new PinData(position,
                       rotation,
                       getName(),
                       savePorts(),
                       selected,
                       properties.bitWidth,
                       properties.alignment,
                       properties.inverting,
                       properties.overline,
                       properties.clockNotch,
                       properties.family.getFamily(),
                       properties.radix);
  }

  @Override
  public void clampProperties(PinProperties newProperties)
  {
  }

  @Override
  public void simulationStarted(Simulation simulation)
  {
  }

  @Override
  protected Pin createPassive(Circuit circuit)
  {
    return new Pin(circuit,
                   properties.name,
                   properties.bitWidth);
  }

  @Override
  public void paint(Graphics2D graphics, Viewport viewport, long time)
  {
    super.paint(graphics, viewport, time);
    super.paint(graphics, viewport, time);

    Color color = graphics.getColor();
    Stroke stroke = graphics.getStroke();
    Font font = graphics.getFont();

    if (circleView != null)
    {
      circleView.paint(graphics, viewport);
    }
    if (frameView != null)
    {
      frameView.paint(graphics, viewport);
    }
    //drawCenteredString(graphics, viewport, Voltage.toVoltageString(properties.voltage_V, false));

    paintPorts(graphics, viewport, time);
    graphics.setColor(color);
    graphics.setStroke(stroke);
    graphics.setFont(font);
  }

  public Int2D getRelativeInstancePosition()
  {
    return relativeSubcircuitPosition;
  }
}

