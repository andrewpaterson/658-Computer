package net.logicim.data.passive.wire;

import net.logicim.common.type.Int2D;
import net.logicim.data.integratedcircuit.common.PassiveData;
import net.logicim.data.port.MultiPortData;
import net.logicim.data.wire.TraceLoader;
import net.logicim.domain.common.Circuit;
import net.logicim.ui.circuit.SubcircuitView;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.simulation.component.passive.pin.PinProperties;
import net.logicim.ui.simulation.component.passive.pin.PinView;

import java.util.List;

public class PinData
    extends PassiveData<PinView>
{
  protected int bitWidth;

  public PinData()
  {
  }

  public PinData(Int2D position,
                 Rotation rotation,
                 String name,
                 List<MultiPortData> ports,
                 boolean selected,
                 int bitWidth)
  {
    super(position, rotation, name, ports, selected);
    this.bitWidth = bitWidth;
  }

  @Override
  protected PinView create(SubcircuitView subcircuitView, Circuit circuit, TraceLoader traceLoader)
  {
    return new PinView(subcircuitView,
                       circuit,
                       position,
                       rotation,
                       new PinProperties(name,
                                         bitWidth));
  }
}

