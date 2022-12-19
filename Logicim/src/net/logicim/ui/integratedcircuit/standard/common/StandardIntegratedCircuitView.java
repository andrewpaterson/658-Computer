package net.logicim.ui.integratedcircuit.standard.common;

import net.logicim.common.type.Int2D;
import net.logicim.domain.common.IntegratedCircuit;
import net.logicim.domain.common.propagation.Family;
import net.logicim.ui.CircuitEditor;
import net.logicim.ui.common.IntegratedCircuitView;
import net.logicim.ui.common.PortView;
import net.logicim.ui.common.Rotation;

public abstract class StandardIntegratedCircuitView<IC extends IntegratedCircuit<?, ?>>
    extends IntegratedCircuitView<IC>
{
  protected boolean explicitPowerPorts;

  public StandardIntegratedCircuitView(CircuitEditor circuitEditor, Int2D position, Rotation rotation, String name, Family family, boolean explicitPowerPorts)
  {
    super(circuitEditor, position, rotation, name, family);
    this.explicitPowerPorts = explicitPowerPorts;
  }

  protected void createPorts()
  {
    if (explicitPowerPorts)
    {
      updateBoundingBox();

      new PortView(this, this.integratedCircuit.getPort("VCC"), new Int2D((int) Math.floor(boundingBox.getLeft() + 0.5f), 0));
      new PortView(this, this.integratedCircuit.getPort("GND"), new Int2D((int) Math.ceil(boundingBox.getRight() - 0.5f), 0));
    }
  }
}

