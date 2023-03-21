package net.logicim.ui.simulation.component.passive.power;

import net.logicim.common.type.Int2D;
import net.logicim.domain.Simulation;
import net.logicim.domain.common.Circuit;
import net.logicim.domain.passive.power.PowerSource;
import net.logicim.ui.circuit.SubcircuitView;
import net.logicim.ui.common.Rotation;
import net.logicim.data.common.properties.ComponentProperties;
import net.logicim.ui.common.integratedcircuit.PassiveView;

public abstract class PowerSourceView<PROPERTIES extends ComponentProperties>
    extends PassiveView<PowerSource, PROPERTIES>
{
  public PowerSourceView(SubcircuitView subcircuitView,
                         Circuit circuit,
                         Int2D position,
                         Rotation rotation,
                         PROPERTIES properties)
  {
    super(subcircuitView, circuit, position, rotation, properties);
  }

  @Override
  public void simulationStarted(Simulation simulation)
  {
  }

  @Override
  protected PowerSource createPassive(Circuit circuit)
  {
    return new PowerSource(circuit,
                           properties.name,
                           getVoltageOut());
  }

  public abstract float getVoltageOut();
}

