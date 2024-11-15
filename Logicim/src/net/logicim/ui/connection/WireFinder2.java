package net.logicim.ui.connection;

import net.logicim.ui.circuit.path.ViewPath;
import net.logicim.ui.circuit.path.ViewPaths;
import net.logicim.ui.common.ConnectionView;

public class WireFinder2
{
  protected ViewPaths paths;

  public WireFinder2(ViewPaths paths, ViewPath initialViewPath, ConnectionView initialConnectionView)
  {
    this.paths = paths;
  }
}

