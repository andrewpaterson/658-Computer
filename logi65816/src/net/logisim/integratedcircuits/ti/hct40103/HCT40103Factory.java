package net.logisim.integratedcircuits.ti.hct40103;

import net.integratedcircuits.ti.hct40103.HCT40103;
import net.logisim.common.*;

import java.awt.*;

import static net.logisim.common.PortPosition.LEFT;
import static net.logisim.common.PortPosition.RIGHT;

public class HCT40103Factory
    extends PropagatingInstanceFactory<HCT40103LogisimPins>
    implements LogisimPainter<HCT40103LogisimPins>
{
  protected static LogiPin PORT_CP;
  protected static LogiPin PORT_MRB;
  protected static LogiPin PORT_PLB;
  protected static LogiPin PORT_PEB;
  protected static LogiPin PORT_TE;

  protected static LogiPin PORT_TCB;
  protected static LogiBus PORT_P;

  public static HCT40103Factory create()
  {
    PortFactory factory = new PortFactory();

    PORT_MRB = factory.inputShared("MR", LEFT).setInverting().setDrawBar().createPin(20);
    PORT_PLB = factory.inputShared("PL", LEFT).setInverting().setDrawBar().setTooltip("Parallel load from P asynchronously (input)").createPin(20);
    PORT_PEB = factory.inputShared("PE", LEFT).setInverting().setDrawBar().setTooltip("Parallel load from P on next CP (input)").createPin(20);
    PORT_TE = factory.inputShared("TE", LEFT).setTooltip("Inhibit counting").createPin(20);
    PORT_CP = factory.inputShared("CP", LEFT).setTooltip("Up Count Clock, low to high triggered (input)").createPin(20);

    PORT_P = factory.inputShared("P", 8, RIGHT).createBus(20);
    PORT_TCB = factory.inputShared("TC", RIGHT).setTooltip("Borrow (output)").setInverting().setDrawBar().createPin(20);

    return new HCT40103Factory(new ComponentDescription(HCT40103.class.getSimpleName(),
                                                        HCT40103.TYPE,
                                                        160,
                                                        factory.getPorts(),
                                                        factory.getCommonPortNames()));
  }

  protected HCT40103Factory(ComponentDescription description)
  {
    super(description);
  }

  @Override
  public void paint(HCT40103LogisimPins instance, Graphics2D graphics2D)
  {
    HCT40103 counter = instance.getIntegratedCircuit();
    drawField(graphics2D, getTopOffset(0), WIDTH_8BIT, "Value:", counter.getCounterValueString(), true);
  }

  @Override
  protected HCT40103LogisimPins createInstance()
  {
    HCT40103LogisimPins pins = new HCT40103LogisimPins();
    new HCT40103("", pins);
    return pins;
  }
}

