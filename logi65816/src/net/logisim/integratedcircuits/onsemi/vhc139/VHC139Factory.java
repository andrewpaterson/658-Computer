package net.logisim.integratedcircuits.onsemi.vhc139;

import net.integratedcircuits.onsemi.vhc139.VHC139;
import net.logisim.common.*;

import java.awt.*;

import static net.logisim.common.PortPosition.LEFT;
import static net.logisim.common.PortPosition.RIGHT;

public class VHC139Factory
    extends PropagatingInstanceFactory<VHC139LogisimPins>
    implements LogisimPainter<VHC139LogisimPins>
{
  protected static LogiBus[] PORT_A;
  protected static LogiPin[] PORT_EB;
  protected static LogiBus[] PORT_Y;

  public static VHC139Factory create()
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

    return new VHC139Factory(new ComponentDescription(VHC139.class.getSimpleName(),
                                                      VHC139.TYPE,
                                                      160,
                                                      factory.getPorts(),
                                                      factory.getCommonPortNames()));
  }

  protected VHC139Factory(ComponentDescription description)
  {
    super(description);
  }

  @Override
  protected VHC139LogisimPins createInstance()
  {
    VHC139LogisimPins pins = new VHC139LogisimPins();
    new VHC139("", pins);
    return pins;
  }

  @Override
  public void paint(VHC139LogisimPins instance, Graphics2D graphics2D)
  {
  }
}

