package net.logicim.ui.simulation;

import net.logicim.common.SimulatorException;
import net.logicim.common.type.Float2D;
import net.logicim.common.type.Int2D;
import net.logicim.data.circuit.CircuitData;
import net.logicim.data.circuit.LastSubcircuitSimulationData;
import net.logicim.data.circuit.SubcircuitEditorData;
import net.logicim.data.integratedcircuit.common.StaticData;
import net.logicim.data.simulation.*;
import net.logicim.data.subciruit.SubcircuitInstanceData;
import net.logicim.data.subciruit.SubcircuitInstanceSimulationSimulationData;
import net.logicim.data.wire.TraceData;
import net.logicim.domain.CircuitSimulation;
import net.logicim.domain.common.Circuit;
import net.logicim.domain.common.Described;
import net.logicim.domain.common.event.Event;
import net.logicim.domain.passive.subcircuit.SubcircuitInstanceSimulation;
import net.logicim.domain.passive.subcircuit.SubcircuitSimulation;
import net.logicim.domain.passive.subcircuit.SubcircuitTopSimulation;
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
import net.logicim.ui.simulation.subcircuit.SubcircuitTopEditorSimulation;
import net.logicim.ui.subcircuit.SubcircuitList;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.*;

public class CircuitEditor
{
  protected SubcircuitList subcircuitEditors;

  protected SubcircuitEditor currentSubcircuitEditor;
  protected SubcircuitSimulation currentSubcircuitSimulation;
  protected Map<SubcircuitEditor, SubcircuitSimulation> lastSubcircuitEditorSimulation;

  public CircuitEditor(SubcircuitList subcircuitList)
  {
    subcircuitEditors = subcircuitList;
    subcircuitList.clear(true);

    currentSubcircuitEditor = null;
    currentSubcircuitSimulation = null;
    lastSubcircuitEditorSimulation = new LinkedHashMap<>();
  }

  public CircuitEditor(String mainSubcircuitTypeName, SubcircuitList subcircuitList)
  {
    this(subcircuitList);
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

    SubcircuitSimulation subcircuitSimulation = getSubcircuitSimulation();
    for (View view : views)
    {
      view.paint(graphics, viewport, subcircuitSimulation);
    }
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
      currentSubcircuitEditor.deleteTraceViews(traceViews);
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
      currentSubcircuitEditor.deleteTraceViews(traceViews);
      return true;
    }
    else
    {
      return false;
    }
  }

  public void runSimultaneous()
  {
    SubcircuitSimulation subcircuitSimulation = getSubcircuitSimulation();
    if (subcircuitSimulation != null)
    {
      subcircuitSimulation.runSimultaneous();
    }
  }

  public void runToTime(long timeForward)
  {
    SubcircuitSimulation subcircuitSimulation = getSubcircuitSimulation();
    if (subcircuitSimulation != null)
    {
      getSubcircuitSimulation().runToTime(timeForward);
    }
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
    SubcircuitEditorOrderer orderer = new SubcircuitEditorOrderer(subcircuitEditors.findAll());
    List<SubcircuitEditor> orderedSubcircuitEditors = orderer.order();

    if (orderedSubcircuitEditors != null)
    {
      Set<CircuitSimulation> circuitSimulations = getCircuitSimulationsForSave(orderedSubcircuitEditors);
      List<CircuitSimulationData> circuitSimulationDatas = saveCircuitSimulations(circuitSimulations);

      List<SubcircuitSimulationData> subcircuitSimulationDatas = saveSubcircuitSimulations(orderedSubcircuitEditors);
      List<SubcircuitEditorData> subcircuitEditorDatas = saveSubcircuitEditors(orderedSubcircuitEditors);

      List<LastSubcircuitSimulationData> lastSubcircuitSimulationDatas = saveLastSubcircuitSimulations();

      return new CircuitData(subcircuitEditorDatas,
                             circuitSimulationDatas,
                             subcircuitSimulationDatas,
                             lastSubcircuitSimulationDatas,
                             currentSubcircuitEditor.getId(),
                             getCurrentSubcircuitSimulationId());
    }
    else
    {
      return null;
    }
  }

  private List<LastSubcircuitSimulationData> saveLastSubcircuitSimulations()
  {
    ArrayList<LastSubcircuitSimulationData> lastSubcircuitSimulationDatas = new ArrayList<>();
    for (Map.Entry<SubcircuitEditor, SubcircuitSimulation> entry : lastSubcircuitEditorSimulation.entrySet())
    {
      SubcircuitEditor subcircuitEditor = entry.getKey();
      SubcircuitSimulation subcircuitSimulation = entry.getValue();
      lastSubcircuitSimulationDatas.add(new LastSubcircuitSimulationData(subcircuitEditor.getId(), subcircuitSimulation.getId()));
    }
    return lastSubcircuitSimulationDatas;
  }

  protected List<SubcircuitEditorData> saveSubcircuitEditors(List<SubcircuitEditor> orderedSubcircuitEditors)
  {
    List<SubcircuitEditorData> subcircuitEditorDatas = new ArrayList<>();
    for (SubcircuitEditor subcircuitEditor : orderedSubcircuitEditors)
    {
      SubcircuitEditorData subcircuitEditorData = subcircuitEditor.save();
      subcircuitEditorDatas.add(subcircuitEditorData);
    }
    return subcircuitEditorDatas;
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
      circuitSimulations.addAll(subcircuitEditor.getCircuitSimulations());
    }
    return circuitSimulations;
  }

  protected List<SubcircuitSimulationData> saveSubcircuitSimulations(List<SubcircuitEditor> orderedSubcircuitEditors)
  {
    List<SubcircuitSimulationData> subcircuitSimulationDatas = new ArrayList<>();
    for (SubcircuitEditor subcircuitEditor : orderedSubcircuitEditors)
    {
      for (SubcircuitSimulation subcircuitSimulation : subcircuitEditor.getSubcircuitSimulations())
      {
        SubcircuitSimulationData subcircuitSimulationData = subcircuitSimulation.save(subcircuitEditor.getId());
        subcircuitSimulationDatas.add(subcircuitSimulationData);
      }
    }
    return subcircuitSimulationDatas;
  }

  protected long getCurrentSubcircuitSimulationId()
  {
    if (currentSubcircuitSimulation != null)
    {
      return currentSubcircuitSimulation.getId();
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
    SimulationLoader simulationLoader = loaders.getSimulationLoader();
    SubcircuitEditor.resetNextId();
    Event.resetNextId();
    View.resetNextId();

    subcircuitEditors.clear(false);
    Map<Long, SubcircuitEditor> subcircuitEditorMap = new HashMap<>();
    Map<SubcircuitEditor, DataViewMap> subcircuitEditorViews = new LinkedHashMap<>();

    loadSubcircuitEditors(circuitData, subcircuitEditorMap, subcircuitEditorViews);
    loadTimeline(circuitData, loaders);
    loadSubcircuitSimulations(circuitData, loaders, simulationLoader, subcircuitEditorMap);
    loadSubcircuitInstances(loaders, subcircuitEditorViews);
    validateLoadSimulationSubcircuitInstances(circuitData.subcircuitSimulations, loaders, subcircuitEditorMap, subcircuitEditorViews);
    loadViews(circuitData, loaders, subcircuitEditorMap, subcircuitEditorViews);

    lastSubcircuitEditorSimulation = new LinkedHashMap<>();
    loadLastSubcircuitEditorSimulation(circuitData, loaders, subcircuitEditorMap);

    currentSubcircuitEditor = getCurrentSubcircuitEditor(circuitData.currentSubcircuit);
    setLastSubcircuitSimulation(loaders.getSubcircuitSimulation(circuitData.currentSubcircuitSimulation));

    SwingUtilities.invokeLater(new Runnable()
    {
      @Override
      public void run()
      {
        subcircuitEditors.notifyChange();
      }
    });
  }

  protected void loadLastSubcircuitEditorSimulation(CircuitData circuitData, CircuitLoaders loaders, Map<Long, SubcircuitEditor> subcircuitEditorMap)
  {
    if (circuitData.lastSubcircuitSimulationDatas != null)
    {
      for (LastSubcircuitSimulationData data : circuitData.lastSubcircuitSimulationDatas)
      {
        SubcircuitSimulation subcircuitSimulation = loaders.getSubcircuitSimulation(data.subcircuitSimulation);
        SubcircuitEditor subcircuitEditor = subcircuitEditorMap.get(data.subcircuitEditor);
        if ((subcircuitSimulation != null) && (subcircuitEditor != null))
        {
          lastSubcircuitEditorSimulation.put(subcircuitEditor, subcircuitSimulation);
        }
      }
    }
  }

  protected void loadSubcircuitEditors(CircuitData circuitData, Map<Long, SubcircuitEditor> subcircuitEditorMap, Map<SubcircuitEditor, DataViewMap> subcircuitEditorViews)
  {
    for (SubcircuitEditorData subcircuitEditorData : circuitData.subcircuits)
    {
      SubcircuitEditor subcircuitEditor = new SubcircuitEditor(this, subcircuitEditorData.typeName, subcircuitEditorData.id);
      subcircuitEditors.add(subcircuitEditor, false);
      subcircuitEditorMap.put(subcircuitEditor.getId(), subcircuitEditor);

      DataViewMap views = subcircuitEditor.loadSubcircuit(subcircuitEditorData.subcircuit, false, false);

      subcircuitEditorViews.put(subcircuitEditor, views);
    }
  }

  protected void loadTimeline(CircuitData circuitData, CircuitLoaders loaders)
  {
    for (CircuitSimulationData circuitSimulationData : circuitData.circuitSimulations)
    {
      CircuitSimulation circuitSimulation = loaders.simulationLoader.createCircuitSimulation(circuitSimulationData.name, circuitSimulationData.id);
      circuitSimulation.getTimeline().load(circuitSimulationData.timeline);
    }
  }

  protected void loadViews(CircuitData circuitData, CircuitLoaders loaders, Map<Long, SubcircuitEditor> subcircuitEditorMap, Map<SubcircuitEditor, DataViewMap> subcircuitEditorViews)
  {
    for (SubcircuitSimulationData subcircuitSimulationData : circuitData.subcircuitSimulations)
    {
      SubcircuitEditor subcircuitEditor = subcircuitEditorMap.get(subcircuitSimulationData.subcircuitEditor);
      SubcircuitSimulation containingSubcircuitSimulation = loaders.getSubcircuitSimulation(subcircuitSimulationData.subcircuitSimulation);

      DataViewMap dataViewMap = subcircuitEditorViews.get(subcircuitEditor);
      subcircuitEditor.loadStatics(dataViewMap, containingSubcircuitSimulation, loaders);
      subcircuitEditor.loadTraces(dataViewMap, containingSubcircuitSimulation, loaders);
    }
  }

  protected void loadSubcircuitInstances(CircuitLoaders loaders, Map<SubcircuitEditor, DataViewMap> subcircuitEditorViews)
  {
    for (Map.Entry<SubcircuitEditor, DataViewMap> entry : subcircuitEditorViews.entrySet())
    {
      DataViewMap dataViewMap = entry.getValue();
      for (Map.Entry<SubcircuitInstanceData, SubcircuitInstanceView> viewEntry : dataViewMap.subcircuitInstanceViews.entrySet())
      {
        SubcircuitInstanceData subcircuitInstanceData = viewEntry.getKey();
        SubcircuitInstanceView subcircuitInstanceView = viewEntry.getValue();
        for (SubcircuitInstanceSimulationSimulationData data : subcircuitInstanceData.subcircuitInstanceSimulations)
        {
          SubcircuitSimulation containingSubcircuitSimulation = loaders.getSubcircuitSimulation(data.containingSimulation);
          SubcircuitInstanceSimulation subcircuitSimulation = (SubcircuitInstanceSimulation) loaders.getSubcircuitSimulation(data.instanceSimulation);
          subcircuitInstanceData.createAndConnectComponent(containingSubcircuitSimulation, subcircuitSimulation, loaders, subcircuitInstanceView);
        }
      }
    }
  }

  protected void loadSubcircuitSimulations(CircuitData circuitData, CircuitLoaders loaders, SimulationLoader simulationLoader, Map<Long, SubcircuitEditor> subcircuitEditorMap)
  {
    for (SubcircuitSimulationData subcircuitSimulationData : circuitData.subcircuitSimulations)
    {
      CircuitSimulation circuitSimulation = loaders.getCircuitSimulation(subcircuitSimulationData.circuitSimulation);
      if (circuitSimulation == null)
      {
        throw new SimulatorException("Cannot find a circuit simulation with id [%s].", subcircuitSimulationData.circuitSimulation);
      }

      SubcircuitEditor subcircuitEditor = getSubcircuitEditor(subcircuitEditorMap, subcircuitSimulationData.subcircuitEditor);
      if (subcircuitSimulationData instanceof SubcircuitTopSimulationData)
      {
        simulationLoader.createSubcircuitTopSimulation(circuitSimulation, subcircuitEditor, subcircuitSimulationData.subcircuitSimulation);
      }
      else if (subcircuitSimulationData instanceof SubcircuitInstanceSimulationData)
      {
        simulationLoader.createSubcircuitInstanceSimulation(circuitSimulation, subcircuitEditor, subcircuitSimulationData.subcircuitSimulation);
      }
      else
      {
        throw new SimulatorException("Cannot load subcircuit simulations with unknown data type [%s].", subcircuitSimulationData.getClass().getSimpleName());
      }
    }
  }

  public void setLastSubcircuitSimulation(SubcircuitSimulation subcircuitSimulation)
  {
    setLastSubcircuitSimulation(currentSubcircuitEditor, subcircuitSimulation);
  }

  public void setLastSubcircuitSimulation(SubcircuitEditor newSubcircuitEditor, SubcircuitSimulation newSubcircuitSimulation)
  {
    this.currentSubcircuitSimulation = newSubcircuitSimulation;
    this.lastSubcircuitEditorSimulation.put(newSubcircuitEditor, newSubcircuitSimulation);
  }

  protected void validateLoadSimulationSubcircuitInstances(List<SubcircuitSimulationData> subcircuitSimulations, CircuitLoaders loaders, Map<Long, SubcircuitEditor> subcircuitEditorMap, Map<SubcircuitEditor, DataViewMap> subcircuitEditorViews)
  {
    for (SubcircuitSimulationData subcircuitSimulationData : subcircuitSimulations)
    {
      SubcircuitEditor subcircuitEditor = subcircuitEditorMap.get(subcircuitSimulationData.subcircuitEditor);

      DataViewMap dataViewMap = subcircuitEditorViews.get(subcircuitEditor);
      for (Map.Entry<SubcircuitInstanceData, SubcircuitInstanceView> entry : dataViewMap.subcircuitInstanceViews.entrySet())
      {
        SubcircuitInstanceData subcircuitInstanceData = entry.getKey();
        SubcircuitInstanceView subcircuitInstanceView = entry.getValue();

        for (SubcircuitInstanceSimulationSimulationData data : subcircuitInstanceData.subcircuitInstanceSimulations)
        {
          long containingSimulationId = data.containingSimulation;

          SubcircuitSimulation loaderContainingSimulation = loaders.getSubcircuitSimulation(containingSimulationId);
          SubcircuitSimulation viewContainingSimulation = subcircuitInstanceView.getContainingSubcircuitSimulation(containingSimulationId);
          if (loaderContainingSimulation != viewContainingSimulation)
          {
            throw new SimulatorException("Subcircuit instance simulation [%s] loaded on view does not match subcircuit instance in data [%s].", Described.getDescription(viewContainingSimulation), Described.getDescription(loaderContainingSimulation));
          }

          long instanceSimulationId = data.instanceSimulation;
          SubcircuitSimulation loaderInstanceSimulation = loaders.getSubcircuitSimulation(instanceSimulationId);
          SubcircuitSimulation viewInstanceSimulation = subcircuitInstanceView.getInstanceSubcircuitSimulation(instanceSimulationId);
          if (loaderInstanceSimulation != viewInstanceSimulation)
          {
            throw new SimulatorException("Subcircuit instance simulation [%s] loaded on view does not match subcircuit instance in data [%s].", Described.getDescription(viewInstanceSimulation), Described.getDescription(loaderInstanceSimulation));
          }
        }
      }
    }
  }

  protected SubcircuitEditor getSubcircuitEditor(Map<Long, SubcircuitEditor> subcircuitEditorMap, long subcircuitEditorId)
  {
    SubcircuitEditor subcircuitEditor = subcircuitEditorMap.get(subcircuitEditorId);
    if (subcircuitEditor == null)
    {
      throw new SimulatorException("Cannot find a subcircuit editor with id [%s].", subcircuitEditorId);
    }
    return subcircuitEditor;
  }

  protected SubcircuitEditor getSubcircuitEditor(long subcircuitId)
  {
    for (SubcircuitEditor subcircuitEditor : subcircuitEditors.findAll())
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
    currentSubcircuitEditor.recreateComponentView(staticView);
  }

  public void startMoveComponents(List<StaticView<?>> staticViews, List<TraceView> traceViews)
  {
    currentSubcircuitEditor.startMoveComponents(staticViews, traceViews);
  }

  public void doneMoveComponents(List<StaticView<?>> staticViews,
                                 List<TraceView> traceViews,
                                 Set<StaticView<?>> selectedViews,
                                 boolean newComponents)
  {
    currentSubcircuitEditor.doneMoveComponents(staticViews,
                                               traceViews,
                                               selectedViews,
                                               newComponents);
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
    currentSubcircuitEditor.deleteSelection();
  }

  public SubcircuitEditor getCurrentSubcircuitEditor()
  {
    return currentSubcircuitEditor;
  }

  public SubcircuitEditor getSubcircuitEditor(String subcircuitTypeName)
  {
    for (SubcircuitEditor subcircuitEditor : subcircuitEditors.findAll())
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
    return new ArrayList<>(subcircuitEditors.findAll());
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
    deleteComponentView(staticView, currentSubcircuitEditor);
  }

  public void deleteComponentView(StaticView<?> staticView, SubcircuitEditor subcircuitEditor)
  {
    subcircuitEditor.deleteComponentView(staticView);
  }

  public void validate()
  {
    for (SubcircuitEditor subcircuitEditor : subcircuitEditors.findAll())
    {
      subcircuitEditor.validate();
    }
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
    currentSubcircuitEditor.deleteStaticViews(staticViews);
  }

  public SubcircuitSimulation getSubcircuitSimulation()
  {
    return currentSubcircuitSimulation;
  }

  public SubcircuitView getCurrentSubcircuitView()
  {
    return currentSubcircuitEditor.getCircuitSubcircuitView();
  }

  public String setCurrentSubcircuitEditor(SubcircuitEditor subcircuitEditor)
  {
    currentSubcircuitEditor = subcircuitEditor;
    return currentSubcircuitEditor.getTypeName();
  }

  private void setCurrentSubcircuitSimulation(SubcircuitEditor newSubcircuitEditor, SubcircuitSimulation lastSubcircuitSimulation)
  {
    SubcircuitSimulation newSubcircuitSimulation = lastSubcircuitEditorSimulation.get(newSubcircuitEditor);
    if (newSubcircuitSimulation != null)
    {
      setLastSubcircuitSimulation(newSubcircuitEditor, newSubcircuitSimulation);
      return;
    }
    else if (lastSubcircuitSimulation != null)
    {
      CircuitSimulation circuitSimulation = lastSubcircuitSimulation.getCircuitSimulation();
      Collection<SubcircuitSimulation> simulations = newSubcircuitEditor.getSubcircuitSimulations(circuitSimulation);
      if (simulations.size() > 0)
      {
        setLastSubcircuitSimulation(newSubcircuitEditor, simulations.iterator().next());
        return;
      }
    }

    List<SubcircuitTopSimulation> simulations = newSubcircuitEditor.getCircuitSubcircuitView().getTopSimulations();
    if (!simulations.isEmpty())
    {
      setLastSubcircuitSimulation(newSubcircuitEditor, simulations.get(0));
    }
    else
    {
      throw new SimulatorException("No top level simulation found for subcircuit editor.");
    }
  }

  private boolean isValidSimulation(SubcircuitView subcircuitView, SubcircuitSimulation subcircuitSimulation)
  {
    return subcircuitView.getSimulations().hasSimulation(subcircuitSimulation);
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
      return gotoSubcircuit(index);
    }
    return null;
  }

  public String gotoNextSubcircuit()
  {
    int index = subcircuitEditors.indexOf(this.currentSubcircuitEditor);
    if (index != -1)
    {
      index++;
      if (index > subcircuitEditors.size() - 1)
      {
        index = 0;
      }
      return gotoSubcircuit(index);
    }
    return null;
  }

  public String gotoSubcircuit(SubcircuitEditor subcircuitEditor)
  {
    int index = subcircuitEditors.indexOf(subcircuitEditor);
    if (index != -1)
    {
      return gotoSubcircuit(index);
    }
    return null;
  }

  protected String gotoSubcircuit(int index)
  {
    SubcircuitSimulation lastSubcircuitSimulation = this.currentSubcircuitSimulation;

    SubcircuitEditor newSubcircuitEditor = subcircuitEditors.get(index);
    setCurrentSubcircuitSimulation(newSubcircuitEditor, lastSubcircuitSimulation);
    return setCurrentSubcircuitEditor(newSubcircuitEditor);
  }

  public String gotoSubcircuit(SubcircuitEditor newSubcircuitEditor, SubcircuitInstanceSimulation instanceSimulation)
  {
    setLastSubcircuitSimulation(newSubcircuitEditor, instanceSimulation);
    return setCurrentSubcircuitEditor(newSubcircuitEditor);
  }

  public void addNewSubcircuit(String subcircuitName)
  {
    String error = subcircuitEditors.getSubcircuitNameError(subcircuitName);
    if (error != null)
    {
      throw new SimulatorException(error);
    }

    SubcircuitEditor subcircuitEditor = new SubcircuitEditor(this, subcircuitName);
    subcircuitEditors.add(subcircuitEditor, true);

    currentSubcircuitEditor = subcircuitEditor;
    setCurrentSubcircuitSimulation(subcircuitEditor, null);
  }

  public void circuitUpdated()
  {
  }

  protected Map<SubcircuitEditor, List<SubcircuitInstanceView>> getSubcircuitInstanceViewsInSubcircuitEditor(SubcircuitEditor currentSubcircuitEditor)
  {
    List<SubcircuitEditor> subcircuitEditors = getSubcircuitEditors();
    Map<SubcircuitEditor, List<SubcircuitInstanceView>> map = new LinkedHashMap<>();
    for (SubcircuitEditor subcircuitEditor : subcircuitEditors)
    {
      List<SubcircuitInstanceView> subcircuitInstanceViews = subcircuitEditor.getCircuitSubcircuitView().getSubcircuitInstanceViews(currentSubcircuitEditor.getCircuitSubcircuitView());
      if (subcircuitInstanceViews.size() > 0)
      {
        map.put(subcircuitEditor, subcircuitInstanceViews);
      }
    }

    return map;
  }

  public List<SubcircuitTopEditorSimulation> getSubcircuitTopSimulations()
  {
    ArrayList<SubcircuitTopEditorSimulation> result = new ArrayList<>();
    for (SubcircuitEditor subcircuitEditor : subcircuitEditors.findAll())
    {
      Collection<SubcircuitSimulation> subcircuitSimulations = subcircuitEditor.getSubcircuitSimulations();
      for (SubcircuitSimulation subcircuitSimulation : subcircuitSimulations)
      {
        if (subcircuitSimulation instanceof SubcircuitTopSimulation)
        {
          result.add(new SubcircuitTopEditorSimulation(subcircuitEditor, (SubcircuitTopSimulation) subcircuitSimulation));
        }
      }
    }
    return result;
  }

  public SubcircuitTopSimulation addNewSimulation(String simulationName)
  {
    return currentSubcircuitEditor.getCircuitSubcircuitView().addNewSimulation(simulationName);
  }

  public String getSubcircuitNameError(String subcircuitName)
  {
    return subcircuitEditors.getSubcircuitNameError(subcircuitName);
  }
}

