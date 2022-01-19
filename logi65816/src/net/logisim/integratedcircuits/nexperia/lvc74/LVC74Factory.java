package net.logisim.integratedcircuits.nexperia.lvc74;

import net.integratedcircuits.nexperia.lvc74.LVC74;
import net.logisim.common.*;

import java.awt.*;

import static net.integratedcircuits.nexperia.lvc74.LVC74Pins.FLIP_FLOP_1_INDEX;
import static net.integratedcircuits.nexperia.lvc74.LVC74Pins.FLIP_FLOP_2_INDEX;
import static net.logisim.common.ComponentDescription.PIXELS_PER_PIN;
import static net.logisim.common.PortPosition.LEFT;
import static net.logisim.common.PortPosition.RIGHT;

public class LVC74Factory
    extends PropagatingInstanceFactory<LVC74LogisimPins>
    implements LogisimPainter<LVC74LogisimPins>
{
  protected static LogiPin[] PORT_CP;
  protected static LogiPin[] PORT_RD;
  protected static LogiPin[] PORT_SD;
  protected static LogiPin[] PORT_D;
  protected static LogiPin[] PORT_Q;
  protected static LogiPin[] PORT_QB;

  public static LVC74Factory create()
  {
    PORT_CP = new LogiPin[2];
    PORT_RD = new LogiPin[2];
    PORT_SD = new LogiPin[2];
    PORT_D = new LogiPin[2];
    PORT_CP = new LogiPin[2];
    PORT_Q = new LogiPin[2];
    PORT_QB = new LogiPin[2];

    PortFactory factory = new PortFactory();

    PORT_CP[FLIP_FLOP_1_INDEX] = factory.inputShared("1CP", LEFT).createPin(1);
    PORT_D[FLIP_FLOP_1_INDEX] = factory.inputShared("1D", LEFT).createPin(1);
    PORT_RD[FLIP_FLOP_1_INDEX] = factory.inputShared("1RD", LEFT).setInverting().setDrawBar().createPin(1);
    PORT_SD[FLIP_FLOP_1_INDEX] = factory.inputShared("1SD", LEFT).setInverting().setDrawBar().createPin(1);

    factory.blank(RIGHT);
    PORT_Q[FLIP_FLOP_1_INDEX] = factory.outputExclusive("1Q", RIGHT).createPin(1);
    PORT_QB[FLIP_FLOP_1_INDEX] = factory.outputExclusive("1Q", RIGHT).setInverting().setDrawBar().createPin(1);
    factory.blank(RIGHT);

    PORT_CP[FLIP_FLOP_2_INDEX] = factory.inputShared("2CP", LEFT).createPin(1);
    PORT_D[FLIP_FLOP_2_INDEX] = factory.inputShared("2D", LEFT).createPin(1);
    PORT_RD[FLIP_FLOP_2_INDEX] = factory.inputShared("2RD", LEFT).setInverting().setDrawBar().createPin(1);
    PORT_SD[FLIP_FLOP_2_INDEX] = factory.inputShared("2SD", LEFT).setInverting().setDrawBar().createPin(1);

    factory.blank(RIGHT);
    PORT_Q[FLIP_FLOP_2_INDEX] = factory.outputExclusive("2Q", RIGHT).createPin(1);
    PORT_QB[FLIP_FLOP_2_INDEX] = factory.outputExclusive("2Q", RIGHT).setInverting().setDrawBar().createPin(1);
    factory.blank(RIGHT);

    return new LVC74Factory(new ComponentDescription(LVC74.class.getSimpleName(),
                                                     LVC74.TYPE,
                                                     160,
                                                     factory.getPorts(),
                                                     factory.getCommonPortNames()));
  }

  private LVC74Factory(ComponentDescription description)
  {
    super(description);
  }

  @Override
  public void paint(LVC74LogisimPins instance, Graphics2D graphics2D)
  {
    LVC74 flipFlop = instance.getIntegratedCircuit();
    drawField(graphics2D, getTopOffset(0) + PIXELS_PER_PIN / 2, WIDTH_8BIT, "1 Value:", flipFlop.getValueString(FLIP_FLOP_1_INDEX), true);
    drawField(graphics2D, getTopOffset(4) + PIXELS_PER_PIN / 2, WIDTH_8BIT, "2 Value:", flipFlop.getValueString(FLIP_FLOP_2_INDEX), true);
  }

  @Override
  protected LVC74LogisimPins createInstance()
  {
    LVC74LogisimPins pins = new LVC74LogisimPins();
    new LVC74("", pins);
    return pins;
  }
}

