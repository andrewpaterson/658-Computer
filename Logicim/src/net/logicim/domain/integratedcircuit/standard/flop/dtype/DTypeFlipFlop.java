package net.logicim.domain.integratedcircuit.standard.flop.dtype;

import net.logicim.domain.Simulation;
import net.logicim.domain.common.Component;
import net.logicim.domain.common.IntegratedCircuit;
import net.logicim.domain.common.Timeline;
import net.logicim.domain.common.port.LogicPort;
import net.logicim.domain.common.wire.TraceValue;
import net.logicim.domain.passive.subcircuit.SubcircuitSimulation;

public class DTypeFlipFlop
    extends IntegratedCircuit<DTypeFlipFlopPins, DTypeFlipFlopState>
    implements Component
{
  public static final String TYPE = "D-type flip flop";

  public DTypeFlipFlop(SubcircuitSimulation containingSubcircuitSimulation,
                       String name,
                       DTypeFlipFlopPins pins)
  {
    super(containingSubcircuitSimulation,
          name,
          pins);
  }

  @Override
  public void simulationStarted(Simulation simulation)
  {
  }

  @Override
  public void inputTransition(Timeline timeline, LogicPort port)
  {
    boolean stateChanged = false;
    boolean setLow = pins.getSet().readValue(timeline).isLow();
    boolean resetLow = pins.getReset().readValue(timeline).isLow();
    if (setLow && resetLow)
    {
      //most likely for LVC.  Could be metastable also
      stateChanged = state.setState(true);
    }
    else if (!setLow && !resetLow)
    {
      if (pins.isClock(port))
      {
        TraceValue clockValue = port.readValue(timeline);
        if (clockValue.isHigh())
        {
          TraceValue dataValue = pins.getData().readValue(timeline);
          if (dataValue.isHigh())
          {
            stateChanged = state.setState(true);
          }
          else if (dataValue.isLow())
          {
            stateChanged = state.setState(false);
          }
        }
      }
    }
    else if (setLow)
    {
      stateChanged = state.setState(true);
    }
    else if (resetLow)
    {
      stateChanged = state.setState(false);
    }

    if (stateChanged)
    {
      pins.getOutputQ().writeBool(timeline, state.getState());
      pins.getOutputQB().writeBool(timeline, !state.getState());
    }
  }

  @Override
  public String getType()
  {
    return TYPE;
  }

  @Override
  public DTypeFlipFlopState createState()
  {
    return new DTypeFlipFlopState();
  }
}

