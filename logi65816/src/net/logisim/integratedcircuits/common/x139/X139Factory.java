package net.logisim.integratedcircuits.common.x139;

import net.integratedcircuits.common.x139.X139;
import net.logisim.common.*;

import java.awt.*;

import static net.logisim.common.PortPosition.LEFT;
import static net.logisim.common.PortPosition.RIGHT;

public class X139Factory
    extends PropagatingInstanceFactory<X139LogisimPins>
    implements LogisimPainter<X139LogisimPins>
{
  protected static LogiBus[] PORT_A;
  protected static LogiPin[] PORT_EB;
  protected static LogiBus[] PORT_Y;

  public static ComponentDescription create(String name)
  {
    PortFactory factory = new PortFactory();

    PORT_EB = new LogiPin[2];
    PORT_A = new LogiBus[2];
    PORT_Y = new LogiBus[2];

    PORT_EB[0] = factory.inputShared("1E", LEFT).setTooltip("Input 1 enable (input: decode low, high high").setInverting().setDrawBar().createPin(3);
    PORT_A[0] = factory.inputShared("1A", 2, LEFT).setTooltip("Input 1A (input)").createBus(3);
    PORT_EB[1] = factory.inputShared("2E", LEFT).setTooltip("Input 2 enable (input: decode low, high high").setInverting().setDrawBar().createPin(3);
    PORT_A[1] = factory.inputShared("2A", 2, LEFT).setTooltip("Input 2A (input)").createBus(3);

    factory.blank(RIGHT);
    PORT_Y[0] = factory.outputShared("1Y", 4, RIGHT).setInverting().setDrawBar().createBus(3);
    factory.blank(RIGHT);
    PORT_Y[1] = factory.outputShared("2Y", 4, RIGHT).setInverting().setDrawBar().createBus(3);

    return new ComponentDescription(name,
                                    X139.TYPE,
                                    160,
                                    factory.getPorts(),
                                    factory.getCommonPortNames());
  }

  protected X139Factory(ComponentDescription description)
  {
    super(description);
  }

  @Override
  protected X139LogisimPins createInstance()
  {
    X139LogisimPins pins = new X139LogisimPins();
    new X139("", pins);
    return pins;
  }

  @Override
  public void paint(X139LogisimPins instance, Graphics2D graphics2D)
  {
  }
}

