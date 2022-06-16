package net.logisim.integratedcircuits.nexperia.hc4040;

import net.logisim.common.ComponentDescription;
import net.logisim.common.PortFactory;
import net.logisim.integratedcircuits.common.x4040.X4040Factory;

public class HCT4040Factory
    extends X4040Factory
{
  public static HCT4040Factory create()
  {
    PortFactory factory = new PortFactory();

    return new HCT4040Factory(X4040Factory.create("HC4040"));
  }

  protected HCT4040Factory(ComponentDescription description)
  {
    super(description);
  }
}

