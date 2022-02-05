package net.logisim.integratedcircuits.diodesinc.b3251;

import net.integratedcircuits.diodesinc.pib3b3251.B3251;
import net.logisim.common.*;

import java.awt.*;

import static net.logisim.common.PortPosition.LEFT;
import static net.logisim.common.PortPosition.RIGHT;

public class B3251Factory
    extends PropagatingInstanceFactory<B3251LogisimPins>
    implements LogisimPainter<B3251LogisimPins>
{
  protected static LogiBus PORT_SELECT;

  protected static LogiBus PORT_INPUT;
  protected static LogiPin PORT_ENABLE_B;
  protected static LogiPin PORT_Y;

  public static B3251Factory create()
  {

    PortFactory factory = new PortFactory();

    PORT_SELECT = factory.inputShared("S", 3, LEFT).createBus(6);
    PORT_INPUT = factory.inputShared("IA", 8, LEFT).createBus(3);
    PORT_ENABLE_B = factory.inputShared("EA", LEFT).setInverting().setDrawBar().createPin(4);
    PORT_Y = factory.outputExclusive("YA", RIGHT).createPin(1);

    return new B3251Factory(new ComponentDescription(B3251.class.getSimpleName(),
                                                     B3251.TYPE,
                                                     160,
                                                     factory.getPorts(),
                                                     factory.getCommonPortNames()));
  }

  private B3251Factory(ComponentDescription description)
  {
    super(description);
  }

  @Override
  public void paint(B3251LogisimPins instance, Graphics2D graphics2D)
  {
  }

  @Override
  protected B3251LogisimPins createInstance()
  {
    B3251LogisimPins pins = new B3251LogisimPins();
    new B3251("", pins);
    return pins;
  }
}

