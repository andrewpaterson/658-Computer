package net.logisim.integratedcircuits.nexperia.lvc125;

import net.integratedcircuits.nexperia.lvc125.LVC125;
import net.logisim.common.*;

import java.awt.*;

import static net.integratedcircuits.nexperia.lvc125.LVC125Pins.*;
import static net.logisim.common.PortPosition.LEFT;
import static net.logisim.common.PortPosition.RIGHT;

public class LVC125Factory
    extends PropagatingInstanceFactory<LVC125LogisimPins>
    implements LogisimPainter<LVC125LogisimPins>
{
  protected static LogiPin[] PORT_Y;
  protected static LogiPin[] PORT_A;
  protected static LogiPin[] PORT_OE;

  public static LVC125Factory create()
  {
    PORT_A = new LogiPin[4];
    PORT_Y = new LogiPin[4];
    PORT_OE = new LogiPin[4];

    PortFactory factory = new PortFactory();

    PORT_OE[PORT_1_INDEX] = factory.inputShared("1OE", LEFT).setInverting().setDrawBar().createPin(2);
    PORT_OE[PORT_2_INDEX] = factory.inputShared("2OE", LEFT).setInverting().setDrawBar().createPin(2);
    PORT_OE[PORT_3_INDEX] = factory.inputShared("3OE", LEFT).setInverting().setDrawBar().createPin(2);
    PORT_OE[PORT_4_INDEX] = factory.inputShared("4OE", LEFT).setInverting().setDrawBar().createPin(2);
    PORT_A[PORT_1_INDEX] = factory.inputShared("1A", LEFT).createPin(2);
    PORT_A[PORT_2_INDEX] = factory.inputShared("2A", LEFT).createPin(2);
    PORT_A[PORT_3_INDEX] = factory.inputShared("3A", LEFT).createPin(2);
    PORT_A[PORT_4_INDEX] = factory.inputShared("4A", LEFT).createPin(2);

    factory.blank(RIGHT);
    factory.blank(RIGHT);
    factory.blank(RIGHT);
    factory.blank(RIGHT);
    PORT_Y[PORT_1_INDEX] = factory.outputExclusive("1Y", RIGHT).createPin(2);
    PORT_Y[PORT_2_INDEX] = factory.outputExclusive("2Y", RIGHT).createPin(2);
    PORT_Y[PORT_3_INDEX] = factory.outputExclusive("3Y", RIGHT).createPin(2);
    PORT_Y[PORT_4_INDEX] = factory.outputExclusive("4Y", RIGHT).createPin(2);

    return new LVC125Factory(new ComponentDescription(LVC125.class.getSimpleName(),
                                                      LVC125.TYPE,
                                                      160,
                                                      factory.getPorts(),
                                                      factory.getCommonPortNames()));
  }

  private LVC125Factory(ComponentDescription description)
  {
    super(description);
  }

  @Override
  public void paint(LVC125LogisimPins instance, Graphics2D graphics2D)
  {
  }

  @Override
  protected LVC125LogisimPins createInstance()
  {
    LVC125LogisimPins pins = new LVC125LogisimPins();
    new LVC125("", pins);
    return pins;
  }
}

