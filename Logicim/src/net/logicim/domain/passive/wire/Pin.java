package net.logicim.domain.passive.wire;

import net.logicim.domain.Simulation;
import net.logicim.domain.common.Circuit;
import net.logicim.domain.common.port.Port;
import net.logicim.domain.common.port.PortType;
import net.logicim.domain.common.port.PowerInPort;
import net.logicim.domain.common.port.TracePort;
import net.logicim.domain.passive.common.Passive;

import java.util.ArrayList;
import java.util.List;

import static net.logicim.domain.passive.power.PowerPinNames.GND;
import static net.logicim.domain.passive.power.PowerPinNames.VCC;

public class Pin
    extends Passive
{
  protected List<TracePort> tracePorts;
  protected PowerInPort vcc;
  protected PowerInPort vss;

  public Pin(Circuit circuit, String name, int portCount)
  {
    super(circuit, name);
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

  public float getGND(long time)
  {
    return vss.getVoltageIn(time);
  }

  public PowerInPort getVoltageGround()
  {
    return vss;
  }

  public List<TracePort> getTracePorts()
  {
    return tracePorts;
  }
}

