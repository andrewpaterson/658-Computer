package net.logisim.integratedcircuits.ti.lvc543;

import net.integratedcircuits.ti.lvc543.LVC543;
import net.logisim.common.ComponentDescription;
import net.logisim.common.LogisimFactory;
import net.logisim.common.LogisimPainter;
import net.logisim.common.PortDescription;

import java.awt.*;

import static net.logisim.common.ComponentDescription.height;

public class LVC543Factory
    extends LogisimFactory<LVC543LogisimPins>
    implements LogisimPainter<LVC543LogisimPins>
{
  protected static final int[] PORT_OEB = new int[]{0, 3};
  protected static final int[] PORT_IO = new int[]{1, 4};
  protected static final int[] PORT_LEB = new int[]{2, 5};
  protected static final int[] PORT_CEB = new int[]{9, 8};

  protected static final int[] PORT_B = new int[]{7, 6};

  public LVC543Factory()
  {
    super(LVC543.class.getSimpleName(),
          new ComponentDescription(160, height(6),
                                   PortDescription.inputShared(PORT_LEB[1], "LEBA").setInverting(true),
                                   PortDescription.inputShared(PORT_OEB[1], "OEBA").setInverting(true),
                                   PortDescription.inputShared(PORT_IO[0], "A", 8),
                                   PortDescription.inputShared(PORT_OEB[0], "CEAB").setInverting(true),

                                   PortDescription.inputShared(PORT_OEB[1], "CEBA").setInverting(true),
                                   PortDescription.inputShared(PORT_IO[1], "B", 8),
                                   PortDescription.inputShared(PORT_OEB[0], "OEAB").setInverting(true),
                                   PortDescription.inputShared(PORT_LEB[0], "LEAB").setInverting(true)
          ));
  }

  @Override
  public void paint(LVC543LogisimPins instance, Graphics2D graphics2D)
  {
    LVC543 latch = instance.getIntegratedCircuit();
    drawField(graphics2D, getTopOffset(0), WIDTH_8BIT, "Value 1:", latch.getValueString(0), true);
    drawField(graphics2D, getTopOffset(3), WIDTH_8BIT, "Value 2:", latch.getValueString(1), true);
  }

  @Override
  protected LVC543LogisimPins createInstance()
  {
    LVC543LogisimPins pins = new LVC543LogisimPins();
    new LVC543("", pins);
    return pins;
  }
}

