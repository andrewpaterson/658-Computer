package net.maxim.logisim;

import com.cburch.logisim.instance.InstancePainter;
import com.cburch.logisim.instance.InstanceState;
import com.cburch.logisim.std.wiring.Clock;
import net.logisim.common.ComponentDescription;
import net.logisim.common.LogisimPainter;
import net.logisim.common.PortDescription;
import net.maxim.ds1813.DS1813;

import java.awt.*;

import static net.logisim.common.LogisimFactory.TOP_OFFSET;
import static net.logisim.common.LogisimFactory.WIDTH_8BIT;

public class DS1813Factory
    extends Clock
    implements LogisimPainter<DS1813LogisimPins>
{
  protected static final int PORT_RSTB = 0;

  private final ComponentDescription description;

  public DS1813Factory()
  {
    super(DS1813.class.getSimpleName());
    description = new ComponentDescription(160, 60, 10,
                                           PortDescription.blank(),
                                           PortDescription.inoutShared(PORT_RSTB, "RSTB"));
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
    drawField(graphics2D, TOP_OFFSET, WIDTH_8BIT, "Reset:", "", true);
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

