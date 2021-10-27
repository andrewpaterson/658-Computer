package net.maxim.logisim;

import net.logisim.common.ComponentDescription;
import net.logisim.common.LogisimFactory;
import net.logisim.common.PortDescription;
import net.maxim.ds1813.DS1813;
import net.nexperia.lvc573.LVC573;

import java.awt.*;

public class DS1813Factory
    extends LogisimFactory<DS1813LogisimPins>
{
  protected static final int PORT_RSTB = 0;

  public DS1813Factory()
  {
    super("LVC573",
          new ComponentDescription(100, 20, 10, true,
                                   PortDescription.blank(),
                                   PortDescription.inoutShared(PORT_RSTB, "RSTB")
          ));
  }

  @Override
  protected void paint(DS1813LogisimPins instance, Graphics2D graphics2D)
  {
    DS1813 latch = instance.getIntegratedCircuit();
//    drawField(graphics2D, TOP_OFFSET, WIDTH_8BIT, "Value:", latch.getValueString(), true);
  }

  @Override
  protected DS1813LogisimPins createInstance()
  {
    DS1813LogisimPins pins = new DS1813LogisimPins();
    new DS1813("", pins);
    return pins;
  }
}

