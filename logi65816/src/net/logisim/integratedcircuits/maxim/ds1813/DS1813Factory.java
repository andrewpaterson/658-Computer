package net.logisim.integratedcircuits.maxim.ds1813;

import com.cburch.logisim.instance.InstancePainter;
import com.cburch.logisim.instance.InstanceState;
import net.integratedcircuits.maxim.ds1813.DS1813;
import net.logisim.common.*;

import java.awt.*;

import static net.logisim.common.ComponentDescription.height;
import static net.logisim.common.PortPosition.LEFT;
import static net.logisim.common.PortPosition.RIGHT;

public class DS1813Factory
    extends LogisimFactory<DS1813LogisimPins>
    implements LogisimPainter<DS1813LogisimPins>
{
  protected static LogiPin PORT_CLOCK;
  protected static LogiPin PORT_RSTB;
  protected static LogiPin PORT_RESET_SW;

  public static DS1813Factory create()
  {
    PortFactory factory = new PortFactory();

    PORT_RESET_SW = factory.inputShared("RSW", LEFT).setTooltip("Reset Switch (low reset)").createPin(100);
    factory.blank(LEFT);
    PORT_CLOCK = factory.inputShared("", LEFT).createPin(1);
    PORT_RSTB = factory.inoutShared("RST", RIGHT).setInverting().setDrawBar().createPin(100);

    return new DS1813Factory(new ComponentDescription(DS1813.class.getSimpleName(),
                                                      DS1813.TYPE,
                                                      160,
                                                      height(3),
                                                      factory.getPorts()));
  }

  private DS1813Factory(ComponentDescription description)
  {
    super(description);
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

