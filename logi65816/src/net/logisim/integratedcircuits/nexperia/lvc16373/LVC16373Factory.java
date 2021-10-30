package net.logisim.integratedcircuits.nexperia.lvc16373;

import net.integratedcircuits.nexperia.lvc16373.LVC16373;
import net.logisim.common.ComponentDescription;
import net.logisim.common.LogisimFactory;
import net.logisim.common.LogisimPainter;
import net.logisim.common.PortFactory;

import java.awt.*;

import static net.logisim.common.PortPosition.LEFT;
import static net.logisim.common.PortPosition.RIGHT;

public class LVC16373Factory
    extends LogisimFactory<LVC16373LogisimPins>
    implements LogisimPainter<LVC16373LogisimPins>
{
  protected static int[] PORT_OEB ;
  protected static int[] PORT_D ;
  protected static int[] PORT_LE;

  protected static int[] PORT_Q ;

  public static LVC16373Factory create()
  {
    PORT_OEB = new int[2];
    PORT_D = new int[2];
    PORT_LE = new int[2];
    PORT_Q = new int[2];

    PortFactory factory = new PortFactory();

    PORT_OEB[0] = factory.inputShared("1OEB", LEFT).index();
    PORT_D[0] = factory.inputShared("1D", 8, LEFT).setTooltip("Input D(1) (input)").index();
    PORT_LE[0] = factory.inputShared("1LE", LEFT).index();
    PORT_OEB[1] = factory.inputShared("2OEB", LEFT).index();
    PORT_D[1] = factory.inputShared("2D", 8, LEFT).setTooltip("Input D(2) (input)").index();
    PORT_LE[1] = factory.inputShared("2LE", LEFT).index();

    PORT_Q[0] = factory.outputShared("1Q", 8, RIGHT).setTooltip("Output Q(1) (output)").index();
    factory.blank(RIGHT);
    factory.blank(RIGHT);
    PORT_Q[1] = factory.outputShared("2Q", 8, RIGHT).setTooltip("Output Q(2) (output)").index();

    return new LVC16373Factory(new ComponentDescription(LVC16373.class.getSimpleName(),
                                                        160,
                                                        factory.getPorts()));
  }

  private LVC16373Factory(ComponentDescription description)
  {
    super(description);
  }

  @Override
  public void paint(LVC16373LogisimPins instance, Graphics2D graphics2D)
  {
    LVC16373 latch = instance.getIntegratedCircuit();
    drawField(graphics2D, getTopOffset(0), WIDTH_8BIT, "Value 1:", latch.getValueString(0), true);
    drawField(graphics2D, getTopOffset(3), WIDTH_8BIT, "Value 2:", latch.getValueString(1), true);
  }

  @Override
  protected LVC16373LogisimPins createInstance()
  {
    LVC16373LogisimPins pins = new LVC16373LogisimPins();
    new LVC16373("", pins);
    return pins;
  }
}

