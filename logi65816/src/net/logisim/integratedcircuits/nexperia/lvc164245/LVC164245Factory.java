package net.logisim.integratedcircuits.nexperia.lvc164245;

import net.integratedcircuits.nexperia.lvc164245.LVC164245;
import net.logisim.common.*;

import java.awt.*;

import static net.logisim.common.ComponentDescription.height;
import static net.logisim.common.PortPosition.LEFT;
import static net.logisim.common.PortPosition.RIGHT;

public class LVC164245Factory
    extends PropagatingInstanceFactory<LVC164245LogisimPins>
    implements LogisimPainter<LVC164245LogisimPins>
{
  protected static LogiPin[] PORT_DIR;
  protected static LogiBus[] PORT_A;
  protected static LogiBus[] PORT_B;
  protected static LogiPin[] PORT_OEB;

  public static LVC164245Factory create()
  {
    PORT_DIR = new LogiPin[2];
    PORT_A = new LogiBus[2];
    PORT_B = new LogiBus[2];
    PORT_OEB = new LogiPin[2];

    PortFactory factory = new PortFactory();

    PORT_OEB[0] = factory.inputShared("1OE", LEFT).setInverting().setDrawBar().createPin(2);
    PORT_B[0] = factory.inoutShared("1B", 8, LEFT).createBus(2);
    PORT_DIR[0] = factory.inputShared("1DIR", LEFT).createPin(4);
    PORT_OEB[1] = factory.inputShared("2OE", LEFT).setInverting().setDrawBar().createPin(2);
    PORT_B[1] = factory.inoutShared("2B", 8, LEFT).createBus(3);
    PORT_DIR[1] = factory.inputShared("2DIR", LEFT).createPin(4);

    PORT_A[0] = factory.inoutShared("1A", 8, RIGHT).createBus(2);
    factory.blank(RIGHT);
    factory.blank(RIGHT);
    PORT_A[1] = factory.inoutShared("2A", 8, RIGHT).createBus(3);

    return new LVC164245Factory(new ComponentDescription(LVC164245.class.getSimpleName(),
                                                         LVC164245.TYPE,
                                                         160,
                                                         height(7),
                                                         factory.getPorts(),
                                                         factory.getCommonPortNames()));
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

