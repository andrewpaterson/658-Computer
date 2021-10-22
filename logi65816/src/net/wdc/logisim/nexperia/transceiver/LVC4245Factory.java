package net.wdc.logisim.nexperia.transceiver;

import com.cburch.logisim.instance.InstancePainter;
import net.wdc.logisim.common.ComponentDescription;
import net.wdc.logisim.common.LogisimFactory;
import net.wdc.logisim.common.PortDescription;

public class LVC4245Factory
    extends LogisimFactory<LVC4245Pins>
{
  // Left side, top to bottom
  protected static final int PORT_DIR = 0;
  protected static final int PORT_A = 1;

  // Right side, bottom to top
  protected static final int PORT_B = 2;
  protected static final int PORT_OEB = 3;

  public LVC4245Factory()
  {
    super("LVC4245",
          new ComponentDescription(100, 60, 10,
                                   PortDescription.inputShared(PORT_DIR, "DIR").setTooltip("Direction (input: B to A low, A to B high)"),
                                   PortDescription.inputShared(PORT_A, "A").setTooltip("Data A (input or output)"),
                                   PortDescription.inputShared(PORT_B, "B").setTooltip("Data B (input or output)"),
                                   PortDescription.inputShared(PORT_OEB, "OEB").setTooltip("Output enable (input: A and B high impedance high, A and B enable low)")
          ));
  }

  @Override
  public void paintInstance(InstancePainter painter)
  {
    LVC4245Pins instance = getOrCreateInstance(painter);
    paintPorts(painter, instance);
  }

  @Override
  protected LVC4245Pins createInstance()
  {
    return new LVC4245Pins();
  }
}

