package net.logicim.ui.integratedcircuit.standard.logic.buffer;

import net.logicim.common.type.Float2D;
import net.logicim.common.type.Int2D;
import net.logicim.domain.common.IntegratedCircuit;
import net.logicim.ui.CircuitEditor;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.Viewport;
import net.logicim.ui.common.port.PortView;
import net.logicim.ui.integratedcircuit.standard.common.StandardIntegratedCircuitView;
import net.logicim.ui.shape.polygon.PolygonView;

import java.awt.*;

import static net.logicim.ui.common.Rotation.North;

public abstract class BaseInverterView<IC extends IntegratedCircuit<?, ?>>
    extends StandardIntegratedCircuitView<IC, BufferProperties>
{
  protected PolygonView polygon;

  public BaseInverterView(CircuitEditor circuitEditor,
                          Int2D position,
                          Rotation rotation,
                          BufferProperties properties)
  {
    super(circuitEditor, position, rotation, properties);
    createGraphics();
  }

  protected void createGraphics()
  {
    polygon = new PolygonView(this, true, true, new Float2D(0, -0.9f), new Float2D(0.75f, 1), new Float2D(-0.75f, 1));
  }

  protected void createPortViews(boolean negateOutput)
  {
    new PortView(this, integratedCircuit.getPorts("Input"), new Int2D(0, 1));

    PortView outputPortView = new PortView(this, integratedCircuit.getPorts("Output"), new Int2D(0, -1));
    if (negateOutput)
    {
      outputPortView.setInverting(true, North);
    }
  }

  @Override
  public void paint(Graphics2D graphics, Viewport viewport, long time)
  {
    super.paint(graphics, viewport, time);

    if (polygon != null)
    {
      Stroke stroke = graphics.getStroke();
      Color color = graphics.getColor();

      polygon.paint(graphics, viewport);

      paintPorts(graphics, viewport, time);

      graphics.setStroke(stroke);
      graphics.setColor(color);
    }
  }

  @Override
  protected BufferProperties createProperties()
  {
    return new BufferProperties();
  }

  @Override
  public void clampProperties()
  {
    super.clampProperties();
  }
}

