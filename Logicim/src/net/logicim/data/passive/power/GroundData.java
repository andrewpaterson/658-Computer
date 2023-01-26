package net.logicim.data.passive.power;

import net.logicim.common.type.Int2D;
import net.logicim.data.integratedcircuit.common.PassiveData;
import net.logicim.data.port.MultiPortData;
import net.logicim.data.wire.TraceLoader;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.simulation.CircuitEditor;
import net.logicim.ui.simulation.component.passive.power.GroundProperties;
import net.logicim.ui.simulation.component.passive.power.GroundView;

import java.util.List;

public class GroundData
    extends PassiveData<GroundView>
{
  public GroundData()
  {
  }

  public GroundData(Int2D position,
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
}

