package net.logicim.domain.integratedcircuit.sevenfour.x74;

import net.logicim.domain.Simulation;
import net.logicim.domain.common.Component;
import net.logicim.domain.common.IntegratedCircuit;
import net.logicim.domain.common.Timeline;
import net.logicim.domain.common.port.LogicPort;
import net.logicim.domain.passive.subcircuit.SubcircuitSimulation;

public class SevenFourX74
    extends IntegratedCircuit<SevenFourX74Pins, SevenFourX74State>
    implements Component
{
  public static final String TYPE = "74x74";

  public SevenFourX74(SubcircuitSimulation containingSubcircuitSimulation,
                      String name,
                      SevenFourX74Pins pins)
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

  }

  @Override
  public String getType()
  {
    return null;
  }

  @Override
  public SevenFourX74State createState()
  {
    return new SevenFourX74State();
  }
}

