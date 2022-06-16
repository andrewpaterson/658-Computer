package net.logisim.integratedcircuits.onsemi.vhc139;

import net.logisim.common.ComponentDescription;
import net.logisim.integratedcircuits.common.x139.X139Factory;

public class VHC139Factory
    extends X139Factory
{
  public static VHC139Factory create()
  {
    return new VHC139Factory(X139Factory.create("VHC139"));
  }

  protected VHC139Factory(ComponentDescription description)
  {
    super(description);
  }
}

