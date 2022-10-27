package net.logicim.test;

import net.logicim.domain.Simulation;
import net.logicim.domain.common.Circuit;
import net.logicim.domain.common.Timeline;
import net.logicim.domain.common.trace.TraceNet;
import net.logicim.domain.integratedcircuit.nexperia.LVC541;
import net.logicim.domain.integratedcircuit.standard.clock.ClockOscillator;
import net.logicim.domain.integratedcircuit.standard.clock.ClockOscillatorPins;
import net.logicim.domain.integratedcircuit.standard.logic.NotGate;
import net.logicim.domain.integratedcircuit.standard.logic.NotGatePins;

import static net.logicim.domain.common.Units.MHz;

public class TestSimulator
{
  public static void main(String[] args)
  {
    Circuit circuit = new Circuit();

    ClockOscillator clock = new ClockOscillator(circuit, "10Mhz", new ClockOscillatorPins(), 10 * MHz);
    TraceNet clockTrace = new TraceNet();
    clock.getPins().getOutput().connect(clockTrace);

    NotGate notGate = new NotGate(circuit, "Not", new NotGatePins());
    notGate.getPins().getInput().connect(clockTrace);
    notGate.getPins().getOutput().connect(new TraceNet());

    Simulation simulation = circuit.resetSimulation();
    simulation.run();
  }
}

