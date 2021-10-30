package net.logisim.integratedcircuits.nexperia.lvc16373;

import net.integratedcircuits.nexperia.lvc16373.LVC16373;
import net.logisim.common.ComponentDescription;
import net.logisim.common.LogisimFactory;
import net.logisim.common.LogisimPainter;
import net.logisim.common.PortDescription;

import java.awt.*;

import static net.logisim.common.ComponentDescription.height;

public class LVC16373Factory
    extends LogisimFactory<LVC16373LogisimPins>
    implements LogisimPainter<LVC16373LogisimPins>
{
  protected static final int[] PORT_OEB = new int[]{0, 3};
  protected static final int[] PORT_D = new int[]{1, 4};
  protected static final int[] PORT_LE = new int[]{2, 5};

  protected static final int[] PORT_Q = new int[]{7, 6};

  public LVC16373Factory()
  {
    super(LVC16373.class.getSimpleName(),
          new ComponentDescription(160, height(6),
                                   PortDescription.inputShared(PORT_OEB[0], "1OEB"),
                                   PortDescription.inputShared(PORT_D[0], "1D", 8).setTooltip("Input D(1) (input)"),
                                   PortDescription.inputShared(PORT_LE[0], "1LE"),
                                   PortDescription.inputShared(PORT_OEB[1], "2OEB"),
                                   PortDescription.inputShared(PORT_D[1], "2D", 8).setTooltip("Input D(2) (input)"),
                                   PortDescription.inputShared(PORT_LE[1], "2LE"),

                                   PortDescription.blank(),
                                   PortDescription.outputShared(PORT_Q[1], "2Q", 8).setTooltip("Output Q(2) (output)"),
                                   PortDescription.blank(),
                                   PortDescription.blank(),
                                   PortDescription.outputShared(PORT_Q[0], "1Q", 8).setTooltip("Output Q(1) (output)"),
                                   PortDescription.blank()

          ));
  }

  @Override
  public void paint(LVC16373LogisimPins instance, Graphics2D graphics2D)
  {
    LVC16373 latch = instance.getIntegratedCircuit();
    drawField(graphics2D, getTopOffset(0), WIDTH_8BIT, "Value 1:", latch.getValueString(0), true);
    drawField(graphics2D, getTopOffset(3), WIDTH_8BIT, "Value 2:", latch.getValueString(1), true);
  }

  @Override
  protected LVC16373LogisimPins createInstance()
  {
    LVC16373LogisimPins pins = new LVC16373LogisimPins();
    new LVC16373("", pins);
    return pins;
  }
}

