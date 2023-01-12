package net.logicim.ui.simulation.component.passive.pin;

import net.logicim.common.type.Int2D;
import net.logicim.data.integratedcircuit.common.PassiveData;
import net.logicim.data.passive.wire.PinData;
import net.logicim.domain.Simulation;
import net.logicim.domain.passive.wire.Pin;
import net.logicim.ui.simulation.CircuitEditor;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.integratedcircuit.PassiveView;
import net.logicim.ui.common.port.PortView;

public class PinView
    extends PassiveView<Pin, PinProperties>
{
  private PortView port;

  public PinView(CircuitEditor circuitEditor,
                 Int2D position,
                 Rotation rotation,
                 PinProperties properties)
  {
    super(circuitEditor, position, rotation, properties);
  }

  @Override
  public String getType()
  {
    return "Pin";
  }

  @Override
  protected void createPortViews()
  {
    port = new PortView(this, passive.getPorts(), new Int2D(0, 0));
  }

  @Override
  public PassiveData<?> save(boolean selected)
  {
    return new PinData(position,
                       rotation,
                       getName(),
                       savePorts(),
                       selected,
                       properties.bitWidth);
  }

  @Override
  public void clampProperties()
  {

  }

  @Override
  public void simulationStarted(Simulation simulation)
  {

  }

  @Override
  protected Pin createPassive()
  {
    return new Pin(circuitEditor.getCircuit(),
                   properties.name,
                   properties.bitWidth);
  }
}

