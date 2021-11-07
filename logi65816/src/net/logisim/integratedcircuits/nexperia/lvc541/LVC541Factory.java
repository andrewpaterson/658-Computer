package net.logisim.integratedcircuits.nexperia.lvc541;

import net.integratedcircuits.nexperia.lvc541.LVC541;
import net.logisim.common.*;

import java.awt.*;

import static net.integratedcircuits.nexperia.lvc541.LVC541Pins.PORT_1_INDEX;
import static net.integratedcircuits.nexperia.lvc541.LVC541Pins.PORT_2_INDEX;
import static net.logisim.common.PortPosition.LEFT;
import static net.logisim.common.PortPosition.RIGHT;

public class LVC541Factory
    extends LogisimFactory<LVC541LogisimPins>
    implements LogisimPainter<LVC541LogisimPins>
{
  protected static LogiBus[] PORT_Y;
  protected static LogiBus[] PORT_A;
  protected static LogiPin[] PORT_OEB;

  public static LVC541Factory create()
  {
    PORT_A = new LogiBus[4];
    PORT_Y = new LogiBus[4];
    PORT_OEB = new LogiPin[4];

    PortFactory factory = new PortFactory();

    PORT_OEB[PORT_1_INDEX] = factory.inputShared("1OE", LEFT).setInverting().setDrawBar().createPin(2);
    PORT_OEB[PORT_2_INDEX] = factory.inputShared("2OE", LEFT).setInverting().setDrawBar().createPin(2);
    PORT_A[PORT_1_INDEX] = factory.inputShared("1A", 2, LEFT).createBus(2);
    PORT_A[PORT_2_INDEX] = factory.inputShared("2A", 2, LEFT).createBus(2);

    factory.blank(RIGHT);
    factory.blank(RIGHT);
    PORT_Y[PORT_1_INDEX] = factory.outputShared("1Y", 2, RIGHT).createBus(2);
    PORT_Y[PORT_2_INDEX] = factory.outputShared("2y", 2, RIGHT).createBus(2);

    return new LVC541Factory(new ComponentDescription(LVC541.class.getSimpleName(),
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

