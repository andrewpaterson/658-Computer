package net.logicim.ui;

import net.logicim.common.type.Int2D;
import net.logicim.domain.Simulation;
import net.logicim.domain.common.Circuit;
import net.logicim.ui.common.IntegratedCircuitView;
import net.logicim.ui.common.View;
import net.logicim.ui.common.Viewport;
import net.logicim.ui.shape.BoundingBox;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CircuitEditor
{
  protected List<View> views;
  protected Circuit circuit;
  protected Simulation simulation;

  public CircuitEditor()
  {
    circuit = new Circuit();
    views = new ArrayList<>();
  }

  public void paint(Graphics2D graphics, Viewport viewport)
  {
    for (View view : views)
    {
      view.paint(graphics, viewport);
    }
  }

  public Circuit getCircuit()
  {
    return circuit;
  }

  public void add(View view)
  {
    views.add(view);
  }

  public void remove(IntegratedCircuitView<?> view)
  {
    circuit.remove(view.getIntegratedCircuit());
    views.remove(view);
  }

  public Simulation reset()
  {
    return circuit.resetSimulation();
  }

  public void runSimultaneous()
  {
    ensureSimulation();

    simulation.runSimultaneous();
  }

  public void ensureSimulation()
  {
    if (simulation == null)
    {
      simulation = circuit.resetSimulation();
    }
  }

  public View getView(Viewport viewport, Int2D position)
  {
    List<View> selectedViews = getViews(viewport, position);

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
      View closestView = null;
      for (View view : selectedViews)
      {
        view.getBoundingBoxInScreenSpace(viewport, boundBoxPosition, boundBoxDimension);
        boundBoxPosition.add(boundBoxDimension.x / 2, boundBoxDimension.y / 2);
        float distance = BoundingBox.calculateDistance(position, boundBoxPosition);
        if (distance < shortestDistance)
        {
          closestView = view;
          shortestDistance = distance;
        }
      }
      return closestView;
    }
  }

  private List<View> getViews(Viewport viewport, Int2D position)
  {
    Int2D boundBoxPosition = new Int2D();
    Int2D boundBoxDimension = new Int2D();
    List<View> selectedViews = new ArrayList<>();
    for (View view : views)
    {
      if (view.isEnabled())
      {
        view.getBoundingBoxInScreenSpace(viewport, boundBoxPosition, boundBoxDimension);
        if (BoundingBox.isContained(position, boundBoxPosition, boundBoxDimension))
        {
          selectedViews.add(view);
        }
      }
    }
    return selectedViews;
  }
}

