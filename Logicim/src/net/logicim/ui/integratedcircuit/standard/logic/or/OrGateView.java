package net.logicim.ui.integratedcircuit.standard.logic.or;

import net.logicim.common.type.Float2D;
import net.logicim.common.type.Int2D;
import net.logicim.domain.integratedcircuit.standard.logic.OrGate;
import net.logicim.domain.integratedcircuit.standard.logic.OrGatePins;
import net.logicim.ui.CircuitEditor;
import net.logicim.ui.common.IntegratedCircuitView;
import net.logicim.ui.common.PortView;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.Viewport;
import net.logicim.ui.shape.arc.ArcView;
import net.logicim.ui.shape.polygon.PolygonView;

import java.awt.*;

public class OrGateView
    extends IntegratedCircuitView<OrGate>
{
  protected ArcView arcViewRight;
  protected ArcView arcViewLeft;
  protected ArcView arcViewBottom;
  protected ArcView arcViewFillRight;
  protected ArcView arcViewFillLeft;
  protected PolygonView polygonViewFillRight;
  protected PolygonView polygonViewFillLeft;

  public OrGateView(CircuitEditor circuitEditor,
                    int inputCount,
                    Int2D position,
                    Rotation rotation)
  {
    super(circuitEditor,
          new OrGate(circuitEditor.getCircuit(), "", new OrGatePins(inputCount)),
          position,
          rotation);

    int start;
    int end;
    boolean skipZero = false;
    if (inputCount % 2 == 0)
    {
      end = inputCount / 2;
      skipZero = true;
    }
    else
    {
      end = inputCount / 2;
    }
    start = -end;

    int portNumber = 0;
    for (int i = start; i <= end; i++)
    {
      if (!((i == 0) & skipZero))
      {
        new PortView(this, this.integratedCircuit.getPort("Input " + portNumber), new Int2D(i, 1));
        portNumber++;
      }
    }
    new PortView(this, this.integratedCircuit.getPort("Output"), new Int2D(0, -2));

    arcViewRight = new ArcView(this, new Float2D(-1.5f, 0.6f), 3, 357, 61, true, false);
    arcViewLeft = new ArcView(this, new Float2D(1.5f, 0.6f), 3, 122, 61, true, false);
    arcViewBottom = new ArcView(this, new Float2D(0, 3.5f), 3, 61, 58, true, false);
    arcViewFillRight = new ArcView(this, new Float2D(-1.5f, 0.6f), 3, 0, 58, false, true);
    arcViewFillLeft = new ArcView(this, new Float2D(1.5f, 0.6f), 3, 122, 58, false, true);

    polygonViewFillLeft = new PolygonView(this, false, true, new Float2D(-1.5f, 0.9f), new Float2D(-1.5f, 0.0f), new Float2D(0, 0.0f));
    polygonViewFillRight = new PolygonView(this, false, true, new Float2D(1.5f, 0.9f), new Float2D(1.5f, 0.0f), new Float2D(0, 0.0f));

    finaliseView();
  }

  @Override
  protected void updateBoundingBox()
  {
    boundingBox.include(1.5f, 0f);
    boundingBox.include(-1.5f, 0f);
    super.updateBoundingBox();
  }

  @Override
  public void paint(Graphics2D graphics, Viewport viewport)
  {
    if ((arcViewRight != null) && (arcViewLeft != null) && (arcViewBottom != null))
    {
      Stroke stroke = graphics.getStroke();
      Color color = graphics.getColor();

      arcViewFillLeft.paint(graphics, viewport);
      arcViewFillRight.paint(graphics, viewport);
      polygonViewFillRight.paint(graphics, viewport);
      polygonViewFillLeft.paint(graphics, viewport);
      arcViewLeft.paint(graphics, viewport);
      arcViewRight.paint(graphics, viewport);
      arcViewBottom.paint(graphics, viewport);
      super.paint(graphics, viewport);

      graphics.setStroke(stroke);
      graphics.setColor(color);
    }
  }
}

