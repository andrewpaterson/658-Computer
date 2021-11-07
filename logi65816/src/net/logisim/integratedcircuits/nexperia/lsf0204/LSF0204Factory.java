package net.logisim.integratedcircuits.nexperia.lsf0204;

import net.integratedcircuits.nexperia.lsf0204.LSF0204;
import net.logisim.common.*;

import java.awt.*;

import static net.logisim.common.PortPosition.LEFT;
import static net.logisim.common.PortPosition.RIGHT;

public class LSF0204Factory
    extends LogisimFactory<LSF0204LogisimPins>
    implements LogisimPainter<LSF0204LogisimPins>
{
  protected static LogiPin PORT_EN;
  protected static LogiBus PORT_A;

  protected static LogiBus PORT_B;

  public static LSF0204Factory create()
  {
    PortFactory factory = new PortFactory();

    PORT_EN = factory.inputShared("EN", LEFT).createPin(5);
    PORT_A = factory.inoutShared("A", 4, LEFT).setTooltip("A (input or output)").createBus(1);

    factory.blank(RIGHT);
    PORT_B = factory.inoutShared("B", 4, RIGHT).setTooltip("B (input or output)").createBus(1);

    return new LSF0204Factory(new ComponentDescription(LSF0204.class.getSimpleName(),
                                                       160,
                                                       factory.getPorts()));
  }

  protected LSF0204Factory(ComponentDescription description)
  {
    super(description);
  }

  @Override
  public void paint(LSF0204LogisimPins instance, Graphics2D graphics2D)
  {
  }

  @Override
  protected LSF0204LogisimPins createInstance()
  {
    LSF0204LogisimPins pins = new LSF0204LogisimPins();
    new LSF0204("", pins);
    return pins;
  }
}

