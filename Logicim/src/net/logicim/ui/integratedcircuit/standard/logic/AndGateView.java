package net.logicim.ui.integratedcircuit.standard.logic;

import net.logicim.common.type.Float2D;
import net.logicim.common.type.Int2D;
import net.logicim.domain.integratedcircuit.standard.logic.AndGate;
import net.logicim.domain.integratedcircuit.standard.logic.AndGatePins;
import net.logicim.ui.CircuitEditor;
import net.logicim.ui.common.IntegratedCircuitView;
import net.logicim.ui.common.PortView;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.Viewport;
import net.logicim.ui.shape.ArcView;
import net.logicim.ui.shape.LineView;
import net.logicim.ui.shape.RectangleView;

import java.awt.*;

public class AndGateView
    extends IntegratedCircuitView<AndGate>
{
  protected ArcView arcViewFront;
  protected RectangleView rectangleViewBack;
  protected LineView lineView1;
  protected LineView lineView2;
  protected LineView lineView3;

  public AndGateView(CircuitEditor circuitEditor,
                     int inputCount,
                     Int2D position,
                     Rotation rotation)
  {
    super(circuitEditor,
          new AndGate(circuitEditor.getCircuit(), "", new AndGatePins(inputCount)),
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

    arcViewFront = new ArcView(this, new Float2D(0, -0.4f), 1.5f, 0, 180, true, true);
    float height = 1.4f;
    rectangleViewBack = new RectangleView(this, new Float2D(-1.5f, -0.5f), new Float2D(3, height), false, true);
    lineView1 = new LineView(this, new Float2D(-1.5f, -0.5f), new Float2D(-1.5f, -0.5f + height));
    lineView2 = new LineView(this, new Float2D(-1.5f, -0.5f + height), new Float2D(1.5f, -0.5f + height));
    lineView3 = new LineView(this, new Float2D(1.5f, -0.5f + height), new Float2D(1.5f, -0.5f));
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
    if ((arcViewFront != null) && (rectangleViewBack != null))
    {
      Stroke stroke = graphics.getStroke();
      Color color = graphics.getColor();

      rectangleViewBack.paint(graphics, viewport);
      arcViewFront.paint(graphics, viewport);
      lineView1.paint(graphics, viewport);
      lineView2.paint(graphics, viewport);
      lineView3.paint(graphics, viewport);
      super.paint(graphics, viewport);

      graphics.setStroke(stroke);
      graphics.setColor(color);
    }
  }
}

