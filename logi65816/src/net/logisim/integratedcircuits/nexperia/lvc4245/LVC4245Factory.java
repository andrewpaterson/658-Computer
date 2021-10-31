package net.logisim.integratedcircuits.nexperia.lvc4245;

import net.integratedcircuits.nexperia.lvc4245.LVC4245;
import net.logisim.common.ComponentDescription;
import net.logisim.common.LogisimFactory;
import net.logisim.common.LogisimPainter;
import net.logisim.common.PortFactory;

import java.awt.*;

import static net.logisim.common.PortPosition.LEFT;
import static net.logisim.common.PortPosition.RIGHT;

public class LVC4245Factory
    extends LogisimFactory<LVC4245LogisimPins>
    implements LogisimPainter<LVC4245LogisimPins>
{
  protected static int PORT_DIR;
  protected static int PORT_A;
  protected static int PORT_B;
  protected static int PORT_OEB;

  public static LVC4245Factory create()
  {
    PortFactory factory = new PortFactory();

    PORT_OEB = factory.inputShared("OE", LEFT).setInverting().setDrawBar().setTooltip("Output enable (input: A and B high impedance high, A and B enable low)").index();
    PORT_B = factory.inoutShared("B", 8, LEFT).setTooltip("Data B (input or output)").index();
    PORT_DIR = factory.inputShared("DIR", LEFT).setTooltip("Direction (input: B to A low, A to B high)").index();

    PORT_A = factory.inoutShared("A", 8, RIGHT).setTooltip("Data A (input or output)").index();

    return new LVC4245Factory(new ComponentDescription(LVC4245.class.getSimpleName(),
                                                       160,
                                                       factory.getPorts()));
  }

  private LVC4245Factory(ComponentDescription description)
  {
    super(description);
  }

  @Override
  public void paint(LVC4245LogisimPins instance, Graphics2D graphics2D)
  {
    LVC4245 transceiver = instance.getIntegratedCircuit();
    drawField(graphics2D, getTopOffset(0), WIDTH_8BIT, "Dir:", transceiver.getDirectionString(), transceiver.isDirectionValid());
  }

  @Override
  protected LVC4245LogisimPins createInstance()
  {
    LVC4245LogisimPins pins = new LVC4245LogisimPins();
    new LVC4245("", pins);
    return pins;
  }
}

