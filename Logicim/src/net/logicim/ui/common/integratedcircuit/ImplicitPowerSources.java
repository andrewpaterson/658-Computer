package net.logicim.ui.common.integratedcircuit;

import net.logicim.domain.common.Circuit;
import net.logicim.domain.common.port.Port;
import net.logicim.domain.common.port.PortHolder;
import net.logicim.domain.common.wire.Trace;
import net.logicim.domain.passive.power.PowerSource;

import java.util.ArrayList;

//  Components without explicit power ports are given a private Power Source on each of their power ports when they
//  are created.  Those Power Sources must be removed from the Circuit when the component is destroyed.
public class ImplicitPowerSources
{
  public static void remove(Circuit circuit, Port powerInPort)
  {
    Trace trace = powerInPort.getTrace();
    if (trace != null)
    {
      for (Port port : new ArrayList<>(trace.getConnectedPorts()))
      {
        PortHolder holder = port.getHolder();
        if (holder instanceof PowerSource)
        {
          circuit.remove((PowerSource) holder);
        }
      }
    }
  }
}
