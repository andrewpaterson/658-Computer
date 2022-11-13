package net.logicim.domain;

import net.logicim.domain.common.Circuit;
import net.logicim.domain.common.trace.TraceNet;
import net.logicim.domain.integratedcircuit.standard.clock.ClockOscillator;
import net.logicim.domain.integratedcircuit.standard.clock.ClockOscillatorPins;
import net.logicim.domain.integratedcircuit.standard.logic.buffer.BufferPins;
import net.logicim.domain.integratedcircuit.standard.logic.buffer.Inverter;

import static net.logicim.domain.common.Units.MHz;

public class SimulationTest
{
  private static void testClockOscillator()
  {
    Circuit circuit = new Circuit();

    ClockOscillator clock = new ClockOscillator(circuit, "10Mhz", new ClockOscillatorPins(), 10 * MHz);
    TraceNet clockTrace = new TraceNet();
    clock.getPins().getOutput().connect(clockTrace);

    Inverter inverter = new Inverter(circuit, "Not", new BufferPins());
    inverter.getPins().getInput().connect(clockTrace);
    inverter.getPins().getOutput().connect(new TraceNet());

    Simulation simulation = circuit.resetSimulation();
    simulation.runSimultaneous();
  }

  private static void testInverterEvents()
  {
    Circuit circuit = new Circuit();
    TraceNet connectingTraceNet = new TraceNet();
    TraceNet outputTraceNet = new TraceNet();
    Inverter inverter1 = new Inverter(circuit, "Not 1", new BufferPins());
    inverter1.getPins().getOutput().connect(connectingTraceNet);
    Inverter inverter2 = new Inverter(circuit, "Not 1", new BufferPins());
    inverter2.getPins().getInput().connect(connectingTraceNet);
    inverter2.getPins().getOutput().connect(outputTraceNet);

    Simulation simulation = circuit.resetSimulation();
    inverter1.inputTransition(simulation, inverter1.getInputPort());
    simulation.runSimultaneous();

  }

  public static void test()
  {
    testInverterEvents();
    testClockOscillator();
  }
}

