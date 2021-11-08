package net.logisim.integratedcircuits.nexperia.lvc16244;

import net.integratedcircuits.nexperia.lvc16244.LVC16244;
import net.logisim.common.*;

import java.awt.*;

import static net.integratedcircuits.nexperia.lvc16244.LVC16244Pins.*;
import static net.logisim.common.PortPosition.LEFT;
import static net.logisim.common.PortPosition.RIGHT;

public class LVC16244Factory
    extends LogisimFactory<LVC16244LogisimPins>
    implements LogisimPainter<LVC16244LogisimPins>
{
  protected static LogiBus[] PORT_Y;
  protected static LogiBus[] PORT_A;
  protected static LogiPin[] PORT_OEB;

  public static LVC16244Factory create()
  {
    PORT_A = new LogiBus[4];
    PORT_Y = new LogiBus[4];
    PORT_OEB = new LogiPin[4];

    PortFactory factory = new PortFactory();

    PORT_OEB[PORT_1_INDEX] = factory.inputShared("1OE", LEFT).setInverting().setDrawBar().createPin(2);
    PORT_OEB[PORT_2_INDEX] = factory.inputShared("2OE", LEFT).setInverting().setDrawBar().createPin(2);
    PORT_OEB[PORT_3_INDEX] = factory.inputShared("3OE", LEFT).setInverting().setDrawBar().createPin(2);
    PORT_OEB[PORT_4_INDEX] = factory.inputShared("4OE", LEFT).setInverting().setDrawBar().createPin(2);
    PORT_A[PORT_1_INDEX] = factory.inputShared("1A", 4, LEFT).createBus(2);
    PORT_A[PORT_2_INDEX] = factory.inputShared("2A", 4, LEFT).createBus(2);
    PORT_A[PORT_3_INDEX] = factory.inputShared("3A", 4, LEFT).createBus(2);
    PORT_A[PORT_4_INDEX] = factory.inputShared("4A", 4, LEFT).createBus(2);

    factory.blank(RIGHT);
    factory.blank(RIGHT);
    factory.blank(RIGHT);
    factory.blank(RIGHT);
    PORT_Y[PORT_1_INDEX] = factory.outputShared("1Y", 4, RIGHT).createBus(2);
    PORT_Y[PORT_2_INDEX] = factory.outputShared("2y", 4, RIGHT).createBus(2);
    PORT_Y[PORT_3_INDEX] = factory.outputShared("3Y", 4, RIGHT).createBus(2);
    PORT_Y[PORT_4_INDEX] = factory.outputShared("4Y", 4, RIGHT).createBus(2);

    return new LVC16244Factory(new ComponentDescription(LVC16244.class.getSimpleName(),
                                                        LVC16244.TYPE,
                                                        160,
                                                        factory.getPorts()));
  }

  private LVC16244Factory(ComponentDescription description)
  {
    super(description);
  }

  @Override
  public void paint(LVC16244LogisimPins instance, Graphics2D graphics2D)
  {
  }

  @Override
  protected LVC16244LogisimPins createInstance()
  {
    LVC16244LogisimPins pins = new LVC16244LogisimPins();
    new LVC16244("", pins);
    return pins;
  }
}

