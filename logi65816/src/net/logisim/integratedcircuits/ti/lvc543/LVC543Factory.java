package net.logisim.integratedcircuits.ti.lvc543;

import net.integratedcircuits.ti.lvc543.LVC543;
import net.logisim.common.ComponentDescription;
import net.logisim.common.LogisimFactory;
import net.logisim.common.LogisimPainter;
import net.logisim.common.PortFactory;

import java.awt.*;

import static net.integratedcircuits.ti.lvc543.LVC543.AB;
import static net.integratedcircuits.ti.lvc543.LVC543.BA;
import static net.logisim.common.PortPosition.LEFT;
import static net.logisim.common.PortPosition.RIGHT;

public class LVC543Factory
    extends LogisimFactory<LVC543LogisimPins>
    implements LogisimPainter<LVC543LogisimPins>
{
  protected static int[] PORT_OEB;
  protected static int[] PORT_IO;
  protected static int[] PORT_LEB;
  protected static int[] PORT_CEB;

  public static LVC543Factory create()
  {
    PORT_OEB = new int[2];
    PORT_IO = new int[2];
    PORT_LEB = new int[2];
    PORT_CEB = new int[2];

    PortFactory factory = new PortFactory();

    PORT_LEB[AB] = factory.inputShared("LEAB", LEFT).setInverting().setDrawBar().index();
    PORT_OEB[AB] = factory.inputShared("OEAB", LEFT).setInverting().setDrawBar().index();
    PORT_IO[AB] = factory.inputShared("A", 8, LEFT).index();
    PORT_OEB[AB] = factory.inputShared("CEAB", LEFT).setInverting().setDrawBar().index();

    PORT_LEB[BA] = factory.inputShared("LEBA", RIGHT).setInverting().setDrawBar().index();
    PORT_OEB[BA] = factory.inputShared("OEBA", RIGHT).setInverting().setDrawBar().index();
    PORT_IO[BA] = factory.inputShared("B", 8, RIGHT).index();
    PORT_OEB[BA] = factory.inputShared("CEBA", RIGHT).setInverting().setDrawBar().index();

    return new LVC543Factory(new ComponentDescription(LVC543.class.getSimpleName(),
                                                      160,
                                                      factory.getPorts()));
  }

  private LVC543Factory(ComponentDescription description)
  {
    super(description);
  }

  @Override
  public void paint(LVC543LogisimPins instance, Graphics2D graphics2D)
  {
    LVC543 latch = instance.getIntegratedCircuit();
    drawField(graphics2D, getTopOffset(0), WIDTH_8BIT, "A:", latch.getValueString(AB), true);
    drawField(graphics2D, getTopOffset(1), WIDTH_8BIT, "B:", latch.getValueString(BA), true);
  }

  @Override
  protected LVC543LogisimPins createInstance()
  {
    LVC543LogisimPins pins = new LVC543LogisimPins();
    new LVC543("", pins);
    return pins;
  }
}

