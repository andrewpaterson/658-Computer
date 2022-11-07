package net.logicim.ui.integratedcircuit.standard.logic.buffer;

import net.logicim.common.type.Float2D;
import net.logicim.common.type.Int2D;
import net.logicim.domain.integratedcircuit.standard.logic.buffer.NotGate;
import net.logicim.domain.integratedcircuit.standard.logic.buffer.NotGatePins;
import net.logicim.ui.CircuitEditor;
import net.logicim.ui.common.IntegratedCircuitView;
import net.logicim.ui.common.PortView;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.Viewport;
import net.logicim.ui.shape.polygon.PolygonView;

import java.awt.*;

import static net.logicim.ui.common.Rotation.North;

public class NotGateView
    extends IntegratedCircuitView<NotGate>
{
  private PolygonView polygon;

  public NotGateView(CircuitEditor circuitEditor,
                     Int2D position,
                     Rotation rotation)
  {
    super(circuitEditor,
          new NotGate(circuitEditor.getCircuit(), "", new NotGatePins()),
          position,
          rotation);
    new PortView(this, this.integratedCircuit.getPort("Input"), new Int2D(0, 1));
    new PortView(this, this.integratedCircuit.getPort("Output"), new Int2D(0, -1)).setInverting(true, North);

    polygon = new PolygonView(this, true, true, new Float2D(0, -0.9f), new Float2D(0.75f, 1), new Float2D(-0.75f, 1));
    finaliseView();
  }

  @Override
  public void paint(Graphics2D graphics, Viewport viewport)
  {
    if (polygon != null)
    {
      Stroke stroke = graphics.getStroke();
      Color color = graphics.getColor();

      polygon.paint(graphics, viewport);

      super.paint(graphics, viewport);

      graphics.setStroke(stroke);
      graphics.setColor(color);
    }
  }
}

