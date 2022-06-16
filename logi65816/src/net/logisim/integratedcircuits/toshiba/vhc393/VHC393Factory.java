package net.logisim.integratedcircuits.toshiba.vhc393;

import net.integratedcircuits.toshiba.vhc393.VHC393;
import net.logisim.common.*;

import java.awt.*;

import static net.logisim.common.PortPosition.LEFT;
import static net.logisim.common.PortPosition.RIGHT;

public class VHC393Factory
    extends PropagatingInstanceFactory<VHC393LogisimPins>
    implements LogisimPainter<VHC393LogisimPins>
{
  protected static LogiPin[] PORT_CLR;
  protected static LogiPin[] PORT_CPB;
  protected static LogiBus[] PORT_Q;

  public static VHC393Factory create()
  {
    PortFactory factory = new PortFactory();
    PORT_CLR = new LogiPin[2];
    PORT_CPB = new LogiPin[2];
    PORT_Q = new LogiBus[2];

    PORT_CLR[0] = factory.inputShared("1CLR", LEFT).createPin(4);
    PORT_CPB[0] = factory.inputShared("1CP", LEFT).setInverting().setDrawBar().setTooltip("Clock, high to low triggered (input)").createPin(4);
    PORT_CLR[1] = factory.inputShared("2CLR", LEFT).createPin(4);
    PORT_CPB[1] = factory.inputShared("2CP", LEFT).setInverting().setDrawBar().setTooltip("Clock, high to low triggered (input)").createPin(4);

    factory.blank(RIGHT);
    PORT_Q[0] = factory.outputShared("1Q", 4, RIGHT).setTooltip("Output Q (output)").createBus(4);
    PORT_Q[1] = factory.outputShared("2Q", 4, RIGHT).setTooltip("Output Q (output)").createBus(4);
    factory.blank(RIGHT);

    return new VHC393Factory(new ComponentDescription(VHC393.class.getSimpleName(),
                                                      VHC393.TYPE,
                                                      160,
                                                      factory.getPorts(),
                                                      factory.getCommonPortNames()));
  }

  protected VHC393Factory(ComponentDescription description)
  {
    super(description);
  }

  @Override
  public void paint(VHC393LogisimPins instance, Graphics2D graphics2D)
  {
    VHC393 latch = instance.getIntegratedCircuit();
    drawField(graphics2D, getTopOffset(0), WIDTH_8BIT, "Value:", latch.getCounterValueString(0), true);
    drawField(graphics2D, getTopOffset(1), WIDTH_8BIT, "Value:", latch.getCounterValueString(1), true);
  }

  @Override
  protected VHC393LogisimPins createInstance()
  {
    VHC393LogisimPins pins = new VHC393LogisimPins();
    new VHC393("", pins);
    return pins;
  }
}

