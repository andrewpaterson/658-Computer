package net.logicim.ui.circuit;

import net.common.type.Int2D;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.simulation.component.integratedcircuit.standard.clock.ClockView;
import net.logicim.ui.simulation.component.passive.pin.PinView;
import net.logicim.ui.simulation.component.subcircuit.SubcircuitInstanceView;
import net.logicim.ui.simulation.subcircuit.SubcircuitEditor;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

//  A sub-circuit "class" (SubcircuitEditor / SubcircuitView) can be placed many times, and each placement along
//  each route from a top level circuit is a sub-circuit "instance" (a ViewPath with its own SubcircuitSimulation).
//  These tests check that every wire is connected in every instance.
public class SubcircuitWireConnectionTest
{
  //  A sub-circuit with an input pin on the left and an output pin on the right.  Instances are placed rotated
  //  West so that In is 6 to the left of the instance position and Out is 6 to the right.
  protected static class PassThrough
  {
    protected SubcircuitEditor subcircuitEditor;
    protected PinView in;
    protected PinView out;

    protected PassThrough(TestCircuit circuit, String typeName)
    {
      subcircuitEditor = circuit.addSubcircuit(typeName);
      in = circuit.addPin(subcircuitEditor, "In", 0, 0, Rotation.West);
      out = circuit.addPin(subcircuitEditor, "Out", 20, 0, Rotation.East);
    }

    protected void wireInToOut(TestCircuit circuit)
    {
      circuit.connect(subcircuitEditor, TestCircuit.getPosition(in), TestCircuit.getPosition(out));
    }

    protected Int2D getIn(SubcircuitInstanceView subcircuitInstanceView)
    {
      return TestCircuit.getPosition(subcircuitInstanceView, in);
    }

    protected Int2D getOut(SubcircuitInstanceView subcircuitInstanceView)
    {
      return TestCircuit.getPosition(subcircuitInstanceView, out);
    }
  }

  @Test
  public void testWireInTopLevelCircuit()
  {
    TestCircuit circuit = new TestCircuit("Main");
    SubcircuitEditor main = circuit.getSubcircuitEditor("Main");
    ClockView clock = circuit.addClock(main, 0, 0);
    PinView pin = circuit.addPin(main, "Out", 20, 0, Rotation.East);
    circuit.connect(main, TestCircuit.getPosition(clock), TestCircuit.getPosition(pin));

    assertNoDisconnectedWires(circuit);
  }

  @Test
  public void testSubcircuitWiredBeforeBeingInstanced()
  {
    TestCircuit circuit = new TestCircuit("Main");
    PassThrough passThrough = new PassThrough(circuit, "PassThrough");
    passThrough.wireInToOut(circuit);

    SubcircuitEditor main = circuit.getSubcircuitEditor("Main");
    SubcircuitInstanceView instance = circuit.addSubcircuitInstance(main, passThrough.subcircuitEditor, "Instance", 0, 0, Rotation.West);
    ClockView clock = circuit.addClock(main, -20, -10);
    circuit.connect(main, TestCircuit.getPosition(clock), passThrough.getIn(instance));

    assertNoDisconnectedWires(circuit);
  }

  @Test
  public void testSubcircuitWiredAfterBeingInstanced()
  {
    TestCircuit circuit = new TestCircuit("Main");
    PassThrough passThrough = new PassThrough(circuit, "PassThrough");

    SubcircuitEditor main = circuit.getSubcircuitEditor("Main");
    SubcircuitInstanceView instance = circuit.addSubcircuitInstance(main, passThrough.subcircuitEditor, "Instance", 0, 0, Rotation.West);
    ClockView clock = circuit.addClock(main, -20, -10);
    circuit.connect(main, TestCircuit.getPosition(clock), passThrough.getIn(instance));

    passThrough.wireInToOut(circuit);

    assertNoDisconnectedWires(circuit);
  }

  @Test
  public void testTwoInstancesOnSeparateNets()
  {
    TestCircuit circuit = new TestCircuit("Main");
    PassThrough passThrough = new PassThrough(circuit, "PassThrough");
    passThrough.wireInToOut(circuit);

    SubcircuitEditor main = circuit.getSubcircuitEditor("Main");
    SubcircuitInstanceView first = circuit.addSubcircuitInstance(main, passThrough.subcircuitEditor, "First", 0, 0, Rotation.West);
    SubcircuitInstanceView second = circuit.addSubcircuitInstance(main, passThrough.subcircuitEditor, "Second", 0, 30, Rotation.West);
    ClockView firstClock = circuit.addClock(main, -20, -10);
    ClockView secondClock = circuit.addClock(main, -20, 20);
    circuit.connect(main, TestCircuit.getPosition(firstClock), passThrough.getIn(first));
    circuit.connect(main, TestCircuit.getPosition(secondClock), passThrough.getIn(second));

    assertNoDisconnectedWires(circuit);
  }

  //  One wire in Main touches the same pin on two instances of the same sub-circuit, so the wire inside the
  //  sub-circuit appears in the connection net once for each instance's view path.
  @Test
  public void testTwoInstancesOnOneNet()
  {
    TestCircuit circuit = new TestCircuit("Main");
    PassThrough passThrough = new PassThrough(circuit, "PassThrough");
    passThrough.wireInToOut(circuit);

    SubcircuitEditor main = circuit.getSubcircuitEditor("Main");
    SubcircuitInstanceView first = circuit.addSubcircuitInstance(main, passThrough.subcircuitEditor, "First", 0, 0, Rotation.West);
    SubcircuitInstanceView second = circuit.addSubcircuitInstance(main, passThrough.subcircuitEditor, "Second", 0, 30, Rotation.West);
    circuit.connect(main, passThrough.getIn(first), passThrough.getIn(second));

    assertNoDisconnectedWires(circuit);
  }

  //  Main contains Middle, Middle contains Leaf.
  @Test
  public void testThreeLevelsOfNesting()
  {
    TestCircuit circuit = new TestCircuit("Main");
    PassThrough leaf = new PassThrough(circuit, "Leaf");
    leaf.wireInToOut(circuit);

    PassThrough middle = new PassThrough(circuit, "Middle");
    SubcircuitInstanceView leafInstance = circuit.addSubcircuitInstance(middle.subcircuitEditor, leaf.subcircuitEditor, "Leaf", 10, 20, Rotation.West);
    circuit.connect(middle.subcircuitEditor, TestCircuit.getPosition(middle.in), leaf.getIn(leafInstance));
    circuit.connect(middle.subcircuitEditor, leaf.getOut(leafInstance), TestCircuit.getPosition(middle.out));

    SubcircuitEditor main = circuit.getSubcircuitEditor("Main");
    SubcircuitInstanceView middleInstance = circuit.addSubcircuitInstance(main, middle.subcircuitEditor, "Middle", 0, 0, Rotation.West);
    ClockView clock = circuit.addClock(main, -20, -10);
    circuit.connect(main, TestCircuit.getPosition(clock), middle.getIn(middleInstance));

    assertNoDisconnectedWires(circuit);
  }

  //  Main contains two Middles, each Middle contains two Leafs, so Leaf has four instances below Main.
  @Test
  public void testRepeatedSubcircuitsAtTwoLevels()
  {
    TestCircuit circuit = new TestCircuit("Main");
    PassThrough leaf = new PassThrough(circuit, "Leaf");
    leaf.wireInToOut(circuit);

    PassThrough middle = new PassThrough(circuit, "Middle");
    SubcircuitInstanceView firstLeaf = circuit.addSubcircuitInstance(middle.subcircuitEditor, leaf.subcircuitEditor, "First Leaf", 10, 20, Rotation.West);
    SubcircuitInstanceView secondLeaf = circuit.addSubcircuitInstance(middle.subcircuitEditor, leaf.subcircuitEditor, "Second Leaf", 10, 50, Rotation.West);
    circuit.connect(middle.subcircuitEditor, TestCircuit.getPosition(middle.in), leaf.getIn(firstLeaf));
    circuit.connect(middle.subcircuitEditor, leaf.getIn(firstLeaf), leaf.getIn(secondLeaf));
    circuit.connect(middle.subcircuitEditor, leaf.getOut(firstLeaf), TestCircuit.getPosition(middle.out));

    SubcircuitEditor main = circuit.getSubcircuitEditor("Main");
    SubcircuitInstanceView firstMiddle = circuit.addSubcircuitInstance(main, middle.subcircuitEditor, "First Middle", 0, 0, Rotation.West);
    SubcircuitInstanceView secondMiddle = circuit.addSubcircuitInstance(main, middle.subcircuitEditor, "Second Middle", 0, 40, Rotation.West);
    ClockView clock = circuit.addClock(main, -20, -10);
    circuit.connect(main, TestCircuit.getPosition(clock), middle.getIn(firstMiddle));
    circuit.connect(main, middle.getIn(firstMiddle), middle.getIn(secondMiddle));

    assertNoDisconnectedWires(circuit);
  }

  //  The innermost wire is added last, after Leaf has been nested two levels deep.
  @Test
  public void testLeafWiredAfterNesting()
  {
    TestCircuit circuit = new TestCircuit("Main");
    PassThrough leaf = new PassThrough(circuit, "Leaf");

    PassThrough middle = new PassThrough(circuit, "Middle");
    SubcircuitInstanceView leafInstance = circuit.addSubcircuitInstance(middle.subcircuitEditor, leaf.subcircuitEditor, "Leaf", 10, 20, Rotation.West);
    circuit.connect(middle.subcircuitEditor, TestCircuit.getPosition(middle.in), leaf.getIn(leafInstance));
    circuit.connect(middle.subcircuitEditor, leaf.getOut(leafInstance), TestCircuit.getPosition(middle.out));

    SubcircuitEditor main = circuit.getSubcircuitEditor("Main");
    SubcircuitInstanceView middleInstance = circuit.addSubcircuitInstance(main, middle.subcircuitEditor, "Middle", 0, 0, Rotation.West);
    ClockView clock = circuit.addClock(main, -20, -10);
    circuit.connect(main, TestCircuit.getPosition(clock), middle.getIn(middleInstance));

    leaf.wireInToOut(circuit);

    assertNoDisconnectedWires(circuit);
  }

  protected static void assertNoDisconnectedWires(TestCircuit circuit)
  {
    List<String> disconnectedWires = DisconnectedWireFinder.findDisconnectedWires(circuit.getCircuitEditor());
    assertTrue(disconnectedWires.isEmpty(), DisconnectedWireFinder.toReport(disconnectedWires));
  }
}
