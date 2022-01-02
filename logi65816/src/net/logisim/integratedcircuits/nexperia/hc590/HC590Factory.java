package net.logisim.integratedcircuits.nexperia.hc590;

import net.integratedcircuits.nexperia.hc590.HC590;
import net.logisim.common.*;

import java.awt.*;

import static net.logisim.common.PortPosition.LEFT;
import static net.logisim.common.PortPosition.RIGHT;

public class HC590Factory
    extends PropagatingInstanceFactory<HC590LogisimPins>
    implements LogisimPainter<HC590LogisimPins>
{
  protected static LogiPin PORT_MRCB;
  protected static LogiPin PORT_RCOB;
  protected static LogiPin PORT_CPC;
  protected static LogiPin PORT_CPR;
  protected static LogiPin PORT_CEB;
  protected static LogiPin PORT_OEB;

  protected static LogiBus PORT_Q;

  public static HC590Factory create()
  {
    PortFactory factory = new PortFactory();

    PORT_MRCB = factory.inputShared("MRC", LEFT).setTooltip("Reset (input: reset low").setInverting().setDrawBar().createPin(19);
    PORT_CPC = factory.inputShared("CPC", LEFT).setTooltip("Counter clock, low to high triggered (input)").createPin(19);
    PORT_CEB = factory.inputShared("CE", LEFT).setTooltip("Count enable (input: enable high)").setInverting().setDrawBar().createPin(19);
    PORT_CPR = factory.inputShared("CPR", LEFT).setTooltip("Counter to register clock, low to high triggered (input)").createPin(19);
    PORT_OEB = factory.inputShared("OE", LEFT).setTooltip("Register output enable (input: enable low)").setInverting().setDrawBar().createPin(19);

    PORT_Q = factory.outputExclusive("Q", 8, RIGHT).setTooltip("Output Q (output)").createBus(19);
    PORT_RCOB = factory.outputExclusive("RCO", RIGHT).setTooltip("Carry (output").setInverting().setDrawBar().createPin(19);
    return new HC590Factory(new ComponentDescription(HC590.class.getSimpleName(),
                                                     HC590.TYPE,
                                                     160,
                                                     factory.getPorts(),
                                                     factory.getCommonPortNames()));
  }

  protected HC590Factory(ComponentDescription description)
  {
    super(description);
  }

  @Override
  public void paint(HC590LogisimPins instance, Graphics2D graphics2D)
  {
    HC590 latch = instance.getIntegratedCircuit();
    drawField(graphics2D, getTopOffset(0), WIDTH_8BIT, "Counter:", latch.getCounterValueString(), true);
    drawField(graphics2D, getTopOffset(1), WIDTH_8BIT, "Register:", latch.getRegisterValueString(), true);
  }

  @Override
  protected HC590LogisimPins createInstance()
  {
    HC590LogisimPins pins = new HC590LogisimPins();
    new HC590("", pins);
    return pins;
  }
}

