package net.logisim.integratedcircuits.ti.hc147;

import net.integratedcircuits.ti.hc147.HC147;
import net.logisim.common.*;

import java.awt.*;

import static net.logisim.common.PortPosition.LEFT;
import static net.logisim.common.PortPosition.RIGHT;

public class HC147Factory
    extends PropagatingInstanceFactory<HC147LogisimPins>
    implements LogisimPainter<HC147LogisimPins>
{
  protected static LogiBus PORT_INPUT;

  protected static LogiBus PORT_OUTPUT;

  public static HC147Factory create()
  {
    PortFactory factory = new PortFactory();

    PORT_INPUT = factory.inputShared("I", 9, LEFT).setInverting().setDrawBar().createBus(32);

    PORT_OUTPUT = factory.outputShared("Y", 4, RIGHT).setInverting().setDrawBar().createBus(32);

    return new HC147Factory(new ComponentDescription(HC147.class.getSimpleName(),
                                                     HC147.TYPE,
                                                     160, ComponentDescription.height(3),
                                                     factory.getPorts(),
                                                     factory.getCommonPortNames()));
  }

  protected HC147Factory(ComponentDescription description)
  {
    super(description);
  }

  @Override
  protected HC147LogisimPins createInstance()
  {
    HC147LogisimPins pins = new HC147LogisimPins();
    new HC147("", pins);
    return pins;
  }

  @Override
  public void paint(HC147LogisimPins instance, Graphics2D graphics2D)
  {
  }
}

