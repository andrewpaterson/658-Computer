package net.logicim.ui.simulation;

import net.common.SimulatorException;
import net.common.geometry.Line;
import net.common.type.Float2D;
import net.common.type.Int2D;
import net.logicim.data.circuit.CircuitData;
import net.logicim.data.circuit.LastSubcircuitSimulationData;
import net.logicim.data.circuit.SubcircuitEditorData;
import net.logicim.data.integratedcircuit.common.StaticData;
import net.logicim.data.simulation.*;
import net.logicim.data.subciruit.SubcircuitInstanceData;
import net.logicim.data.subciruit.SubcircuitInstanceSimulationSimulationData;
import net.logicim.data.subciruit.ViewPathData;
import net.logicim.data.subciruit.ViewPathElementData;
import net.logicim.data.wire.TraceData;
import net.logicim.domain.CircuitSimulation;
import net.logicim.domain.common.Circuit;
import net.logicim.domain.common.Described;
import net.logicim.domain.common.event.Event;
import net.logicim.domain.passive.subcircuit.SubcircuitInstance;
import net.logicim.domain.passive.subcircuit.SubcircuitInstanceSimulation;
import net.logicim.domain.passive.subcircuit.SubcircuitSimulation;
import net.logicim.domain.passive.subcircuit.SubcircuitTopSimulation;
import net.logicim.ui.circuit.CircuitInstanceView;
import net.logicim.ui.circuit.SubcircuitInstanceViewFinder;
import net.logicim.ui.circuit.SubcircuitSimulationPaths;
import net.logicim.ui.circuit.SubcircuitView;
import net.logicim.ui.circuit.path.UpdatedViewPaths;
import net.logicim.ui.circuit.path.ViewPath;
import net.logicim.ui.circuit.path.ViewPathCircuitSimulation;
import net.logicim.ui.circuit.path.ViewPaths;
import net.logicim.ui.clipboard.ClipboardData;
import net.logicim.ui.common.ConnectionView;
import net.logicim.ui.common.HoverConnectionView;
import net.logicim.ui.common.Viewport;
import net.logicim.ui.common.integratedcircuit.StaticView;
import net.logicim.ui.common.integratedcircuit.View;
import net.logicim.ui.common.wire.TraceView;
import net.logicim.ui.shape.common.BoundingBox;
import net.logicim.ui.simulation.component.subcircuit.SubcircuitInstanceView;
import net.logicim.ui.simulation.navigation.NavigationStack;
import net.logicim.ui.simulation.order.SubcircuitEditorOrderer;
import net.logicim.ui.simulation.selection.Selection;
import net.logicim.ui.simulation.subcircuit.SubcircuitEditor;
import net.logicim.ui.simulation.subcircuit.SubcircuitTopEditorSimulation;
import net.logicim.ui.subcircuit.SubcircuitEditorList;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.*;

public class CircuitEditor
{
  protected SubcircuitEditorList subcircuitEditorList;

  protected ViewPathCircuitSimulation currentPathSimulation;
  protected Map<SubcircuitEditor, ViewPathCircuitSimulation> lastSubcircuitEditorPathSimulation;

  protected NavigationStack navigationStack;

  protected DebugGlobalEnvironment globals;

  protected ViewPaths viewPaths;
  protected SubcircuitSimulationPaths simulationPaths;

  public CircuitEditor()
  {
    subcircuitEditorList = new SubcircuitEditorList();
    subcircuitEditorList.clear(true);

    currentPathSimulation = null;
    lastSubcircuitEditorPathSimulation = new LinkedHashMap<>();

    navigationStack = new NavigationStack();

    globals = new DebugGlobalEnvironment();

    viewPaths = new ViewPaths(subcircuitEditorList.findAll());
    simulationPaths = new SubcircuitSimulationPaths(viewPaths.getViewPaths());
  }

  public CircuitEditor(String mainSubcircuitTypeName)
  {
    this();
    addNewSubcircuit(mainSubcircuitTypeName);
  }

  private List<View> getAllCurrentViews()
  {
    return getCurrentSubcircuitEditor().getAllViews();
  }

  public void paint(Graphics2D graphics, Viewport viewport)
  {
    List<View> views;
    synchronized (this)
    {
      views = getAllCurrentViews();
    }

    ViewPath path = getCurrentViewPath();
    CircuitSimulation circuitSimulation = getCurrentCircuitSimulation();
    for (View view : views)
    {
      view.paint(graphics, viewport, path, circuitSimulation);
    }
  }

  public Circuit getCircuit()
  {
    SubcircuitSimulation subcircuitSimulation = getCurrentSubcircuitSimulation();
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
      getCurrentSubcircuitEditor().deleteTraceViews(traceViews);
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
      getCurrentSubcircuitEditor().deleteTraceViews(traceViews);
      return true;
    }
    else
    {
      return false;
    }
  }

  public void runSimultaneous()
  {
    SubcircuitSimulation subcircuitSimulation = getCurrentSubcircuitSimulation();
    if (subcircuitSimulation != null)
    {
      subcircuitSimulation.runSimultaneous();
    }
  }

  public void runToTime(long timeForward)
  {
    SubcircuitSimulation subcircuitSimulation = getCurrentSubcircuitSimulation();
    if (subcircuitSimulation != null)
    {
      getCurrentSubcircuitSimulation().runToTime(timeForward);
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
    return getCurrentSubcircuitEditor().getComponentViewsInScreenSpace(viewport, screenPosition);
  }

  public CircuitData save()
  {
    SubcircuitEditorOrderer orderer = new SubcircuitEditorOrderer(subcircuitEditorList.findAll());
    List<SubcircuitEditor> orderedSubcircuitEditors = orderer.order();

    if (orderedSubcircuitEditors != null)
    {
      Set<CircuitSimulation> circuitSimulations = getCircuitSimulationsForSave(orderedSubcircuitEditors);
      List<CircuitSimulationData> circuitSimulationDatas = saveCircuitSimulations(circuitSimulations);

      List<SubcircuitSimulationData> subcircuitSimulationDatas = saveSubcircuitSimulations(orderedSubcircuitEditors);
      List<SubcircuitEditorData> subcircuitEditorDatas = saveSubcircuitEditors(orderedSubcircuitEditors);

      List<LastSubcircuitSimulationData> lastSubcircuitSimulationDatas = saveLastSubcircuitSimulations();
      List<ViewPathData> viewPathDatas = saveViewPaths();

      return new CircuitData(subcircuitEditorDatas,
                             circuitSimulationDatas,
                             subcircuitSimulationDatas,
                             viewPathDatas,
                             lastSubcircuitSimulationDatas,
                             getCurrentSubcircuitEditor().getId(),
                             getCurrentViewPathId(),
                             getCurrentCircuitSimulationId());
    }
    else
    {
      return null;
    }
  }

  private List<ViewPathData> saveViewPaths()
  {
    ArrayList<ViewPathData> viewPathDatas = new ArrayList<>();
    List<ViewPath> paths = viewPaths.getViewPaths();
    for (ViewPath path : paths)
    {
      ArrayList<ViewPathElementData> elements = new ArrayList<>();
      List<CircuitInstanceView> circuitInstanceViews = path.getPath();
      for (CircuitInstanceView circuitInstanceView : circuitInstanceViews)
      {
        elements.add(new ViewPathElementData(circuitInstanceView.getId(), circuitInstanceView.getType()));
      }
      long previousId = path.getPrevious() != null ? path.getPrevious().getId() : 0L;
      long nextId = path.getNext() != null ? path.getNext().getId() : 0L;
      viewPathDatas.add(new ViewPathData(path.getId(),
                                         elements,
                                         previousId,
                                         nextId));
    }
    return viewPathDatas;
  }

  private long getCurrentViewPathId()
  {
    if (currentPathSimulation != null)
    {
      return currentPathSimulation.getViewPath().getId();
    }
    return 0L;
  }

  private long getCurrentCircuitSimulationId()
  {
    if (currentPathSimulation != null)
    {
      return currentPathSimulation.getCircuitSimulation().getId();
    }
    return 0L;
  }

  private List<LastSubcircuitSimulationData> saveLastSubcircuitSimulations()
  {
    ArrayList<LastSubcircuitSimulationData> lastSubcircuitSimulationDatas = new ArrayList<>();
    for (Map.Entry<SubcircuitEditor, ViewPathCircuitSimulation> entry : lastSubcircuitEditorPathSimulation.entrySet())
    {
      SubcircuitEditor subcircuitEditor = entry.getKey();
      ViewPathCircuitSimulation pathCircuitSimulation = entry.getValue();
      CircuitSimulation circuitSimulation = pathCircuitSimulation.getCircuitSimulation();
      ViewPath viewPath = pathCircuitSimulation.getViewPath();
      lastSubcircuitSimulationDatas.add(new LastSubcircuitSimulationData(subcircuitEditor.getId(), circuitSimulation.getId(), viewPath.getId()));
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
        ViewPath viewPath = getViewPath(subcircuitSimulation);
        SubcircuitSimulationData subcircuitSimulationData = subcircuitSimulation.save(subcircuitEditor.getId(), viewPath.getId());
        subcircuitSimulationDatas.add(subcircuitSimulationData);
      }
    }
    return subcircuitSimulationDatas;
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
    CircuitLoaders loaders = new CircuitLoaders();
    SubcircuitEditor.resetNextId();
    ViewPath.resetNextId();
    Event.resetNextId();
    View.resetNextId();

    navigationStack.clear();
    subcircuitEditorList.clear(false);
    Map<Long, SubcircuitEditor> subcircuitEditorMap = new HashMap<>();
    Map<Long, StaticView<?>> staticMap = new HashMap<>();
    Map<Long, SubcircuitInstanceView> subcircuitInstanceViewMap = new HashMap<>();
    Map<SubcircuitEditor, DataViewMap> subcircuitEditorViews = new HashMap<>();
    Map<Long, ViewPath> viewPathMap = new HashMap<>();

    loadSubcircuitEditors(circuitData, subcircuitEditorMap, staticMap, subcircuitEditorViews, subcircuitInstanceViewMap);
    loadTimeline(circuitData, loaders);
    loadViewPaths(circuitData, subcircuitEditorMap, subcircuitInstanceViewMap, viewPathMap);
    loadSubcircuitSimulations(circuitData, loaders, subcircuitEditorMap, viewPathMap);
    loadSubcircuitInstances(loaders, subcircuitEditorViews);
    validateLoadSimulationSubcircuitInstances(circuitData.subcircuitSimulations, loaders, subcircuitEditorMap, subcircuitEditorViews);
    updateSimulationPaths();

    loadViews(circuitData, loaders, subcircuitEditorMap, subcircuitEditorViews);

    subcircuitEditorList.setSubcircuitPaths(viewPaths.getViewPaths());

    lastSubcircuitEditorPathSimulation = new LinkedHashMap<>();
    loadLastSubcircuitEditorSimulation(circuitData, loaders, subcircuitEditorMap);

    SubcircuitEditor subcircuitEditor = getCurrentSubcircuitEditor(circuitData.currentSubcircuit);

    setCurrentViewPathCircuitSimulation(subcircuitEditor, loadCurrentPathSimulation(circuitData, loaders, viewPathMap));
    setCurrentSubcircuitEditor(subcircuitEditor);

    SwingUtilities.invokeLater(new Runnable()
    {
      @Override
      public void run()
      {
        subcircuitEditorList.notifySubcircuitListChanged(true);
      }
    });
  }

  protected ViewPathCircuitSimulation loadCurrentPathSimulation(CircuitData circuitData,
                                                                CircuitLoaders loaders,
                                                                Map<Long, ViewPath> viewPathMap)
  {
    return new ViewPathCircuitSimulation(viewPathMap.get(circuitData.currentViewPath), loaders.simulationLoader.getCircuitSimulation(circuitData.currentCircuitSimulation));
  }

  private void loadViewPaths(CircuitData circuitData,
                             Map<Long, SubcircuitEditor> subcircuitEditorMap,
                             Map<Long, SubcircuitInstanceView> subcircuitInstanceViewMap,
                             Map<Long, ViewPath> viewPathMap)
  {
    List<ViewPathData> viewPathDatas = circuitData.viewPathDatas;
    viewPaths = new ViewPaths();
    for (ViewPathData viewPathData : viewPathDatas)
    {
      ArrayList<CircuitInstanceView> circuitInstanceViews = new ArrayList<>();
      for (ViewPathElementData pathElementData : viewPathData.elements)
      {
        long id = pathElementData.id;
        String type = pathElementData.type;
        CircuitInstanceView circuitInstanceView;
        switch (type)
        {
          case SubcircuitEditor.SUBCIRCUIT_EDITOR:
            circuitInstanceView = subcircuitEditorMap.get(id);
            break;
          case SubcircuitInstanceView.SUBCIRCUIT_INSTANCE:
            circuitInstanceView = subcircuitInstanceViewMap.get(id);
            break;
          default:
            throw new SimulatorException("Cannot load CircuitInstanceView with Type [%s] and ID [%s].", type, id);
        }
        if (circuitInstanceView == null)
        {
          throw new SimulatorException("Cannot load CircuitInstanceView with Type [%s] and ID [%s].", type, id);
        }
        circuitInstanceViews.add(circuitInstanceView);
      }

      ViewPath viewPath = new ViewPath(viewPathData.id, circuitInstanceViews);
      viewPaths.getViewPaths().add(viewPath);
      viewPathMap.put(viewPathData.id, viewPath);
    }

    for (ViewPathData viewPathData : viewPathDatas)
    {
      ViewPath viewPath = viewPathMap.get(viewPathData.id);
      ViewPath next = viewPathMap.get(viewPathData.prevId);
      ViewPath previous = viewPathMap.get(viewPathData.nextId);
      viewPath.setNext(next);
      viewPath.setPrevious(previous);
    }
  }

  protected void loadLastSubcircuitEditorSimulation(CircuitData circuitData,
                                                    CircuitLoaders loaders,
                                                    Map<Long, SubcircuitEditor> subcircuitEditorMap)
  {
    if (circuitData.lastSubcircuitSimulationDatas != null)
    {
      for (LastSubcircuitSimulationData data : circuitData.lastSubcircuitSimulationDatas)
      {
        ViewPathCircuitSimulation viewPathCircuitSimulation = loaders.getViewPathLoader().getViewPathCircuitSimulation(data.viewPath, data.circuitSimulation);
        SubcircuitEditor subcircuitEditor = subcircuitEditorMap.get(data.subcircuitEditor);
        if ((viewPathCircuitSimulation != null) && (subcircuitEditor != null))
        {
          lastSubcircuitEditorPathSimulation.put(subcircuitEditor, viewPathCircuitSimulation);
        }
      }
    }
  }

  protected void loadSubcircuitEditors(CircuitData circuitData,
                                       Map<Long, SubcircuitEditor> subcircuitEditorMap,
                                       Map<Long, StaticView<?>> staticMap,
                                       Map<SubcircuitEditor, DataViewMap> subcircuitEditorViews,
                                       Map<Long, SubcircuitInstanceView> subcircuitInstanceViewMap)
  {
    for (SubcircuitEditorData subcircuitEditorData : circuitData.subcircuits)
    {
      SubcircuitEditor subcircuitEditor = new SubcircuitEditor(this, subcircuitEditorData.typeName, subcircuitEditorData.id);
      subcircuitEditorList.add(subcircuitEditor, false);
      subcircuitEditorMap.put(subcircuitEditor.getId(), subcircuitEditor);

      DataViewMap views = subcircuitEditor.loadSubcircuit(subcircuitEditorData.subcircuit,
                                                          false,
                                                          false,
                                                          subcircuitInstanceViewMap);

      subcircuitEditorViews.put(subcircuitEditor, views);

      for (Map.Entry<StaticData<?>, StaticView<?>> entry : views.staticViews.entrySet())
      {
        StaticData<?> data = entry.getKey();
        StaticView<?> staticView = entry.getValue();
        staticMap.put(data.id, staticView);
      }
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

  protected void loadViews(CircuitData circuitData,
                           CircuitLoaders loaders,
                           Map<Long, SubcircuitEditor> subcircuitEditorMap,
                           Map<SubcircuitEditor, DataViewMap> subcircuitEditorViews)
  {
    for (SubcircuitSimulationData subcircuitSimulationData : circuitData.subcircuitSimulations)
    {
      SubcircuitEditor subcircuitEditor = subcircuitEditorMap.get(subcircuitSimulationData.subcircuitEditor);
      ViewPathCircuitSimulation viewPathCircuitSimulation = loaders.getViewPathLoader().getViewPathCircuitSimulation(subcircuitSimulationData.viewPath, subcircuitSimulationData.circuitSimulation);
      ViewPath path = viewPathCircuitSimulation.getViewPath();
      CircuitSimulation circuitSimulation = viewPathCircuitSimulation.getCircuitSimulation();

      DataViewMap dataViewMap = subcircuitEditorViews.get(subcircuitEditor);
      subcircuitEditor.loadStatics(dataViewMap,
                                   path,
                                   circuitSimulation,
                                   loaders);
      subcircuitEditor.loadTraces(dataViewMap,
                                  path,
                                  circuitSimulation,
                                  loaders);
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
        for (SubcircuitInstanceSimulationSimulationData subcircuitInstanceSimulationData : subcircuitInstanceData.subcircuitInstanceSimulations)
        {
          SubcircuitInstanceSimulation subcircuitSimulation = (SubcircuitInstanceSimulation) loaders.getSubcircuitSimulation(subcircuitInstanceSimulationData.instanceSubcircuitSSimulation);
          ViewPathCircuitSimulation viewPathCircuitSimulation = loaders.getViewPathLoader().getViewPathCircuitSimulation(subcircuitInstanceSimulationData.viewPath, subcircuitInstanceSimulationData.circuitSimulation);
          ViewPath path = viewPathCircuitSimulation.getViewPath();
          CircuitSimulation circuitSimulation = viewPathCircuitSimulation.getCircuitSimulation();

          subcircuitInstanceData.createAndConnectComponentDuringLoad(path,
                                                                     circuitSimulation,
                                                                     subcircuitSimulation,
                                                                     loaders,
                                                                     subcircuitInstanceView);
        }
      }
    }
  }

  protected void loadSubcircuitSimulations(CircuitData circuitData,
                                           CircuitLoaders loaders,
                                           Map<Long, SubcircuitEditor> subcircuitEditorMap,
                                           Map<Long, ViewPath> viewPathMap)
  {
    SimulationLoader simulationLoader = loaders.getSimulationLoader();
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
      ViewPath viewPath = viewPathMap.get(subcircuitSimulationData.viewPath);
      loaders.getViewPathLoader().put(new ViewPathCircuitSimulation(viewPath, circuitSimulation));
    }
  }

  public void setCurrentViewPathCircuitSimulation(SubcircuitEditor newSubcircuitEditor, ViewPathCircuitSimulation newSubcircuitSimulation)
  {
    this.currentPathSimulation = newSubcircuitSimulation;
    this.lastSubcircuitEditorPathSimulation.put(newSubcircuitEditor, newSubcircuitSimulation);
  }

  protected void validateLoadSimulationSubcircuitInstances(List<SubcircuitSimulationData> subcircuitSimulations,
                                                           CircuitLoaders loaders,
                                                           Map<Long, SubcircuitEditor> subcircuitEditorMap,
                                                           Map<SubcircuitEditor, DataViewMap> subcircuitEditorViews)
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
          long containingSimulationId = data.containingSubcircuitSimulation;

          SubcircuitSimulation loaderContainingSimulation = loaders.getSubcircuitSimulation(containingSimulationId);
          SubcircuitSimulation viewContainingSimulation = subcircuitInstanceView.getContainingSubcircuitSimulation(containingSimulationId);
          if (loaderContainingSimulation != viewContainingSimulation)
          {
            throw new SimulatorException("Subcircuit instance simulation [%s] loaded on view does not match subcircuit instance in data [%s].", Described.getDescription(viewContainingSimulation), Described.getDescription(loaderContainingSimulation));
          }

          long instanceSimulationId = data.instanceSubcircuitSSimulation;
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
    for (SubcircuitEditor subcircuitEditor : subcircuitEditorList.findAll())
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
      return subcircuitEditorList.get(0);
    }
  }

  public List<View> pasteClipboardViews(List<TraceData> traces, List<StaticData<?>> components)
  {
    return getCurrentSubcircuitEditor().pasteClipboardViews(traces, components);
  }

  public void recreateComponentView(StaticView<?> staticView)
  {
    List<StaticView<?>> singleStaticView = new ArrayList<>();
    singleStaticView.add(staticView);

    getCurrentSubcircuitEditor().doneMoveComponents(singleStaticView,
                                                    new ArrayList<>(),
                                                    new ArrayList<>(),
                                                    new HashSet<>(),
                                                    true);
  }

  public void startMoveComponents(List<StaticView<?>> staticViews, List<TraceView> traceViews)
  {
    getCurrentSubcircuitEditor().startMoveComponents(staticViews, traceViews);
  }

  public void doneMoveComponents(List<StaticView<?>> staticViews,
                                 List<TraceView> removeTraceViews,
                                 Set<StaticView<?>> selectedViews,
                                 boolean newComponents)
  {
    List<Line> newTraceViewLines = SubcircuitView.getTraceViewLines(removeTraceViews);

    getCurrentSubcircuitEditor().doneMoveComponents(staticViews,
                                                    newTraceViewLines,
                                                    removeTraceViews,
                                                    selectedViews,
                                                    newComponents);
  }

  public Selection getCurrentSelection()
  {
    return getCurrentSubcircuitEditor().getSelection();
  }

  public List<View> getSelectionFromRectangle(Float2D start, Float2D end)
  {
    return getCurrentSubcircuitEditor().getSelectionFromRectangle(start, end);
  }

  public void deleteSelection()
  {
    getCurrentSubcircuitEditor().deleteSelection();
  }

  public SubcircuitEditor getSubcircuitEditor(String subcircuitTypeName)
  {
    for (SubcircuitEditor subcircuitEditor : subcircuitEditorList.findAll())
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
    return new ArrayList<>(subcircuitEditorList.findAll());
  }

  public SubcircuitEditorList getSubcircuitEditorList()
  {
    return subcircuitEditorList;
  }

  public boolean isCurrentSelectionEmpty()
  {
    return getCurrentSubcircuitEditor().isSelectionEmpty();
  }

  public ConnectionView getConnectionInCurrentSubcircuit(int x, int y)
  {
    return getCurrentSubcircuitEditor().getConnection(x, y);
  }

  public void deleteComponentView(StaticView<?> staticView)
  {
    deleteComponentView(staticView, getCurrentSubcircuitEditor());
  }

  public void deleteComponentView(StaticView<?> staticView, SubcircuitEditor subcircuitEditor)
  {
    subcircuitEditor.deleteComponentView(staticView);
  }

  public void validate()
  {
    List<SubcircuitEditor> subcircuitEditors = subcircuitEditorList.findAll();
    for (SubcircuitEditor subcircuitEditor : subcircuitEditors)
    {
      subcircuitEditor.validate();
    }

    ViewPaths newViewPaths = new ViewPaths(getSubcircuitEditors());
    if (!viewPaths.matches(newViewPaths))
    {
      throw new SimulatorException("Circuit Editor Paths:\n%s does not match generated paths:\n%s", viewPaths.toString(), newViewPaths.toString());
    }
  }

  public void replaceSelection(View newView, View oldView)
  {
    getCurrentSubcircuitEditor().replaceSelection(newView, oldView);
  }

  public List<View> duplicateViews(List<View> views)
  {
    ClipboardData clipboardData = copyViews(views);
    return pasteClipboardViews(clipboardData.getTraces(), clipboardData.getComponents());
  }

  public void deleteTraceViews(Collection<TraceView> traceViews)
  {
    getCurrentSubcircuitEditor().removeTraceViews(traceViews);
  }

  public void deleteComponentViews(List<StaticView<?>> staticViews)
  {
    getCurrentSubcircuitEditor().deleteStaticViews(staticViews);
  }

  public SubcircuitSimulation getCurrentSubcircuitSimulation()
  {
    CircuitSimulation circuitSimulation = currentPathSimulation.getCircuitSimulation();
    ViewPath viewPath = currentPathSimulation.getViewPath();
    return viewPath.getSubcircuitSimulation(circuitSimulation);
  }

  public ViewPath getCurrentViewPath()
  {
    if (currentPathSimulation != null)
    {
      return currentPathSimulation.getViewPath();
    }
    else
    {
      return null;
    }
  }

  public CircuitSimulation getCurrentCircuitSimulation()
  {
    if (currentPathSimulation != null)
    {
      return currentPathSimulation.getCircuitSimulation();
    }
    else
    {
      return null;
    }
  }

  public SubcircuitView getCurrentSubcircuitView()
  {
    return getCurrentSubcircuitEditor().getInstanceSubcircuitView();
  }

  public SubcircuitEditor getCurrentSubcircuitEditor()
  {
    return subcircuitEditorList.getCurrentSubcircuitEditor();
  }

  public String setCurrentSubcircuitEditor(SubcircuitEditor subcircuitEditor)
  {
    String editor = subcircuitEditorList.setSubcircuitEditor(subcircuitEditor, true);
    navigationStack.push(currentPathSimulation);
    return editor;
  }

  private void setSubcircuitSimulationForSubcircuitEditor(SubcircuitEditor newSubcircuitEditor, CircuitSimulation lastCircuitSimulation)
  {
    ViewPathCircuitSimulation newViewPathCircuitSimulation = lastSubcircuitEditorPathSimulation.get(newSubcircuitEditor);
    SubcircuitView subcircuitView = newSubcircuitEditor.getInstanceSubcircuitView();
    if (newViewPathCircuitSimulation != null)
    {
      setCurrentViewPathCircuitSimulation(newSubcircuitEditor, newViewPathCircuitSimulation);
      return;
    }
    else if (lastCircuitSimulation != null)
    {
      List<ViewPath> viewPaths = subcircuitView.getViewPaths(lastCircuitSimulation);
      if (viewPaths.size() > 0)
      {
        setCurrentViewPathCircuitSimulation(newSubcircuitEditor, new ViewPathCircuitSimulation(viewPaths.iterator().next(), lastCircuitSimulation));
        return;
      }
    }

    List<CircuitSimulation> circuitSimulations = subcircuitView.getCircuitSimulations();
    for (CircuitSimulation circuitSimulation : circuitSimulations)
    {
      List<ViewPath> viewPaths = subcircuitView.getViewPaths(circuitSimulation);
      if (viewPaths.size() > 0)
      {
        setCurrentViewPathCircuitSimulation(newSubcircuitEditor, new ViewPathCircuitSimulation(viewPaths.iterator().next(), circuitSimulation));
        return;
      }
    }

    throw new SimulatorException("No top level simulation found for subcircuit editor.");
  }

  public boolean hasMultipleSubcircuits()
  {
    return subcircuitEditorList.size() > 1;
  }

  public String gotoPreviousSubcircuit()
  {
    return gotoSubcircuit(subcircuitEditorList.getPreviousSubcircuit());
  }

  public String gotoNextSubcircuit()
  {
    return gotoSubcircuit(subcircuitEditorList.getNextSubcircuit());
  }

  public String gotoSubcircuit(SubcircuitEditor newSubcircuitEditor)
  {
    if (newSubcircuitEditor == null)
    {
      return null;
    }

    setSubcircuitSimulationForSubcircuitEditor(newSubcircuitEditor, currentPathSimulation.getCircuitSimulation());
    return setCurrentSubcircuitEditor(newSubcircuitEditor);
  }

  public String gotoSubcircuit(SubcircuitEditor newSubcircuitEditor, ViewPathCircuitSimulation viewPathCircuitSimulation)
  {
    setCurrentViewPathCircuitSimulation(newSubcircuitEditor, viewPathCircuitSimulation);
    return setCurrentSubcircuitEditor(newSubcircuitEditor);
  }

  public void addNewSubcircuit(String subcircuitName)
  {
    String error = subcircuitEditorList.getSubcircuitNameError(subcircuitName);
    if (error != null)
    {
      throw new SimulatorException(error);
    }

    SubcircuitEditor subcircuitEditor = new SubcircuitEditor(this, subcircuitName);
    subcircuitEditorList.add(subcircuitEditor, true);

    SubcircuitView subcircuitView = subcircuitEditor.getInstanceSubcircuitView();
    UpdatedViewPaths updatedPaths = viewPathsUpdate();
    subcircuitView.pathsUpdated(updatedPaths);

    setSubcircuitSimulationForSubcircuitEditor(subcircuitEditor, null);
    setCurrentSubcircuitEditor(subcircuitEditor);
  }

  public UpdatedViewPaths viewPathsUpdate()
  {
    ViewPaths newViewPaths = new ViewPaths(getSubcircuitEditors());

    Map<ViewPath, ViewPath> newToExistingPaths = createNewPathToExistingPathMap(newViewPaths);

    UpdatedViewPaths updatedViewPaths = updateViewPaths(newViewPaths);

    updatePathsLinks(newToExistingPaths);

    validatePathLinks();

    updateSimulationPaths();

    return updatedViewPaths;
  }

  public void updateSimulationPaths()
  {
    simulationPaths = new SubcircuitSimulationPaths(viewPaths.getViewPaths());
  }

  protected void validatePathLinks()
  {
    for (ViewPath path : viewPaths.getViewPaths())
    {
      ViewPath previous = path.getPrevious();
      if (previous != null)
      {
        if (!viewPaths.getViewPaths().contains(previous))
        {
          throw new SimulatorException("View Paths does not contain Previous path [%s].", previous);
        }
      }
      ViewPath next = path.getNext();
      if (next != null)
      {
        if (!viewPaths.getViewPaths().contains(next))
        {
          throw new SimulatorException("View Paths does not contain Next path [%s].", next);
        }
      }
    }
  }

  protected UpdatedViewPaths updateViewPaths(ViewPaths newViewPaths)
  {
    List<ViewPath> newPaths = new ArrayList<>();
    for (ViewPath newPath : newViewPaths.getViewPaths())
    {
      boolean newPathAdded = viewPaths.addIfNotPresent(newPath);
      if (newPathAdded)
      {
        newPaths.add(newPath);
      }
    }

    List<ViewPath> removedPaths = new ArrayList<>();
    int length = viewPaths.getViewPaths().size();
    for (int i = 0; i < length; i++)
    {
      ViewPath existingPath = viewPaths.getPath(i);
      if (!newViewPaths.contains(existingPath))
      {
        removedPaths.add(existingPath);
        viewPaths.removePath(i);
        i--;
        length--;
      }
    }

    return new UpdatedViewPaths(newPaths, removedPaths);
  }

  protected void updatePathsLinks(Map<ViewPath, ViewPath> newToExistingPaths)
  {
    for (ViewPath path : viewPaths.getViewPaths())
    {
      ViewPath previous = path.getPrevious();
      ViewPath existingPath = newToExistingPaths.get(previous);
      if (existingPath != null)
      {
        path.clearPrevious();
        path.setPrevious(existingPath);
      }

      path.clearNext();
    }
    viewPaths.createPathLinks();
  }

  protected Map<ViewPath, ViewPath> createNewPathToExistingPathMap(ViewPaths newViewPaths)
  {
    Map<ViewPath, ViewPath> newToExistingPaths = new LinkedHashMap<>();
    for (ViewPath newPath : newViewPaths.getViewPaths())
    {
      for (ViewPath existingPath : viewPaths.getViewPaths())
      {
        if (newPath.equals(existingPath))
        {
          newToExistingPaths.put(newPath, existingPath);
        }
      }
    }
    return newToExistingPaths;
  }

  public void circuitUpdated()
  {
  }

  public List<SubcircuitTopEditorSimulation> getSubcircuitTopSimulations()
  {
    ArrayList<SubcircuitTopEditorSimulation> result = new ArrayList<>();
    for (SubcircuitEditor subcircuitEditor : subcircuitEditorList.findAll())
    {
      List<? extends SubcircuitSimulation> subcircuitSimulations = subcircuitEditor.getSubcircuitSimulations();
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

  public Set<CircuitSimulation> getCircuitSimulations()
  {
    Set<CircuitSimulation> result = new LinkedHashSet<>();
    for (SubcircuitEditor subcircuitEditor : subcircuitEditorList.findAll())
    {
      List<? extends SubcircuitSimulation> subcircuitSimulations = subcircuitEditor.getSubcircuitSimulations();
      for (SubcircuitSimulation subcircuitSimulation : subcircuitSimulations)
      {
        if (subcircuitSimulation instanceof SubcircuitTopSimulation)
        {
          result.add(subcircuitSimulation.getCircuitSimulation());
        }
      }
    }
    return result;
  }

  public ViewPathCircuitSimulation addNewSimulation(String simulationName)
  {
    SubcircuitEditor subcircuitEditor = getCurrentSubcircuitEditor();

    CircuitSimulation circuitSimulation = subcircuitEditor.getInstanceSubcircuitView().addNewSimulation(simulationName);
    ViewPath path = getCurrentViewPath();

    return new ViewPathCircuitSimulation(path, circuitSimulation);
  }

  public String getSubcircuitNameError(String subcircuitName)
  {
    return subcircuitEditorList.getSubcircuitNameError(subcircuitName);
  }

  public boolean navigateBackwardSubcircuit()
  {
    ViewPathCircuitSimulation pathCircuitSimulation = navigationStack.pop();
    if (pathCircuitSimulation != null)
    {
      SubcircuitEditor subcircuitEditor = pathCircuitSimulation.getSubcircuitEditor();
      setCurrentViewPathCircuitSimulation(subcircuitEditor, pathCircuitSimulation);
      subcircuitEditorList.setSubcircuitEditor(subcircuitEditor, true);
    }
    return false;
  }

  public boolean navigateForwardSubcircuit()
  {
    ViewPathCircuitSimulation pathCircuitSimulation = navigationStack.unpop();
    if (pathCircuitSimulation != null)
    {
      SubcircuitEditor subcircuitEditor = pathCircuitSimulation.getSubcircuitEditor();
      setCurrentViewPathCircuitSimulation(subcircuitEditor, pathCircuitSimulation);
      subcircuitEditorList.setSubcircuitEditor(subcircuitEditor, true);
    }
    return false;
  }

  public SubcircuitInstanceViewFinder getSubcircuitInstanceViewFinder()
  {
    return subcircuitEditorList;
  }

  public ViewPaths getViewPaths()
  {
    return viewPaths;
  }

  public ViewPath getViewPath(CircuitSimulation circuitSimulation, SubcircuitInstance subcircuitInstance)
  {
    for (ViewPath path : viewPaths.getViewPaths())
    {
      SubcircuitSimulation subcircuitSimulation = path.getSubcircuitSimulationOrNull(circuitSimulation);
      if (subcircuitSimulation != null)
      {
        if (subcircuitSimulation instanceof SubcircuitInstanceSimulation)
        {
          SubcircuitInstanceSimulation subcircuitInstanceSimulation = (SubcircuitInstanceSimulation) subcircuitSimulation;
          if (subcircuitInstanceSimulation.getSubcircuitInstance() == subcircuitInstance)
          {
            return path;
          }
        }
      }
    }
    return null;
  }

  public ViewPath getViewPath(SubcircuitSimulation existingSubcircuitSimulation)
  {
    CircuitSimulation circuitSimulation = existingSubcircuitSimulation.getCircuitSimulation();
    for (ViewPath path : viewPaths.getViewPaths())
    {
      SubcircuitSimulation subcircuitSimulation = path.getSubcircuitSimulationOrNull(circuitSimulation);
      if (subcircuitSimulation == existingSubcircuitSimulation)
      {
        return path;
      }
    }
    return null;
  }
}

