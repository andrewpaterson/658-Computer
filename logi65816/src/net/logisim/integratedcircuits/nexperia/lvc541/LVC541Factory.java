package net.logisim.integratedcircuits.nexperia.lvc541;

import net.integratedcircuits.nexperia.lvc541.LVC541;
import net.logisim.common.*;

import java.awt.*;

import static net.logisim.common.PortPosition.LEFT;
import static net.logisim.common.PortPosition.RIGHT;

public class LVC541Factory
    extends LogisimFactory<LVC541LogisimPins>
    implements LogisimPainter<LVC541LogisimPins>
{
  protected static LogiBus PORT_Y;
  protected static LogiBus PORT_A;
  protected static LogiPin PORT_OE1B;
  protected static LogiPin PORT_OE2B;

  public static LVC541Factory create()
  {
    PortFactory factory = new PortFactory();

    PORT_OE1B = factory.inputShared("1OE", LEFT).setInverting().setDrawBar().createPin(3);
    PORT_OE2B = factory.inputShared("2OE", LEFT).setInverting().setDrawBar().createPin(3);
    PORT_A = factory.inputShared("A", 8, LEFT).createBus(3);

    factory.blank(RIGHT);
    factory.blank(RIGHT);
    PORT_Y = factory.outputShared("Y", 8, RIGHT).createBus(3);

    return new LVC541Factory(new ComponentDescription(LVC541.class.getSimpleName(),
                                                      LVC541.TYPE,
                                                      160,
                                                      factory.getPorts()));
  }

  private LVC541Factory(ComponentDescription description)
  {
    super(description);
  }

  @Override
  public void paint(LVC541LogisimPins instance, Graphics2D graphics2D)
  {
  }

  @Override
  protected LVC541LogisimPins createInstance()
  {
    LVC541LogisimPins pins = new LVC541LogisimPins();
    new LVC541("", pins);
    return pins;
  }
}

