package net.logicim.data.integratedcircuit.standard.power;

import net.logicim.common.type.Int2D;
import net.logicim.data.integratedcircuit.common.ComponentData;
import net.logicim.data.integratedcircuit.common.PassiveData;
import net.logicim.data.port.MultiPortData;
import net.logicim.data.port.PortData;
import net.logicim.data.trace.TraceLoader;
import net.logicim.domain.common.port.Port;
import net.logicim.ui.CircuitEditor;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.integratedcircuit.standard.power.GroundProperties;
import net.logicim.ui.integratedcircuit.standard.power.GroundView;

import java.util.List;

public class GroundPortData
    extends PassiveData
{
  public GroundPortData()
  {
  }

  public GroundPortData(Int2D position,
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

  protected GroundView create(CircuitEditor circuitEditor, TraceLoader traceLoader)
  {
    return new GroundView(circuitEditor,
                          position,
                          rotation,
                          new GroundProperties(name));
  }

  @Override
  protected void loadPort(CircuitEditor circuitEditor, PortData portData, Port port)
  {
  }
}

