package net.logicim.domain.integratedcircuit.standard.constant;

import net.logicim.domain.Simulation;
import net.logicim.domain.common.IntegratedCircuit;
import net.logicim.domain.common.Timeline;
import net.logicim.domain.common.port.LogicPort;
import net.logicim.domain.passive.subcircuit.SubcircuitSimulation;

import java.util.List;

public class Constant
    extends IntegratedCircuit<ConstantPins, ConstantState>
{
  public static final String TYPE = "Constant";

  protected long defaultValue;

  public Constant(SubcircuitSimulation containingSubcircuitSimulation,
                  String name,
                  ConstantPins pins,
                  int defaultValue)
  {
    super(containingSubcircuitSimulation,
          name,
          pins);
    this.defaultValue = defaultValue;
  }

  @Override
  public void inputTransition(Timeline timeline, LogicPort port)
  {
  }

  @Override
  public void executeTick(Simulation simulation)
  {
  }

  @Override
  public String getType()
  {
    return TYPE;
  }

  @Override
  public ConstantState createState()
  {
    return new ConstantState(defaultValue);
  }

  @Override
  public void simulationStarted(Simulation simulation)
  {
    //This should work but as the Constant is placed it is disconnected and then reconnected.
    //Disconnecting it causes the slew event to be deleted from each port.
    List<LogicPort> outputs = getPins().getOutputs();
    Timeline timeline = simulation.getTimeline();
    long constantValue = state.getConstantValue();
    for (LogicPort output : outputs)
    {
      output.writeBool(timeline, (constantValue & 1) == 1);
      constantValue >>= 1;
    }
  }
}

