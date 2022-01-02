package net.logisim.integratedcircuits.ti.lvc543;

import net.integratedcircuits.ti.lvc543.LVC543;
import net.logisim.common.*;

import java.awt.*;

import static net.integratedcircuits.ti.lvc543.LVC543.A;
import static net.integratedcircuits.ti.lvc543.LVC543.B;
import static net.logisim.common.PortPosition.LEFT;
import static net.logisim.common.PortPosition.RIGHT;

public class LVC543Factory
    extends PropagatingInstanceFactory<LVC543LogisimPins>
    implements LogisimPainter<LVC543LogisimPins>
{
  protected static LogiPin[] PORT_OEB;
  protected static LogiBus[] PORT_IO;
  protected static LogiPin[] PORT_LEB;
  protected static LogiPin[] PORT_CEB;

  public static LVC543Factory create()
  {
    PORT_OEB = new LogiPin[2];
    PORT_IO = new LogiBus[2];
    PORT_LEB = new LogiPin[2];
    PORT_CEB = new LogiPin[2];

    PortFactory factory = new PortFactory();

    PORT_LEB[A] = factory.inputShared("LEAB", LEFT).setInverting().setDrawBar().createPin(2);
    PORT_OEB[A] = factory.inputShared("OEAB", LEFT).setInverting().setDrawBar().createPin(2);
    PORT_IO[A] = factory.inputShared("A", 8, LEFT).createBus(2);
    PORT_CEB[A] = factory.inputShared("CEAB", LEFT).setInverting().setDrawBar().createPin(2);

    PORT_LEB[B] = factory.inputShared("LEBA", RIGHT).setInverting().setDrawBar().createPin(2);
    PORT_OEB[B] = factory.inputShared("OEBA", RIGHT).setInverting().setDrawBar().createPin(2);
    PORT_IO[B] = factory.inputShared("B", 8, RIGHT).createBus(2);
    PORT_CEB[B] = factory.inputShared("CEBA", RIGHT).setInverting().setDrawBar().createPin(2);

    return new LVC543Factory(new ComponentDescription(LVC543.class.getSimpleName(),
                                                      LVC543.TYPE,
                                                      160,
                                                      factory.getPorts(),
                                                      factory.getCommonPortNames()));
  }

  private LVC543Factory(ComponentDescription description)
  {
    super(description);
  }

  @Override
  public void paint(LVC543LogisimPins instance, Graphics2D graphics2D)
  {
    LVC543 latch = instance.getIntegratedCircuit();
    drawField(graphics2D, getTopOffset(0), WIDTH_8BIT, "A:", latch.getValueString(A), true);
    drawField(graphics2D, getTopOffset(1), WIDTH_8BIT, "B:", latch.getValueString(B), true);
  }

  @Override
  protected LVC543LogisimPins createInstance()
  {
    LVC543LogisimPins pins = new LVC543LogisimPins();
    new LVC543("", pins);
    return pins;
  }
}

