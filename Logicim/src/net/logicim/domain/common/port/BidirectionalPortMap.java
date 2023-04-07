package net.logicim.domain.common.port;

import java.util.LinkedHashMap;
import java.util.Map;

public class BidirectionalPortMap
{
  protected Map<Port, Port> bidirectionalPortMap;

  public BidirectionalPortMap(Map<Port, Port> bidirectionalPortMap)
  {
    this.bidirectionalPortMap = bidirectionalPortMap;
  }

  public Map<Port, Port> getBidirectionalPortMap()
  {
    return bidirectionalPortMap;
  }
}

