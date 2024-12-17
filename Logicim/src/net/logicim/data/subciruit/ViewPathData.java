package net.logicim.data.subciruit;

import net.logicim.data.common.ReflectiveData;

import java.util.List;
import java.util.Map;

public class ViewPathData
    extends ReflectiveData
{
  public long id;
  public List<ViewPathElementData> elements;
  public long prevId;
  public Map<Long, Long> circuitSimulations;

  public ViewPathData()
  {
  }

  public ViewPathData(long id,
                      List<ViewPathElementData> elements,
                      long prevId,
                      Map<Long, Long> circuitSimulations)
  {
    this.id = id;
    this.elements = elements;
    this.prevId = prevId;
    this.circuitSimulations = circuitSimulations;
  }
}

