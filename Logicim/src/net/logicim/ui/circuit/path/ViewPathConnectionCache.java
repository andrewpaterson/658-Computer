package net.logicim.ui.circuit.path;

import net.logicim.ui.common.ConnectionView;
import net.logicim.ui.connection.PathConnectionView;

import java.util.LinkedHashMap;
import java.util.Map;

public class ViewPathConnectionCache
{
  protected Map<CircuitInstanceViewPath, Map<ConnectionView, PathConnectionView>> pathConnectionViewMap;

  public ViewPathConnectionCache()
  {
    pathConnectionViewMap = new LinkedHashMap<>();
  }
}

