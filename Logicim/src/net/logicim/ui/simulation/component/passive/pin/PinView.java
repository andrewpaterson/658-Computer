package net.logicim.ui.simulation.component.passive.pin;

import net.logicim.common.type.Int2D;
import net.logicim.data.integratedcircuit.common.PassiveData;
import net.logicim.data.passive.wire.PinData;
import net.logicim.domain.Simulation;
import net.logicim.domain.common.Circuit;
import net.logicim.domain.passive.wire.Pin;
import net.logicim.ui.circuit.SubcircuitView;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.integratedcircuit.PassiveView;
import net.logicim.ui.common.port.PortView;

public class PinView
    extends PassiveView<Pin, PinProperties>
{
  protected PortView port;

  public PinView(SubcircuitView subcircuitView,
                 Circuit circuit,
                 Int2D position,
                 Rotation rotation,
                 PinProperties properties)
  {
    super(subcircuitView, circuit, position, rotation, properties);
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
  public void clampProperties(PinProperties newProperties)
  {
  }

  @Override
  public void simulationStarted(Simulation simulation)
  {

  }

  @Override
  protected Pin createPassive(Circuit circuit)
  {
    return new Pin(circuit,
                   properties.name,
                   properties.bitWidth);
  }
}

