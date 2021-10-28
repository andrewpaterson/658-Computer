package net.maxim.logisim;

import net.logisim.common.ComponentDescription;
import net.logisim.common.LogisimFactory;
import net.logisim.common.PortDescription;
import net.maxim.ds1813.DS1813;

import java.awt.*;

public class DS1813Factory
    extends LogisimFactory<DS1813LogisimPins>
{
  protected static final int PORT_RSTB = 0;

  public DS1813Factory()
  {
    super(DS1813.class.getSimpleName(),
          new ComponentDescription(160, 60, 10,
                                   PortDescription.blank(),
                                   PortDescription.inoutShared(PORT_RSTB, "RSTB")
          ));
  }

  @Override
  protected void paint(DS1813LogisimPins instance, Graphics2D graphics2D)
  {
    drawField(graphics2D, TOP_OFFSET, WIDTH_8BIT, "Reset:", "", true);
  }

  @Override
  protected DS1813LogisimPins createInstance()
  {
    DS1813LogisimPins pins = new DS1813LogisimPins();
    new DS1813("", pins);
    return pins;
  }
}

