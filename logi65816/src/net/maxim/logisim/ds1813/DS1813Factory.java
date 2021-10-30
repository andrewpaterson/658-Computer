package net.maxim.logisim.ds1813;

import com.cburch.logisim.instance.InstancePainter;
import com.cburch.logisim.instance.InstanceState;
import com.cburch.logisim.instance.Port;
import net.logisim.common.ComponentDescription;
import net.logisim.common.LogisimFactory;
import net.logisim.common.LogisimPainter;
import net.logisim.common.PortDescription;
import net.maxim.ds1813.DS1813;

import java.awt.*;

public class DS1813Factory
    extends LogisimFactory<DS1813LogisimPins>
    implements LogisimPainter<DS1813LogisimPins>
{
  protected static final int PORT_CLOCK = 0;
  protected static final int PORT_RSTB = 1;

  public DS1813Factory()
  {
    super(DS1813.class.getSimpleName(),
          new ComponentDescription(160, ComponentDescription.height(3),
                                   PortDescription.inputShared(PORT_CLOCK, ""),
                                   PortDescription.inoutShared(PORT_RSTB, "RSTB")));
  }

  @Override
  protected Port createLeftSidePort(ComponentDescription description, PortDescription portDescription, int pinPerSide)
  {
    return new Port(description.getLeft() + description.pixelsPerPin(), description.getPinStart() + pinPerSide * description.pixelsPerPin(), portDescription.type, portDescription.bitWidth, portDescription.exclusive);
  }

  @Override
  public ComponentDescription getDescription()
  {
    return description;
  }

  public void paintInstance(InstancePainter painter)
  {
    DS1813LogisimPins pins = getOrCreateInstance(painter);
    paintInstance(painter, pins);
  }

  @Override
  public void paint(DS1813LogisimPins instance, Graphics2D graphics2D)
  {
    drawField(graphics2D, getTopOffset(0), WIDTH_8BIT, "Reset:", instance.getResetString(), true);
  }

  public DS1813LogisimPins getOrCreateInstance(InstanceState instanceState)
  {
    DS1813LogisimPins instance = (DS1813LogisimPins) instanceState.getData();
    if (instance == null)
    {
      instance = createInstance();
      instanceState.setData(instance);
    }
    return instance;
  }

  protected DS1813LogisimPins createInstance()
  {
    DS1813LogisimPins pins = new DS1813LogisimPins();
    new DS1813("", pins);
    return pins;
  }
}

