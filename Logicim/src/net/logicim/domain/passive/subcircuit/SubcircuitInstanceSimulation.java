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
  public SubcircuitInstanceSimulationData save(long subcircuitEditorId)
  {
    return new SubcircuitInstanceSimulationData(getId(),
                                                subcircuitEditorId,
                                                circuitSimulation.getId());
  }

  @Override
  public SubcircuitObject getSubcircuitObject()
  {
    return subcircuitInstance.getSubcircuitObject();
  }

  public void setSubcircuitInstance(SubcircuitInstance subcircuitInstance)
  {
    this.subcircuitInstance = subcircuitInstance;
  }

  @Override
  public String getDescription()
  {
    String name = subcircuitInstance.getName();
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

