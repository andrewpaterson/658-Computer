package net.logicim.ui.simulation.component.passive.power;

import net.logicim.common.type.Int2D;
import net.logicim.domain.Simulation;
import net.logicim.domain.passive.power.PowerSource;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.integratedcircuit.ComponentProperties;
import net.logicim.ui.common.integratedcircuit.PassiveView;
import net.logicim.ui.simulation.CircuitEditor;

public abstract class PowerSourceView<PROPERTIES extends ComponentProperties>
    extends PassiveView<PowerSource, PROPERTIES>
{
  public PowerSourceView(CircuitEditor circuitEditor,
                         Int2D position,
                         Rotation rotation,
                         PROPERTIES properties)
  {
    super(circuitEditor, position, rotation, properties);
  }

  @Override
  public void simulationStarted(Simulation simulation)
  {
  }

  @Override
  protected PowerSource createPassive()
  {
    return new PowerSource(circuitEditor.getCircuit(),
                           properties.name,
                           getVoltageOut());
  }

  public abstract float getVoltageOut();
}

