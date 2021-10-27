package net.nexperia.logisim;

import net.logisim.common.ComponentDescription;
import net.logisim.common.LogisimFactory;
import net.logisim.common.PortDescription;
import net.nexperia.lvc4245.LVC4245;
import net.wdc.wdc65816.WDC65816;

import java.awt.*;

public class LVC4245Factory
    extends LogisimFactory<LVC4245LogisimPins>
{
  protected static final int PORT_DIR = 2;
  protected static final int PORT_A = 3;
  protected static final int PORT_B = 1;
  protected static final int PORT_OEB = 0;

  public LVC4245Factory()
  {
    super("LVC4245",
          new ComponentDescription(140, 60, 10, true,
                                   PortDescription.inputShared(PORT_OEB, "OEB").setTooltip("Output enable (input: A and B high impedance high, A and B enable low)"),
                                   PortDescription.inoutShared(PORT_B, "B", 8).setTooltip("Data B (input or output)"),
                                   PortDescription.inputShared(PORT_DIR, "DIR").setTooltip("Direction (input: B to A low, A to B high)"),

                                   PortDescription.blank(),
                                   PortDescription.inoutShared(PORT_A, "A", 8).setTooltip("Data A (input or output)"),
                                   PortDescription.blank()

          ));
  }

  @Override
  protected void paint(LVC4245LogisimPins instance, Graphics2D graphics2D)
  {
    LVC4245 transceiver = instance.getIntegratedCircuit();
    drawField(graphics2D, TOP_OFFSET, WIDTH_8BIT, "Dir:", transceiver.getDirectionString(), transceiver.isDirectionValid());
  }

  @Override
  protected LVC4245LogisimPins createInstance()
  {
    LVC4245LogisimPins pins = new LVC4245LogisimPins();
    new LVC4245("", pins);
    return pins;
  }
}

