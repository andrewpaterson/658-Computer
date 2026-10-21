package net.logicim.ui.circuit;

import net.common.geometry.Line;
import net.common.type.Int2D;
import net.logicim.data.family.Family;
import net.logicim.data.passive.wire.PinProperties;
import net.logicim.data.subciruit.SubcircuitInstanceProperties;
import net.logicim.domain.common.propagation.FamilyVoltageConfigurationStore;
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

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static net.logicim.domain.common.Units.MHz;

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
