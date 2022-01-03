package net.logisim.integratedcircuits.nexperia.lvc273;

import net.integratedcircuits.nexperia.lvc273.LVC273;
import net.logisim.common.*;

import java.awt.*;

import static net.logisim.common.PortPosition.LEFT;
import static net.logisim.common.PortPosition.RIGHT;

public class LVC273Factory
    extends PropagatingInstanceFactory<LVC273LogisimPins>
    implements LogisimPainter<LVC273LogisimPins>
{
  protected static LogiPin PORT_MRB;
  protected static LogiBus PORT_D;
  protected static LogiPin PORT_CP;

  protected static LogiBus PORT_Q;

  public static LVC273Factory create()
  {
    PortFactory factory = new PortFactory();

    PORT_MRB = factory.inputShared("MR", LEFT).setInverting().setDrawBar().createPin(2);
    PORT_D = factory.inputShared("D", 8, LEFT).setTooltip("Input D (input)").createBus(2);
    PORT_CP = factory.inputShared("CP", LEFT).createPin(2);

    PORT_Q = factory.outputShared("Q", 8, RIGHT).setTooltip("Output Q (output)").createBus(2);

    return new LVC273Factory(new ComponentDescription(LVC273.class.getSimpleName(),
                                                      LVC273.TYPE,
                                                      160,
                                                      factory.getPorts(),
                                                      factory.getCommonPortNames()));
  }

  protected LVC273Factory(ComponentDescription description)
  {
    super(description);
  }

  @Override
  public void paint(LVC273LogisimPins instance, Graphics2D graphics2D)
  {
    LVC273 latch = instance.getIntegratedCircuit();
    drawField(graphics2D, getTopOffset(0), WIDTH_8BIT, "Value:", latch.getValueString(), true);
  }

  @Override
  protected LVC273LogisimPins createInstance()
  {
    LVC273LogisimPins pins = new LVC273LogisimPins();
    new LVC273("", pins);
    return pins;
  }
}

