package net.logicim.ui.simulation;

import net.logicim.common.SimulatorException;
import net.logicim.common.type.Float2D;
import net.logicim.common.type.Int2D;
import net.logicim.common.util.StringUtil;
import net.logicim.data.circuit.CircuitData;
import net.logicim.data.circuit.SubcircuitEditorData;
import net.logicim.data.common.ViewData;
import net.logicim.data.integratedcircuit.common.StaticData;
import net.logicim.data.simulation.CircuitSimulationData;
import net.logicim.data.simulation.SubcircuitInstanceSimulationData;
import net.logicim.data.simulation.SubcircuitSimulationData;
import net.logicim.data.simulation.SubcircuitTopSimulationData;
import net.logicim.data.wire.TraceData;
import net.logicim.domain.CircuitSimulation;
import net.logicim.domain.common.Circuit;
import net.logicim.domain.common.event.Event;
import net.logicim.domain.passive.subcircuit.SubcircuitInstance;
import net.logicim.domain.passive.subcircuit.SubcircuitSimulation;
import net.logicim.ui.circuit.SubcircuitView;
import net.logicim.ui.clipboard.ClipboardData;
import net.logicim.ui.common.ConnectionView;
import net.logicim.ui.common.HoverConnectionView;
import net.logicim.ui.common.Viewport;
import net.logicim.ui.common.integratedcircuit.StaticView;
import net.logicim.ui.common.integratedcircuit.View;
import net.logicim.ui.common.wire.TraceView;
import net.logicim.ui.shape.common.BoundingBox;
import net.logicim.ui.simulation.component.subcircuit.SubcircuitInstanceView;
import net.logicim.ui.simulation.order.SubcircuitEditorOrderer;
import net.logicim.ui.simulation.selection.Selection;
import net.logicim.ui.simulation.subcircuit.SubcircuitEditor;
import net.logicim.ui.simulation.subcircuit.SubcircuitTopSimulation;

import java.awt.*;
import java.util.List;
import java.util.*;

public class CircuitEditor
{
  protected List<SubcircuitEditor> subcircuitEditors;
  protected SubcircuitEditor currentSubcircuitEditor;
  protected CircuitSimulation currentCircuitSimulation;

  public CircuitEditor()
  {
    subcircuitEditors = new ArrayList<>();
    currentSubcircuitEditor = null;
    currentCircuitSimulation = null;
  }

  public CircuitEditor(String mainSubcircuitTypeName)
  {
    this();
    addNewSubcircuit(mainSubcircuitTypeName);
  }

  private List<View> getAllViews()
  {
    if (currentSubcircuitEditor != null)
    {
      return currentSubcircuitEditor.getAllViews();
    }
    else
    {
      return new ArrayList<>();
    }
  }

  public void paint(Graphics2D graphics, Viewport viewport)
  {
    List<View> views;
    synchronized (this)
    {
      views = getAllViews();
    }

    SubcircuitSimulation circuitSimulation = getSubcircuitSimulation();
    for (View view : views)
    {
      view.paint(graphics, viewport, circuitSimulation);
    }
  }

  public CircuitSimulation getCurrentCircuitSimulation()
  {
    return currentCircuitSimulation;
  }

  public SubcircuitTopSimulation getCurrentSubcircuitTopSimulation()
  {
    List<SubcircuitTopSimulation> subcircuitTopSimulations = getSubcircuitTopSimulations();
    for (SubcircuitTopSimulation subcircuitTopSimulation : subcircuitTopSimulations)
    {
      if (subcircuitTopSimulation.getCircuitSimulation() == currentCircuitSimulation)
      {
        return subcircuitTopSimulation;
      }
    }
    return null;
  }


  public Circuit getCircuit()
  {
    SubcircuitSimulation subcircuitSimulation = getSubcircuitSimulation();
    if (subcircuitSimulation != null)
    {
      return subcircuitSimulation.getCircuit();
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
      currentSubcircuitEditor.deleteTraceViews(traceViews, getSubcircuitSimulation());
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
      currentSubcircuitEditor.deleteTraceViews(traceViews, getSubcircuitSimulation());
      return true;
    }
    else
    {
      return false;
    }
  }

  public void runSimultaneous()
  {
    getSubcircuitSimulation().runSimultaneous();
  }

  public void runToTime(long timeForward)
  {
    getSubcircuitSimulation().runToTime(timeForward);
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
    SubcircuitEditorOrderer orderer = new SubcircuitEditorOrderer(subcircuitEditors);
    List<SubcircuitEditor> orderedSubcircuitEditors = orderer.order();

    if (orderedSubcircuitEditors != null)
    {
      Set<CircuitSimulation> circuitSimulations = getCircuitSimulationsForSave(orderedSubcircuitEditors);
      List<CircuitSimulationData> circuitSimulationDatas = saveCircuitSimulations(circuitSimulations);

      Set<SubcircuitSimulation> subcircuitSimulations = getSubcircuitSimulationsForSave(orderedSubcircuitEditors);
      List<SubcircuitSimulationData> subcircuitSimulationDatas = saveSubcircuitSimulations(subcircuitSimulations);

      List<SubcircuitEditorData> subcircuitEditorDatas = saveSubcircuitEditor(orderedSubcircuitEditors);

      return new CircuitData(subcircuitEditorDatas,
                             circuitSimulationDatas,
                             subcircuitSimulationDatas,
                             currentSubcircuitEditor.getId(),
                             getCurrentSimulationId());
    }
    else
    {
      return null;
    }
  }

  protected List<SubcircuitEditorData> saveSubcircuitEditor(List<SubcircuitEditor> orderedSubcircuitEditors)
  {
    List<SubcircuitEditorData> subcircuitEditorDatas = new ArrayList<>();
    for (SubcircuitEditor subcircuitEditor : orderedSubcircuitEditors)
    {
      SubcircuitEditorData subcircuitEditorData = subcircuitEditor.save();
      subcircuitEditorDatas.add(subcircuitEditorData);
    }
    return subcircuitEditorDatas;
  }

  protected List<SubcircuitSimulationData> saveSubcircuitSimulations(Set<SubcircuitSimulation> subcircuitSimulations)
  {
    List<SubcircuitSimulationData> subcircuitSimulationDatas = new ArrayList<>();
    for (SubcircuitSimulation subcircuitSimulation : subcircuitSimulations)
    {
      SubcircuitSimulationData subcircuitSimulationData = subcircuitSimulation.save();
      subcircuitSimulationDatas.add(subcircuitSimulationData);
    }
    return subcircuitSimulationDatas;
  }

  protected List<CircuitSimulationData> saveCircuitSimulations(Set<CircuitSimulation> circuitSimulations)
  {
    List<CircuitSimulationData> circuitSimulationDatas = new ArrayList<>();
    for (CircuitSimulation circuitSimulation : circuitSimulations)
    {
      CircuitSimulationData circuitSimulationData = circuitSimulation.save();
      circuitSimulationDatas.add(circuitSimulationData);
    }
    return circuitSimulationDatas;
  }

  protected Set<CircuitSimulation> getCircuitSimulationsForSave(List<SubcircuitEditor> orderedSubcircuitEditors)
  {
    Set<CircuitSimulation> circuitSimulations = new LinkedHashSet<>();
    for (SubcircuitEditor subcircuitEditor : orderedSubcircuitEditors)
    {
      Map<CircuitSimulation, SubcircuitSimulation> simulations = subcircuitEditor.getSimulations();
      for (SubcircuitSimulation subcircuitSimulation : simulations.values())
      {
        circuitSimulations.add(subcircuitSimulation.getCircuitSimulation());
      }
    }
    return circuitSimulations;
  }

  protected Set<SubcircuitSimulation> getSubcircuitSimulationsForSave(List<SubcircuitEditor> orderedSubcircuitEditors)
  {
    Set<SubcircuitSimulation> subcircuitSimulations = new LinkedHashSet<>();
    for (SubcircuitEditor subcircuitEditor : orderedSubcircuitEditors)
    {
      Map<CircuitSimulation, SubcircuitSimulation> simulations = subcircuitEditor.getSimulations();
      for (SubcircuitSimulation subcircuitSimulation : simulations.values())
      {
        subcircuitSimulations.add(subcircuitSimulation);
      }
    }
    return subcircuitSimulations;
  }

  protected long getCurrentSimulationId()
  {
    if (currentCircuitSimulation != null)
    {
      return currentCircuitSimulation.getId();
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

    CircuitLoaders loaders = new CircuitLoaders();
    SubcircuitEditor.resetNextId();
    Event.resetNextId();
    View.resetNextId();

    subcircuitEditors = new ArrayList<>();
    Map<Long, SubcircuitEditor> subcircuitEditorMap = new HashMap<>();
    Map<SubcircuitEditorData, Map<ViewData, View>> subcircuitEditorViews = new LinkedHashMap<>();
    for (SubcircuitEditorData subcircuitEditorData : circuitData.subcircuits)
    {
      SubcircuitEditor subcircuitEditor = new SubcircuitEditor(this, subcircuitEditorData.typeName, subcircuitEditorData.id);
      subcircuitEditors.add(subcircuitEditor);
      subcircuitEditorMap.put(subcircuitEditor.getId(), subcircuitEditor);
      Map<ViewData, View> views = subcircuitEditor.loadViews(subcircuitEditorData.subcircuit.traces,
                                                             subcircuitEditorData.subcircuit.components,
                                                             false,
                                                             false);
      subcircuitEditorViews.put(subcircuitEditorData, views);
    }

    for (CircuitSimulationData circuitSimulationData : circuitData.circuitSimulations)
    {
      CircuitSimulation circuitSimulation = loaders.createCircuitSimulation(circuitSimulationData.name, circuitSimulationData.id);
      circuitSimulation.getTimeline().load(circuitSimulationData.timeline);
    }

    for (SubcircuitSimulationData subcircuitSimulationData : circuitData.subcircuitSimulations)
    {
      CircuitSimulation circuitSimulation = loaders.getCircuitSimulation(subcircuitSimulationData.circuitSimulationId);
      if (circuitSimulation == null)
      {
        throw new SimulatorException("Cannot find a circuit simulation with id [%s].", subcircuitSimulationData.circuitSimulationId);
      }

      if (subcircuitSimulationData instanceof SubcircuitTopSimulationData)
      {
        SubcircuitTopSimulationData subcircuitTopSimulationData = (SubcircuitTopSimulationData) subcircuitSimulationData;
        SubcircuitEditor subcircuitEditor = subcircuitEditorMap.get(subcircuitTopSimulationData.subcircuitEditorId);
        SubcircuitTopSimulation subcircuitTopSimulation = loaders.createSubcircuitTopSimulation(circuitSimulation, subcircuitEditor, subcircuitSimulationData.subcircuitSimulationId);
        circuitSimulation.reset(subcircuitTopSimulation);
      }
      else if (subcircuitSimulationData instanceof SubcircuitInstanceSimulationData)
      {
        SubcircuitInstanceSimulationData subcircuitInstanceSimulationData = (SubcircuitInstanceSimulationData) subcircuitSimulationData;
        SubcircuitInstance subcircuitInstance = loaders.getSubcircuitInstance(subcircuitInstanceSimulationData.subcircuitInstanceId);
        loaders.createSubcircuitInstanceSimulation(circuitSimulation, subcircuitInstance, subcircuitSimulationData.subcircuitSimulationId);
      }
      else
      {
        throw new SimulatorException("Cannot load subcircuit simulations with unknown data type [%s].", subcircuitSimulationData.getClass().getSimpleName());
      }
    }

    for (SubcircuitSimulationData subcircuitSimulationData : circuitData.subcircuitSimulations)
    {
      for (SubcircuitEditorData subcircuitData : circuitData.subcircuits)
      {
        Map<ViewData, View> viewDataViewMap = subcircuitEditorViews.get(subcircuitData);
        SubcircuitEditor subcircuitEditor = getSubcircuitEditor(subcircuitData.id);
        SubcircuitSimulation subcircuitSimulation = loaders.getSubcircuitSimulation(subcircuitSimulationData.subcircuitSimulationId);
        subcircuitEditor.loadComponents(viewDataViewMap,
                                        subcircuitSimulation,
                                        loaders);
      }
    }

    currentSubcircuitEditor = getCurrentSubcircuitEditor(circuitData.currentSubcircuit);
    currentCircuitSimulation = loaders.getCircuitSimulation(circuitData.currentSimulation);
  }

  protected SubcircuitEditor getSubcircuitEditor(long subcircuitId)
  {
    for (SubcircuitEditor subcircuitEditor : subcircuitEditors)
    {
      if (subcircuitEditor.getId() == subcircuitId)
      {
        return subcircuitEditor;
      }
    }
    return null;
  }

  protected SubcircuitEditor getCurrentSubcircuitEditor(long currentSubcircuitId)
  {
    SubcircuitEditor subcircuitEditor = getSubcircuitEditor(currentSubcircuitId);
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
    currentSubcircuitEditor.recreateComponentView(staticView, getSubcircuitSimulation());
  }

  public void startMoveComponents(List<StaticView<?>> staticViews, List<TraceView> traceViews)
  {
    currentSubcircuitEditor.startMoveComponents(staticViews, traceViews, getSubcircuitSimulation());
  }

  public void doneMoveComponents(List<StaticView<?>> staticViews, List<TraceView> traceViews, Set<StaticView<?>> selectedViews, boolean newComponents)
  {
    currentSubcircuitEditor.doneMoveComponents(staticViews, traceViews, selectedViews, getSubcircuitSimulation(), newComponents);
  }

  public Selection getCurrentSelection()
  {
    if (currentSubcircuitEditor != null)
    {
      return currentSubcircuitEditor.getSelection();
    }
    else
    {
      return null;
    }
  }

  public List<View> getSelectionFromRectangle(Float2D start, Float2D end)
  {
    return currentSubcircuitEditor.getSelectionFromRectangle(start, end);
  }

  public void deleteSelection()
  {
    currentSubcircuitEditor.deleteSelection(getSubcircuitSimulation());
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
    deleteComponentView(staticView, currentSubcircuitEditor, getSubcircuitSimulation());
  }

  public void deleteComponentView(StaticView<?> staticView, SubcircuitEditor subcircuitEditor, SubcircuitSimulation circuit)
  {
    subcircuitEditor.deleteComponentView(staticView, circuit);
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
    currentSubcircuitEditor.deleteComponentViews(staticViews, getSubcircuitSimulation());
  }

  public SubcircuitSimulation getSubcircuitSimulation()
  {
    return currentSubcircuitEditor.getSubcircuitSimulation(currentCircuitSimulation);
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

  private void setCurrentCircuitSimulation(SubcircuitEditor subcircuitEditor)
  {
    SubcircuitSimulation subcircuitSimulation = subcircuitEditor.getSubcircuitSimulation(currentCircuitSimulation);
    if (subcircuitSimulation == null)
    {
      List<SubcircuitTopSimulation> simulations = subcircuitEditor.getTopSimulations();
      if (!simulations.isEmpty())
      {
        currentCircuitSimulation = simulations.get(0).getCircuitSimulation();
      }
      else
      {
        throw new SimulatorException("No top level simulation found for subcircuit editor.");
      }
    }
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
      SubcircuitEditor subcircuitEditor = subcircuitEditors.get(index);
      setCurrentCircuitSimulation(subcircuitEditor);
      return setCurrentSubcircuitEditor(subcircuitEditor);
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
      SubcircuitEditor subcircuitEditor = subcircuitEditors.get(index);
      setCurrentCircuitSimulation(subcircuitEditor);
      return setCurrentSubcircuitEditor(subcircuitEditor);
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
    setCurrentCircuitSimulation(subcircuitEditor);
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

  public void circuitUpdated()
  {
    SubcircuitEditor currentSubcircuitEditor = getCurrentSubcircuitEditor();
    SubcircuitView currentSubcircuitView = currentSubcircuitEditor.getSubcircuitView();

    SubcircuitEditorOrderer orderer = new SubcircuitEditorOrderer(getSubcircuitEditors());
    List<SubcircuitEditor> orderedSubcircuitEditors = orderer.order();

    Map<SubcircuitEditor, List<SubcircuitInstanceView>> map = getSubcircuitInstanceViewsInSubcircuitEditor(currentSubcircuitEditor, currentSubcircuitView, orderedSubcircuitEditors);

    recreateSubcircuitInstanceViews(map);
  }

  protected void recreateSubcircuitInstanceViews(Map<SubcircuitEditor, List<SubcircuitInstanceView>> map)
  {
    for (Map.Entry<SubcircuitEditor, List<SubcircuitInstanceView>> entry : map.entrySet())
    {
      SubcircuitEditor containingSubcircuitEditor = entry.getKey();
      List<SubcircuitInstanceView> instanceViews = entry.getValue();
      for (SubcircuitInstanceView instanceView : instanceViews)
      {
        SubcircuitSimulation circuit = getSubcircuitSimulation();
        deleteComponentView(instanceView, containingSubcircuitEditor, circuit);

        instanceView = (SubcircuitInstanceView) instanceView.duplicate(this,
                                                                       containingSubcircuitEditor.getSubcircuitView(),
                                                                       instanceView.getProperties());
        containingSubcircuitEditor.recreateComponentView(instanceView, circuit);
      }
    }
  }

  protected Map<SubcircuitEditor, List<SubcircuitInstanceView>> getSubcircuitInstanceViewsInSubcircuitEditor(SubcircuitEditor currentSubcircuitEditor, SubcircuitView currentSubcircuitView, List<SubcircuitEditor> orderedSubcircuitEditors)
  {
    Map<SubcircuitEditor, List<SubcircuitInstanceView>> map = new LinkedHashMap<>();
    if (orderedSubcircuitEditors != null)
    {
      for (SubcircuitEditor containingSubcircuitEditor : orderedSubcircuitEditors)
      {
        if (containingSubcircuitEditor != currentSubcircuitEditor)
        {
          List<SubcircuitInstanceView> instanceViews = getSubcircuitInstanceView(currentSubcircuitView,
                                                                                 containingSubcircuitEditor.getSubcircuitView());
          map.put(containingSubcircuitEditor, instanceViews);
        }
      }
    }
    return map;
  }

  protected List<SubcircuitInstanceView> getSubcircuitInstanceView(SubcircuitView currentSubcircuitView, SubcircuitView containingSubcircuitView)
  {
    Set<SubcircuitInstanceView> subcircuitInstanceViews = containingSubcircuitView.findAllSubcircuitInstanceViews();

    List<SubcircuitInstanceView> instanceViews = new ArrayList<>();
    for (SubcircuitInstanceView subcircuitInstanceView : subcircuitInstanceViews)
    {
      if (subcircuitInstanceView.getInstanceSubcircuitView() == currentSubcircuitView)
      {
        instanceViews.add(subcircuitInstanceView);
      }
    }
    return instanceViews;
  }

  public List<CircuitSimulation> getCircuitSimulations()
  {
    ArrayList<CircuitSimulation> result = new ArrayList<>();
    List<SubcircuitTopSimulation> subcircuitTopSimulations = getSubcircuitTopSimulations();
    for (SubcircuitTopSimulation subcircuitTopSimulation : subcircuitTopSimulations)
    {
      result.add(subcircuitTopSimulation.getCircuitSimulation());
    }
    return result;
  }

  public List<SubcircuitTopSimulation> getSubcircuitTopSimulations()
  {
    ArrayList<SubcircuitTopSimulation> result = new ArrayList<>();
    for (SubcircuitEditor subcircuitEditor : subcircuitEditors)
    {
      Collection<SubcircuitSimulation> subcircuitSimulations = subcircuitEditor.getSimulations().values();
      for (SubcircuitSimulation subcircuitSimulation : subcircuitSimulations)
      {
        if (subcircuitSimulation instanceof SubcircuitTopSimulation)
        {
          result.add((SubcircuitTopSimulation) subcircuitSimulation);
        }
      }
    }
    return result;
  }

  public void setCurrentCircuitSimulation(CircuitSimulation circuitSimulation)
  {
    this.currentCircuitSimulation = circuitSimulation;
  }
}

