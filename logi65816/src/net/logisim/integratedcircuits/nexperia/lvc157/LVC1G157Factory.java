package net.logisim.integratedcircuits.nexperia.lvc157;

import net.integratedcircuits.nexperia.lvc157.LVC1G157;
import net.logisim.common.*;

import java.awt.*;

import static net.logisim.common.PortPosition.LEFT;
import static net.logisim.common.PortPosition.RIGHT;

public class LVC1G157Factory
    extends PropagatingInstanceFactory<LVC1G157LogisimPins>
    implements LogisimPainter<LVC1G157LogisimPins>
{
  public static LogiPin PORT_EB;
  public static LogiPin PORT_S;
  public static LogiPin[] PORT_INPUT;
  public static LogiPin PORT_Y;

  public static LVC1G157Factory create()
  {
    PORT_INPUT = new LogiPin[2];
    PortFactory factory = new PortFactory();

    PORT_EB = factory.inputShared("E", LEFT).setInverting().setDrawBar().createPin(3);
    PORT_S = factory.inputShared("S", LEFT).setTooltip("Input select (input: I0 low, I1 high").createPin(3);
    PORT_INPUT[0] = factory.inputShared("I0", 1, LEFT).createPin(3);
    PORT_INPUT[1] = factory.inputShared("I1", 1, LEFT).createPin(3);

    factory.blank(RIGHT);
    factory.blank(RIGHT);
    PORT_Y = factory.inoutShared("Y", RIGHT).createPin(3);

    return new LVC1G157Factory(new ComponentDescription(LVC1G157.class.getSimpleName(),
                                                        LVC1G157.TYPE,
                                                        160,
                                                        factory.getPorts(),
                                                        factory.getCommonPortNames()));
  }

  private LVC1G157Factory(ComponentDescription description)
  {
    super(description);
  }

  @Override
  public void paint(LVC1G157LogisimPins instance, Graphics2D graphics2D)
  {
  }

  @Override
  protected LVC1G157LogisimPins createInstance()
  {
    LVC1G157LogisimPins pins = new LVC1G157LogisimPins();
    new LVC1G157("", pins);
    return pins;
  }
}

