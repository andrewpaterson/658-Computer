package net.logicim.ui.simulation;

import net.logicim.common.SimulatorException;
import net.logicim.common.dependency.Orderer;
import net.logicim.common.type.Float2D;
import net.logicim.common.type.Int2D;
import net.logicim.common.util.StringUtil;
import net.logicim.data.circuit.CircuitData;
import net.logicim.data.circuit.SubcircuitData;
import net.logicim.data.circuit.TimelineData;
import net.logicim.data.integratedcircuit.common.StaticData;
import net.logicim.data.wire.TraceData;
import net.logicim.domain.CircuitSimulation;
import net.logicim.domain.Simulation;
import net.logicim.domain.common.Circuit;
import net.logicim.domain.common.Timeline;
import net.logicim.ui.circuit.SubcircuitView;
import net.logicim.ui.clipboard.ClipboardData;
import net.logicim.ui.common.ConnectionView;
import net.logicim.ui.common.HoverConnectionView;
import net.logicim.ui.common.Viewport;
import net.logicim.ui.common.integratedcircuit.StaticView;
import net.logicim.ui.common.integratedcircuit.View;
import net.logicim.ui.common.wire.TraceView;
import net.logicim.ui.shape.common.BoundingBox;
import net.logicim.ui.simulation.selection.Selection;

import java.awt.*;
import java.util.List;
import java.util.*;

public class CircuitEditor
{
  protected CircuitSimulation simulation;

  protected List<SubcircuitEditor> subcircuitEditors;
  protected SubcircuitEditor currentSubcircuitEditor;

  public CircuitEditor()
  {
    simulation = new CircuitSimulation();
    subcircuitEditors = new ArrayList<>();
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
    long time = getTime();
    List<View> views;
    synchronized (this)
    {
      views = getAllViews();
    }

    for (View view : views)
    {
      view.paint(graphics, viewport, time);
    }
  }

  protected long getTime()
  {
    return simulation.getTime();
  }

  public Circuit getCircuit()
  {
    return simulation.getCircuit();
  }

  public void editActionDeleteTraceView(ConnectionView connectionView, TraceView traceView)
  {
    if (connectionView instanceof HoverConnectionView)
    {
      Set<TraceView> traceViews = new LinkedHashSet<>();
      traceViews.add(traceView);
      currentSubcircuitEditor.deleteTraceViews(traceViews, simulation);
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
      currentSubcircuitEditor.deleteTraceViews(traceViews, simulation);
      return true;
    }
    else
    {
      return false;
    }
  }

  public void reset()
  {
    simulation.reset();
  }

  public void runSimultaneous()
  {
    simulation.runSimultaneous();
  }

  public void runToTime(long timeForward)
  {
    simulation.runToTime(timeForward);
  }

  public StaticView<?> getComponentViewInScreenSpace(Viewport viewport, Int2D screenPosition)
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
    ArrayList<SubcircuitData> data = new ArrayList<>();

    LinkedHashMap<SubcircuitEditor, List<String>> fulfillmentsMap = new LinkedHashMap<>();
    LinkedHashMap<SubcircuitEditor, List<String>> requirementsMap = new LinkedHashMap<>();
    for (SubcircuitEditor subcircuitEditor : subcircuitEditors)
    {
      fulfillmentsMap.put(subcircuitEditor, subcircuitEditor.getTypeNameAsList());
      requirementsMap.put(subcircuitEditor, subcircuitEditor.getSubcircuitInstanceNames());
    }

    List<SubcircuitEditor> orderedSubcircuitEditors = Orderer.order(fulfillmentsMap, requirementsMap);
    for (SubcircuitEditor subcircuitEditor : orderedSubcircuitEditors)
    {
      SubcircuitData subcircuitData = subcircuitEditor.save();
      data.add(subcircuitData);
    }

    TimelineData timelineData = getTimeline().save();
    return new CircuitData(timelineData, data);
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
    getTimeline().load(circuitData.timeline);

    for (SubcircuitData subcircuitData : circuitData.subcircuit)
    {
      SubcircuitEditor subcircuitEditor = new SubcircuitEditor(this, subcircuitData.typeName);
      subcircuitEditors.add(subcircuitEditor);

      subcircuitEditor.loadViews(subcircuitData, simulation);

      if (currentSubcircuitEditor == null)
      {
        currentSubcircuitEditor = subcircuitEditor;
      }
    }
  }

  public List<View> pasteClipboardViews(List<TraceData> traces, List<StaticData<?>> components)
  {
    return currentSubcircuitEditor.pasteClipboardViews(traces, components, simulation);
  }

  public Timeline getTimeline()
  {
    return simulation.getSimulation().getTimeline();
  }

  public Simulation getSimulation()
  {
    return simulation.getSimulation();
  }

  public void placeComponentView(StaticView<?> staticView)
  {
    currentSubcircuitEditor.placeComponentView(staticView, simulation);
  }

  public void startMoveComponents(List<StaticView<?>> staticViews, List<TraceView> traceViews)
  {
    currentSubcircuitEditor.startMoveComponents(staticViews, traceViews, simulation);
  }

  public void doneMoveComponents(List<StaticView<?>> staticViews, List<TraceView> traceViews, Set<StaticView<?>> selectedViews)
  {
    currentSubcircuitEditor.doneMoveComponents(staticViews, traceViews, selectedViews, simulation);
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
    currentSubcircuitEditor.deleteSelection(simulation);
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
    currentSubcircuitEditor.deleteComponentView(staticView, simulation);
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
    return currentSubcircuitEditor.duplicateViews(views, simulation);
  }

  public void removeTraceView(TraceView traceView)
  {
    currentSubcircuitEditor.removeTraceView(traceView);
  }

  public void deleteComponentViews(List<StaticView<?>> staticViews)
  {
    currentSubcircuitEditor.deleteComponentViews(staticViews, simulation);
  }

  public CircuitSimulation getCircuitSimulation()
  {
    return simulation;
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
}

