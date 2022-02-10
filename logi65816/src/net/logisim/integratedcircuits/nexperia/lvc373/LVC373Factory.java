package net.logisim.integratedcircuits.nexperia.lvc373;

import net.integratedcircuits.nexperia.lvc373.LVC373;
import net.logisim.common.*;

import java.awt.*;

import static net.logisim.common.PortPosition.LEFT;
import static net.logisim.common.PortPosition.RIGHT;

public class LVC373Factory
    extends PropagatingInstanceFactory<LVC373LogisimPins>
    implements LogisimPainter<LVC373LogisimPins>
{
  protected static LogiPin PORT_OEB;
  protected static LogiBus PORT_D;
  protected static LogiPin PORT_LE;

  protected static LogiBus PORT_Q;

  public static LVC373Factory create()
  {
    PortFactory factory = new PortFactory();

    PORT_OEB = factory.inputShared("OE", LEFT).setInverting().setDrawBar().createPin(2);
    PORT_D = factory.inputShared("D", 8, LEFT).setTooltip("Input D (input)").createBus(2);
    PORT_LE = factory.inputShared("LE", LEFT).setTooltip("Latch data (input: high latch)").createPin(2);

    PORT_Q = factory.outputShared("Q", 8, RIGHT).setTooltip("Output Q (output)").createBus(2);

    return new LVC373Factory(new ComponentDescription(LVC373.class.getSimpleName(),
                                                      LVC373.TYPE,
                                                      160,
                                                      factory.getPorts(),
                                                      factory.getCommonPortNames()));
  }

  protected LVC373Factory(ComponentDescription description)
  {
    super(description);
  }

  @Override
  public void paint(LVC373LogisimPins instance, Graphics2D graphics2D)
  {
    LVC373 latch = instance.getIntegratedCircuit();
    drawField(graphics2D, getTopOffset(0), WIDTH_8BIT, "Value:", latch.getValueString(), true);
  }

  @Override
  protected LVC373LogisimPins createInstance()
  {
    LVC373LogisimPins pins = new LVC373LogisimPins();
    new LVC373("", pins);
    return pins;
  }
}

