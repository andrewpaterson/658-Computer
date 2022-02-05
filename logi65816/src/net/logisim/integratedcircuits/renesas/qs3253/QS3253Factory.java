package net.logisim.integratedcircuits.renesas.qs3253;

import net.integratedcircuits.renesas.qs3253.QS3253;
import net.logisim.common.*;

import java.awt.*;

import static net.logisim.common.PortPosition.LEFT;
import static net.logisim.common.PortPosition.RIGHT;

public class QS3253Factory
    extends PropagatingInstanceFactory<QS3253LogisimPins>
    implements LogisimPainter<QS3253LogisimPins>
{
  protected static LogiBus PORT_SELECT;

  protected static LogiBus PORT_INPUT_A;
  protected static LogiPin PORT_ENABLE_AB;
  protected static LogiPin PORT_YA;

  protected static LogiBus PORT_INPUT_B;
  protected static LogiPin PORT_ENABLE_BB;
  protected static LogiPin PORT_YB;

  public static QS3253Factory create()
  {

    PortFactory factory = new PortFactory();

    PORT_SELECT = factory.inputShared("S", 2, LEFT).createBus(6);
    PORT_INPUT_A = factory.inputShared("IA", 4, LEFT).createBus(3);
    PORT_ENABLE_AB = factory.inputShared("EA", LEFT).setInverting().setDrawBar().createPin(4);
    PORT_INPUT_B = factory.inputShared("IB", 4, LEFT).createBus(1);
    PORT_ENABLE_BB = factory.inputShared("EB", LEFT).setInverting().setDrawBar().createPin(4);
    PORT_YA = factory.outputExclusive("YA", RIGHT).createPin(1);
    factory.blank(RIGHT);
    PORT_YB = factory.outputExclusive("YB", RIGHT).createPin(1);

    return new QS3253Factory(new ComponentDescription(QS3253.class.getSimpleName(),
                                                      QS3253.TYPE,
                                                      160,
                                                      factory.getPorts(),
                                                      factory.getCommonPortNames()));
  }

  private QS3253Factory(ComponentDescription description)
  {
    super(description);
  }

  @Override
  public void paint(QS3253LogisimPins instance, Graphics2D graphics2D)
  {
  }

  @Override
  protected QS3253LogisimPins createInstance()
  {
    QS3253LogisimPins pins = new QS3253LogisimPins();
    new QS3253("", pins);
    return pins;
  }
}

