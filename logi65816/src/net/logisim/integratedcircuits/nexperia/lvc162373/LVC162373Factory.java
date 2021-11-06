package net.logisim.integratedcircuits.nexperia.lvc162373;

import net.integratedcircuits.nexperia.lvc162373.LVC162373;
import net.logisim.common.*;

import java.awt.*;

import static net.logisim.common.PortPosition.LEFT;
import static net.logisim.common.PortPosition.RIGHT;

public class LVC162373Factory
    extends LogisimFactory<LVC162373LogisimPins>
    implements LogisimPainter<LVC162373LogisimPins>
{
  protected static LogiPin[] PORT_OEB;
  protected static LogiBus[] PORT_D;
  protected static LogiPin[] PORT_LE;

  protected static LogiBus[] PORT_Q;

  public static LVC162373Factory create()
  {
    PORT_OEB = new LogiPin[2];
    PORT_D = new LogiBus[2];
    PORT_LE = new LogiPin[2];
    PORT_Q = new LogiBus[2];

    PortFactory factory = new PortFactory();

    PORT_OEB[0] = factory.inputShared("1OE", LEFT).setInverting().setDrawBar().createPin(2);
    PORT_D[0] = factory.inputShared("1D", 8, LEFT).setTooltip("Input D(1) (input)").createBus(2);
    PORT_LE[0] = factory.inputShared("1LE", LEFT).createPin(2);
    PORT_OEB[1] = factory.inputShared("2OE", LEFT).setInverting().setDrawBar().createPin(2);
    PORT_D[1] = factory.inputShared("2D", 8, LEFT).setTooltip("Input D(2) (input)").createBus(2);
    PORT_LE[1] = factory.inputShared("2LE", LEFT).createPin(2);

    PORT_Q[0] = factory.outputShared("1Q", 8, RIGHT).setTooltip("Output Q(1) (output)").createBus(2);
    factory.blank(RIGHT);
    factory.blank(RIGHT);
    PORT_Q[1] = factory.outputShared("2Q", 8, RIGHT).setTooltip("Output Q(2) (output)").createBus(2);

    return new LVC162373Factory(new ComponentDescription(LVC162373.class.getSimpleName(),
                                                         160,
                                                         factory.getPorts()));
  }

  private LVC162373Factory(ComponentDescription description)
  {
    super(description);
  }

  @Override
  public void paint(LVC162373LogisimPins instance, Graphics2D graphics2D)
  {
    LVC162373 latch = instance.getIntegratedCircuit();
    drawField(graphics2D, getTopOffset(0), WIDTH_8BIT, "Value 1:", latch.getValueString(0), true);
    drawField(graphics2D, getTopOffset(3), WIDTH_8BIT, "Value 2:", latch.getValueString(1), true);
  }

  @Override
  protected LVC162373LogisimPins createInstance()
  {
    LVC162373LogisimPins pins = new LVC162373LogisimPins();
    new LVC162373("", pins);
    return pins;
  }
}

