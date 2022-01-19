package net.logisim.integratedcircuits.toshiba.vhc161;

import net.integratedcircuits.toshiba.vhc161.VHC161;
import net.logisim.common.*;

import java.awt.*;

import static net.logisim.common.PortPosition.LEFT;
import static net.logisim.common.PortPosition.RIGHT;

public class VHC161Factory
    extends PropagatingInstanceFactory<VHC161LogisimPins>
    implements LogisimPainter<VHC161LogisimPins>
{
  protected static LogiBus PORT_ABCD;
  protected static LogiPin PORT_LOAD;
  protected static LogiPin PORT_CLR;
  protected static LogiPin PORT_CK;
  protected static LogiPin PORT_ENP;
  protected static LogiPin PORT_ENT;
  protected static LogiPin PORT_CO;

  protected static LogiBus PORT_QABCD;

  public static VHC161Factory create()
  {
    PortFactory factory = new PortFactory();

    PORT_CLR = factory.inputShared("CLR", LEFT).setInverting().setDrawBar().createPin(4);
    PORT_LOAD = factory.inputShared("LOAD", LEFT).setInverting().setDrawBar().createPin(4);
    PORT_ABCD = factory.inputShared("ABCD", 4, LEFT).setTooltip("Input A, B, C, D (input)").createBus(4);
    PORT_CK = factory.inputShared("CK", LEFT).setTooltip("Clock, low to high triggered (input)").createPin(4);
    PORT_ENT = factory.inputShared("ENT", LEFT).setTooltip("Count enable carry (input: enable high)").createPin(4);
    PORT_ENP = factory.inputShared("ENP", LEFT).setTooltip("Count enable (input: enable high)").createPin(4);

    PORT_QABCD = factory.outputShared("QABCD", 4, RIGHT).setTooltip("Output QA, QB, QC, QD (output)").createBus(4);
    PORT_CO = factory.inputShared("CO", RIGHT).setTooltip("Carry (output)").createPin(4);

    return new VHC161Factory(new ComponentDescription(VHC161.class.getSimpleName(),
                                                      VHC161.TYPE,
                                                      160,
                                                      factory.getPorts(),
                                                      factory.getCommonPortNames()));
  }

  protected VHC161Factory(ComponentDescription description)
  {
    super(description);
  }

  @Override
  public void paint(VHC161LogisimPins instance, Graphics2D graphics2D)
  {
    VHC161 latch = instance.getIntegratedCircuit();
    drawField(graphics2D, getTopOffset(0), WIDTH_8BIT, "Value:", latch.getCounterValueString(), true);
  }

  @Override
  protected VHC161LogisimPins createInstance()
  {
    VHC161LogisimPins pins = new VHC161LogisimPins();
    new VHC161("", pins);
    return pins;
  }
}

