package net.logisim.integratedcircuits.ti.f251;

import net.integratedcircuits.ti.f251.F251;
import net.logisim.common.*;

import java.awt.*;

import static net.logisim.common.PortPosition.LEFT;
import static net.logisim.common.PortPosition.RIGHT;

public class F251Factory
    extends PropagatingInstanceFactory<F251LogisimPins>
    implements LogisimPainter<F251LogisimPins>
{
  protected static LogiBus PORT_A;
  protected static LogiBus PORT_D;
  protected static LogiPin PORT_GB;
  protected static LogiPin PORT_Y;
  protected static LogiPin PORT_W;

  public static F251Factory create()
  {
    PortFactory factory = new PortFactory();

    PORT_GB = factory.inputShared("G", LEFT).setInverting().setDrawBar().createPin(6);
    PORT_A = factory.inputShared("A", 3, LEFT).setTooltip("Select (input)").createBus(6);
    PORT_D = factory.inputShared("D", 8, LEFT).setTooltip("Data (input)").createBus(6);

    PORT_Y = factory.outputShared("Y", RIGHT).createPin(6);
    factory.blank(RIGHT);
    PORT_W = factory.outputShared("W", RIGHT).setInverting().setDrawBar().createPin(6);

    return new F251Factory(new ComponentDescription(F251.class.getSimpleName(),
                                                    F251.TYPE,
                                                    160,
                                                    factory.getPorts(),
                                                    factory.getCommonPortNames()));
  }

  protected F251Factory(ComponentDescription description)
  {
    super(description);
  }

  @Override
  protected F251LogisimPins createInstance()
  {
    F251LogisimPins pins = new F251LogisimPins();
    new F251("", pins);
    return pins;
  }

  @Override
  public void paint(F251LogisimPins instance, Graphics2D graphics2D)
  {
  }
}

