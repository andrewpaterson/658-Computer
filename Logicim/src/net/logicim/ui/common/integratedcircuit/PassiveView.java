package net.logicim.ui.common.integratedcircuit;

import net.logicim.common.SimulatorException;
import net.logicim.common.type.Int2D;
import net.logicim.data.integratedcircuit.common.PassiveData;
import net.logicim.data.common.properties.ComponentProperties;
import net.logicim.domain.common.Circuit;
import net.logicim.domain.passive.common.Passive;
import net.logicim.ui.circuit.SubcircuitView;
import net.logicim.ui.common.Rotation;

public abstract class PassiveView<PASSIVE extends Passive, PROPERTIES extends ComponentProperties>
    extends ComponentView<PROPERTIES>
{
  protected PASSIVE passive;

  public PassiveView(SubcircuitView subcircuitView,
                     Circuit circuit,
                     Int2D position,
                     Rotation rotation,
                     PROPERTIES properties)
  {
    super(subcircuitView,
          circuit,
          position,
          rotation,
          properties);
  }

  protected void createComponent(Circuit circuit)
  {
    this.passive = createPassive(circuit);
    this.passive.disable();
    subcircuitView.addPassiveView(this);
  }

  protected void validateComponent()
  {
    if (getComponent() == null)
    {
      throw new SimulatorException("Component not configured on [%s].  Call create().", getClass().getSimpleName());
    }
  }

  @Override
  protected void finaliseView(Circuit circuit)
  {
    createComponent(circuit);
    createPortViews();
    super.finaliseView(circuit);
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

  protected abstract PASSIVE createPassive(Circuit circuit);
}

