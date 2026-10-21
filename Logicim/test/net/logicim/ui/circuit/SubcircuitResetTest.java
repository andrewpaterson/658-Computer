package net.logicim.ui.circuit;

import net.logicim.domain.CircuitSimulation;
import net.logicim.domain.common.wire.Trace;
import net.logicim.ui.circuit.path.ViewPath;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.wire.TraceView;
import net.logicim.ui.simulation.component.integratedcircuit.standard.clock.ClockView;
import net.logicim.ui.simulation.component.passive.pin.PinView;
import net.logicim.ui.simulation.component.subcircuit.SubcircuitInstanceView;
import net.logicim.ui.simulation.subcircuit.SubcircuitEditor;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

//  Resetting a circuit simulation destroys every component, sub-circuit instance simulation and trace it owns and
//  re-creates them from the views, so it must leave the circuit connected, running and identically wired.
public class SubcircuitResetTest
{
  //  Main: Clock -> Instance.In.  PassThrough: In -> Out.
  protected static class ClockedPassThrough
  {
    protected TestCircuit circuit;
    protected SubcircuitEditor main;
    protected SubcircuitInstanceView instance;
    protected Set<TraceView> innerWire;

    protected ClockedPassThrough()
    {
      circuit = new TestCircuit("Main");
      SubcircuitEditor passThrough = circuit.addSubcircuit("PassThrough");
      PinView in = circuit.addPin(passThrough, "In", 0, 0, Rotation.West);
      PinView out = circuit.addPin(passThrough, "Out", 20, 0, Rotation.East);
      innerWire = circuit.connect(passThrough, TestCircuit.getPosition(in), TestCircuit.getPosition(out));

      main = circuit.getSubcircuitEditor("Main");
      instance = circuit.addSubcircuitInstance(main, passThrough, "Instance", 0, 0, Rotation.West);
      ClockView clock = circuit.addClock(main, -20, -10);
      circuit.connect(main, TestCircuit.getPosition(clock), TestCircuit.getPosition(instance, in));
    }

    protected ViewPath getInstancePath()
    {
      return circuit.getViewPath(main, instance);
    }

    protected CircuitSimulation getMainSimulation()
    {
      return circuit.getViewPath(main).getCircuitSimulations().get(0);
    }
  }

  @Test
  public void testResetRestartsSimulation()
  {
    ClockedPassThrough circuit = new ClockedPassThrough();
    CircuitSimulation circuitSimulation = circuit.getMainSimulation();
    assertTrue(runAndFindDrivenVoltages(circuit, circuitSimulation) > 0, "Inner wire was never driven before reset.");

    circuit.circuit.getCircuitEditor().resetSimulation(circuitSimulation);

    assertEquals(0L, circuitSimulation.getTime());
    assertFalse(circuitSimulation.getTimeline().getAllEvents().isEmpty(), "Reset left no events on the timeline.");
    assertTrue(runAndFindDrivenVoltages(circuit, circuitSimulation) > 0, "Inner wire was never driven after reset.");
  }

  @Test
  public void testResetPreservesConnectivity()
  {
    ClockedPassThrough circuit = new ClockedPassThrough();
    assertResetPreservesWires(circuit.circuit, circuit.getMainSimulation());
  }

  @Test
  public void testResetTwice()
  {
    ClockedPassThrough circuit = new ClockedPassThrough();
    CircuitSimulation circuitSimulation = circuit.getMainSimulation();
    assertResetPreservesWires(circuit.circuit, circuitSimulation);
    assertResetPreservesWires(circuit.circuit, circuitSimulation);
    assertTrue(runAndFindDrivenVoltages(circuit, circuitSimulation) > 0, "Inner wire was never driven after two resets.");
  }

  //  Main contains two Middles, each Middle contains two Leafs.
  @Test
  public void testResetRepeatedSubcircuitsAtTwoLevels()
  {
    TestCircuit circuit = new TestCircuit("Main");
    SubcircuitEditor leaf = circuit.addSubcircuit("Leaf");
    PinView leafIn = circuit.addPin(leaf, "In", 0, 0, Rotation.West);
    PinView leafOut = circuit.addPin(leaf, "Out", 20, 0, Rotation.East);
    circuit.connect(leaf, TestCircuit.getPosition(leafIn), TestCircuit.getPosition(leafOut));

    SubcircuitEditor middle = circuit.addSubcircuit("Middle");
    PinView middleIn = circuit.addPin(middle, "In", 0, 0, Rotation.West);
    PinView middleOut = circuit.addPin(middle, "Out", 20, 0, Rotation.East);
    SubcircuitInstanceView firstLeaf = circuit.addSubcircuitInstance(middle, leaf, "First Leaf", 10, 20, Rotation.West);
    SubcircuitInstanceView secondLeaf = circuit.addSubcircuitInstance(middle, leaf, "Second Leaf", 10, 50, Rotation.West);
    circuit.connect(middle, TestCircuit.getPosition(middleIn), TestCircuit.getPosition(firstLeaf, leafIn));
    circuit.connect(middle, TestCircuit.getPosition(firstLeaf, leafIn), TestCircuit.getPosition(secondLeaf, leafIn));
    circuit.connect(middle, TestCircuit.getPosition(firstLeaf, leafOut), TestCircuit.getPosition(middleOut));

    SubcircuitEditor main = circuit.getSubcircuitEditor("Main");
    SubcircuitInstanceView firstMiddle = circuit.addSubcircuitInstance(main, middle, "First Middle", 0, 0, Rotation.West);
    SubcircuitInstanceView secondMiddle = circuit.addSubcircuitInstance(main, middle, "Second Middle", 0, 40, Rotation.West);
    ClockView clock = circuit.addClock(main, -20, -10);
    circuit.connect(main, TestCircuit.getPosition(clock), TestCircuit.getPosition(firstMiddle, middleIn));
    circuit.connect(main, TestCircuit.getPosition(firstMiddle, middleIn), TestCircuit.getPosition(secondMiddle, middleIn));

    assertResetPreservesWires(circuit, circuit.getViewPath(main).getCircuitSimulations().get(0));
  }

  //  A wire with no traces on an instance path is what a file saved while sub-circuit wiring was broken loads as.
  @Test
  public void testResetRepairsMissingTraces()
  {
    ClockedPassThrough circuit = new ClockedPassThrough();
    CircuitSimulation circuitSimulation = circuit.getMainSimulation();
    String connectivity = WireConnectivity.describe(circuit.circuit.getCircuitEditor());

    for (TraceView traceView : circuit.innerWire)
    {
      traceView.destroyComponent(circuit.getInstancePath(), circuitSimulation);
    }
    assertFalse(DisconnectedWireFinder.findDisconnectedWires(circuit.circuit.getCircuitEditor()).isEmpty(), "Expected the inner wire to be disconnected.");

    circuit.circuit.getCircuitEditor().resetSimulation(circuitSimulation);

    assertNoDisconnectedWires(circuit.circuit);
    assertEquals(connectivity, WireConnectivity.describe(circuit.circuit.getCircuitEditor()));
  }

  protected static int runAndFindDrivenVoltages(ClockedPassThrough circuit, CircuitSimulation circuitSimulation)
  {
    ViewPath instancePath = circuit.getInstancePath();
    int drivenVoltages = 0;
    for (int i = 0; i < 20; i++)
    {
      circuitSimulation.runToTime(2000);
      for (Trace trace : TestCircuit.getTraces(circuit.innerWire, instancePath))
      {
        if (!Float.isNaN(trace.getVoltage(circuitSimulation.getTime())))
        {
          drivenVoltages++;
        }
      }
    }
    return drivenVoltages;
  }

  protected static void assertResetPreservesWires(TestCircuit circuit, CircuitSimulation circuitSimulation)
  {
    String connectivityBeforeReset = WireConnectivity.describe(circuit.getCircuitEditor());
    circuit.getCircuitEditor().resetSimulation(circuitSimulation);
    assertNoDisconnectedWires(circuit);
    assertEquals(connectivityBeforeReset, WireConnectivity.describe(circuit.getCircuitEditor()), "Wire connectivity changed after reset.");
  }

  protected static void assertNoDisconnectedWires(TestCircuit circuit)
  {
    List<String> disconnectedWires = DisconnectedWireFinder.findDisconnectedWires(circuit.getCircuitEditor());
    assertTrue(disconnectedWires.isEmpty(), DisconnectedWireFinder.toReport(disconnectedWires));
  }
}
