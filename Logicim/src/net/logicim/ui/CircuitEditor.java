package net.logicim.ui;

import net.logicim.common.type.Float2D;
import net.logicim.common.type.Int2D;
import net.logicim.domain.Simulation;
import net.logicim.domain.common.Circuit;
import net.logicim.ui.common.IntegratedCircuitView;
import net.logicim.ui.common.DiscreteView;
import net.logicim.ui.common.Viewport;
import net.logicim.ui.shape.BoundingBox;
import net.logicim.ui.trace.TraceView;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CircuitEditor
{
  protected List<DiscreteView> discreteViews;
  protected List<TraceView> traceViews;
  protected Circuit circuit;
  protected Simulation simulation;

  public CircuitEditor()
  {
    circuit = new Circuit();
    discreteViews = new ArrayList<>();
    traceViews = new ArrayList<>();
  }

  public void paint(Graphics2D graphics, Viewport viewport)
  {
    for (TraceView traceView : traceViews)
    {
      traceView.paint(graphics, viewport);
    }

    for (DiscreteView discreteView : discreteViews)
    {
      discreteView.paint(graphics, viewport);
    }
  }

  public Circuit getCircuit()
  {
    return circuit;
  }

  public void add(DiscreteView view)
  {
    discreteViews.add(view);
  }

  public void add(TraceView view)
  {
    traceViews.add(view);
  }

  public void remove(IntegratedCircuitView<?> view)
  {
    circuit.remove(view.getIntegratedCircuit());
    discreteViews.remove(view);
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

  public void runToTime(long timeForward)
  {
    ensureSimulation();

    simulation.runToTime(timeForward);
  }


  public void ensureSimulation()
  {
    if (simulation == null)
    {
      simulation = circuit.resetSimulation();
    }
  }

  public DiscreteView getViewInScreenSpace(Viewport viewport, Int2D screenPosition)
  {
    List<DiscreteView> selectedViews = getDiscreteViewsInScreenSpace(viewport, screenPosition);

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
      DiscreteView closestView = null;
      for (DiscreteView view : selectedViews)
      {
        view.getBoundingBoxInScreenSpace(viewport, boundBoxPosition, boundBoxDimension);
        boundBoxPosition.add(boundBoxDimension.x / 2, boundBoxDimension.y / 2);
        float distance = BoundingBox.calculateDistance(screenPosition, boundBoxPosition);
        if (distance < shortestDistance)
        {
          closestView = view;
          shortestDistance = distance;
        }
      }
      return closestView;
    }
  }

  public List<DiscreteView> getDiscreteViewsInScreenSpace(Viewport viewport, Int2D screenPosition)
  {
    Int2D boundBoxPosition = new Int2D();
    Int2D boundBoxDimension = new Int2D();
    List<DiscreteView> selectedViews = new ArrayList<>();
    for (DiscreteView view : discreteViews)
    {
      if (view.isEnabled())
      {
        view.getBoundingBoxInScreenSpace(viewport, boundBoxPosition, boundBoxDimension);
        if (BoundingBox.isContained(screenPosition, boundBoxPosition, boundBoxDimension))
        {
          selectedViews.add(view);
        }
      }
    }
    return selectedViews;
  }

  public List<DiscreteView> getDiscreteViewsInGridSpace(Int2D gridPosition)
  {
    Float2D boundBoxPosition = new Float2D();
    Float2D boundBoxDimension = new Float2D();
    List<DiscreteView> selectedViews = new ArrayList<>();
    for (DiscreteView view : discreteViews)
    {
      if (view.isEnabled())
      {
        view.getBoundingBoxInGridSpace(boundBoxPosition, boundBoxDimension);
        if (BoundingBox.isContained(gridPosition, boundBoxPosition, boundBoxDimension))
        {
          selectedViews.add(view);
        }
      }
    }
    return selectedViews;
  }
}

