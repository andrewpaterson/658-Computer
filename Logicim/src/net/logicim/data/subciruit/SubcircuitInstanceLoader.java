package net.logicim.data.subciruit;

import net.logicim.domain.passive.subcircuit.SubcircuitInstance;

import java.util.HashMap;
import java.util.Map;

public class SubcircuitInstanceLoader
{
  protected Map<Long, SubcircuitInstance> subcircuitInstancesById;

  public SubcircuitInstanceLoader()
  {
    SubcircuitInstance.resetNextId();
    subcircuitInstancesById = new HashMap<>();
  }

  public SubcircuitInstance get(long subcircuitInstanceId)
  {
    return subcircuitInstancesById.get(subcircuitInstanceId);
  }
}

