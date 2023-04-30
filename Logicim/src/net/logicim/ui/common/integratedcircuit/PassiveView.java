package net.logicim.ui.common.integratedcircuit;

import net.logicim.common.SimulatorException;
import net.logicim.common.type.Int2D;
import net.logicim.data.common.properties.ComponentProperties;
import net.logicim.data.integratedcircuit.common.PassiveData;
import net.logicim.domain.CircuitSimulation;
import net.logicim.domain.passive.common.Passive;
import net.logicim.ui.circuit.SubcircuitView;
import net.logicim.ui.common.Rotation;

import java.util.LinkedHashMap;
import java.util.Map;

public abstract class PassiveView<PASSIVE extends Passive, PROPERTIES extends ComponentProperties>
    extends ComponentView<PROPERTIES>
{
  protected Map<CircuitSimulation, PASSIVE> simulationPassives;

  public PassiveView(SubcircuitView subcircuitView,
                     Int2D position,
                     Rotation rotation,
                     PROPERTIES properties)
  {
    super(subcircuitView,
          position,
          rotation,
          properties);
    this.simulationPassives = new LinkedHashMap<>();
  }

  protected PASSIVE createComponent(CircuitSimulation simulation)
  {
    if (simulation == null)
    {
      throw new SimulatorException("Cannot create %s component with [null] simulation.", getClass().getSimpleName());
    }

    PASSIVE passive = createPassive(simulation);
    simulationPassives.put(simulation, passive);
    validateComponent(simulation);
    validatePorts(simulation);
    passive.reset(simulation);
    return passive;
  }

  protected void validateComponent(CircuitSimulation simulation)
  {
    if (getComponent(simulation) == null)
    {
      throw new SimulatorException("Component not configured on [%s].  Call create().", getClass().getSimpleName());
    }
  }

  @Override
  protected void finaliseView()
  {
    createPortViews();
    super.finaliseView();
    subcircuitView.addPassiveView(this);
  }

  @Override
  public void simulationStarted(CircuitSimulation simulation)
  {
    if (simulation == null)
    {
      throw new SimulatorException("Cannot start a simulation with a [null] simulation.");
    }

    Passive passive = simulationPassives.get(simulation);
    if (passive == null)
    {
      passive = createComponent(simulation);
    }
    passive.simulationStarted(simulation.getSimulation());
  }

  @Override
  public String getComponentType()
  {
    for (PASSIVE passive : simulationPassives.values())
    {
      return passive.getType();
    }
    return "";
  }

  protected void validatePorts(CircuitSimulation simulation)
  {
    PASSIVE passive = simulationPassives.get(simulation);
    if (passive != null)
    {
      validatePorts(simulation, passive.getPorts(), portViews);
    }
  }

  public PASSIVE getComponent(CircuitSimulation simulation)
  {
    return simulationPassives.get(simulation);
  }

  public abstract PassiveData<?> save(boolean selected);

  protected abstract PASSIVE createPassive(CircuitSimulation circuit);
}

