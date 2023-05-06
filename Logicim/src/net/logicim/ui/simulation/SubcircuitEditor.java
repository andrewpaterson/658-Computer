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
import net.logicim.domain.CircuitSimulation;
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
                                  CircuitSimulation circuitSimulation)
  {
    clearSelection();

    subcircuitView.startMoveComponents(staticViews, traceViews, circuitSimulation);
  }

  public void doneMoveComponents(List<StaticView<?>> staticViews,
                                 List<TraceView> traceViews,
                                 Set<StaticView<?>> selectedViews,
                                 CircuitSimulation circuitSimulation)
  {
    List<View> newSelection = subcircuitView.doneMoveComponents(circuitSimulation, staticViews,
                                                                traceViews,
                                                                selectedViews
    );
    this.selection.setSelection(newSelection);
  }

  public void deleteSelection(CircuitSimulation circuitSimulation)
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
    subcircuitView.deleteTraceViews(traceViews, circuitSimulation);

    List<StaticView<?>> staticViews = new ArrayList<>();
    for (View view : selectedViews)
    {
      if (view instanceof StaticView)
      {
        staticViews.add((StaticView<?>) view);
      }
    }
    subcircuitView.deleteComponentViews(staticViews, circuitSimulation);
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
                               CircuitSimulation circuitSimulation)
  {
    subcircuitView.deleteTraceViews(traceViews, circuitSimulation);
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

  public void deleteComponentViews(List<StaticView<?>> staticViews, CircuitSimulation circuitSimulation)
  {
    subcircuitView.deleteComponentViews(staticViews, circuitSimulation);
  }

  public List<View> pasteClipboardViews(List<TraceData> traces,
                                        List<StaticData<?>> components)
  {
    Map<ViewData, View> views = loadViews(traces, components, true, true);
    return new ArrayList<>(views.values());
  }

  public void placeComponentView(StaticView<?> staticView,
                                 CircuitSimulation circuitSimulation)
  {
    Set<ConnectionView> updatedConnectionViews = subcircuitView.connectStaticView(staticView, circuitSimulation);

    subcircuitView.fireConnectionEvents(updatedConnectionViews, circuitSimulation);
  }

  public Selection getSelection()
  {
    return selection;
  }

  public List<View> getSelectionFromRectangle(CircuitSimulation simulation, Float2D start, Float2D end)
  {
    return subcircuitView.getSelectionFromRectangle(start, end);
  }

  public ConnectionView getConnection(int x, int y)
  {
    return subcircuitView.getConnection(x, y);
  }

  public void deleteComponentView(StaticView<?> staticView,
                                  CircuitSimulation circuitSimulation)
  {
    subcircuitView.deleteComponentView(staticView, circuitSimulation);
  }

  public void validateConsistency()
  {
    subcircuitView.validateConsistency();
  }

  public void createTraceViews(List<Line> lines,
                               CircuitSimulation circuitSimulation)
  {
    subcircuitView.createTraceViews(lines, circuitSimulation);
  }

  public Map<ViewData, View> loadViews(List<TraceData> traces,
                                       List<StaticData<?>> components,
                                       boolean appendIds,
                                       boolean newComponentPropertyStep)
  {
    Map<ViewData, View> views = new LinkedHashMap<>();

    for (TraceData traceData : traces)
    {
      TraceView traceView = traceData.create(this);
      views.put(traceData, traceView);
      traceView.updateId(appendIds, traceData.id);
    }

    for (StaticData<?> staticData : components)
    {
      StaticView<?> staticView = staticData.createView(this, newComponentPropertyStep);
      views.put(staticData, staticView);
    }

    return views;
  }

  public void loadComponents(Map<ViewData, View> dataViewMap,
                             CircuitSimulation circuitSimulation,
                             TraceLoader traceLoader)
  {
    for (Map.Entry<ViewData, View> entry : dataViewMap.entrySet())
    {
      ViewData data = entry.getKey();
      View view = entry.getValue();
      if (view instanceof StaticView)
      {
        StaticView staticView = (StaticView) view;
        StaticData staticData = (StaticData) data;
        staticData.createAndConnectComponent(this, circuitSimulation, traceLoader, staticView);
     }
      else if (view instanceof TraceView)
      {
        TraceView traceView = (TraceView) view;
        TraceData traceData = (TraceData) data;
        traceData.createAndConnectComponent(this, circuitSimulation, traceLoader, traceView);
      }
      else
      {
        throw new SimulatorException("Cannot load component of type [%s].", view);
      }
    }
  }

  public String getTypeName()
  {
    return subcircuitView.getTypeName();
  }

  public List<String> getTypeNameAsList()
  {
    ArrayList<String> list = new ArrayList<>();
    list.add(getTypeName());
    return list;
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
    Set<String> names = new HashSet<>();
    Set<SubcircuitInstanceView> subcircuitInstanceViews = subcircuitView.getSubcircuitInstanceViews();
    for (SubcircuitInstanceView subcircuitInstanceView : subcircuitInstanceViews)
    {
      names.add(subcircuitInstanceView.getTypeName());
    }
    return new ArrayList<>(names);
  }

  public long getId()
  {
    return id;
  }

  public static void resetNextId()
  {
    nextId = 1;
  }

  public void ensureComponentsForSimulation(TopLevelSubcircuitSimulation topLevelSubcircuitSimulation)
  {
    subcircuitView.ensureComponentsForSimulation(topLevelSubcircuitSimulation);
  }
}

