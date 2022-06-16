package net.logisim.integratedcircuits.nexperia.lvc139;

import net.integratedcircuits.common.x139.X139;
import net.logisim.common.ComponentDescription;
import net.logisim.integratedcircuits.common.x139.X139Factory;
import net.logisim.integratedcircuits.common.x139.X139LogisimPins;

public class LVC139Factory
    extends X139Factory
{
  public static LVC139Factory create()
  {
    return new LVC139Factory(X139Factory.create("LVC139"));
  }

  protected LVC139Factory(ComponentDescription description)
  {
    super(description);
  }

  @Override
  protected X139LogisimPins createInstance()
  {
    X139LogisimPins pins = new X139LogisimPins();
    new X139("", pins);
    return pins;
  }
}

