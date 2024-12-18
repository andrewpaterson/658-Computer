package net.logicim.ui.simulation.component.passive.power;

import net.common.type.Int2D;
import net.logicim.data.common.properties.ComponentProperties;
import net.logicim.domain.CircuitSimulation;
import net.logicim.domain.passive.power.PowerSource;
import net.logicim.domain.passive.subcircuit.SubcircuitSimulation;
import net.logicim.ui.circuit.SubcircuitView;
import net.logicim.ui.circuit.path.ViewPath;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.integratedcircuit.PassiveView;

public abstract class PowerSourceView<PROPERTIES extends ComponentProperties>
    extends PassiveView<PowerSource, PROPERTIES>
{
  public PowerSourceView(SubcircuitView subcircuitView,
                         Int2D position,
                         Rotation rotation,
                         PROPERTIES properties)
  {
    super(subcircuitView, position, rotation, properties);
  }

  @Override
  protected PowerSource createPassive(ViewPath viewPath, CircuitSimulation circuitSimulation)
  {
    SubcircuitSimulation containingSubcircuitSimulation = viewPath.getSubcircuitSimulation(circuitSimulation);
    return new PowerSource(containingSubcircuitSimulation,
                           properties.name,
                           getVoltageOut());
  }

  public abstract float getVoltageOut();
}

