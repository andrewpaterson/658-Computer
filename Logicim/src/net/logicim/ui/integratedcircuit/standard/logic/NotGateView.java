package net.logicim.ui.integratedcircuit.standard.logic;

import net.logicim.common.type.Float2D;
import net.logicim.common.type.Int2D;
import net.logicim.common.type.Tuple2;
import net.logicim.domain.integratedcircuit.standard.logic.NotGate;
import net.logicim.domain.integratedcircuit.standard.logic.NotGatePins;
import net.logicim.ui.CircuitEditor;
import net.logicim.ui.common.IntegratedCircuitView;
import net.logicim.ui.common.PortView;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.Viewport;
import net.logicim.ui.shape.TransformablePolygon;

import java.awt.*;
import java.util.List;

public class NotGateView
    extends IntegratedCircuitView<NotGate>
{
  private TransformablePolygon polygon;

  public NotGateView(CircuitEditor circuitEditor,
                     Int2D position,
                     Rotation rotation)
  {
    super(circuitEditor,
          new NotGate(circuitEditor.getCircuit(), "", new NotGatePins()),
          position,
          rotation);
    new PortView(this, this.integratedCircuit.getPort("Input"), new Int2D(0, 1));
    new PortView(this, this.integratedCircuit.getPort("Output"), new Int2D(0, -1));

    polygon = new TransformablePolygon(new Int2D(0, -1), new Float2D(0.75f, 1), new Float2D(-0.75f, 1));
  }

  @Override
  public void paint(Graphics2D graphics, Viewport viewport)
  {
    Stroke stroke = graphics.getStroke();
    Color color = graphics.getColor();

    polygon.rotate(rotation);

    List<Tuple2> transformedBuffer = polygon.getTransformBuffer();
    int[] xArray = new int[transformedBuffer.size()];
    int[] yArray = new int[transformedBuffer.size()];
    for (int i = 0; i < transformedBuffer.size(); i++)
    {
      Tuple2 tuple2 = transformedBuffer.get(i);
      if (tuple2 instanceof Int2D)
      {
        xArray[i] = viewport.transformGridToScreenSpaceX(((Int2D) tuple2).x + position.x);
        yArray[i] = viewport.transformGridToScreenSpaceY(((Int2D) tuple2).y + position.y);
      }
      else if (tuple2 instanceof Float2D)
      {
        xArray[i] = viewport.transformGridToScreenSpaceX(((Float2D) tuple2).x + position.x);
        yArray[i] = viewport.transformGridToScreenSpaceY(((Float2D) tuple2).y + position.y);
      }
    }
    Polygon p = new Polygon(xArray, yArray, transformedBuffer.size());

    graphics.setStroke(new BasicStroke(viewport.getLineWidth()));
    graphics.setColor(Color.WHITE);
    graphics.fillPolygon(p);
    graphics.setColor(Color.BLACK);
    graphics.drawPolygon(p);

    super.paint(graphics, viewport);

    graphics.setStroke(stroke);
    graphics.setColor(color);
  }
}

