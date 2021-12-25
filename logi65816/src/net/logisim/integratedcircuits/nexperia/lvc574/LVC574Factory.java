package net.logisim.integratedcircuits.nexperia.lvc574;

import net.integratedcircuits.nexperia.lvc574.LVC574;
import net.logisim.common.*;

import java.awt.*;

import static net.logisim.common.PortPosition.LEFT;
import static net.logisim.common.PortPosition.RIGHT;

public class LVC574Factory
    extends LogisimFactory<LVC574LogisimPins>
    implements LogisimPainter<LVC574LogisimPins>
{
  protected static LogiPin PORT_OEB;
  protected static LogiBus PORT_D;
  protected static LogiPin PORT_CP;

  protected static LogiBus PORT_Q;

  public static LVC574Factory create()
  {
    PortFactory factory = new PortFactory();

    PORT_OEB = factory.inputShared("OE", LEFT).setInverting().setDrawBar().createPin(2);
    PORT_D = factory.inputShared("D", 8, LEFT).setTooltip("Input D (input)").createBus(2);
    PORT_CP = factory.inputShared("CP", LEFT).createPin(2);

    PORT_Q = factory.outputShared("Q", 8, RIGHT).setTooltip("Output Q (output)").createBus(2);

    return new LVC574Factory(new ComponentDescription(LVC574.class.getSimpleName(),
                                                      LVC574.TYPE,
                                                      160,
                                                      factory.getPorts()));
  }

  protected LVC574Factory(ComponentDescription description)
  {
    super(description);
  }

  @Override
  public void paint(LVC574LogisimPins instance, Graphics2D graphics2D)
  {
    LVC574 latch = instance.getIntegratedCircuit();
    drawField(graphics2D, getTopOffset(0), WIDTH_8BIT, "Value:", latch.getValueString(), true);
  }

  @Override
  protected LVC574LogisimPins createInstance()
  {
    LVC574LogisimPins pins = new LVC574LogisimPins();
    new LVC574("", pins);
    return pins;
  }
}

