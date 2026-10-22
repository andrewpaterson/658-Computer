package net.logicim;

import net.logicim.data.editor.EditorData;
import net.logicim.domain.CircuitSimulation;
import net.logicim.domain.common.propagation.FamilyVoltageConfigurationStore;
import net.logicim.file.reader.LogicimFileReader;
import net.logicim.ui.simulation.CircuitEditor;

import java.io.File;

import static net.logicim.file.writer.ReflectiveWriter.EDITOR_DATA_TAG_NAME;
import static net.logicim.file.writer.ReflectiveWriter.LOGICIM_DOC_NAME;

public class Subcircuit4Runner
{
  public static void main(String[] args)
  {
    FamilyVoltageConfigurationStore.getInstance();

    File file = new File("Circuits/Subcircuit4.logic");
    EditorData editorData = (EditorData) new LogicimFileReader(LOGICIM_DOC_NAME, EDITOR_DATA_TAG_NAME).load(file);

    CircuitEditor circuitEditor = new CircuitEditor();
    circuitEditor.load(editorData.circuit);

    CircuitSimulation circuitSimulation = circuitEditor.getCurrentCircuitSimulation();
    long runTimeStep = editorData.runTimeStep;
    int steps = 1000;

    System.out.println("Loaded [" + file.getPath() + "].  Running " + steps + " steps of [" + runTimeStep + "ns].");
    for (int i = 0; i < steps; i++)
    {
      circuitEditor.runToTime(runTimeStep);
    }
    System.out.println("Simulation time [" + circuitSimulation.getTime() + "ns].");
  }
}
