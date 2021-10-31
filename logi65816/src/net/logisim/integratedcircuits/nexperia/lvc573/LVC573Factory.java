package net.logisim.integratedcircuits.nexperia.lvc573;

import net.integratedcircuits.nexperia.lvc573.LVC573;
import net.logisim.common.ComponentDescription;
import net.logisim.common.LogisimFactory;
import net.logisim.common.LogisimPainter;
import net.logisim.common.PortFactory;

import java.awt.*;

import static net.logisim.common.PortPosition.LEFT;
import static net.logisim.common.PortPosition.RIGHT;

public class LVC573Factory
    extends LogisimFactory<LVC573LogisimPins>
    implements LogisimPainter<LVC573LogisimPins>
{
  protected static int PORT_OEB;
  protected static int PORT_D;
  protected static int PORT_LE;

  protected static int PORT_Q;

  public static LVC573Factory create()
  {
    PortFactory factory = new PortFactory();

    PORT_OEB = factory.inputShared("OE", LEFT).setInverting().setDrawBar().index();
    PORT_D = factory.inputShared("D", 8, LEFT).setTooltip("Input D (input)").index();
    PORT_LE = factory.inputShared("LE", LEFT).index();

    PORT_Q = factory.outputShared("Q", 8, RIGHT).setTooltip("Output Q (output)").index();

    return new LVC573Factory(new ComponentDescription(LVC573.class.getSimpleName(),
                                                      160,
                                                      factory.getPorts()));
  }

  protected LVC573Factory(ComponentDescription description)
  {
    super(description);
  }

  @Override
  public void paint(LVC573LogisimPins instance, Graphics2D graphics2D)
  {
    LVC573 latch = instance.getIntegratedCircuit();
    drawField(graphics2D, getTopOffset(0), WIDTH_8BIT, "Value:", latch.getValueString(), true);
  }

  @Override
  protected LVC573LogisimPins createInstance()
  {
    LVC573LogisimPins pins = new LVC573LogisimPins();
    new LVC573("", pins);
    return pins;
  }
}

