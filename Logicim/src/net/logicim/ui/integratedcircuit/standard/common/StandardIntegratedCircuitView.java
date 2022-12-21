package net.logicim.ui.integratedcircuit.standard.common;

import net.logicim.common.type.Int2D;
import net.logicim.domain.common.IntegratedCircuit;
import net.logicim.domain.common.propagation.Family;
import net.logicim.ui.CircuitEditor;
import net.logicim.ui.common.IntegratedCircuitView;
import net.logicim.ui.common.PortView;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.Viewport;
import net.logicim.ui.shape.common.BoundingBox;
import net.logicim.ui.shape.line.LineView;

import java.awt.*;

public abstract class StandardIntegratedCircuitView<IC extends IntegratedCircuit<?, ?>>
    extends IntegratedCircuitView<IC>
{
  protected boolean explicitPowerPorts;
  protected LineView vccLine;
  protected LineView gndLine;

  public StandardIntegratedCircuitView(CircuitEditor circuitEditor, Int2D position, Rotation rotation, String name, Family family, boolean explicitPowerPorts)
  {
    super(circuitEditor, position, rotation, name, family);
    this.explicitPowerPorts = explicitPowerPorts;
  }

  protected void createPortViews()
  {
    if (explicitPowerPorts)
    {
      BoundingBox boundingBox = new BoundingBox();
      updateBoundingBoxFromShapes(boundingBox);

      PortView vccPortView = new PortView(this, integratedCircuit.getPort("VCC"), new Int2D((int) Math.floor(boundingBox.getLeft()), 0));
      PortView gndPortView = new PortView(this, integratedCircuit.getPort("GND"), new Int2D((int) Math.ceil(boundingBox.getRight()), 0));

      vccLine = new LineView(this, vccPortView.getPosition(), new Int2D(vccPortView.getPosition().x + 1, vccPortView.getPosition().y));
      gndLine = new LineView(this, gndPortView.getPosition(), new Int2D(gndPortView.getPosition().x - 1, gndPortView.getPosition().y));
    }
  }

  @Override
  public void paint(Graphics2D graphics, Viewport viewport, long time)
  {
    super.paint(graphics, viewport, time);
    vccLine.paint(graphics, viewport);
    gndLine.paint(graphics, viewport);
  }
}

