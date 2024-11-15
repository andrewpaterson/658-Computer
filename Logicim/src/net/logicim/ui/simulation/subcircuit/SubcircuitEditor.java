package net.logicim.ui.simulation.subcircuit;

import net.common.SimulatorException;
import net.common.geometry.Line;
import net.common.type.Float2D;
import net.common.type.Int2D;
import net.logicim.data.circuit.SubcircuitData;
import net.logicim.data.circuit.SubcircuitEditorData;
import net.logicim.data.integratedcircuit.common.StaticData;
import net.logicim.data.subciruit.SubcircuitInstanceData;
import net.logicim.data.wire.TraceData;
import net.logicim.domain.CircuitSimulation;
import net.logicim.domain.common.Described;
import net.logicim.domain.passive.subcircuit.SubcircuitSimulation;
import net.logicim.domain.passive.subcircuit.SubcircuitTopSimulation;
import net.logicim.ui.circuit.CircuitInstanceView;
import net.logicim.ui.circuit.SubcircuitView;
import net.logicim.ui.common.ConnectionView;
import net.logicim.ui.common.Viewport;
import net.logicim.ui.common.integratedcircuit.ComponentView;
import net.logicim.ui.common.integratedcircuit.StaticView;
import net.logicim.ui.common.integratedcircuit.View;
import net.logicim.ui.common.wire.TraceView;
import net.logicim.ui.shape.common.BoundingBox;
import net.logicim.ui.simulation.CircuitEditor;
import net.logicim.ui.simulation.CircuitLoaders;
import net.logicim.ui.simulation.DataViewMap;
import net.logicim.ui.simulation.StaticViewIterator;
import net.logicim.ui.simulation.component.common.InstanceView;
import net.logicim.ui.simulation.component.subcircuit.SubcircuitInstanceView;
import net.logicim.ui.simulation.selection.Selection;

import java.util.*;

public class SubcircuitEditor
    implements CircuitInstanceView,
               Described,
               InstanceView
{
  public static long nextId = 1L;

  public static final String SUBCIRCUIT_EDITOR = "Editor";

  protected Selection selection;
  protected SubcircuitView subcircuitView;
  protected long id;

  public SubcircuitEditor(CircuitEditor circuitEditor, String typeName)
  {
    this(new SubcircuitView(circuitEditor), typeName);
    this.subcircuitView.setSubcircuitEditor(this);
    this.subcircuitView.createSubcircuitTopSimulation(getTopSimulationName(typeName));

    id = nextId;
    nextId++;
  }

  public SubcircuitEditor(CircuitEditor circuitEditor, String typeName, long id)
  {
    this(new SubcircuitView(circuitEditor), typeName);
    subcircuitView.setSubcircuitEditor(this);

    this.id = id;
    if (id >= nextId)
    {
      nextId = id + 1;
    }
  }

  public SubcircuitEditor(SubcircuitView subcircuitView, String typeName)
  {
    this.subcircuitView = subcircuitView;
    this.selection = new Selection();
    this.subcircuitView.setSubcircuitEditor(this);
    this.setTypeName(typeName);
  }

  public static String getTopSimulationName(String typeName)
  {
    return "Top " + typeName;
  }

  public static void resetNextId()
  {
    nextId = 1;
  }

  public void startMoveComponents(List<StaticView<?>> staticViews, List<TraceView> traceViews)
  {
    clearSelection();

    subcircuitView.startMoveComponents(staticViews, traceViews);
  }

  public void doneMoveComponents(List<StaticView<?>> staticViews,
                                 List<Line> newTraceViewLines,
                                 List<TraceView> removeTraceViews,
                                 Set<StaticView<?>> selectedViews,
                                 boolean newComponents
  )
  {
    List<View> newSelection = subcircuitView.doneMoveComponents(staticViews,
                                                                newTraceViewLines,
                                                                removeTraceViews,
                                                                selectedViews);

    recalculateViewPropertiesAfterNew(staticViews, newComponents);
    selection.setSelection(newSelection);
  }

  protected void recalculateViewPropertiesAfterNew(List<StaticView<?>> staticViews, boolean newComponents)
  {
    if (newComponents)
    {
      for (StaticView<?> staticView : staticViews)
      {
        staticView.recalculatePropertiesAfterNew(subcircuitView);
      }
    }
  }

  public void deleteSelection()
  {
    Set<TraceView> traceViews = new HashSet<>();
    List<View> selectedViews = selection.getSelection();
    for (View view : selectedViews)
    {
      if (view instanceof TraceView)
      {
        traceViews.add((TraceView) view);
      }
    }
    subcircuitView.deleteTraceViews(traceViews);

    List<StaticView<?>> staticViews = new ArrayList<>();
    for (View view : selectedViews)
    {
      if (view instanceof StaticView)
      {
        staticViews.add((StaticView<?>) view);
      }
    }
    subcircuitView.deleteStaticViews(staticViews);
    selection.clearSelection();
  }

  public CircuitEditor getCircuitEditor()
  {
    return subcircuitView.getCircuitEditor();
  }

  public void clearSelection()
  {
    selection.clearSelection();
  }

  public void select(View view)
  {
    selection.add(view);
  }

  public void replaceSelection(View newView, View oldView)
  {
    selection.replaceSelection(newView, oldView);
  }

  public boolean isSelectionEmpty()
  {
    return selection.isSelectionEmpty();
  }

  public StaticView<?> getSingleSelectionStaticView()
  {
    StaticView<?> componentView = null;
    for (View view : selection.getSelection())
    {
      if (view instanceof StaticView)
      {
        if (componentView == null)
        {
          componentView = (StaticView<?>) view;
        }
        else
        {
          return null;
        }
      }
    }
    return componentView;
  }

  public SubcircuitEditorData save()
  {
    Set<View> selection = new HashSet<>(this.selection.getSelection());
    return subcircuitView.save(selection, id);
  }

  public List<View> getAllViews()
  {
    return subcircuitView.getAllViews();
  }

  public void deleteTraceViews(Set<TraceView> traceViews)
  {
    subcircuitView.deleteTraceViews(traceViews);
  }

  public StaticViewIterator staticViewIterator()
  {
    return subcircuitView.staticViewIterator();
  }

  public List<StaticView<?>> getComponentViewsInScreenSpace(Viewport viewport,
                                                            Int2D screenPosition)
  {
    List<StaticView<?>> selectedViews = new ArrayList<>();
    StaticViewIterator iterator = staticViewIterator();
    while (iterator.hasNext())
    {
      StaticView<?> view = iterator.next();
      if (isInScreenSpaceBoundingBox(viewport, screenPosition, view))
      {
        selectedViews.add(view);
      }
    }
    return selectedViews;
  }

  protected boolean isInScreenSpaceBoundingBox(Viewport viewport,
                                               Int2D screenPosition,
                                               StaticView<?> view)
  {
    if (view.isEnabled())
    {
      Int2D boundBoxPosition = new Int2D();
      Int2D boundBoxDimension = new Int2D();
      view.getBoundingBoxInScreenSpace(viewport, boundBoxPosition, boundBoxDimension);
      if (BoundingBox.containsPoint(screenPosition, boundBoxPosition, boundBoxDimension))
      {
        return true;
      }
    }
    return false;
  }

  public SubcircuitView getInstanceSubcircuitView()
  {
    return subcircuitView;
  }

  public TraceView getTraceViewInScreenSpace(Viewport viewport, Int2D screenPosition)
  {
    List<TraceView> selectedViews = getTraceViewsInScreenSpace(viewport, screenPosition);

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
      Int2D center = new Int2D();
      float shortestDistance = Float.MAX_VALUE;
      TraceView closestView = null;
      for (TraceView view : selectedViews)
      {
        view.getCenter(center);
        float distance = BoundingBox.calculateDistance(screenPosition, center);
        if (distance < shortestDistance)
        {
          closestView = view;
          shortestDistance = distance;
        }
      }
      return closestView;
    }
  }

  public List<TraceView> getTraceViewsInScreenSpace(Viewport viewport, Int2D screenPosition)
  {
    int x = viewport.transformScreenToGridX(screenPosition.x);
    int y = viewport.transformScreenToGridY(screenPosition.y);
    return subcircuitView.getTraceViewsInGridSpace(x, y);
  }

  public void removeTraceViews(Collection<TraceView> traceViews)
  {
    subcircuitView.removeTraceViews(traceViews);
  }

  public void deleteStaticViews(List<StaticView<?>> staticViews)
  {
    subcircuitView.deleteStaticViews(staticViews);
  }

  public List<View> pasteClipboardViews(List<TraceData> traces,
                                        List<StaticData<?>> components)
  {
    List<StaticData<?>> statics = new ArrayList<>();
    List<SubcircuitInstanceData> subcircuitInstances = new ArrayList<>();
    for (StaticData<?> component : components)
    {
      if (component instanceof SubcircuitInstanceData)
      {
        subcircuitInstances.add((SubcircuitInstanceData) component);
      }
      else
      {
        statics.add(component);
      }
    }
    return loadViews(traces, subcircuitInstances, statics, true, true);
  }

  public Selection getSelection()
  {
    return selection;
  }

  public List<View> getSelectionFromRectangle(Float2D start, Float2D end)
  {
    return subcircuitView.getSelectionFromRectangle(start, end);
  }

  public ConnectionView getConnection(int x, int y)
  {
    return subcircuitView.getConnection(x, y);
  }

  public void deleteComponentView(StaticView<?> staticView)
  {
    subcircuitView.deleteStaticView(staticView);
  }

  public void validate()
  {
    subcircuitView.validate();
    validateSimulations();
  }

  public List<View> loadViews(List<TraceData> traces,
                              List<SubcircuitInstanceData> subcircuitInstances,
                              List<StaticData<?>> staticDatas,
                              boolean appendIds,
                              boolean newComponentPropertyStep)
  {
    Map<TraceData, TraceView> traceViews = loadTraceViews(traces, appendIds);
    Map<StaticData<?>, StaticView<?>> staticViews = loadStaticViews(staticDatas, appendIds, newComponentPropertyStep);
    Map<SubcircuitInstanceData, SubcircuitInstanceView> subcircuitInstanceViews = loadSubcircuitInstances(subcircuitInstances, appendIds, newComponentPropertyStep);

    ArrayList<View> views = new ArrayList<>();
    views.addAll(traceViews.values());
    views.addAll(staticViews.values());
    views.addAll(subcircuitInstanceViews.values());
    return views;
  }

  public Map<SubcircuitInstanceData, SubcircuitInstanceView> loadSubcircuitInstances(List<SubcircuitInstanceData> subcircuitInstances,
                                                                                     boolean appendIds,
                                                                                     boolean newComponentPropertyStep)
  {
    Map<SubcircuitInstanceData, SubcircuitInstanceView> subcircuitInstanceViews = new LinkedHashMap<>();
    for (SubcircuitInstanceData subcircuitInstance : subcircuitInstances)
    {
      SubcircuitInstanceView subcircuitInstanceView = subcircuitInstance.createAndEnableStaticView(this, newComponentPropertyStep);
      subcircuitInstanceViews.put(subcircuitInstance, subcircuitInstanceView);
      if (!appendIds)
      {
        subcircuitInstanceView.setId(subcircuitInstance.id);
      }
    }
    return subcircuitInstanceViews;
  }

  public Map<StaticData<?>, StaticView<?>> loadStaticViews(List<StaticData<?>> staticDatas,
                                                           boolean appendIds,
                                                           boolean newComponentPropertyStep)
  {
    Map<StaticData<?>, StaticView<?>> staticViews = new LinkedHashMap<>();
    for (StaticData<?> staticData : staticDatas)
    {
      StaticView<?> staticView = staticData.createAndEnableStaticView(this, newComponentPropertyStep);
      staticViews.put(staticData, staticView);
      if (!appendIds)
      {
        staticView.setId(staticData.id);
      }
    }
    return staticViews;
  }

  public Map<TraceData, TraceView> loadTraceViews(List<TraceData> traces,
                                                  boolean appendIds)
  {
    Map<TraceData, TraceView> traceViews = new LinkedHashMap<>();
    for (TraceData traceData : traces)
    {
      TraceView traceView = traceData.createAndEnableTraceView(this);
      traceViews.put(traceData, traceView);
      if (!appendIds)
      {
        traceView.setId(traceData.id);
      }
    }
    return traceViews;
  }

  public DataViewMap loadSubcircuit(SubcircuitData subcircuitData,
                                    boolean appendIds,
                                    boolean newComponentPropertyStep,
                                    Map<Long, SubcircuitInstanceView> subcircuitInstanceViewMap)
  {
    Map<TraceData, TraceView> traceViews = loadTraceViews(subcircuitData.traces, appendIds);
    Map<StaticData<?>, StaticView<?>> staticViews = loadStaticViews(subcircuitData.statics, appendIds, newComponentPropertyStep);
    Map<SubcircuitInstanceData, SubcircuitInstanceView> subcircuitInstanceViews = loadSubcircuitInstances(subcircuitData.subcircuitInstances, appendIds, newComponentPropertyStep);
    for (Map.Entry<SubcircuitInstanceData, SubcircuitInstanceView> entry : subcircuitInstanceViews.entrySet())
    {
      subcircuitInstanceViewMap.put(entry.getKey().id, entry.getValue());
    }

    return new DataViewMap(traceViews, staticViews, subcircuitInstanceViews);
  }

  public void loadTraces(DataViewMap dataViewMap, SubcircuitSimulation subcircuitSimulation, CircuitLoaders circuitLoaders)
  {
    Map<TraceData, TraceView> traceViews = dataViewMap.traceViews;
    for (Map.Entry<TraceData, TraceView> entry : traceViews.entrySet())
    {
      TraceData data = entry.getKey();
      if (data.appliesToSimulation(subcircuitSimulation.getId()))
      {
        TraceView traceView = entry.getValue();
        SubcircuitEditorLoadDataHelper.loadViewData(traceView,
                                                    data,
                                                    subcircuitSimulation,
                                                    circuitLoaders.getTraceLoader());
      }
    }
  }

  public void loadStatics(DataViewMap dataViewMap, SubcircuitSimulation subcircuitSimulation, CircuitLoaders circuitLoaders)
  {
    Map<StaticData<?>, StaticView<?>> staticViews = dataViewMap.staticViews;
    for (Map.Entry<StaticData<?>, StaticView<?>> entry : staticViews.entrySet())
    {
      StaticData<?> data = entry.getKey();
      if (data.appliesToSimulation(subcircuitSimulation.getId()))
      {
        StaticView<?> staticView = entry.getValue();
        SubcircuitEditorLoadDataHelper.loadViewData(staticView,
                                                    data,
                                                    subcircuitSimulation,
                                                    circuitLoaders);
      }
    }
  }

  public String getTypeName()
  {
    return subcircuitView.getTypeName();
  }

  public void setTypeName(String subcircuitName)
  {
    subcircuitView.setTypeName(subcircuitName);
  }

  public List<String> getTypeNameAsList()
  {
    return subcircuitView.getTypeNameAsList();
  }

  public SubcircuitEditor getSubcircuitEditor(String subcircuitTypeName)
  {
    return getCircuitEditor().getSubcircuitEditor(subcircuitTypeName);
  }

  public List<String> getSubcircuitInstanceNames()
  {
    return subcircuitView.getSubcircuitInstanceNames();
  }

  public long getId()
  {
    return id;
  }

  @Override
  public String getType()
  {
    return SUBCIRCUIT_EDITOR;
  }

  public Set<StaticView<?>> findAllViewsOfClass(Class<? extends StaticView<?>> viewClass)
  {
    return subcircuitView.findAllViewsOfClass(viewClass);
  }

  @Override
  public String toString()
  {
    return subcircuitView.getTypeName();
  }

  public Collection<CircuitSimulation> getCircuitSimulations()
  {
    return subcircuitView.getSimulations().getCircuitSimulations();
  }

  public List<? extends SubcircuitSimulation> getSubcircuitSimulations()
  {
    return subcircuitView.getSimulations().getSubcircuitSimulations();
  }

  @Override
  public SubcircuitSimulation getSubcircuitSimulationForParent(SubcircuitSimulation parentSubcircuitSimulation)
  {
    return null;
  }

  @Override
  public List<? extends SubcircuitSimulation> getInstanceSubcircuitSimulations()
  {
    return subcircuitView.getSimulations().getSubcircuitTopSimulations();
  }

  public List<? extends SubcircuitSimulation> getInstanceSubcircuitSimulations(CircuitSimulation circuitSimulation)
  {
    List<SubcircuitSimulation> result = new ArrayList<>();
    List<SubcircuitTopSimulation> subcircuitTopSimulations = subcircuitView.getSimulations().getSubcircuitTopSimulations();
    for (SubcircuitTopSimulation subcircuitTopSimulation : subcircuitTopSimulations)
    {
      if (subcircuitTopSimulation.getCircuitSimulation() == circuitSimulation)
      {
        result.add(subcircuitTopSimulation);
      }
    }
    return result;
  }

  @Override
  public String getDescription()
  {
    return getType() + " " + getTypeName();
  }

  public void validateSimulations()
  {
    List<CircuitInstanceView> orderedTopDownCircuitInstanceViews = getOrderedCircuitInstanceViews();
    subcircuitView.validateSimulations(orderedTopDownCircuitInstanceViews);
  }

  @Override
  public int compareTo(InstanceView obj)
  {
    if (obj instanceof ComponentView)
    {
      return -1;
    }
    else if (obj instanceof SubcircuitEditor)
    {
      SubcircuitEditor other = (SubcircuitEditor) obj;
      return Long.compare(id, other.id);
    }
    else
    {
      throw new SimulatorException("Don't know how to compare [%s] to [%s].", this.getClass().getSimpleName(), obj.getClass().getSimpleName());
    }
  }
}

