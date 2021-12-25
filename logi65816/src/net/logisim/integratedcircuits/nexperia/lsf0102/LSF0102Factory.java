package net.logisim.integratedcircuits.nexperia.lsf0102;

import net.integratedcircuits.nexperia.lsf0102.LSF0102;
import net.logisim.common.*;

import java.awt.*;

import static net.logisim.common.PortPosition.LEFT;
import static net.logisim.common.PortPosition.RIGHT;

public class LSF0102Factory
    extends LogisimFactory<LSF0102LogisimPins>
    implements LogisimPainter<LSF0102LogisimPins>
{
  protected static LogiPin PORT_EN;
  protected static LogiBus PORT_A;

  protected static LogiBus PORT_B;

  public static LSF0102Factory create()
  {
    PortFactory factory = new PortFactory();

    PORT_EN = factory.inputShared("EN", LEFT).createPin(5);
    PORT_A = factory.inoutShared("A", 2, LEFT).setTooltip("A (input or output)").createBus(1);

    factory.blank(RIGHT);
    PORT_B = factory.inoutShared("B", 2, RIGHT).setTooltip("B (input or output)").createBus(1);

    return new LSF0102Factory(new ComponentDescription(LSF0102.class.getSimpleName(),
                                                       LSF0102.TYPE,
                                                       160,
                                                       factory.getPorts()));
  }

  protected LSF0102Factory(ComponentDescription description)
  {
    super(description);
  }

  @Override
  public void paint(LSF0102LogisimPins instance, Graphics2D graphics2D)
  {
  }

  @Override
  protected LSF0102LogisimPins createInstance()
  {
    LSF0102LogisimPins pins = new LSF0102LogisimPins();
    new LSF0102("", pins);
    return pins;
  }
}

