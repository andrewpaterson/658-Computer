package net.logicim.ui.circuit;

import net.common.SimulatorException;
import net.common.geometry.Line;
import net.common.type.Int2D;
import net.logicim.data.editor.EditorData;
import net.logicim.data.family.Family;
import net.logicim.data.passive.wire.PinProperties;
import net.logicim.data.subciruit.SubcircuitInstanceProperties;
import net.logicim.domain.CircuitSimulation;
import net.logicim.domain.common.propagation.FamilyVoltageConfigurationStore;
import net.logicim.domain.common.wire.Trace;
import net.logicim.domain.common.wire.Traces;
import net.logicim.file.reader.LogicimFileReader;
import net.logicim.file.writer.LogicimFileWriter;
import net.logicim.ui.circuit.path.ViewPath;
import net.logicim.ui.circuit.path.ViewPaths;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.integratedcircuit.StaticView;
import net.logicim.ui.common.wire.TraceView;
import net.logicim.ui.placement.WirePull;
import net.logicim.ui.simulation.CircuitEditor;
import net.logicim.ui.simulation.component.integratedcircuit.standard.clock.ClockProperties;
import net.logicim.ui.simulation.component.integratedcircuit.standard.clock.ClockView;
import net.logicim.ui.simulation.component.integratedcircuit.standard.clock.ClockViewFactory;
import net.logicim.ui.simulation.component.passive.pin.PinView;
import net.logicim.ui.simulation.component.passive.pin.PinViewFactory;
import net.logicim.ui.simulation.component.subcircuit.SubcircuitInstanceView;
import net.logicim.ui.simulation.subcircuit.SubcircuitEditor;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static net.logicim.domain.common.Units.MHz;
import static net.logicim.file.writer.ReflectiveWriter.EDITOR_DATA_TAG_NAME;
import static net.logicim.file.writer.ReflectiveWriter.LOGICIM_DOC_NAME;

//  Builds circuits through the same CircuitEditor and SubcircuitEditor calls the UI makes when a user places
//  components and pulls wires, so that the view paths, simulations and traces are created the same way.
public class TestCircuit
{
  protected CircuitEditor circuitEditor;

  public TestCircuit(String mainSubcircuitTypeName)
  {
    FamilyVoltageConfigurationStore.getInstance();
    circuitEditor = new CircuitEditor(mainSubcircuitTypeName);
  }

  //  Writes the circuit to XML the way the editor saves a file (and an undo step) and loads it into a new CircuitEditor.
  //  Views held from before the reload belong to the old CircuitEditor.
  public void saveAndReload()
  {
    EditorData editorData = new EditorData(circuitEditor.save(),
                                           0,
                                           0,
                                           false,
                                           Rotation.North,
                                           new ArrayList<>(),
                                           new ArrayList<>(),
                                           new ArrayList<>(),
                                           false);
    StringWriter writer = new StringWriter();
    LogicimFileWriter.writeXML(editorData, writer, LOGICIM_DOC_NAME, EDITOR_DATA_TAG_NAME);

    EditorData loadedEditorData = (EditorData) new LogicimFileReader(LOGICIM_DOC_NAME, EDITOR_DATA_TAG_NAME).load(writer.toString());
    circuitEditor = new CircuitEditor();
    circuitEditor.load(loadedEditorData.circuit);
  }

  public CircuitEditor getCircuitEditor()
  {
    return circuitEditor;
  }

  public SubcircuitEditor getSubcircuitEditor(String subcircuitTypeName)
  {
    return circuitEditor.getSubcircuitEditor(subcircuitTypeName);
  }

  public SubcircuitEditor addSubcircuit(String subcircuitTypeName)
  {
    circuitEditor.addNewSubcircuit(subcircuitTypeName);
    return circuitEditor.getCurrentSubcircuitEditor();
  }

  public void edit(SubcircuitEditor subcircuitEditor)
  {
    if (circuitEditor.getCurrentSubcircuitEditor() != subcircuitEditor)
    {
      circuitEditor.gotoSubcircuit(subcircuitEditor);
    }
  }

  public PinView addPin(SubcircuitEditor subcircuitEditor, String name, int x, int y, Rotation rotation)
  {
    edit(subcircuitEditor);
    PinViewFactory pinViewFactory = new PinViewFactory();
    PinProperties properties = pinViewFactory.createInitialProperties();
    properties.name = name;
    PinView pinView = pinViewFactory.create(circuitEditor,
                                            subcircuitEditor.getInstanceSubcircuitView(),
                                            new Int2D(x, y),
                                            rotation,
                                            properties);
    return place(subcircuitEditor, pinView);
  }

  public ClockView addClock(SubcircuitEditor subcircuitEditor, int x, int y)
  {
    edit(subcircuitEditor);
    Family family = FamilyVoltageConfigurationStore.get("LVC").getFamily();
    ClockView clockView = new ClockViewFactory().create(circuitEditor,
                                                        subcircuitEditor.getInstanceSubcircuitView(),
                                                        new Int2D(x, y),
                                                        Rotation.North,
                                                        new ClockProperties("Clock",
                                                                            family,
                                                                            false,
                                                                            100 * MHz,
                                                                            false));
    return place(subcircuitEditor, clockView);
  }

  public SubcircuitInstanceView addSubcircuitInstance(SubcircuitEditor containingSubcircuitEditor,
                                                      SubcircuitEditor instanceSubcircuitEditor,
                                                      String name,
                                                      int x,
                                                      int y)
  {
    return addSubcircuitInstance(containingSubcircuitEditor, instanceSubcircuitEditor, name, x, y, Rotation.North);
  }

  public SubcircuitInstanceView addSubcircuitInstance(SubcircuitEditor containingSubcircuitEditor,
                                                      SubcircuitEditor instanceSubcircuitEditor,
                                                      String name,
                                                      int x,
                                                      int y,
                                                      Rotation rotation)
  {
    edit(containingSubcircuitEditor);
    SubcircuitInstanceProperties properties = new SubcircuitInstanceProperties(name,
                                                                               instanceSubcircuitEditor.getTypeName(),
                                                                               "",
                                                                               0,
                                                                               0);
    SubcircuitInstanceView subcircuitInstanceView = new SubcircuitInstanceViewFactory().create(circuitEditor,
                                                                                               containingSubcircuitEditor.getInstanceSubcircuitView(),
                                                                                               new Int2D(x, y),
                                                                                               rotation,
                                                                                               properties);
    return place(containingSubcircuitEditor, subcircuitInstanceView);
  }

  //  Points must be joined by horizontal or vertical lines.
  public Set<TraceView> addWire(SubcircuitEditor subcircuitEditor, Int2D... points)
  {
    edit(subcircuitEditor);
    List<Line> lines = new ArrayList<>();
    for (int i = 0; i < points.length - 1; i++)
    {
      lines.add(Line.createLine(points[i], points[i + 1]));
    }
    return new WirePull().createTraceViews(subcircuitEditor, lines);
  }

  public void deleteWires(SubcircuitEditor subcircuitEditor, Set<TraceView> traceViews)
  {
    edit(subcircuitEditor);
    subcircuitEditor.deleteTraceViews(traceViews);
  }

  //  Joins two points with a horizontal then vertical wire, or a single straight wire when they line up.
  public Set<TraceView> connect(SubcircuitEditor subcircuitEditor, Int2D from, Int2D to)
  {
    if ((from.x == to.x) || (from.y == to.y))
    {
      return addWire(subcircuitEditor, from, to);
    }
    else
    {
      return addWire(subcircuitEditor, from, new Int2D(to.x, from.y), to);
    }
  }

  //  The wires currently touching a grid position.  Adding wires can replace the TraceViews they join to.
  public static Set<TraceView> getWiresAt(SubcircuitEditor subcircuitEditor, Int2D position)
  {
    return new LinkedHashSet<>(subcircuitEditor.getInstanceSubcircuitView().getTraceViewsInGridSpace(position.x, position.y));
  }

  //  Finds views by name, for example after saveAndReload has replaced them.
  public PinView getPin(SubcircuitEditor subcircuitEditor, String name)
  {
    for (PinView pinView : subcircuitEditor.getInstanceSubcircuitView().findAllPins())
    {
      if (pinView.getName().equals(name))
      {
        return pinView;
      }
    }
    throw new SimulatorException("Cannot find Pin [%s] in Subcircuit [%s].", name, subcircuitEditor.getTypeName());
  }

  public SubcircuitInstanceView getSubcircuitInstance(SubcircuitEditor subcircuitEditor, String name)
  {
    for (SubcircuitInstanceView subcircuitInstanceView : subcircuitEditor.getInstanceSubcircuitView().getSubcircuitInstanceViews())
    {
      if (subcircuitInstanceView.getName().equals(name))
      {
        return subcircuitInstanceView;
      }
    }
    throw new SimulatorException("Cannot find Subcircuit Instance [%s] in Subcircuit [%s].", name, subcircuitEditor.getTypeName());
  }

  //  The view path from the top level sub-circuit editor down through the given sub-circuit instances.
  public ViewPath getViewPath(SubcircuitEditor topSubcircuitEditor, SubcircuitInstanceView... subcircuitInstanceViews)
  {
    ViewPaths viewPaths = circuitEditor.getViewPaths();
    ViewPath viewPath = viewPaths.getViewPath(viewPaths.getEmptyPath(), topSubcircuitEditor);
    for (SubcircuitInstanceView subcircuitInstanceView : subcircuitInstanceViews)
    {
      viewPath = viewPaths.getViewPath(viewPath, subcircuitInstanceView);
    }
    return viewPath;
  }

  //  The traces of a wire on a view path in the path's only circuit simulation.
  public static Set<Trace> getTraces(Set<TraceView> traceViews, ViewPath viewPath)
  {
    List<CircuitSimulation> circuitSimulations = viewPath.getCircuitSimulations();
    if (circuitSimulations.size() != 1)
    {
      throw new SimulatorException("Expected one Circuit Simulation on Path [%s] but found [%s].", viewPath.getDescription(), circuitSimulations.size());
    }
    CircuitSimulation circuitSimulation = circuitSimulations.get(0);

    Set<Trace> result = new LinkedHashSet<>();
    for (TraceView traceView : traceViews)
    {
      Traces traces = traceView.getTraces(viewPath, circuitSimulation);
      if (traces != null)
      {
        result.addAll(traces.getTraces());
      }
    }
    return result;
  }

  public static Int2D getPosition(PinView pinView)
  {
    return pinView.getPortView().getGridPosition();
  }

  public static Int2D getPosition(ClockView clockView)
  {
    return clockView.getPortViews().get(0).getGridPosition();
  }

  public static Int2D getPosition(SubcircuitInstanceView subcircuitInstanceView, PinView pinView)
  {
    return subcircuitInstanceView.getSubcircuitPinView(pinView).getConnection().getGridPosition();
  }

  protected <T extends StaticView<?>> T place(SubcircuitEditor subcircuitEditor, T staticView)
  {
    List<StaticView<?>> staticViews = new ArrayList<>();
    staticViews.add(staticView);
    subcircuitEditor.startMoveComponents(staticViews, new ArrayList<>());
    subcircuitEditor.doneMoveComponents(staticViews,
                                        new ArrayList<>(),
                                        new ArrayList<>(),
                                        new LinkedHashSet<>(staticViews),
                                        true);
    return staticView;
  }
}
