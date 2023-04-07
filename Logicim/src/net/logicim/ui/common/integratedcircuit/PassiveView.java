package net.logicim.ui.common.integratedcircuit;

import net.logicim.common.SimulatorException;
import net.logicim.common.type.Int2D;
import net.logicim.data.common.properties.ComponentProperties;
import net.logicim.data.integratedcircuit.common.PassiveData;
import net.logicim.domain.CircuitSimulation;
import net.logicim.domain.passive.common.Passive;
import net.logicim.ui.circuit.SubcircuitView;
import net.logicim.ui.common.Rotation;

public abstract class PassiveView<PASSIVE extends Passive, PROPERTIES extends ComponentProperties>
    extends ComponentView<PROPERTIES>
{
  protected PASSIVE passive;

  public PassiveView(SubcircuitView subcircuitView,
                     Int2D position,
                     Rotation rotation,
                     PROPERTIES properties)
  {
    super(subcircuitView,
          position,
          rotation,
          properties);
  }

  protected void createComponent(CircuitSimulation simulation)
  {
    this.passive = createPassive(simulation);
    this.passive.disable();
  }

  protected void validateComponent()
  {
    if (getComponent() == null)
    {
      throw new SimulatorException("Component not configured on [%s].  Call create().", getClass().getSimpleName());
    }
  }

  @Override
  protected void finaliseView(CircuitSimulation simulation)
  {
    createComponent(simulation);
    subcircuitView.addPassiveView(this);
    createPortViews();
    super.finaliseView(simulation);
    validateComponent();
    validatePorts();
  }

  protected void validatePorts()
  {
    validatePorts(passive.getPorts(), portViews);
  }

  public PASSIVE getComponent()
  {
    return passive;
  }

  public abstract PassiveData<?> save(boolean selected);

  protected abstract PASSIVE createPassive(CircuitSimulation circuit);
}

