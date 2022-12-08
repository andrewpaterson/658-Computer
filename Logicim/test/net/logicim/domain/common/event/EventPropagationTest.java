package net.logicim.domain.common.event;

import net.logicim.assertions.SmoothVoltage;
import net.logicim.assertions.Validator;
import net.logicim.data.circuit.CircuitData;
import net.logicim.domain.Simulation;
import net.logicim.domain.common.Circuit;
import net.logicim.domain.common.Voltage;
import net.logicim.domain.common.port.Port;
import net.logicim.domain.integratedcircuit.standard.clock.ClockOscillator;
import net.logicim.domain.integratedcircuit.standard.logic.and.AndGate;
import net.logicim.file.LogicimFileReader;
import net.logicim.ui.CircuitEditor;

import java.util.List;

public class EventPropagationTest
{

  private static void testEventPropagation()
  {
    CircuitEditor circuitEditor = new CircuitEditor();

    LogicimFileReader fileReader = new LogicimFileReader();
    CircuitData circuitData = fileReader.load(MultipleXORIntoAndSourceXML.xml);
    circuitEditor.load(circuitData);
    Circuit circuit = circuitEditor.getCircuit();

    List<AndGate> andGates = (List) circuit.getIntegratedCircuits(AndGate.TYPE);
    Validator.validate(andGates.size(), 1);
    AndGate andGate = andGates.get(0);
    Port andOutput = andGate.getPins().getOutput();

    List<ClockOscillator> clocks = (List) circuit.getIntegratedCircuits(ClockOscillator.TYPE);
    Validator.validate(clocks.size(), 1);
    ClockOscillator clock = clocks.get(0);

    Simulation simulation = circuit.resetSimulation();

    while (true)
    {
      simulation.runToTime(100);
      float voltage1 = andOutput.getVoltage(simulation.getTime());
      if (!Float.isNaN(voltage1))
      {
        break;
      }
    }

    SmoothVoltage andGateSmoothVoltage = new SmoothVoltage(andOutput, 0.2f, simulation);

    do
    {
      System.out.println("Simulation Time [" + simulation.getTime() + "]  " + andOutput.toEventsString());
      andGateSmoothVoltage.validate();

      System.out.println(Voltage.getVoltageString(andOutput.getVoltage(simulation.getTime())));
      simulation.runToTime(100);
    }
    while (clock.getFullTicks() != 2);
  }

  public static void test()
  {
    testEventPropagation();
  }
}

