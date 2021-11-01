package net.logisim.integratedcircuits.ti.ls148;

import net.integratedcircuits.ti.ls148.LS148;
import net.logisim.common.*;

import java.awt.*;

import static net.logisim.common.PortPosition.LEFT;
import static net.logisim.common.PortPosition.RIGHT;

public class LS148Factory
    extends LogisimFactory<LS148LogisimPins>
    implements LogisimPainter<LS148LogisimPins>
{
  protected static LogiPin PORT_EI;
  protected static LogiBus PORT_INPUT;

  protected static LogiPin PORT_GS;
  protected static LogiBus PORT_A;
  protected static LogiPin PORT_EO;

  public static LS148Factory create()
  {
    PortFactory factory = new PortFactory();

    PORT_EI = factory.inputShared("EI", LEFT).createPin(16);
    PORT_INPUT = factory.inputShared("I", 8, LEFT).setInverting().createBus(16);

    PORT_GS = factory.outputShared("GS", RIGHT).createPin(16);
    PORT_A = factory.outputShared("A", 3, RIGHT).createBus(16);
    PORT_EO = factory.inputShared("EO", RIGHT).createPin(16);

    return new LS148Factory(new ComponentDescription(LS148.class.getSimpleName(),
                                                     160, ComponentDescription.height(3),
                                                     factory.getPorts()));
  }

  protected LS148Factory(ComponentDescription description)
  {
    super(description);
  }

  @Override
  protected LS148LogisimPins createInstance()
  {
    LS148LogisimPins pins = new LS148LogisimPins();
    new LS148("", pins);
    return pins;
  }

  @Override
  public void paint(LS148LogisimPins instance, Graphics2D graphics2D)
  {
  }
}

