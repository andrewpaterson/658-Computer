package net.logicim.domain.common;

import net.logicim.common.util.StringUtil;
import net.logicim.domain.Simulation;
import net.logicim.domain.common.port.Port;
import net.logicim.domain.common.state.State;
import net.logicim.domain.integratedcircuit.standard.clock.ClockOscillatorState;

import java.util.List;

public abstract class IntegratedCircuit<PINS extends Pins, STATE extends State>
{
  protected Circuit circuit;
  protected PINS pins;
  protected STATE state;
  protected String name;

  public IntegratedCircuit(Circuit circuit, String name, PINS pins)
  {
    this.circuit = circuit;
    this.name = name;
    this.pins = pins;
    this.pins.setIntegratedCircuit(this);
    this.state = null;

    circuit.add(this);
  }

  public PINS getPins()
  {
    return pins;
  }

  public String getName()
  {
    return name;
  }

  public String getDescription()
  {
    if (StringUtil.isEmptyOrNull(name))
    {
      return getType();
    }
    else
    {
      return getType() + " \"" + name + "\"";
    }
  }

  public void clockChanged(Simulation simulation)
  {
  }

  public boolean isStateless()
  {
    if (state != null)
    {
      return state.isStateless();
    }
    else
    {
      return false;
    }
  }

  public void setState(State state)
  {
    this.state = (STATE) state;
  }

  public abstract State simulationStarted(Simulation simulation);

  public abstract void inputTraceChanged(Simulation simulation, List<Port> updatedPorts);

  public abstract String getType();
}

