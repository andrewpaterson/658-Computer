package net.logicim;


import net.logicim.domain.SimulationTest;
import net.logicim.domain.common.event.EventPropagationTest;
import net.logicim.domain.common.propagation.FamilyVoltageConfigurationTest;
import net.logicim.ui.editor.InternationalUnitsTest;

public class LogicimTest
{
  public static void main(String[] args)
  {
    InternationalUnitsTest.test();
    FamilyVoltageConfigurationTest.test();
    SimulationTest.test();
    EventPropagationTest.test();
  }
}

