package net.logicim.domain.common.event;

import net.logicim.assertions.PortSmoothVoltage;
import net.logicim.assertions.SmoothVoltage;
import net.logicim.assertions.Validator;
import net.logicim.data.circuit.CircuitData;
import net.logicim.data.editor.EditorData;
import net.logicim.domain.Simulation;
import net.logicim.domain.common.Circuit;
import net.logicim.domain.common.port.LogicPort;
import net.logicim.domain.common.voltage.Voltage;
import net.logicim.domain.integratedcircuit.standard.clock.ClockOscillator;
import net.logicim.domain.integratedcircuit.standard.logic.and.AndGate;
import net.logicim.file.reader.LogicimFileReader;
import net.logicim.ui.simulation.CircuitEditor;

import java.util.List;

public class EventPropagationTest
{
  private static void testEventPropagation()
  {
    CircuitEditor circuitEditor = new CircuitEditor("Main");

    LogicimFileReader fileReader = new LogicimFileReader();
    EditorData editorData = fileReader.load(MultipleXORIntoAndSourceXML.xml);
    circuitEditor.load(editorData.circuit);
    Circuit circuit = circuitEditor.getCircuit();

    List<AndGate> andGates = (List) circuit.getIntegratedCircuits(AndGate.TYPE);
    Validator.validate(andGates.size(), 1);
    AndGate andGate = andGates.get(0);
    LogicPort andOutput = andGate.getPins().getOutput();

    List<ClockOscillator> clocks = (List) circuit.getIntegratedCircuits(ClockOscillator.TYPE);
    Validator.validate(clocks.size(), 1);
    ClockOscillator clock = clocks.get(0);

    Simulation simulation = circuit.resetSimulation();

    while (true)
    {
      simulation.runToTime(100);
      float voltage1 = andOutput.getVoltageOut(simulation.getTime());
      if (!Float.isNaN(voltage1))
      {
        break;
      }
    }

    SmoothVoltage andGateSmoothVoltage = new PortSmoothVoltage(andOutput, 0.3f, simulation);

    String simulationResult = runSimulation(andOutput, clock, simulation, andGateSmoothVoltage);
    Validator.validate("Time [13900]  1.8V\n" +
                       "Time [14050]  2.0V\n" +
                       "Time [14200]  2.1V\n" +
                       "Time [14350]  1.9V\n" +
                       "Time [14500]  1.8V\n" +
                       "Time [14650]  1.6V\n" +
                       "Time [14800]  1.4V\n" +
                       "Time [14950]  1.2V\n" +
                       "Time [15100]  1.0V\n" +
                       "Time [15250]  0.8V\n" +
                       "Time [15400]  0.6V\n" +
                       "Time [15550]  0.4V\n" +
                       "Time [15700]  0.2V\n" +
                       "Time [15850]  0.0V\n" +
                       "Time [16000]  0.0V\n" +
                       "              ...\n" +
                       "Time [16750]  0.1V\n" +
                       "Time [16900]  0.3V\n" +
                       "Time [17050]  0.5V\n" +
                       "Time [17200]  0.3V\n" +
                       "Time [17350]  0.1V\n" +
                       "Time [17500]  0.0V\n" +
                       "              ...\n" +
                       "Time [26200]  0.1V\n" +
                       "Time [26350]  0.3V\n" +
                       "Time [26500]  0.5V\n" +
                       "Time [26650]  0.3V\n" +
                       "Time [26800]  0.2V\n" +
                       "Time [26950]  0.0V\n" +
                       "              ...\n" +
                       "Time [29500]  0.2V\n" +
                       "Time [29650]  0.4V\n" +
                       "Time [29800]  0.4V\n" +
                       "Time [29950]  0.2V\n" +
                       "Time [30100]  0.1V\n" +
                       "Time [30250]  0.0V\n" +
                       "              ...\n" +
                       "Time [35650]  0.1V\n" +
                       "Time [35800]  0.3V\n" +
                       "Time [35950]  0.5V\n" +
                       "Time [36100]  0.3V\n" +
                       "Time [36250]  0.1V\n" +
                       "Time [36400]  0.0V\n" +
                       "              ...\n" +
                       "Time [38500]  0.1V\n" +
                       "Time [38650]  0.3V\n" +
                       "Time [38800]  0.5V\n" +
                       "Time [38950]  0.3V\n" +
                       "Time [39100]  0.1V\n" +
                       "Time [39250]  0.0V\n" +
                       "              ...\n" +
                       "Time [45700]  0.1V\n" +
                       "Time [45850]  0.3V\n" +
                       "Time [46000]  0.5V\n" +
                       "Time [46150]  0.3V\n" +
                       "Time [46300]  0.1V\n" +
                       "Time [46450]  0.0V\n" +
                       "              ...\n" +
                       "Time [48850]  0.0V\n" +
                       "Time [49000]  0.2V\n" +
                       "Time [49150]  0.4V\n" +
                       "Time [49300]  0.4V\n" +
                       "Time [49450]  0.2V\n" +
                       "Time [49600]  0.0V\n" +
                       "              ...\n" +
                       "Time [55150]  0.2V\n" +
                       "Time [55300]  0.4V\n" +
                       "Time [55450]  0.4V\n" +
                       "Time [55600]  0.3V\n" +
                       "Time [55750]  0.1V\n" +
                       "Time [55900]  0.0V\n" +
                       "              ...\n" +
                       "Time [58000]  0.2V\n" +
                       "Time [58150]  0.4V\n" +
                       "Time [58300]  0.4V\n" +
                       "Time [58450]  0.2V\n" +
                       "Time [58600]  0.0V\n" +
                       "Time [58750]  0.0V\n" +
                       "              ...\n" +
                       "Time [65200]  0.2V\n" +
                       "Time [65350]  0.4V\n" +
                       "Time [65500]  0.4V\n" +
                       "Time [65650]  0.2V\n" +
                       "Time [65800]  0.0V\n" +
                       "Time [65950]  0.0V\n" +
                       "              ...\n", simulationResult);
  }

  private static String runSimulation(LogicPort testPort, ClockOscillator clock, Simulation simulation, SmoothVoltage smoothVoltage)
  {
    StringBuilder builder = new StringBuilder();
    float previousVoltage = Float.NaN;
    do
    {
      smoothVoltage.validate();

      float voltage = testPort.getVoltageOut(simulation.getTime());
      if ((voltage != 0.0f) || (previousVoltage != 0.0f))
      {
        builder.append("Time [" + simulation.getTime() + "]  " + Voltage.toVoltageString(voltage) + "\n");
      }
      if ((voltage == 0.0f) && (previousVoltage != 0.0f))
      {
        builder.append("              ...\n");
      }
      previousVoltage = voltage;
      simulation.runToTime(150);
    }
    while (clock.getFullTicks() != 4);
    return builder.toString();
  }

  public static void test()
  {
    testEventPropagation();
  }
}

