package net.logicim.ui.circuit;

import net.common.type.Int2D;
import net.logicim.data.editor.EditorData;
import net.logicim.domain.common.propagation.FamilyVoltageConfigurationStore;
import net.logicim.file.reader.LogicimFileReader;
import net.logicim.ui.common.ConnectionView;
import net.logicim.ui.common.wire.TraceView;
import net.logicim.ui.simulation.CircuitEditor;
import net.logicim.ui.simulation.component.passive.pin.PinView;
import net.logicim.ui.simulation.component.subcircuit.SubcircuitInstanceView;
import net.logicim.ui.simulation.subcircuit.SubcircuitEditor;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static net.logicim.file.writer.ReflectiveWriter.EDITOR_DATA_TAG_NAME;
import static net.logicim.file.writer.ReflectiveWriter.LOGICIM_DOC_NAME;
import static org.junit.jupiter.api.Assertions.assertTrue;

//  The saved circuits were drawn on Windows.  If sub-circuit instances lay out differently here, their pins move and
//  wires that ended on a pin are left running across it instead.
public class SavedCircuitLayoutTest
{
  @TestFactory
  public List<DynamicTest> testSubcircuitPinsMeetTheirWires()
  {
    FamilyVoltageConfigurationStore.getInstance();
    List<DynamicTest> tests = new ArrayList<>();
    File[] files = new File("Circuits").listFiles((directory, name) -> name.endsWith(".logic"));
    assertTrue((files != null) && (files.length > 0), "No circuits found in [" + new File("Circuits").getAbsolutePath() + "].");
    for (File file : files)
    {
      tests.add(DynamicTest.dynamicTest(file.getName(), () -> assertPinsMeetTheirWires(file)));
    }
    return tests;
  }

  protected static void assertPinsMeetTheirWires(File file)
  {
    EditorData editorData = (EditorData) new LogicimFileReader(LOGICIM_DOC_NAME, EDITOR_DATA_TAG_NAME).load(file);
    CircuitEditor circuitEditor = new CircuitEditor();
    circuitEditor.load(editorData.circuit);

    List<String> misplacedPins = new ArrayList<>();
    for (SubcircuitEditor subcircuitEditor : circuitEditor.getSubcircuitEditors())
    {
      SubcircuitView subcircuitView = subcircuitEditor.getInstanceSubcircuitView();
      for (SubcircuitInstanceView subcircuitInstanceView : subcircuitView.getSubcircuitInstanceViews())
      {
        for (PinView pinView : subcircuitInstanceView.getInstanceSubcircuitView().findAllPins())
        {
          ConnectionView connectionView = subcircuitInstanceView.getSubcircuitPinView(pinView).getConnection();
          Int2D position = connectionView.getGridPosition();
          boolean wireCrossesPin = !subcircuitView.getTraceViewsInGridSpace(position.x, position.y).isEmpty();
          boolean wireEndsOnPin = connectionView.getConnectedComponents().stream().anyMatch(view -> view instanceof TraceView);
          if (wireCrossesPin && !wireEndsOnPin)
          {
            misplacedPins.add(subcircuitEditor.getTypeName() + ": pin [" + pinView.getName() + "] of " + subcircuitInstanceView.getDescription() + " at " + position);
          }
        }
      }
    }
    assertTrue(misplacedPins.isEmpty(), "Wires cross these pins without ending on them:\n" + String.join("\n", misplacedPins));
  }
}
