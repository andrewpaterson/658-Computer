package net.logicim.domain.integratedcircuit.standard.counter;

import net.logicim.domain.Simulation;
import net.logicim.domain.common.Component;
import net.logicim.domain.common.IntegratedCircuit;
import net.logicim.domain.common.Timeline;
import net.logicim.domain.common.port.LogicPort;
import net.logicim.domain.common.wire.TraceValue;
import net.logicim.domain.passive.subcircuit.SubcircuitSimulation;

import static net.common.util.StringUtil.getByteStringHex;

public class Counter
    extends IntegratedCircuit<CounterPins, CounterState>
    implements Component
{
  public static final String TYPE = "Counter";

  protected int bitWidth;
  protected int terminalValue;

  public Counter(SubcircuitSimulation containingSubcircuitSimulation,
                 String name,
                 CounterPins pins,
                 int bitWidth,
                 int terminalValue)
  {
    super(containingSubcircuitSimulation,
          name,
          pins);
    this.bitWidth = bitWidth;
    this.terminalValue = terminalValue;
  }

  @Override
  public void simulationStarted(Simulation simulation)
  {
  }

  @Override
  public void inputTransition(Timeline timeline, LogicPort port)
  {
    boolean resetLow = pins.getReset().readValue(timeline).isLow();
    boolean loadLow = pins.getLoad().readValue(timeline).isLow();
    boolean countEnable = pins.getCountEnable().readValue(timeline).isHigh();
    boolean countEnableTerminal = pins.getCountEnableTerminal().readValue(timeline).isHigh();
    int oldState = state.getState();
    if (resetLow)
    {
      state.setState(0);
    }
    else if (loadLow)
    {
      int data = pins.readData(timeline, bitWidth);
      state.setState(data);
    }
    else if (pins.isClock(port))
    {
      if (countEnable && countEnableTerminal)
      {
        TraceValue clockValue = port.readValue(timeline);
        if (clockValue.isHigh())
        {
          state.count(terminalValue);
        }
      }
    }

    int state = this.state.getState();
    if (oldState != state)
    {
      pins.writeOutput(timeline, state, bitWidth);
    }

    boolean oldTerminal = this.state.getTerminal();
    boolean terminal = false;
    if ((state == terminalValue) && countEnableTerminal)
    {
      terminal = true;
    }
    this.state.setTerminal(terminal);
    if (oldTerminal != terminal)
    {
      pins.getTerminalOutput().writeBool(timeline, terminal);
    }
  }

  @Override
  public String getType()
  {
    return TYPE;
  }

  @Override
  public CounterState createState()
  {
    return new CounterState();
  }

  public String getCounterValueHex()
  {
    return getByteStringHex(state.getState());
  }
}

