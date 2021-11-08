package net.logisim.integratedcircuits.nexperia.lvc257;

import net.integratedcircuits.nexperia.lvc257.LVC257;
import net.logisim.common.*;

import java.awt.*;

import static net.logisim.common.PortPosition.LEFT;
import static net.logisim.common.PortPosition.RIGHT;

public class LVC257Factory
    extends LogisimFactory<LVC257LogisimPins>
    implements LogisimPainter<LVC257LogisimPins>
{

  public static LogiPin PORT_OEB;
  public static LogiPin PORT_S;
  public static LogiBus[] PORT_INPUT;
  public static LogiBus PORT_Y;

  public static LVC257Factory create()
  {
    PORT_INPUT = new LogiBus[2];
    PortFactory factory = new PortFactory();

    PORT_OEB = factory.inputShared("1OE", LEFT).setInverting().setDrawBar().createPin(3);
    PORT_S = factory.inputShared("S", LEFT).createPin(3);
    PORT_INPUT[0] = factory.inputShared("I0", 4, LEFT).createBus(3);
    PORT_INPUT[1] = factory.inputShared("I1", 4, LEFT).createBus(3);

    factory.blank(RIGHT);
    factory.blank(RIGHT);
    PORT_Y = factory.inoutShared("Y", 4, RIGHT).createBus(3);

    return new LVC257Factory(new ComponentDescription(LVC257.class.getSimpleName(),
                                                      LVC257.TYPE,
                                                      160,
                                                      factory.getPorts()));
  }

  private LVC257Factory(ComponentDescription description)
  {
    super(description);
  }

  @Override
  public void paint(LVC257LogisimPins instance, Graphics2D graphics2D)
  {
  }

  @Override
  protected LVC257LogisimPins createInstance()
  {
    LVC257LogisimPins pins = new LVC257LogisimPins();
    new LVC257("", pins);
    return pins;
  }
}

