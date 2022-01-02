package net.logisim.integratedcircuits.nexperia.lvc157;

import net.integratedcircuits.nexperia.lvc157.LVC157;
import net.logisim.common.*;

import java.awt.*;

import static net.logisim.common.PortPosition.LEFT;
import static net.logisim.common.PortPosition.RIGHT;

public class LVC157Factory
    extends PropagatingInstanceFactory<LVC157LogisimPins>
    implements LogisimPainter<LVC157LogisimPins>
{

  public static LogiPin PORT_EB;
  public static LogiPin PORT_S;
  public static LogiBus[] PORT_INPUT;
  public static LogiBus PORT_Y;

  public static LVC157Factory create()
  {
    PORT_INPUT = new LogiBus[2];
    PortFactory factory = new PortFactory();

    PORT_EB = factory.inputShared("E", LEFT).setInverting().setDrawBar().createPin(3);
    PORT_S = factory.inputShared("S", LEFT).setTooltip("Input select (input: I0 low, I1 high").createPin(3);
    PORT_INPUT[0] = factory.inputShared("I0", 4, LEFT).createBus(3);
    PORT_INPUT[1] = factory.inputShared("I1", 4, LEFT).createBus(3);

    factory.blank(RIGHT);
    factory.blank(RIGHT);
    PORT_Y = factory.inoutShared("Y", 4, RIGHT).createBus(3);

    return new LVC157Factory(new ComponentDescription(LVC157.class.getSimpleName(),
                                                      LVC157.TYPE,
                                                      160,
                                                      factory.getPorts(),
                                                      factory.getCommonPortNames()));
  }

  private LVC157Factory(ComponentDescription description)
  {
    super(description);
  }

  @Override
  public void paint(LVC157LogisimPins instance, Graphics2D graphics2D)
  {
  }

  @Override
  protected LVC157LogisimPins createInstance()
  {
    LVC157LogisimPins pins = new LVC157LogisimPins();
    new LVC157("", pins);
    return pins;
  }
}

