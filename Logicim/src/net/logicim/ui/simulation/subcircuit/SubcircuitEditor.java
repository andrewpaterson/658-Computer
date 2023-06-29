package net.logicim.ui.simulation.subcircuit;

import net.logicim.common.SimulatorException;
import net.logicim.common.geometry.Line;
import net.logicim.common.type.Float2D;
import net.logicim.common.type.Int2D;
import net.logicim.common.util.Counter;
import net.logicim.data.circuit.SubcircuitEditorData;
import net.logicim.data.common.ViewData;
import net.logicim.data.integratedcircuit.common.StaticData;
import net.logicim.data.wire.TraceData;
import net.logicim.domain.CircuitSimulation;
import net.logicim.domain.passive.subcircuit.SubcircuitSimulation;
import net.logicim.domain.passive.subcircuit.SubcircuitSimulations;
import net.logicim.domain.passive.subcircuit.SubcircuitTopSimulation;
import net.logicim.ui.circuit.CircuitInstanceOrderer;
import net.logicim.ui.circuit.CircuitInstanceView;
import net.logicim.ui.circuit.CircuitInstanceViewParent;
import net.logicim.ui.circuit.SubcircuitView;
import net.logicim.ui.common.ConnectionView;
import net.logicim.ui.common.Viewport;
import net.logicim.ui.common.integratedcircuit.StaticView;
import net.logicim.ui.common.integratedcircuit.View;
import net.logicim.ui.common.wire.TraceView;
import net.logicim.ui.shape.common.BoundingBox;
import net.logicim.ui.simulation.CircuitEditor;
import net.logicim.ui.simulation.CircuitLoaders;
import net.logicim.ui.simulation.StaticViewIterator;
import net.logicim.ui.simulation.component.subcircuit.SubcircuitInstanceView;
import net.logicim.ui.simulation.selection.Selection;

import java.util.*;

public class SubcircuitEditor
    implements CircuitInstanceView
{
  public static long nextId = 1L;

  protected Selection selection;
  protected SubcircuitView subcircuitView;
  protected CircuitEditor circuitEditor;
  protected SubcircuitSimulations simulations;
  protected long id;

  public SubcircuitEditor(CircuitEditor circuitEditor, String typeName, CircuitSimulation circuitSimulation)
  {
    this(circuitEditor, new SubcircuitView());
    this.setTypeName(typeName);
    this.simulations = new SubcircuitSimulations();
    this.simulations.add(createSubcircuitSimulation(circuitSimulation));

    id = nextId;
    nextId++;
  }

  public SubcircuitEditor(CircuitEditor circuitEditor, String typeName, long id)
  {
    this(circuitEditor, new SubcircuitView());
    this.setTypeName(typeName);
    this.simulations = new SubcircuitSimulations();

    this.id = id;
    if (id >= nextId)
    {
      nextId = id + 1;
    }
  }

  public SubcircuitEditor(CircuitEditor circuitEditor, SubcircuitView subcircuitView)
  {
    this.circuitEditor = circuitEditor;
    this.subcircuitView = subcircuitView;
    this.selection = new Selection();
  }

  public void startMoveComponents(List<StaticView<?>> staticViews,
                                  List<TraceView> traceViews,
                                  SubcircuitSimulation subcircuitSimulation)
  {
    clearSelection();

    subcircuitView.startMoveComponents(staticViews, traceViews, subcircuitSimulation);
  }

  public void recreateComponentView(StaticView<?> staticView, SubcircuitSimulation subcircuitSimulation)
  {
    List<StaticView<?>> staticViews = new ArrayList<>();
    staticViews.add(staticView);

    doneMoveComponents(staticViews, new ArrayList<>(), new LinkedHashSet<>(), subcircuitSimulation, true);
  }

  public void doneMoveComponents(List<StaticView<?>> staticViews,
                                 List<TraceView> traceViews,
                                 Set<StaticView<?>> selectedViews,
                                 SubcircuitSimulation subcircuitSimulation,
                                 boolean newComponents)
  {
    List<View> newSelection = subcircuitView.doneMoveComponents(subcircuitSimulation,
                                                                staticViews,
                                                                traceViews,
                                                                selectedViews);
    if (newComponents)
    {
      for (StaticView<?> staticView : staticViews)
      {
        staticView.newPlaced(subcircuitView);
      }
    }
    this.selection.setSelection(newSelection);
  }

  public void deleteSelection(SubcircuitSimulation subcircuitSimulation)
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
    subcircuitView.deleteTraceViews(traceViews, subcircuitSimulation);

    List<StaticView<?>> staticViews = new ArrayList<>();
    for (View view : selectedViews)
    {
      if (view instanceof StaticView)
      {
        staticViews.add((StaticView<?>) view);
      }
    }
    subcircuitView.deleteComponentViews(staticViews, subcircuitSimulation);
    selection.clearSelection();
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

  public void deleteTraceViews(Set<TraceView> traceViews,
                               SubcircuitSimulation subcircuitSimulation)
  {
    subcircuitView.deleteTraceViews(traceViews, subcircuitSimulation);
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

  public SubcircuitView getCircuitSubcircuitView()
  {
    return subcircuitView;
  }

  public SubcircuitTopSimulation createSubcircuitSimulation(CircuitSimulation circuitSimulation)
  {
    return new SubcircuitTopSimulation(circuitSimulation);
  }

  @Override
  public void destroyComponents(CircuitSimulation circuitSimulation)
  {
    subcircuitView.destroyComponents(circuitSimulation);
    simulations.remove(circuitSimulation);
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

  public void removeTraceView(TraceView traceView)
  {
    subcircuitView.removeTraceView(traceView);
  }

  public void deleteComponentViews(List<StaticView<?>> staticViews, SubcircuitSimulation subcircuitSimulation)
  {
    subcircuitView.deleteComponentViews(staticViews, subcircuitSimulation);
  }

  public List<View> pasteClipboardViews(List<TraceData> traces,
                                        List<StaticData<?>> components)
  {
    Map<ViewData, View> views = loadViews(traces, components, true, true);
    return new ArrayList<>(views.values());
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

  public void deleteComponentView(StaticView<?> staticView, SubcircuitSimulation subcircuitSimulation)
  {
    subcircuitView.deleteComponentView(staticView, subcircuitSimulation);
  }

  public void validateConsistency()
  {
    subcircuitView.validateConsistency();
  }

  public void createTraceViews(List<Line> lines, SubcircuitSimulation subcircuitSimulation)
  {
    subcircuitView.createTraceViews(lines, subcircuitSimulation);
  }

  public Map<ViewData, View> loadViews(List<TraceData> traces,
                                       List<StaticData<?>> components,
                                       boolean appendIds,
                                       boolean newComponentPropertyStep)
  {
    Map<ViewData, View> views = new LinkedHashMap<>();

    for (TraceData traceData : traces)
    {
      TraceView traceView = traceData.createAndEnableTraceView(this);
      views.put(traceData, traceView);
      traceView.updateId(appendIds, traceData.id);
    }

    for (StaticData<?> staticData : components)
    {
      StaticView<?> staticView = staticData.createAndEnableStaticView(this, newComponentPropertyStep);
      views.put(staticData, staticView);
    }

    return views;
  }

  public void loadComponents(Map<ViewData, View> dataViewMap,
                             SubcircuitSimulation subcircuitSimulation,
                             CircuitLoaders circuitLoaders)
  {
    for (Map.Entry<ViewData, View> entry : dataViewMap.entrySet())
    {
      ViewData data = entry.getKey();
      if (data.appliesToSimulation(subcircuitSimulation.getId()))
      {
        SubcircuitEditorLoadDataHelper.loadViewData(
            entry.getValue(),
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

  public List<String> getTypeNameAsList()
  {
    return subcircuitView.getTypeNameAsList();
  }

  public SubcircuitEditor getSubcircuitEditor(String subcircuitTypeName)
  {
    return circuitEditor.getSubcircuitEditor(subcircuitTypeName);
  }

  public void setTypeName(String subcircuitName)
  {
    subcircuitView.setTypeName(subcircuitName);
  }

  public List<String> getSubcircuitInstanceNames()
  {
    return subcircuitView.getSubcircuitInstanceNames();
  }

  public long getId()
  {
    return id;
  }

  public Set<StaticView<?>> findAllViewsOfClass(Class<? extends StaticView<?>> viewClass)
  {
    return subcircuitView.findAllViewsOfClass(viewClass);
  }

  public SubcircuitSimulation getSubcircuitSimulation(CircuitSimulation circuitSimulation)
  {
    return simulations.get(circuitSimulation);
  }

  public SubcircuitSimulations getSimulations()
  {
    return simulations;
  }

  public static void resetNextId()
  {
    nextId = 1;
  }

  public List<SubcircuitTopSimulation> getTopSimulations()
  {
    List<SubcircuitTopSimulation> list = new ArrayList<>();
    for (SubcircuitSimulation subcircuitSimulation : simulations.getSubcircuitSimulations())
    {
      if (subcircuitSimulation instanceof SubcircuitTopSimulation)
      {
        list.add((SubcircuitTopSimulation) subcircuitSimulation);
      }
    }
    return list;
  }

  public void addSubcircuitSimulation(SubcircuitSimulation subcircuitSimulation)
  {
    simulations.add(subcircuitSimulation);
  }

  @Override
  public String toString()
  {
    return subcircuitView.getTypeName();
  }

  public List<CircuitInstanceViewParent> getCircuitInstanceViews()
  {
    List<CircuitInstanceViewParent> circuitInstanceViewParents = new ArrayList<>();

    Counter counter = new Counter();
    CircuitInstanceViewParent instanceViewParent = new CircuitInstanceViewParent(null, this, counter.tick());
    circuitInstanceViewParents.add(instanceViewParent);
    recurseFindSubCircuitViews(instanceViewParent, circuitInstanceViewParents, counter);
    return circuitInstanceViewParents;
  }

  protected void recurseFindSubCircuitViews(CircuitInstanceViewParent circuitInstanceViewParent, List<CircuitInstanceViewParent> circuitInstanceViewParents, Counter counter)
  {
    SubcircuitView subcircuitView = circuitInstanceViewParent.getCircuitSubcircuitView();
    Set<SubcircuitInstanceView> instanceViews = subcircuitView.findAllSubcircuitInstanceViews();
    for (SubcircuitInstanceView instanceView : instanceViews)
    {
      CircuitInstanceViewParent instanceViewParent = new CircuitInstanceViewParent(circuitInstanceViewParent, instanceView, counter.tick());
      circuitInstanceViewParents.add(instanceViewParent);
      recurseFindSubCircuitViews(instanceViewParent, circuitInstanceViewParents, counter);
    }
  }

  public List<CircuitInstanceView> getOrderedCircuitInstanceViews()
  {
    List<CircuitInstanceViewParent> viewParents = getCircuitInstanceViews();

    CircuitInstanceOrderer orderer = new CircuitInstanceOrderer(viewParents);
    List<CircuitInstanceViewParent> ordered = orderer.order();
    List<CircuitInstanceView> circuitInstanceViews = new ArrayList<>();
    for (CircuitInstanceViewParent circuitInstanceViewParent : ordered)
    {
      CircuitInstanceView view = circuitInstanceViewParent.getView();
      circuitInstanceViews.add(view);
    }
    return circuitInstanceViews;
  }

  public void validateOnlyThisSubcircuitEditor(List<CircuitInstanceView> circuitInstanceViews)
  {
    int topCount = 0;
    for (CircuitInstanceView circuitInstanceView : circuitInstanceViews)
    {
      if (circuitInstanceView instanceof SubcircuitEditor)
      {
        SubcircuitEditor subcircuitEditor = (SubcircuitEditor) circuitInstanceView;
        if (this != subcircuitEditor || topCount > 1)
        {
          throw new SimulatorException("Expected at most one SubcircuitEditor == current.");
        }
        topCount++;
      }
    }
  }
}

