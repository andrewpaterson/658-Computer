package net.logicim.ui.simulation;

import net.logicim.common.SimulatorException;
import net.logicim.common.type.Float2D;
import net.logicim.common.type.Int2D;
import net.logicim.common.util.StringUtil;
import net.logicim.data.circuit.CircuitData;
import net.logicim.data.circuit.SubcircuitData;
import net.logicim.data.common.ViewData;
import net.logicim.data.integratedcircuit.common.StaticData;
import net.logicim.data.simulation.CircuitSimulationData;
import net.logicim.data.wire.TraceData;
import net.logicim.data.wire.TraceLoader;
import net.logicim.domain.CircuitSimulation;
import net.logicim.domain.common.Circuit;
import net.logicim.domain.common.event.Event;
import net.logicim.ui.circuit.SubcircuitView;
import net.logicim.ui.clipboard.ClipboardData;
import net.logicim.ui.common.ConnectionView;
import net.logicim.ui.common.HoverConnectionView;
import net.logicim.ui.common.Viewport;
import net.logicim.ui.common.integratedcircuit.StaticView;
import net.logicim.ui.common.integratedcircuit.View;
import net.logicim.ui.common.wire.TraceView;
import net.logicim.ui.shape.common.BoundingBox;
import net.logicim.ui.simulation.order.SubcircuitOrderer;
import net.logicim.ui.simulation.selection.Selection;

import java.awt.*;
import java.util.List;
import java.util.*;

public class CircuitEditor
{
  protected List<TopLevelSubcircuitSimulation> simulations;
  protected TopLevelSubcircuitSimulation currentSimulation;

  protected List<SubcircuitEditor> subcircuitEditors;
  protected SubcircuitEditor currentSubcircuitEditor;

  public CircuitEditor()
  {
    simulations = new ArrayList<>();
    currentSimulation = null;

    subcircuitEditors = new ArrayList<>();
    currentSubcircuitEditor = null;
  }

  public CircuitEditor(String mainSubcircuitTypeName)
  {
    this();
    addNewSubcircuit(mainSubcircuitTypeName);
  }

  private List<View> getAllViews()
  {
    return currentSubcircuitEditor.getAllViews();
  }

  public void paint(Graphics2D graphics, Viewport viewport)
  {
    List<View> views;
    synchronized (this)
    {
      views = getAllViews();
    }

    CircuitSimulation circuitSimulation = getCircuitSimulation();
    for (View view : views)
    {
      view.paint(graphics, viewport, circuitSimulation);
    }
  }

  public Circuit getCircuit()
  {
    if (currentSimulation != null)
    {
      return currentSimulation.getCircuitSimulation().getCircuit();
    }
    else
    {
      return null;
    }
  }

  public void editActionDeleteTraceView(ConnectionView connectionView, TraceView traceView)
  {
    if (connectionView instanceof HoverConnectionView)
    {
      Set<TraceView> traceViews = new LinkedHashSet<>();
      traceViews.add(traceView);
      currentSubcircuitEditor.deleteTraceViews(traceViews, getCircuitSimulation());
    }
    else
    {
      editActionDeleteTraceViews(connectionView);
    }
  }

  public boolean editActionDeleteTraceViews(ConnectionView connectionView)
  {
    List<View> connectedComponents = connectionView.getConnectedComponents();
    Set<TraceView> traceViews = new LinkedHashSet<>();
    for (View connectedComponent : connectedComponents)
    {
      if (connectedComponent instanceof TraceView)
      {
        traceViews.add((TraceView) connectedComponent);
      }
    }

    if (traceViews.size() > 0)
    {
      currentSubcircuitEditor.deleteTraceViews(traceViews, getCircuitSimulation());
      return true;
    }
    else
    {
      return false;
    }
  }

  public void reset()
  {
    getCircuitSimulation().reset();
  }

  public void runSimultaneous()
  {
    getCircuitSimulation().runSimultaneous();
  }

  public void runToTime(long timeForward)
  {
    getCircuitSimulation().runToTime(timeForward);
  }

  public StaticView<?> getComponentViewInScreenSpace(Viewport viewport,
                                                     Int2D screenPosition)
  {
    List<StaticView<?>> selectedViews = getComponentViewsInScreenSpace(viewport, screenPosition);

    if (selectedViews.size() == 1)
    {
      return selectedViews.get(0);
    }
    else if (selectedViews.size() == 0)
    {
      return null;
    }
    else
    {
      Int2D boundBoxPosition = new Int2D();
      Int2D boundBoxDimension = new Int2D();
      float shortestDistance = Float.MAX_VALUE;
      StaticView<?> closestView = null;
      for (StaticView<?> view : selectedViews)
      {
        if (view.getBoundingBoxInScreenSpace(viewport, boundBoxPosition, boundBoxDimension))
        {
          boundBoxPosition.add(boundBoxDimension.x / 2, boundBoxDimension.y / 2);
          float distance = BoundingBox.calculateDistance(screenPosition, boundBoxPosition);
          if (distance < shortestDistance)
          {
            closestView = view;
            shortestDistance = distance;
          }
        }
      }
      return closestView;
    }
  }

  public List<StaticView<?>> getComponentViewsInScreenSpace(Viewport viewport, Int2D screenPosition)
  {
    return currentSubcircuitEditor.getComponentViewsInScreenSpace(viewport, screenPosition);
  }

  public CircuitData save()
  {
    SubcircuitOrderer orderer = new SubcircuitOrderer(subcircuitEditors);
    List<SubcircuitEditor> orderedSubcircuitEditors = orderer.order();

    if (orderedSubcircuitEditors != null)
    {
      List<SubcircuitData> subcircuitDatas = new ArrayList<>();
      for (SubcircuitEditor subcircuitEditor : orderedSubcircuitEditors)
      {
        SubcircuitData subcircuitData = subcircuitEditor.save();
        subcircuitDatas.add(subcircuitData);
      }

      List<CircuitSimulationData> circuitSimulationDatas = new ArrayList<>();
      for (TopLevelSubcircuitSimulation topLevelSubcircuitSimulation : simulations)
      {
        CircuitSimulationData circuitSimulationData = topLevelSubcircuitSimulation.save();
        circuitSimulationDatas.add(circuitSimulationData);
      }

      return new CircuitData(subcircuitDatas,
                             circuitSimulationDatas,
                             currentSubcircuitEditor.getId(),
                             getCurrentSimulationId());
    }
    else
    {
      return null;
    }
  }

  protected long getCurrentSimulationId()
  {
    if (currentSimulation != null)
    {
      return currentSimulation.getCircuitSimulation().getId();
    }
    else
    {
      return 0L;
    }
  }

  public ClipboardData copyViews(List<View> views)
  {
    ArrayList<StaticData<?>> componentDatas = new ArrayList<>();
    ArrayList<TraceData> traceDatas = new ArrayList<>();
    for (View view : views)
    {
      if (view instanceof StaticView)
      {
        StaticView<?> staticView = (StaticView<?>) view;
        StaticData<?> staticData = (StaticData<?>) staticView.save(false);
        if (staticData == null)
        {
          throw new SimulatorException("%s save may not return null.", staticView.toIdentifierString());
        }

        componentDatas.add(staticData);
      }
      else if (view instanceof TraceView)
      {
        TraceView traceView = (TraceView) view;
        TraceData traceData = traceView.save(false);
        traceDatas.add(traceData);
      }
    }

    return new ClipboardData(componentDatas, traceDatas);
  }

  public void load(CircuitData circuitData)
  {
    currentSubcircuitEditor = null;

    SubcircuitEditor.resetNextId();
    Event.resetNextId();
    CircuitSimulation.resetNextId();
    View.resetNextId();
    TraceLoader traceLoader = new TraceLoader();

    subcircuitEditors = new ArrayList<>();
    for (SubcircuitData subcircuitData : circuitData.subcircuits)
    {
      SubcircuitEditor subcircuitEditor = new SubcircuitEditor(this, subcircuitData.typeName, subcircuitData.id);
      subcircuitEditors.add(subcircuitEditor);
    }

    simulations = new ArrayList<>();
    for (CircuitSimulationData circuitSimulationData : circuitData.circuitSimulationDatas)
    {
      SubcircuitEditor subcircuitEditor = getSubcircuitEditor(circuitSimulationData.subcircuitId);
      CircuitSimulation circuitSimulation = new CircuitSimulation(circuitSimulationData.circuitSimulationId, circuitSimulationData.circuitSimulationName);
      simulations.add(new TopLevelSubcircuitSimulation(subcircuitEditor, circuitSimulation));
      circuitSimulation.getSimulation().getTimeline().load(circuitSimulationData.timeline);
    }

    Map<SubcircuitData, Map<ViewData, View>> maybe = new LinkedHashMap<>();
    for (SubcircuitData subcircuitData : circuitData.subcircuits)
    {
      SubcircuitEditor subcircuitEditor = getSubcircuitEditor(subcircuitData.id);
      Map<ViewData, View> views = subcircuitEditor.loadViews(subcircuitData.traces,
                                                             subcircuitData.components,
                                                             false,
                                                             false);
      maybe.put(subcircuitData, views);
    }

    for (CircuitSimulationData circuitSimulationData : circuitData.circuitSimulationDatas)
    {
      TopLevelSubcircuitSimulation topLevelSimulation = getTopLevelSimulation(circuitSimulationData.circuitSimulationId);
      if (topLevelSimulation == null)
      {
        throw new SimulatorException("Could not find TopLevelSubcircuitSimulation with id [%s].", circuitSimulationData.circuitSimulationId);
      }
      CircuitSimulation circuitSimulation = topLevelSimulation.getCircuitSimulation();

      for (SubcircuitData subcircuitData : circuitData.subcircuits)
      {
        Map<ViewData, View> views = maybe.get(subcircuitData);
        SubcircuitEditor subcircuitEditor = getSubcircuitEditor(subcircuitData.id);
        subcircuitEditor.loadComponents(views,
                                        circuitSimulation,
                                        traceLoader);
      }

      circuitSimulation.getSimulation().getTimeline().load(circuitSimulationData.timeline);
    }

    currentSubcircuitEditor = getCurrentSubcircuitEditor(circuitData.currentSubcircuit);
    currentSimulation = getTopLevelSimulation(circuitData.currentSimulation);
  }

  protected SubcircuitEditor getSubcircuitEditor(long id)
  {
    for (SubcircuitEditor subcircuitEditor : subcircuitEditors)
    {
      if (subcircuitEditor.getId() == id)
      {
        return subcircuitEditor;
      }
    }
    return null;
  }

  private TopLevelSubcircuitSimulation getTopLevelSimulation(long simulationId)
  {
    for (TopLevelSubcircuitSimulation topLevelSubcircuitSimulation : simulations)
    {
      if (topLevelSubcircuitSimulation.getCircuitSimulation().getId() == simulationId)
      {
        return topLevelSubcircuitSimulation;
      }
    }
    return null;
  }

  protected SubcircuitEditor getCurrentSubcircuitEditor(long subcircuitId)
  {
    SubcircuitEditor subcircuitEditor = getSubcircuitEditor(subcircuitId);
    if (subcircuitEditor != null)
    {
      return subcircuitEditor;
    }
    else
    {
      return subcircuitEditors.get(0);
    }
  }

  public List<View> pasteClipboardViews(List<TraceData> traces, List<StaticData<?>> components)
  {
    return currentSubcircuitEditor.pasteClipboardViews(traces, components);
  }

  public void recreateComponentView(StaticView<?> staticView)
  {
    currentSubcircuitEditor.recreateComponentView(staticView, getCircuitSimulation());
  }

  public void startMoveComponents(List<StaticView<?>> staticViews, List<TraceView> traceViews)
  {
    currentSubcircuitEditor.startMoveComponents(staticViews, traceViews, getCircuitSimulation());
  }

  public void doneMoveComponents(List<StaticView<?>> staticViews, List<TraceView> traceViews, Set<StaticView<?>> selectedViews, boolean newComponents)
  {
    currentSubcircuitEditor.doneMoveComponents(staticViews, traceViews, selectedViews, getCircuitSimulation(), newComponents);
  }

  public Selection getCurrentSelection()
  {
    return currentSubcircuitEditor.getSelection();
  }

  public List<View> getSelectionFromRectangle(Float2D start, Float2D end)
  {
    return currentSubcircuitEditor.getSelectionFromRectangle(start, end);
  }

  public void deleteSelection()
  {
    currentSubcircuitEditor.deleteSelection(getCircuitSimulation());
  }

  public SubcircuitEditor getCurrentSubcircuitEditor()
  {
    return currentSubcircuitEditor;
  }

  public SubcircuitEditor getSubcircuitEditor(String subcircuitTypeName)
  {
    for (SubcircuitEditor subcircuitEditor : subcircuitEditors)
    {
      if (subcircuitEditor.getTypeName().equals(subcircuitTypeName))
      {
        return subcircuitEditor;
      }
    }
    return null;
  }

  public List<SubcircuitEditor> getSubcircuitEditors()
  {
    return new ArrayList<>(subcircuitEditors);
  }

  public boolean isCurrentSelectionEmpty()
  {
    return currentSubcircuitEditor.isSelectionEmpty();
  }

  public ConnectionView getConnectionInCurrentSubcircuit(int x, int y)
  {
    return currentSubcircuitEditor.getConnection(x, y);
  }

  public void deleteComponentView(StaticView<?> staticView)
  {
    deleteComponentView(staticView, currentSubcircuitEditor, getCircuitSimulation());
  }

  public void deleteComponentView(StaticView<?> staticView, SubcircuitEditor subcircuitEditor, CircuitSimulation circuitSimulation)
  {
    subcircuitEditor.deleteComponentView(staticView, circuitSimulation);
  }

  public void validateConsistency()
  {
    currentSubcircuitEditor.validateConsistency();
  }

  public void replaceSelection(View newView, View oldView)
  {
    currentSubcircuitEditor.replaceSelection(newView, oldView);
  }

  public List<View> duplicateViews(List<View> views)
  {
    ClipboardData clipboardData = copyViews(views);
    return pasteClipboardViews(clipboardData.getTraces(), clipboardData.getComponents());
  }

  public void removeTraceView(TraceView traceView)
  {
    currentSubcircuitEditor.removeTraceView(traceView);
  }

  public void deleteComponentViews(List<StaticView<?>> staticViews)
  {
    currentSubcircuitEditor.deleteComponentViews(staticViews, getCircuitSimulation());
  }

  public CircuitSimulation getCircuitSimulation()
  {
    if (currentSimulation != null)
    {
      return currentSimulation.getCircuitSimulation();
    }
    else
    {
      return null;
    }
  }

  public SubcircuitView getCurrentSubcircuitView()
  {
    return currentSubcircuitEditor.getSubcircuitView();
  }

  public String gotoSubcircuit(SubcircuitEditor subcircuitEditor)
  {
    if (subcircuitEditor != null)
    {
      if (subcircuitEditors.contains(subcircuitEditor))
      {
        return setCurrentSubcircuitEditor(subcircuitEditor);
      }
    }
    return null;
  }

  public String setCurrentSubcircuitEditor(SubcircuitEditor subcircuitEditor)
  {
    currentSubcircuitEditor = subcircuitEditor;
    return currentSubcircuitEditor.getTypeName();
  }

  public boolean hasMultipleSubcircuits()
  {
    return subcircuitEditors.size() > 1;
  }

  public String gotoPreviousSubcircuit()
  {
    int index = subcircuitEditors.indexOf(currentSubcircuitEditor);
    if (index != -1)
    {
      index--;
      if (index < 0)
      {
        index = subcircuitEditors.size() - 1;
      }
      return setCurrentSubcircuitEditor(subcircuitEditors.get(index));
    }
    return null;
  }

  public String gotoNextSubcircuit()
  {
    int index = subcircuitEditors.indexOf(currentSubcircuitEditor);
    if (index != -1)
    {
      index++;
      if (index > subcircuitEditors.size() - 1)
      {
        index = 0;
      }
      return setCurrentSubcircuitEditor(subcircuitEditors.get(index));
    }
    return null;
  }

  public void addNewSubcircuit(String subcircuitName)
  {
    String error = getSubcircuitNameError(subcircuitName);
    if (error != null)
    {
      throw new SimulatorException(error);
    }

    SubcircuitEditor subcircuitEditor = new SubcircuitEditor(this, subcircuitName);
    subcircuitEditors.add(subcircuitEditor);

    currentSubcircuitEditor = subcircuitEditor;

    currentSimulation = addNewSimulation(subcircuitEditor);
  }

  public TopLevelSubcircuitSimulation addNewSimulation(SubcircuitEditor subcircuitEditor)
  {
    TopLevelSubcircuitSimulation topLevelSubcircuitSimulation = new TopLevelSubcircuitSimulation(subcircuitEditor, new CircuitSimulation());
    simulations.add(topLevelSubcircuitSimulation);
    return topLevelSubcircuitSimulation;
  }

  public String getSubcircuitNameError(String subcircuitName)
  {
    if (StringUtil.isEmptyOrNull(subcircuitName))
    {
      return "Cannot add a subcircuit type named [].";
    }

    for (SubcircuitEditor subcircuitEditor : subcircuitEditors)
    {
      if (subcircuitEditor.getTypeName().equals(subcircuitName))
      {
        return "Cannot add a subcircuit type named [%s], it is already in use.";
      }
    }
    return null;
  }

  public TopLevelSubcircuitSimulation getCurrentTopLevelSimulation()
  {
    return currentSimulation;
  }
}

