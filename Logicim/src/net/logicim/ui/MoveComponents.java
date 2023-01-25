package net.logicim.ui;

import net.logicim.common.SimulatorException;
import net.logicim.common.geometry.Line;
import net.logicim.common.type.Int2D;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.Viewport;
import net.logicim.ui.common.integratedcircuit.StaticView;
import net.logicim.ui.common.integratedcircuit.View;
import net.logicim.ui.common.wire.TraceView;

import java.util.*;

import static net.logicim.ui.common.Rotation.Cannot;

public class MoveComponents
{
  protected Int2D start;
  protected Int2D end;
  protected Int2D diff;
  protected boolean hadDiff;
  protected boolean previousHadDiff;

  protected Map<StaticView<?>, Int2D> componentStartPositions;
  protected Map<StaticView<?>, Rotation> componentStartRotations;
  protected Map<TraceView, Line> traces;
  protected Set<StaticView<?>> selectedComponents;
  protected Rotation rotation;
  protected int rightRotations;

  public MoveComponents(Viewport viewport, StaticView<?> componentView)
  {
    start = new Int2D(viewport.transformScreenToGridX(componentView.getPosition().x),
                      viewport.transformScreenToGridY(componentView.getPosition().y));
    ArrayList<View> views = new ArrayList<>();
    views.add(componentView);
    end = new Int2D(start);
    diff = new Int2D();
    hadDiff = false;
    previousHadDiff = false;
    rotation = calculateRotation(views);
    rightRotations = 0;

    selectedComponents = new HashSet<>();
    componentStartPositions = new LinkedHashMap<>();
    componentStartRotations = new LinkedHashMap<>();
    traces = new LinkedHashMap<>();
    for (View view : views)
    {
      if (view instanceof StaticView)
      {
        StaticView<?> staticView = (StaticView<?>) view;
        componentStartPositions.put(staticView, new Int2D(staticView.getPosition()));
        componentStartRotations.put(staticView, staticView.getRotation());
      }
      else if (view instanceof TraceView)
      {
        TraceView traceView = (TraceView) view;
        traces.put(traceView, traceView.getLine().clone());
      }
    }
  }

  public MoveComponents(Viewport viewport, int mouseX, int mouseY, List<View> views)
  {
    start = new Int2D(viewport.transformScreenToGridX(mouseX),
                      viewport.transformScreenToGridY(mouseY));
    end = new Int2D(start);
    diff = new Int2D();
    hadDiff = false;
    previousHadDiff = false;
    rotation = calculateRotation(views);

    selectedComponents = new HashSet<>();
    componentStartPositions = new LinkedHashMap<>();
    componentStartRotations = new LinkedHashMap<>();
    traces = new LinkedHashMap<>();
    for (View view : views)
    {
      if (view instanceof StaticView)
      {
        StaticView<?> staticView = (StaticView<?>) view;
        componentStartPositions.put(staticView, new Int2D(staticView.getPosition()));
        componentStartRotations.put(staticView, staticView.getRotation());
        selectedComponents.add(staticView);
      }
      else if (view instanceof TraceView)
      {
        TraceView traceView = (TraceView) view;
        traces.put(traceView, traceView.getLine().clone());
      }
    }
  }

  private Rotation calculateRotation(List<View> views)
  {
    Map<Rotation, Integer> rotations = new LinkedHashMap<>();
    for (View view : views)
    {
      if (view instanceof StaticView)
      {
        StaticView<?> staticView = (StaticView<?>) view;
        Rotation rotation = staticView.getRotation();
        Integer integer = rotations.get(rotation);
        if (integer == null)
        {
          integer = 0;
        }
        integer = integer + 1;
        rotations.put(rotation, integer);
      }
    }

    int max = 0;
    Rotation finalRotation = Cannot;
    if (rotations.get(Cannot) != null)
    {
      return Cannot;
    }
    else
    {
      for (Map.Entry<Rotation, Integer> rotationIntegerEntry : rotations.entrySet())
      {
        Rotation rotation = rotationIntegerEntry.getKey();
        Integer count = rotationIntegerEntry.getValue();
        if (count > max)
        {
          max = count;
          finalRotation = rotation;
        }
      }
    }
    return finalRotation;
  }

  public void calculateDiff(Viewport viewport, int mouseX, int mouseY)
  {
    end.set(viewport.transformScreenToGridX(mouseX),
            viewport.transformScreenToGridY(mouseY));
    diff.set(end);
    diff.subtract(start);

    previousHadDiff = hadDiff;
    if (!diff.isZero())
    {
      hadDiff = true;
    }
  }

  public List<View> getComponents()
  {
    return new ArrayList<>(componentStartPositions.keySet());
  }

  public Set<StaticView<?>> getSelectedComponents()
  {
    return selectedComponents;
  }

  public boolean hasDiff()
  {
    return !diff.isZero();
  }

  public boolean hadDiff()
  {
    return hadDiff;
  }

  public boolean hasMoved()
  {
    return !previousHadDiff && hadDiff;
  }

  public void rotate(boolean right)
  {
    if (right)
    {
      rotation = rotation.rotateRight();
      rightRotations++;
      if (rightRotations > 3)
      {
        rightRotations = 0;
      }
    }
    else
    {
      rotation = rotation.rotateLeft();
      rightRotations--;
      if (rightRotations < 0)
      {
        rightRotations = 3;
      }
    }
  }

  public void moveComponents()
  {
    List<View> components = getComponents();
    for (View view : components)
    {
      if (view instanceof StaticView)
      {
        StaticView<?> staticView = (StaticView<?>) view;
        Int2D startPosition = componentStartPositions.get(view);
        Rotation startRotation = componentStartRotations.get(view);

        if ((startPosition != null) && (startRotation != null))
        {
          Int2D position = new Int2D(startPosition);

          position.add(diff);
          staticView.setPosition(position.x, position.y);
          staticView.setRotation(startRotation);
        }
        else
        {
          throw new SimulatorException("Cannot get position for %s [%s].", view.getClass().getSimpleName(), view.getDescription());
        }

      }
      else
      {

      }
    }
  }
}

