package net.integratedcircuits.nexperia.logisim.lvc573;

import net.logisim.common.ComponentDescription;
import net.logisim.common.LogisimFactory;
import net.logisim.common.LogisimPainter;
import net.logisim.common.PortDescription;
import net.integratedcircuits.nexperia.lvc573.LVC573;

import java.awt.*;

import static net.logisim.common.ComponentDescription.height;

public class LVC573Factory
    extends LogisimFactory<LVC573LogisimPins>
    implements LogisimPainter<LVC573LogisimPins>
{
  protected static final int PORT_OEB = 0;
  protected static final int PORT_D = 1;
  protected static final int PORT_LE = 2;

  protected static final int PORT_Q = 3;

  public LVC573Factory()
  {
    super(LVC573.class.getSimpleName(),
          new ComponentDescription(160, height(3),
                                   PortDescription.inputShared(PORT_OEB, "OEB"),
                                   PortDescription.inputShared(PORT_D, "D", 8).setTooltip("Input D (input)"),
                                   PortDescription.inputShared(PORT_LE, "LE"),

                                   PortDescription.blank(),
                                   PortDescription.outputShared(PORT_Q, "Q", 8).setTooltip("Output Q (output)"),
                                   PortDescription.blank()

          ));
  }

  @Override
  public void paint(LVC573LogisimPins instance, Graphics2D graphics2D)
  {
    LVC573 latch = instance.getIntegratedCircuit();
    drawField(graphics2D, getTopOffset(0), WIDTH_8BIT, "Value:", latch.getValueString(), true);
  }

  @Override
  protected LVC573LogisimPins createInstance()
  {
    LVC573LogisimPins pins = new LVC573LogisimPins();
    new LVC573("", pins);
    return pins;
  }
}

