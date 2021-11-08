package net.logisim.integratedcircuits.nexperia.lvc163;

import net.integratedcircuits.nexperia.lvc163.LVC163;
import net.logisim.common.*;

import java.awt.*;

import static net.logisim.common.PortPosition.LEFT;
import static net.logisim.common.PortPosition.RIGHT;

public class LVC163Factory
    extends LogisimFactory<LVC163LogisimPins>
    implements LogisimPainter<LVC163LogisimPins>
{
  protected static LogiBus PORT_D;
  protected static LogiPin PORT_PEB;
  protected static LogiPin PORT_MRB;
  protected static LogiPin PORT_CP;
  protected static LogiPin PORT_CEP;
  protected static LogiPin PORT_CET;
  protected static LogiPin PORT_TC;

  protected static LogiBus PORT_Q;

  public static LVC163Factory create()
  {
    PortFactory factory = new PortFactory();

    PORT_MRB = factory.inputShared("MR", LEFT).setInverting().setDrawBar().createPin(4);
    PORT_PEB = factory.inputShared("PE", LEFT).setInverting().setDrawBar().createPin(4);
    PORT_D = factory.inputShared("D", 4, LEFT).setTooltip("Input D (input)").createBus(4);
    PORT_CP = factory.inputShared("CP", LEFT).setTooltip("Clock, low to high triggered (input)").createPin(4);
    PORT_CET = factory.inputShared("CET", LEFT).setTooltip("Count enable carry (input: enable high)").createPin(4);
    PORT_CEP = factory.inputShared("CEP", LEFT).setTooltip("Count enable (input: enable high)").createPin(4);

    PORT_Q = factory.outputShared("Q", 4, RIGHT).setTooltip("Output Q (output)").createBus(4);
    PORT_TC = factory.inputShared("TC", RIGHT).setTooltip("Carry (output)").createPin(4);

    return new LVC163Factory(new ComponentDescription(LVC163.class.getSimpleName(),
                                                      LVC163.TYPE,
                                                      160,
                                                      factory.getPorts()));
  }

  protected LVC163Factory(ComponentDescription description)
  {
    super(description);
  }

  @Override
  public void paint(LVC163LogisimPins instance, Graphics2D graphics2D)
  {
    LVC163 latch = instance.getIntegratedCircuit();
    drawField(graphics2D, getTopOffset(0), WIDTH_8BIT, "Value:", latch.getCounterValueString(), true);
  }

  @Override
  protected LVC163LogisimPins createInstance()
  {
    LVC163LogisimPins pins = new LVC163LogisimPins();
    new LVC163("", pins);
    return pins;
  }
}

