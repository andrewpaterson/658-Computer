package net.logisim.integratedcircuits.nexperia.lvc595;

import com.cburch.logisim.comp.ComponentFactory;
import net.integratedcircuits.nexperia.lvc595.LVC595;
import net.logisim.common.*;

import java.awt.*;

import static net.logisim.common.PortPosition.LEFT;
import static net.logisim.common.PortPosition.RIGHT;

public class LVC595Factory
    extends LogisimFactory<LVC595LogisimPins>
    implements LogisimPainter<LVC595LogisimPins>
{
  protected static LogiBus PORT_Q;
  protected static LogiPin PORT_Q7S;
  protected static LogiPin PORT_MRB;
  protected static LogiPin PORT_SHCP;
  protected static LogiPin PORT_STCP;
  protected static LogiPin PORT_OEB;
  protected static LogiPin PORT_DS;

  public LVC595Factory(ComponentDescription description)
  {
    super(description);
  }

  public static ComponentFactory create()
  {
    PortFactory factory = new PortFactory();

    PORT_OEB = factory.inputShared("OE", LEFT).setTooltip("Output enable (input: low enable, high disable").setInverting().setDrawBar().createPin(4);
    PORT_MRB = factory.inputShared("MR", LEFT).setTooltip("Master reset (input: low reset)").setInverting().setDrawBar().createPin(4);
    PORT_DS = factory.inputShared("DS", LEFT).setTooltip("Serial data (input)").createPin(4);
    PORT_SHCP = factory.inputShared("SHCP", LEFT).setTooltip("Shift register clock (input: low to high edge triggered)").createPin(4);
    PORT_STCP = factory.inputShared("STCP", LEFT).setTooltip("Storage register clock (input: low to high edge triggered)").createPin(4);

    PORT_Q = factory.outputExclusive("Q", 8, RIGHT).setTooltip("Parallel data (input)").createBus(5);
    PORT_Q7S = factory.outputExclusive("Q7S", RIGHT).setTooltip("Shift register (output bit 7)").createPin(5);

    return new LVC595Factory(new ComponentDescription(LVC595.class.getSimpleName(),
                                                      LVC595.TYPE,
                                                      160,
                                                      factory.getPorts()));
  }

  @Override
  public void paint(LVC595LogisimPins instance, Graphics2D graphics2D)
  {
    LVC595 shiftRegister = instance.getIntegratedCircuit();
    drawField(graphics2D, getTopOffset(0), WIDTH_8BIT, "Shift:", shiftRegister.getShiftValueString(), true);
    drawField(graphics2D, getTopOffset(1), WIDTH_8BIT, "Storage:", shiftRegister.getStorageValueString(), true);
  }

  @Override
  protected LVC595LogisimPins createInstance()
  {
    LVC595LogisimPins pins = new LVC595LogisimPins();
    new LVC595("", pins);
    return pins;
  }
}

