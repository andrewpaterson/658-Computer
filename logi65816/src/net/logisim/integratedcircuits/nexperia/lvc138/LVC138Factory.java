package net.logisim.integratedcircuits.nexperia.lvc138;

import net.integratedcircuits.nexperia.lvc138.LVC138;
import net.logisim.common.*;

import java.awt.*;

import static net.logisim.common.PortPosition.LEFT;
import static net.logisim.common.PortPosition.RIGHT;

public class LVC138Factory
    extends LogisimFactory<LVC138LogisimPins>
    implements LogisimPainter<LVC138LogisimPins>
{
  protected static LogiBus PORT_A;
  protected static LogiPin PORT_E1B;
  protected static LogiPin PORT_E2B;
  protected static LogiPin PORT_E3;
  protected static LogiBus PORT_Y;

  public static LVC138Factory create()
  {
    PortFactory factory = new PortFactory();

    PORT_E1B = factory.inputShared("E1", LEFT).setInverting().setDrawBar().createPin(3);
    PORT_E2B = factory.inputShared("E2", LEFT).setInverting().setDrawBar().createPin(3);
    PORT_A = factory.inputShared("A", 3, LEFT).setTooltip("Input A (input)").createBus(3);
    PORT_E3 = factory.inputShared("E3", LEFT).createPin(3);

    factory.blank(RIGHT);
    PORT_Y = factory.outputShared("Y", 8, RIGHT).setInverting().setDrawBar().createBus(3);

    return new LVC138Factory(new ComponentDescription(LVC138.class.getSimpleName(),
                                                      160,
                                                      factory.getPorts()));
  }

  protected LVC138Factory(ComponentDescription description)
  {
    super(description);
  }

  @Override
  protected LVC138LogisimPins createInstance()
  {
    LVC138LogisimPins pins = new LVC138LogisimPins();
    new LVC138("", pins);
    return pins;
  }

  @Override
  public void paint(LVC138LogisimPins instance, Graphics2D graphics2D)
  {
  }
}

