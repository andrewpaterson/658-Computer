package net.logicim;


import net.common.geometry.LineMinimiserTest;
import net.common.geometry.LineSplitterTest;
import net.logicim.domain.SimulationTest;
import net.logicim.domain.common.event.EventPropagationTest;
import net.logicim.domain.common.propagation.FamilyVoltageConfigurationTest;
import net.logicim.file.writer.ReflectiveWriterTest;
import net.logicim.ui.editor.InternationalUnitsTest;

public class LogicimTest
{
  public static void main(String[] args)
  {
    InternationalUnitsTest.test();
    FamilyVoltageConfigurationTest.test();
    LineMinimiserTest.test();
    LineSplitterTest.test();
    ReflectiveWriterTest.test();
    SimulationTest.test();
    EventPropagationTest.test();
  }
}

