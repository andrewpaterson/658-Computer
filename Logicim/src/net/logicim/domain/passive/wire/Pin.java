package net.logicim.domain.passive.wire;

import net.logicim.domain.Simulation;
import net.logicim.domain.common.port.Port;
import net.logicim.domain.common.port.PortType;
import net.logicim.domain.common.port.PowerInPort;
import net.logicim.domain.common.port.TracePort;
import net.logicim.domain.passive.common.Passive;
import net.logicim.domain.passive.subcircuit.SubcircuitSimulation;

import java.util.ArrayList;
import java.util.List;

import static net.logicim.domain.passive.power.PowerPinNames.GND;
import static net.logicim.domain.passive.power.PowerPinNames.VCC;

public class Pin
    extends Passive
{
  protected List<TracePort> tracePorts;

  //Why do these exist?  As long as you know the VoltageConfiguration you know the high and low.
  protected PowerInPort vcc;
  protected PowerInPort vss;

  public Pin(SubcircuitSimulation containingSubcircuitSimulation,
             String name,
             int portCount)
  {
    super(containingSubcircuitSimulation, name);
    tracePorts = new ArrayList<>();
    for (int i = 0; i < portCount; i++)
    {
      String portName = "Port " + i;
      tracePorts.add(new TracePort(portName, this));
    }

    vcc = new PowerInPort(PortType.PowerIn, VCC, this);
    vss = new PowerInPort(PortType.PowerIn, GND, this);
  }

  @Override
  public String getType()
  {
    return "Pin";
  }

  @Override
  public void traceConnected(Simulation simulation, Port port)
  {
  }

  public PowerInPort getVoltageCommon()
  {
    return vcc;
  }

  public float getVCC(long time)
  {
    return vcc.getVoltageIn(time);
  }

  public PowerInPort getVoltageGround()
  {
    return vss;
  }
}

