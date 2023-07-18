package net.logicim.ui.common.integratedcircuit;

import net.logicim.common.type.Int2D;
import net.logicim.data.common.properties.ComponentProperties;
import net.logicim.data.integratedcircuit.common.PassiveData;
import net.logicim.domain.common.Circuit;
import net.logicim.domain.passive.common.Passive;
import net.logicim.domain.passive.subcircuit.SubcircuitSimulation;
import net.logicim.domain.passive.subcircuit.SubcircuitSimulations;
import net.logicim.ui.circuit.SubcircuitView;
import net.logicim.ui.common.Rotation;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public abstract class PassiveView<PASSIVE extends Passive, PROPERTIES extends ComponentProperties>
    extends ComponentView<PROPERTIES>
{
  protected Map<SubcircuitSimulation, PASSIVE> simulationPassives;

  public PassiveView(SubcircuitView containingSubcircuitView,
                     Int2D position,
                     Rotation rotation,
                     PROPERTIES properties)
  {
    super(containingSubcircuitView,
          position,
          rotation,
          properties);
    this.simulationPassives = new LinkedHashMap<>();
  }

  @Override
  public PASSIVE createComponent(SubcircuitSimulation subcircuitSimulation)
  {
    validateCanCreateComponent(subcircuitSimulation);

    PASSIVE passive = createPassive(subcircuitSimulation);
    simulationPassives.put(subcircuitSimulation, passive);

    postCreateComponent(subcircuitSimulation, passive);
    return passive;
  }

  @Override
  public void destroyComponent()
  {
    for (Map.Entry<SubcircuitSimulation, PASSIVE> entry : simulationPassives.entrySet())
    {
      SubcircuitSimulation subcircuitSimulation = entry.getKey();
      PASSIVE removed = entry.getValue();
      Circuit circuit = subcircuitSimulation.getCircuit();
      circuit.remove(removed);
    }
    simulationPassives.clear();
  }

  @Override
  protected void finaliseView()
  {
    createPortViews();
    super.finaliseView();
    containingSubcircuitView.addPassiveView(this);
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

  @Override
  public Set<SubcircuitSimulation> getComponentSubcircuitSimulations()
  {
    return simulationPassives.keySet();
  }

  public PASSIVE getComponent(SubcircuitSimulation subcircuitSimulation)
  {
    return simulationPassives.get(subcircuitSimulation);
  }

  protected Set<Long> saveSimulationPassiveIDs()
  {
    LinkedHashSet<Long> simulationIDs = new LinkedHashSet<>();
    for (SubcircuitSimulation subcircuitSimulation : simulationPassives.keySet())
    {
      simulationIDs.add(subcircuitSimulation.getId());
    }
    return simulationIDs;
  }

  @Override
  public String toDebugString()
  {
    return super.toDebugString() + String.format("Name [%s]\n", properties.name) + toSimulationsDebugString(simulationPassives.keySet());
  }

  public abstract PassiveData<?> save(boolean selected);

  protected abstract PASSIVE createPassive(SubcircuitSimulation subcircuitSimulation);
}

