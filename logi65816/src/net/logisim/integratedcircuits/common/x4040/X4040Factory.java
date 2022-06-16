package net.logisim.integratedcircuits.common.x4040;

import net.integratedcircuits.common.x4040.X4040;
import net.logisim.common.*;
import net.logisim.integratedcircuits.common.x4040.X4040LogisimPins;

import java.awt.*;

import static net.logisim.common.ComponentDescription.height;
import static net.logisim.common.PortPosition.LEFT;
import static net.logisim.common.PortPosition.RIGHT;

public class X4040Factory
    extends PropagatingInstanceFactory<X4040LogisimPins>
    implements LogisimPainter<X4040LogisimPins>
{
  public static LogiPin PORT_MR;
  public static LogiPin PORT_CP;

  public static LogiBus PORT_Q;

  public static ComponentDescription create(String name)
  {
    PortFactory factory = new PortFactory();

    PORT_MR = factory.inputShared("MR", LEFT).setTooltip("Reset (input: reset high").createPin(60);
    PORT_CP = factory.inputShared("CP", LEFT).setTooltip("Counter clock, high to low triggered (input)").createPin(60);

    PORT_Q = factory.outputExclusive("Q", 12, RIGHT).setTooltip("Output Q (output)").createBus(60);
    return new ComponentDescription(name,
                                    X4040.TYPE,
                                    160,
                                    height(3),
                                    factory.getPorts(),
                                    factory.getCommonPortNames());
  }

  protected X4040Factory(ComponentDescription description)
  {
    super(description);
  }

  @Override
  public void paint(X4040LogisimPins instance, Graphics2D graphics2D)
  {
    X4040 counter = instance.getIntegratedCircuit();
    drawField(graphics2D, getTopOffset(0), WIDTH_16BIT, "Counter:", counter.getCounterValueString(), true);
  }

  @Override
  protected X4040LogisimPins createInstance()
  {
    X4040LogisimPins pins = new X4040LogisimPins();
    new X4040("", pins);
    return pins;
  }
}

