package net.logicim.ui.simulation;

import net.logicim.common.SimulatorException;
import net.logicim.common.geometry.Line;
import net.logicim.common.type.Float2D;
import net.logicim.common.type.Int2D;
import net.logicim.data.circuit.SubcircuitData;
import net.logicim.data.common.ViewData;
import net.logicim.data.integratedcircuit.common.StaticData;
import net.logicim.data.wire.TraceData;
import net.logicim.data.wire.TraceLoader;
import net.logicim.domain.InstanceCircuitSimulation;
import net.logicim.ui.circuit.SubcircuitView;
import net.logicim.ui.common.ConnectionView;
import net.logicim.ui.common.Viewport;
import net.logicim.ui.common.integratedcircuit.StaticView;
import net.logicim.ui.common.integratedcircuit.View;
import net.logicim.ui.common.wire.TraceView;
import net.logicim.ui.shape.common.BoundingBox;
import net.logicim.ui.simulation.component.subcircuit.SubcircuitInstanceView;
import net.logicim.ui.simulation.selection.Selection;

import java.util.*;

public class SubcircuitEditor
{
  public static long nextId = 1L;

  protected Selection selection;
  protected SubcircuitView subcircuitView;
  protected SubcircuitInstanceView subcircuitInstanceView;
  protected CircuitEditor circuitEditor;
  protected long id;

  public SubcircuitEditor(CircuitEditor circuitEditor, String typeName)
  {
    this(circuitEditor, new SubcircuitView());
    this.setTypeName(typeName);

    id = nextId;
    nextId++;
  }

  public SubcircuitEditor(CircuitEditor circuitEditor, String typeName, long id)
  {
    this(circuitEditor, new SubcircuitView());
    this.setTypeName(typeName);

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
                                  InstanceCircuitSimulation circuit)
  {
    clearSelection();

    subcircuitView.startMoveComponents(staticViews, traceViews, circuit);
  }

  public void recreateComponentView(StaticView<?> staticView, InstanceCircuitSimulation circuit)
  {
    List<StaticView<?>> staticViews = new ArrayList<>();
    staticViews.add(staticView);

    doneMoveComponents(staticViews, new ArrayList<>(), new LinkedHashSet<>(), circuit, true);
  }

  public void doneMoveComponents(List<StaticView<?>> staticViews,
                                 List<TraceView> traceViews,
                                 Set<StaticView<?>> selectedViews,
                                 InstanceCircuitSimulation circuit,
                                 boolean newComponents)
  {
    List<View> newSelection = subcircuitView.doneMoveComponents(circuit,
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

  public void deleteSelection(InstanceCircuitSimulation circuit)
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
    subcircuitView.deleteTraceViews(traceViews, circuit);

    List<StaticView<?>> staticViews = new ArrayList<>();
    for (View view : selectedViews)
    {
      if (view instanceof StaticView)
      {
        staticViews.add((StaticView<?>) view);
      }
    }
    subcircuitView.deleteComponentViews(staticViews, circuit);
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

  public SubcircuitData save()
  {
    Set<View> selection = new HashSet<>(this.selection.getSelection());
    return subcircuitView.save(selection, id);
  }

  public List<View> getAllViews()
  {
    return subcircuitView.getAllViews();
  }

  public void deleteTraceViews(Set<TraceView> traceViews,
                               InstanceCircuitSimulation circuit)
  {
    subcircuitView.deleteTraceViews(traceViews, circuit);
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

  public SubcircuitView getSubcircuitView()
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

  public void removeTraceView(TraceView traceView)
  {
    subcircuitView.removeTraceView(traceView);
  }

  public void deleteComponentViews(List<StaticView<?>> staticViews, InstanceCircuitSimulation circuit)
  {
    subcircuitView.deleteComponentViews(staticViews, circuit);
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

  public void deleteComponentView(StaticView<?> staticView,
                                  InstanceCircuitSimulation circuit)
  {
    subcircuitView.deleteComponentView(staticView, circuit);
  }

  public void validateConsistency()
  {
    subcircuitView.validateConsistency();
  }

  public void createTraceViews(List<Line> lines,
                               InstanceCircuitSimulation circuit)
  {
    subcircuitView.createTraceViews(lines, circuit);
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
                             InstanceCircuitSimulation circuit,
                             TraceLoader traceLoader)
  {
    for (Map.Entry<ViewData, View> entry : dataViewMap.entrySet())
    {
      ViewData data = entry.getKey();
      if (data.appliesToSimulation(circuit.getId()))
      {
        View view = entry.getValue();
        if (view instanceof StaticView)
        {
          StaticView staticView = (StaticView) view;
          StaticData staticData = (StaticData) data;
          staticData.createAndConnectComponent(this, circuit, traceLoader, staticView);
        }
        else if (view instanceof TraceView)
        {
          TraceView traceView = (TraceView) view;
          TraceData traceData = (TraceData) data;
          traceData.createAndConnectComponent(circuit, traceLoader, traceView);
        }
        else
        {
          throw new SimulatorException("Cannot load component of type [%s].", view);
        }
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

  public static void resetNextId()
  {
    nextId = 1;
  }
}

