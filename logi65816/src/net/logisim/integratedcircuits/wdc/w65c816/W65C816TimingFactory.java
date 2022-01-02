package net.logisim.integratedcircuits.wdc.w65c816;

import com.cburch.logisim.instance.InstancePainter;
import com.cburch.logisim.instance.InstanceState;
import net.logisim.common.*;

import java.awt.*;

import static net.logisim.common.PortPosition.LEFT;
import static net.logisim.common.PortPosition.RIGHT;

public class W65C816TimingFactory
    extends SimpleInstanceFactory
{
  public static final String TYPE = "Helper";

  protected static LogiPin phi2;
  protected static LogiPin clock;
  protected static LogiBus timingOut1;
  protected static LogiBus timingOut2;

  protected static PairedLogiBus execute;
  protected static PairedLogiBus address;
  protected static PairedLogiBus readData;
  protected static PairedLogiBus writeData;
  protected static PairedLogiBus bankAddress;
  protected static PairedLogiBus interrupts;
  protected static PairedLogiBus abort;
  protected static PairedLogiBus m;
  protected static PairedLogiBus x;
  protected static PairedLogiBus e;

  public W65C816TimingFactory(ComponentDescription description)
  {
    super(description);
  }

  public static W65C816TimingFactory create()
  {
    PortFactory factory = new PortFactory();

    phi2 = factory.inputShared("PHI2", LEFT).createPin(1);
    timingOut1 = factory.outputShared("T1 (Out)", 40, RIGHT).createBus(2);
    factory.getCommonPortNames().add("");

    execute = factory.pairedInputShared("== Read", "Execute", "Write ==", 4, 1);
    address = factory.pairedInputShared(">=", "A0-A15, VDA, VPA, RWB, MLB, VPB", "<=", 4, 1);
    readData = factory.pairedInputShared(">=", "Read D0-D7", "<=", 4, 1);
    writeData = factory.pairedInputShared(">=", "Write D0-D7", "<=", 4, 1);
    bankAddress = factory.pairedInputShared(">=", "BA0-BA7", "<=", 4, 1);
    interrupts = factory.pairedInputShared(">=", "IRQB, NMIB, RESB, RDY", "<=", 4, 1);
    abort = factory.pairedInputShared(">=", "ABORTB", "<=", 4, 1);
    m = factory.pairedInputShared(">=", "M", "<=", 4, 1);
    x = factory.pairedInputShared(">=", "X", "<=", 4, 1);
    e = factory.pairedInputShared(">=", "E", "<=", 4, 1);

    clock = factory.inputShared("CLK", LEFT).createPin(1);
    timingOut2 = factory.outputShared("T2 (Out)", 40, RIGHT).createBus(2);
    factory.getCommonPortNames().add("");

    return new W65C816TimingFactory(new ComponentDescription("W65C816 Timing",
                                                             TYPE,
                                                             240,
                                                             240,
                                                             factory.getPorts(),
                                                             factory.getCommonPortNames()));
  }

  @Override
  public void paintInstance(InstancePainter painter)
  {
    Graphics g = painter.getGraphics();
    if (g instanceof Graphics2D)
    {
      SimpleInstancePainter.paintInstance(painter, description, true);
    }
  }

  @Override
  public void propagate(InstanceState instanceState)
  {
    W65C816TimingInstance instance = W65C816TimingInstance.getOrCreate(instanceState);
    instance.tick(instanceState);
  }
}

