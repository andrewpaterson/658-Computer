package net.logicim.ui.integratedcircuit.standard.logic;

import net.logicim.common.type.Int2D;
import net.logicim.domain.integratedcircuit.standard.logic.OrGate;
import net.logicim.domain.integratedcircuit.standard.logic.OrGatePins;
import net.logicim.ui.CircuitEditor;
import net.logicim.ui.common.IntegratedCircuitView;
import net.logicim.ui.common.PortView;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.Viewport;

import java.awt.*;

import static net.logicim.ui.common.Rotation.North;

public class OrGateView
    extends IntegratedCircuitView<OrGate>
{
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
    new PortView(this, this.integratedCircuit.getPort("Output"), new Int2D(0, -1));

//    polygon = new PolygonView(this, new Float2D(0, -0.9f), new Float2D(0.75f, 1), new Float2D(-0.75f, 1));
    finaliseView();
  }

  @Override
  public void paint(Graphics2D graphics, Viewport viewport)
  {
//    if (polygon != null)
    {
      Stroke stroke = graphics.getStroke();
      Color color = graphics.getColor();

//      polygon.paint(graphics, viewport);

      super.paint(graphics, viewport);

      graphics.setStroke(stroke);
      graphics.setColor(color);
    }
  }
}

