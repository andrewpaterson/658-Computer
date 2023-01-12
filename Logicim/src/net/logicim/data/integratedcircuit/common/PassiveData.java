package net.logicim.data.integratedcircuit.common;

import net.logicim.common.type.Int2D;
import net.logicim.data.port.MultiPortData;
import net.logicim.data.port.PortData;
import net.logicim.data.wire.TraceLoader;
import net.logicim.domain.common.port.Port;
import net.logicim.ui.simulation.CircuitEditor;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.integratedcircuit.ComponentView;
import net.logicim.ui.common.integratedcircuit.PassiveView;

import java.util.List;

public abstract class PassiveData<PASSIVE extends PassiveView<?, ?>>
    extends ComponentData
{
  public PassiveData()
  {
  }

  public PassiveData(Int2D position,
                     Rotation rotation,
                     String name,
                     List<MultiPortData> ports,
                     boolean selected)
  {
    super(position,
          rotation,
          name,
          ports,
          selected);
  }

  @Override
  protected void connectAndLoad(CircuitEditor circuitEditor, TraceLoader traceLoader, ComponentView<?> componentView)
  {
    PASSIVE passive = (PASSIVE) componentView;
    circuitEditor.createConnectionViews(passive);
    loadPorts(circuitEditor, traceLoader, passive);
  }

  @Override
  protected void loadPort(CircuitEditor circuitEditor, PortData portData, Port port)
  {
  }
}

