package net.logicim.domain.passive.subcircuit;

import net.common.util.StringUtil;
import net.logicim.data.simulation.SubcircuitInstanceSimulationData;
import net.logicim.domain.CircuitSimulation;

public class SubcircuitInstanceSimulation
    extends SubcircuitSimulation
{
  protected SubcircuitInstance subcircuitInstance;

  public SubcircuitInstanceSimulation(CircuitSimulation circuitSimulation, SubcircuitInstance subcircuitInstance)
  {
    super(circuitSimulation);
    this.subcircuitInstance = subcircuitInstance;
  }

  public SubcircuitInstanceSimulation(CircuitSimulation circuitSimulation, long id)
  {
    super(circuitSimulation, id);
    this.subcircuitInstance = null;
  }

  @Override
  public SubcircuitInstanceSimulationData save(long subcircuitEditorId, long viewPathId)
  {
    return new SubcircuitInstanceSimulationData(viewPathId,
                                                subcircuitEditorId,
                                                circuitSimulation.getId(),
                                                getId());
  }

  public void setSubcircuitInstance(SubcircuitInstance subcircuitInstance)
  {
    this.subcircuitInstance = subcircuitInstance;
  }

  public SubcircuitInstance getSubcircuitInstance()
  {
    return subcircuitInstance;
  }

  @Override
  public String getDescription()
  {
    String name = null;
    if (subcircuitInstance !=  null)
    {
      name = subcircuitInstance.getName();
    }
    if (StringUtil.isEmptyOrNull(name))
    {
      return super.getDescription();
    }
    else
    {
      return super.getDescription() + " Name[" + name + "]";
    }
  }

  @Override
  public boolean isInstance()
  {
    return true;
  }

  @Override
  protected String getType()
  {
    return "Instance";
  }
}

