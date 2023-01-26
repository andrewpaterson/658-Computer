package net.logicim.ui.simulation.component.integratedcircuit.standard.common;

import net.logicim.common.type.Int2D;
import net.logicim.domain.common.IntegratedCircuit;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.Viewport;
import net.logicim.ui.common.integratedcircuit.IntegratedCircuitView;
import net.logicim.ui.common.port.PortView;
import net.logicim.ui.shape.common.BoundingBox;
import net.logicim.ui.shape.line.LineView;
import net.logicim.ui.simulation.CircuitEditor;

import java.awt.*;

public abstract class StandardIntegratedCircuitView<IC extends IntegratedCircuit<?, ?>, PROPERTIES extends StandardIntegratedCircuitProperties>
    extends IntegratedCircuitView<IC, PROPERTIES>
{
  protected LineView vccLine;
  protected LineView gndLine;

  public StandardIntegratedCircuitView(CircuitEditor circuitEditor, Int2D position, Rotation rotation, PROPERTIES properties)
  {
    super(circuitEditor, position, rotation, properties);
  }

  protected void createPortViews()
  {
    if (properties.explicitPowerPorts)
    {
      BoundingBox boundingBox = new BoundingBox();
      updateBoundingBoxFromShapes(boundingBox);

      PortView vccPortView = new PortView(this, integratedCircuit.getPort("VCC"), new Int2D((int) Math.floor(boundingBox.getLeft()), 0));
      PortView gndPortView = new PortView(this, integratedCircuit.getPort("GND"), new Int2D((int) Math.ceil(boundingBox.getRight()), 0));

      vccLine = new LineView(this, vccPortView.getRelativePosition(), new Int2D(vccPortView.getRelativePosition().x + 1, vccPortView.getRelativePosition().y));
      gndLine = new LineView(this, gndPortView.getRelativePosition(), new Int2D(gndPortView.getRelativePosition().x - 1, gndPortView.getRelativePosition().y));
    }
  }

  @Override
  public void paint(Graphics2D graphics, Viewport viewport, long time)
  {
    super.paint(graphics, viewport, time);

    if (vccLine != null)
    {
      vccLine.paint(graphics, viewport);
    }

    if (gndLine != null)
    {
      gndLine.paint(graphics, viewport);
    }
  }
}

