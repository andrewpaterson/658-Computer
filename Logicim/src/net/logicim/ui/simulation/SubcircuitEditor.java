package net.logicim.ui.simulation;

import net.logicim.common.geometry.Line;
import net.logicim.common.type.Float2D;
import net.logicim.common.type.Int2D;
import net.logicim.data.circuit.SubcircuitData;
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
import net.logicim.ui.simulation.selection.Selection;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SubcircuitEditor
{
  protected Selection selection;

  protected SubcircuitView subcircuitView;

  public SubcircuitEditor()
  {
    this(new SubcircuitView());
  }

  public SubcircuitEditor(SubcircuitView subcircuitView)
  {
    this.subcircuitView = subcircuitView;
    this.selection = new Selection();
  }

  public void startMoveComponents(List<StaticView<?>> staticViews,
                                  List<TraceView> traceViews,
                                  CircuitSimulation simulation)
  {
    clearSelection();

    subcircuitView.startMoveComponents(staticViews, traceViews, simulation);
  }

  public void doneMoveComponents(List<StaticView<?>> staticViews,
                                 List<TraceView> traceViews,
                                 Set<StaticView<?>> selectedViews,
                                 CircuitSimulation simulation)
  {
    List<View> newSelection = subcircuitView.doneMoveComponents(staticViews,
                                                                traceViews,
                                                                selectedViews,
                                                                simulation);
    this.selection.setSelection(newSelection);
  }

  public void deleteSelection(CircuitSimulation simulation)
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
    subcircuitView.deleteTraceViews(traceViews, simulation);

    List<StaticView<?>> staticViews = new ArrayList<>();
    for (View view : selectedViews)
    {
      if (view instanceof StaticView)
      {
        staticViews.add((StaticView<?>) view);
      }
    }
    subcircuitView.deleteComponentViews(staticViews, simulation);
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
    return subcircuitView.save(selection);
  }

  public List<View> getAllViews()
  {
    return subcircuitView.getAllViews();
  }

  public void deleteTraceViews(Set<TraceView> traceViews, CircuitSimulation simulation)
  {
    subcircuitView.deleteTraceViews(traceViews, simulation);
  }

  public StaticViewIterator staticViewIterator()
  {
    return subcircuitView.staticViewIterator();
  }

  public List<StaticView<?>> getComponentViewsInScreenSpace(Viewport viewport, Int2D screenPosition)
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

  protected boolean isInScreenSpaceBoundingBox(Viewport viewport, Int2D screenPosition, StaticView<?> view)
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

  public void deleteComponentViews(List<StaticView<?>> staticViews, CircuitSimulation simulation)
  {
    subcircuitView.deleteComponentViews(staticViews, simulation);
  }

  public List<View> pasteClipboardViews(List<TraceData> traces, List<StaticData<?>> components, CircuitSimulation simulation)
  {
    return loadViews(traces, components, simulation, false);
  }

  public void placeComponentView(StaticView<?> staticView, CircuitSimulation simulation)
  {
    Set<ConnectionView> updatedConnectionViews = subcircuitView.connectStaticView(staticView, simulation);

    subcircuitView.fireConnectionEvents(updatedConnectionViews, simulation);
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

  public void deleteComponentView(StaticView<?> staticView, CircuitSimulation simulation)
  {
    subcircuitView.deleteComponentView(staticView, simulation);
  }

  public void validateConsistency()
  {
    subcircuitView.validateConsistency();
  }

  public List<View> duplicateViews(List<View> views, CircuitSimulation simulation)
  {
    ArrayList<View> duplicates = new ArrayList<>();
    for (View view : views)
    {
      View duplicate = view.duplicate(subcircuitView, simulation.getCircuit());
      duplicates.add(duplicate);
    }
    return duplicates;
  }

  public void createTraceViews(List<Line> lines, CircuitSimulation simulation)
  {
    subcircuitView.createTraceViews(lines, simulation);
  }

  public void loadViews(SubcircuitData subcircuitData, CircuitSimulation simulation)
  {
    loadViews(subcircuitData.traces, subcircuitData.components, simulation, true);
  }

  public List<View> loadViews(List<TraceData> traces,
                              List<StaticData<?>> components,
                              CircuitSimulation simulation,
                              boolean createConnections)
  {
    ArrayList<View> views = new ArrayList<>();
    TraceLoader traceLoader = null;
    if (createConnections)
    {
      traceLoader = new TraceLoader();
    }

    for (TraceData traceData : traces)
    {
      TraceView traceView = traceData.create(this, simulation.getSimulation(), traceLoader, createConnections);
      views.add(traceView);
    }

    for (StaticData<?> staticData : components)
    {
      StaticView<?> staticView = staticData.createAndLoad(this, traceLoader, createConnections, simulation.getSimulation(), simulation.getCircuit());
      views.add(staticView);
    }
    return views;
  }

  public String getTypeName()
  {
    return subcircuitView.getTypeName();
  }

  public void setTypeName(String subcircuitName)
  {
    subcircuitView.setTypeName(subcircuitName);
  }
}
