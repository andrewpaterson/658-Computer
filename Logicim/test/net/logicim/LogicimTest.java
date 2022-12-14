package net.logicim;


import net.logicim.domain.SimulationTest;
import net.logicim.domain.common.event.EventPropagationTest;
import net.logicim.domain.common.propagation.FamilyVoltageConfigurationTest;

public class LogicimTest
{
  public static void main(String[] args)
  {
    FamilyVoltageConfigurationTest.test();
    SimulationTest.test();
    EventPropagationTest.test();
  }
}

