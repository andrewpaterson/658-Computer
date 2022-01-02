package net.logisim.integratedcircuits.nexperia.hc4040;

import net.integratedcircuits.nexperia.hc4040.HC4040;
import net.logisim.common.*;

import java.awt.*;

import static net.logisim.common.ComponentDescription.height;
import static net.logisim.common.PortPosition.LEFT;
import static net.logisim.common.PortPosition.RIGHT;

public class HC4040Factory
    extends PropagatingInstanceFactory<HC4040LogisimPins>
    implements LogisimPainter<HC4040LogisimPins>
{
  protected static LogiPin PORT_MR;
  protected static LogiPin PORT_CP;

  protected static LogiBus PORT_Q;

  public static HC4040Factory create()
  {
    PortFactory factory = new PortFactory();

    PORT_MR = factory.inputShared("MR", LEFT).setTooltip("Reset (input: reset high").createPin(60);
    PORT_CP = factory.inputShared("CP", LEFT).setTooltip("Counter clock, high to low triggered (input)").createPin(60);

    PORT_Q = factory.outputExclusive("Q", 12, RIGHT).setTooltip("Output Q (output)").createBus(60);
    return new HC4040Factory(new ComponentDescription(HC4040.class.getSimpleName(),
                                                      HC4040.TYPE,
                                                      160,
                                                      height(3),
                                                      factory.getPorts(),
                                                      factory.getCommonPortNames()));
  }

  protected HC4040Factory(ComponentDescription description)
  {
    super(description);
  }

  @Override
  public void paint(HC4040LogisimPins instance, Graphics2D graphics2D)
  {
    HC4040 counter = instance.getIntegratedCircuit();
    drawField(graphics2D, getTopOffset(0), WIDTH_16BIT, "Counter:", counter.getCounterValueString(), true);
  }

  @Override
  protected HC4040LogisimPins createInstance()
  {
    HC4040LogisimPins pins = new HC4040LogisimPins();
    new HC4040("", pins);
    return pins;
  }
}

