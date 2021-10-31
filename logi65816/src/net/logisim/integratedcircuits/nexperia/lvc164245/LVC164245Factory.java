package net.logisim.integratedcircuits.nexperia.lvc164245;

import net.integratedcircuits.nexperia.lvc164245.LVC164245;
import net.logisim.common.ComponentDescription;
import net.logisim.common.LogisimFactory;
import net.logisim.common.LogisimPainter;
import net.logisim.common.PortFactory;

import java.awt.*;

import static net.logisim.common.PortPosition.LEFT;
import static net.logisim.common.PortPosition.RIGHT;

public class LVC164245Factory
    extends LogisimFactory<LVC164245LogisimPins>
    implements LogisimPainter<LVC164245LogisimPins>
{
  protected static int[] PORT_DIR;
  protected static int[] PORT_A;
  protected static int[] PORT_B;
  protected static int[] PORT_OEB;

  public static LVC164245Factory create()
  {
    PORT_DIR = new int[2];
    PORT_A = new int[2];
    PORT_B = new int[2];
    PORT_OEB = new int[2];

    PortFactory factory = new PortFactory();

    PORT_OEB[0] = factory.inputShared("1OE", LEFT).setInverting().setDrawBar().index();
    PORT_B[0] = factory.inoutShared("1B", 8, LEFT).index();
    PORT_DIR[0] = factory.inputShared("1DIR", LEFT).index();
    PORT_OEB[1] = factory.inputShared("2OE", LEFT).setInverting().setDrawBar().index();
    PORT_B[1] = factory.inoutShared("2B", 8, LEFT).index();
    PORT_DIR[1] = factory.inputShared("2DIR", LEFT).index();

    PORT_A[0] = factory.inoutShared("1A", 8, RIGHT).index();
    factory.blank(RIGHT);
    factory.blank(RIGHT);
    PORT_A[1] = factory.inoutShared("2A", 8, RIGHT).index();

    return new LVC164245Factory(new ComponentDescription(LVC164245.class.getSimpleName(),
                                                         160,
                                                         factory.getPorts()));
  }

  private LVC164245Factory(ComponentDescription description)
  {
    super(description);
  }

  @Override
  public void paint(LVC164245LogisimPins instance, Graphics2D graphics2D)
  {
    LVC164245 transceiver = instance.getIntegratedCircuit();
    drawField(graphics2D, getTopOffset(0), WIDTH_8BIT, "Dir 1:", transceiver.getDirectionString(0), transceiver.isDirectionValid(0));
    drawField(graphics2D, getTopOffset(3), WIDTH_8BIT, "Dir 2:", transceiver.getDirectionString(1), transceiver.isDirectionValid(1));
  }

  @Override
  protected LVC164245LogisimPins createInstance()
  {
    LVC164245LogisimPins pins = new LVC164245LogisimPins();
    new LVC164245("", pins);
    return pins;
  }
}

