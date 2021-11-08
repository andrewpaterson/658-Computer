package net.logisim.integratedcircuits.ti.f283;

import net.integratedcircuits.ti.f283.F283;
import net.logisim.common.*;

import java.awt.*;

import static net.logisim.common.PortPosition.LEFT;
import static net.logisim.common.PortPosition.RIGHT;

public class F283Factory
    extends LogisimFactory<F283LogisimPins>
    implements LogisimPainter<F283LogisimPins>
{
  protected static LogiBus PORT_A;
  protected static LogiBus PORT_B;
  protected static LogiPin PORT_C0;

  protected static LogiBus PORT_SIGMA;
  protected static LogiPin PORT_CO;

  public static F283Factory create()
  {
    PortFactory factory = new PortFactory();

    PORT_A = factory.inputShared("A", 4, LEFT).setTooltip("Input A (input)").createBus(10);
    PORT_B = factory.inputShared("B", 4, LEFT).setTooltip("Input Q (input)").createBus(10);
    PORT_C0 = factory.inputShared("C0", LEFT).setTooltip("Input C0 (Carry, input)").createPin(10);

    PORT_SIGMA = factory.outputShared("SIGMA", 4, RIGHT).createBus(10);
    factory.blank(RIGHT);
    PORT_CO = factory.outputExclusive("CO", RIGHT).createPin(10);

    return new F283Factory(new ComponentDescription(F283.class.getSimpleName(),
                                                    F283.TYPE,
                                                    160,
                                                    factory.getPorts()));
  }

  protected F283Factory(ComponentDescription description)
  {
    super(description);
  }

  @Override
  public void paint(F283LogisimPins instance, Graphics2D graphics2D)
  {
  }

  @Override
  protected F283LogisimPins createInstance()
  {
    F283LogisimPins pins = new F283LogisimPins();
    new F283("", pins);
    return pins;
  }
}

