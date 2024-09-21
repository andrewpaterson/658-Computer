package net.logicim.ui.editor;

import net.common.type.Int2D;
import net.logicim.data.family.Family;
import net.logicim.domain.common.Units;
import net.logicim.domain.common.propagation.FamilyVoltageConfiguration;
import net.logicim.domain.common.propagation.FamilyVoltageConfigurationStore;
import net.logicim.ui.circuit.SubcircuitView;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.simulation.CircuitEditor;
import net.logicim.ui.simulation.component.integratedcircuit.standard.clock.ClockProperties;
import net.logicim.ui.simulation.component.integratedcircuit.standard.clock.ClockView;
import net.logicim.ui.simulation.component.integratedcircuit.standard.clock.ClockViewFactory;
import net.logicim.ui.simulation.subcircuit.SubcircuitEditor;

public class CircuitEditorTest
{
  public static void test()
  {
    FamilyVoltageConfigurationStore.getInstance();

    CircuitEditor circuitEditor = new CircuitEditor("Main");
    SubcircuitEditor subcircuitEditor = circuitEditor.getCurrentSubcircuitEditor();
    SubcircuitView subcircuitView = subcircuitEditor.getCircuitSubcircuitView();
    FamilyVoltageConfiguration voltageConfiguration = FamilyVoltageConfigurationStore.get("LVC");
    Family family = voltageConfiguration.getFamily();
    ClockView clockView = new ClockViewFactory().create(circuitEditor,
                                                        subcircuitView,
                                                        new Int2D(0, 0),
                                                        Rotation.North,
                                                        new ClockProperties("",
                                                                            family,
                                                                            false,
                                                                            100 * Units.MHz,
                                                                            false));
    circuitEditor.runSimultaneous();
  }

  public static void main(String[] args)
  {
    CircuitEditorTest.test();
  }
}

