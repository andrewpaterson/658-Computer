package net.logisim.integratedcircuits.nexperia.lvc161;

import net.integratedcircuits.nexperia.lvc161.LVC161;
import net.logisim.common.*;

import java.awt.*;

import static net.logisim.common.PortPosition.LEFT;
import static net.logisim.common.PortPosition.RIGHT;

public class LVC161Factory
    extends LogisimFactory<LVC161LogisimPins>
    implements LogisimPainter<LVC161LogisimPins>
{
  protected static LogiBus PORT_D;
  protected static LogiPin PORT_PEB;
  protected static LogiPin PORT_MRB;
  protected static LogiPin PORT_CP;
  protected static LogiPin PORT_CEP;
  protected static LogiPin PORT_CET;
  protected static LogiPin PORT_TC;

  protected static LogiBus PORT_Q;

  public static LVC161Factory create()
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

    return new LVC161Factory(new ComponentDescription(LVC161.class.getSimpleName(),
                                                      LVC161.TYPE,
                                                      160,
                                                      factory.getPorts()));
  }

  protected LVC161Factory(ComponentDescription description)
  {
    super(description);
  }

  @Override
  public void paint(LVC161LogisimPins instance, Graphics2D graphics2D)
  {
    LVC161 latch = instance.getIntegratedCircuit();
    drawField(graphics2D, getTopOffset(0), WIDTH_8BIT, "Value:", latch.getCounterValueString(), true);
  }

  @Override
  protected LVC161LogisimPins createInstance()
  {
    LVC161LogisimPins pins = new LVC161LogisimPins();
    new LVC161("", pins);
    return pins;
  }
}

