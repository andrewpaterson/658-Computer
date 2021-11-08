package net.logisim.integratedcircuits.nexperia.lvc139;

import net.integratedcircuits.nexperia.lvc139.LVC139;
import net.logisim.common.*;

import java.awt.*;

import static net.logisim.common.PortPosition.LEFT;
import static net.logisim.common.PortPosition.RIGHT;

public class LVC139Factory
    extends LogisimFactory<LVC139LogisimPins>
    implements LogisimPainter<LVC139LogisimPins>
{
  protected static LogiBus[] PORT_A;
  protected static LogiPin[] PORT_EB;
  protected static LogiBus[] PORT_Y;

  public static LVC139Factory create()
  {
    PortFactory factory = new PortFactory();

    PORT_EB = new LogiPin[2];
    PORT_A = new LogiBus[2];
    PORT_Y = new LogiBus[2];

    PORT_EB[0] = factory.inputShared("1E", LEFT).setTooltip("Input 1 enable (input: decode low, high high").setInverting().setDrawBar().createPin(3);
    PORT_A[0] = factory.inputShared("1A", 2, LEFT).setTooltip("Input 1A (input)").createBus(3);
    PORT_EB[1] = factory.inputShared("2E", LEFT).setTooltip("Input 2 enable (input: decode low, high high").setInverting().setDrawBar().createPin(3);
    PORT_A[1] = factory.inputShared("2A", 2, LEFT).setTooltip("Input 2A (input)").createBus(3);

    factory.blank(RIGHT);
    PORT_Y[0] = factory.outputShared("1Y", 4, RIGHT).setInverting().setDrawBar().createBus(3);
    factory.blank(RIGHT);
    PORT_Y[1] = factory.outputShared("2Y", 4, RIGHT).setInverting().setDrawBar().createBus(3);

    return new LVC139Factory(new ComponentDescription(LVC139.class.getSimpleName(),
                                                      LVC139.TYPE,
                                                      160,
                                                      factory.getPorts()));
  }

  protected LVC139Factory(ComponentDescription description)
  {
    super(description);
  }

  @Override
  protected LVC139LogisimPins createInstance()
  {
    LVC139LogisimPins pins = new LVC139LogisimPins();
    new LVC139("", pins);
    return pins;
  }

  @Override
  public void paint(LVC139LogisimPins instance, Graphics2D graphics2D)
  {
  }
}

