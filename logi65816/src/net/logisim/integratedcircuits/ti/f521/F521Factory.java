package net.logisim.integratedcircuits.ti.f521;

import net.integratedcircuits.ti.f521.F521;
import net.logisim.common.*;

import java.awt.*;

import static net.logisim.common.PortPosition.LEFT;
import static net.logisim.common.PortPosition.RIGHT;

public class F521Factory
    extends LogisimFactory<F521LogisimPins>
    implements LogisimPainter<F521LogisimPins>
{
  protected static LogiPin PORT_OEB;
  protected static LogiBus PORT_P;
  protected static LogiBus PORT_Q;

  protected static LogiPin PORT_P_EQUALS_Q;

  public static F521Factory create()
  {
    PortFactory factory = new PortFactory();

    PORT_OEB = factory.inputShared("OE", LEFT).setInverting().setDrawBar().createPin(8);
    PORT_P = factory.inputShared("P", 8, LEFT).setTooltip("Input P (input)").createBus(8);
    PORT_Q = factory.inputShared("Q", 8, LEFT).setTooltip("Input Q (input)").createBus(8);

    PORT_P_EQUALS_Q = factory.outputExclusive("P=Q", RIGHT).setInverting().setDrawBar().createPin(8);

    return new F521Factory(new ComponentDescription(F521.class.getSimpleName(),
                                                    160,
                                                    factory.getPorts()));
  }

  protected F521Factory(ComponentDescription description)
  {
    super(description);
  }

  @Override
  public void paint(F521LogisimPins instance, Graphics2D graphics2D)
  {
  }

  @Override
  protected F521LogisimPins createInstance()
  {
    F521LogisimPins pins = new F521LogisimPins();
    new F521("", pins);
    return pins;
  }
}

