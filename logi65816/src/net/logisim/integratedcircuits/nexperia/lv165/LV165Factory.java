package net.logisim.integratedcircuits.nexperia.lv165;

import com.cburch.logisim.comp.ComponentFactory;
import net.integratedcircuits.nexperia.lv165.LV165;
import net.logisim.common.*;

import java.awt.*;

import static net.logisim.common.PortPosition.LEFT;
import static net.logisim.common.PortPosition.RIGHT;

public class LV165Factory
    extends PropagatingInstanceFactory<LV165LogisimPins>
    implements LogisimPainter<LV165LogisimPins>
{
  protected static LogiBus PORT_D;
  protected static LogiPin PORT_PLB;
  protected static LogiPin PORT_CP;
  protected static LogiPin PORT_CEB;
  protected static LogiPin PORT_DS;
  protected static LogiPin PORT_Q;
  protected static LogiPin PORT_QB;

  public LV165Factory(ComponentDescription description)
  {
    super(description);
  }

  public static ComponentFactory create()
  {
    PortFactory factory = new PortFactory();

    PORT_CEB = factory.inputShared("CE", LEFT).setTooltip("Clock enable (input: low active, high hold)").setInverting().setDrawBar().createPin(9);
    PORT_CP = factory.inputShared("CP", LEFT).setTooltip("Clock (input: low to high edge triggered)").createPin(9);
    PORT_PLB = factory.inputShared("PL", LEFT).setTooltip("Parallel load (input: D load low, DS load high").setInverting().setDrawBar().createPin(8);
    PORT_D = factory.inputShared("D", 8, LEFT).setTooltip("Parallel data (input)").createBus(9);
    PORT_DS = factory.inputShared("DS", LEFT).setTooltip("Serial data (input)").createPin(9);

    PORT_Q = factory.outputExclusive("Q", RIGHT).setTooltip("Serial data (output from last stage)").createPin(5);
    PORT_QB = factory.outputExclusive("Q", RIGHT).setTooltip("Complementary serial data (inverse output from last stage)").setInverting().setDrawBar().createPin(5);

    return new LV165Factory(new ComponentDescription(LV165.class.getSimpleName(),
                                                     LV165.TYPE,
                                                     160,
                                                     factory.getPorts(),
                                                     factory.getCommonPortNames()));
  }

  @Override
  public void paint(LV165LogisimPins instance, Graphics2D graphics2D)
  {
    LV165 shiftRegister = instance.getIntegratedCircuit();
    drawField(graphics2D, getTopOffset(0), WIDTH_8BIT, "Value:", shiftRegister.getValueString(), true);
  }

  @Override
  protected LV165LogisimPins createInstance()
  {
    LV165LogisimPins pins = new LV165LogisimPins();
    new LV165("", pins);
    return pins;
  }
}

