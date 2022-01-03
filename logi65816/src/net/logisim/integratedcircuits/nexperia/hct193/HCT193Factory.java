package net.logisim.integratedcircuits.nexperia.hct193;

import net.integratedcircuits.nexperia.hct193.HCT193;
import net.logisim.common.*;

import java.awt.*;

import static net.logisim.common.PortPosition.LEFT;
import static net.logisim.common.PortPosition.RIGHT;

public class HCT193Factory
    extends PropagatingInstanceFactory<HCT193LogisimPins>
    implements LogisimPainter<HCT193LogisimPins>
{
  protected static LogiBus PORT_D;
  protected static LogiPin PORT_CPD;
  protected static LogiPin PORT_CPU;
  protected static LogiPin PORT_MR;
  protected static LogiPin PORT_PLB;

  protected static LogiPin PORT_TCUB;
  protected static LogiPin PORT_TCDB;
  protected static LogiBus PORT_Q;

  public static HCT193Factory create()
  {
    PortFactory factory = new PortFactory();

    PORT_MR = factory.inputShared("MR", LEFT).createPin(4);
    PORT_PLB = factory.inputShared("PL", LEFT).setInverting().setDrawBar().createPin(4);
    PORT_D = factory.inputShared("D", 4, LEFT).setTooltip("Input D (input)").createBus(4);
    PORT_CPU = factory.inputShared("CPU", LEFT).setTooltip("Up Count Clock, low to high triggered (input)").createPin(4);
    PORT_CPD = factory.inputShared("CPD", LEFT).setTooltip("Down Count Clock, low to high triggered (input)").createPin(4);

    PORT_Q = factory.outputShared("Q", 4, RIGHT).setTooltip("Output Q (output)").createBus(4);
    PORT_TCUB = factory.inputShared("TCU", RIGHT).setTooltip("Up Carry (output)").setInverting().setDrawBar().createPin(4);
    PORT_TCDB = factory.inputShared("TCD", RIGHT).setTooltip("Down Borrow (output)").setInverting().setDrawBar().createPin(4);

    return new HCT193Factory(new ComponentDescription(HCT193.class.getSimpleName(),
                                                      HCT193.TYPE,
                                                      160,
                                                      factory.getPorts(),
                                                      factory.getCommonPortNames()));
  }

  protected HCT193Factory(ComponentDescription description)
  {
    super(description);
  }

  @Override
  public void paint(HCT193LogisimPins instance, Graphics2D graphics2D)
  {
    HCT193 counter = instance.getIntegratedCircuit();
    drawField(graphics2D, getTopOffset(0), WIDTH_8BIT, "Value:", counter.getCounterValueString(), true);
  }

  @Override
  protected HCT193LogisimPins createInstance()
  {
    HCT193LogisimPins pins = new HCT193LogisimPins();
    new HCT193("", pins);
    return pins;
  }
}

