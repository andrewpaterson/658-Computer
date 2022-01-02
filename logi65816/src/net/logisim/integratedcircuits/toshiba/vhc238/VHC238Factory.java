package net.logisim.integratedcircuits.toshiba.vhc238;

import net.integratedcircuits.toshiba.vhc238.VHC238;
import net.logisim.common.*;

import java.awt.*;

import static net.logisim.common.PortPosition.LEFT;
import static net.logisim.common.PortPosition.RIGHT;

public class VHC238Factory
    extends PropagatingInstanceFactory<VHC238LogisimPins>
    implements LogisimPainter<VHC238LogisimPins>
{
  protected static LogiBus PORT_ABC;
  protected static LogiPin PORT_G2A;
  protected static LogiPin PORT_G2B;
  protected static LogiPin PORT_G1;
  protected static LogiBus PORT_Y;

  public static VHC238Factory create()
  {
    PortFactory factory = new PortFactory();

    PORT_G2A = factory.inputShared("G2A", LEFT).setInverting().setDrawBar().createPin(3);
    PORT_G2B = factory.inputShared("G2B", LEFT).setInverting().setDrawBar().createPin(3);
    PORT_ABC = factory.inputShared("ABC", 3, LEFT).setTooltip("Input ABC (input)").createBus(3);
    PORT_G1 = factory.inputShared("G1", LEFT).createPin(3);

    factory.blank(RIGHT);
    PORT_Y = factory.outputShared("Y", 8, RIGHT).setInverting().setDrawBar().createBus(3);

    return new VHC238Factory(new ComponentDescription(VHC238.class.getSimpleName(),
                                                      VHC238.TYPE,
                                                      160,
                                                      factory.getPorts(),
                                                      factory.getCommonPortNames()));
  }

  protected VHC238Factory(ComponentDescription description)
  {
    super(description);
  }

  @Override
  protected VHC238LogisimPins createInstance()
  {
    VHC238LogisimPins pins = new VHC238LogisimPins();
    new VHC238("", pins);
    return pins;
  }

  @Override
  public void paint(VHC238LogisimPins instance, Graphics2D graphics2D)
  {
  }
}

