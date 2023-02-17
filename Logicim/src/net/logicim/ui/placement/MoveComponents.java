package net.logicim.ui.placement;

import net.logicim.common.SimulatorException;
import net.logicim.common.geometry.Line;
import net.logicim.common.type.Int2D;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.Viewport;
import net.logicim.ui.common.integratedcircuit.StaticView;
import net.logicim.ui.common.integratedcircuit.View;
import net.logicim.ui.common.wire.TraceView;
import net.logicim.ui.simulation.CircuitEditor;

import java.awt.*;
import java.util.List;
import java.util.*;

import static net.logicim.ui.common.Rotation.Cannot;
import static net.logicim.ui.common.Rotation.North;

public class MoveComponents
    extends StatefulMove
{
  protected Map<StaticView<?>, Int2D> componentStartPositions;
  protected Map<StaticView<?>, Rotation> componentStartRotations;
  protected Map<TraceView, Line> traces;
  protected Set<StaticView<?>> selectedComponents;
  protected Rotation rotation;

  public MoveComponents(StaticView<?> componentView)
  {
    ArrayList<View> views = new ArrayList<>();
    views.add(componentView);
    constructor(views);
  }

  public MoveComponents(List<View> views)
  {
    constructor(views);
    for (View view : views)
    {
      if (view instanceof StaticView)
      {
        StaticView<?> staticView = (StaticView<?>) view;
        selectedComponents.add(staticView);
      }
    }
  }

  protected void constructor(List<View> views)
  {
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
      }
      else if (view instanceof TraceView)
      {
        TraceView traceView = (TraceView) view;
        traces.put(traceView, traceView.getLine().clone());
      }
    }
  }

  @Override
  public void start(float x, float y, CircuitEditor circuitEditor, StatefulEdit statefulEdit)
  {
    circuitEditor.startMoveComponents(getStaticViews(), getTraces());
  }

  @Override
  public StatefulMove move(float x, float y, CircuitEditor circuitEditor, StatefulEdit statefulEdit)
  {
    moveComponents(statefulEdit.getRightRotations(), statefulEdit.getStart(), statefulEdit.getDiff());
    return this;
  }

  @Override
  public void done(float x, float y, CircuitEditor circuitEditor, StatefulEdit statefulEdit)
  {
    circuitEditor.doneMoveComponents(getStaticViews(),
                                     getTraces(),
                                     getSelectedViews());
    if (statefulEdit.hasDiff())
    {
      statefulEdit.pushUndo();
    }

  }

  @Override
  public void paint(Graphics2D graphics, Viewport viewport)
  {
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

  public List<StaticView<?>> getStaticViews()
  {
    return new ArrayList<>(componentStartPositions.keySet());
  }

  public Set<StaticView<?>> getSelectedViews()
  {
    return selectedComponents;
  }

  public List<TraceView> getTraces()
  {
    return new ArrayList<>(traces.keySet());
  }

  public void rotate(boolean right)
  {
    if (right)
    {
      rotation = rotation.rotateRight();
    }
    else
    {
      rotation = rotation.rotateLeft();
    }
  }

  public void moveComponents(int rightRotations, Int2D start, Int2D diff)
  {
    for (Map.Entry<StaticView<?>, Int2D> staticViewInt2DEntry : componentStartPositions.entrySet())
    {
      StaticView<?> staticView = staticViewInt2DEntry.getKey();
      Int2D startPosition = staticViewInt2DEntry.getValue();
      Rotation startRotation = componentStartRotations.get(staticView);

      if ((startPosition != null) && (startRotation != null))
      {
        Rotation rotation = North.rotateRight(rightRotations);

        Int2D position = new Int2D(startPosition);
        position.subtract(start);
        rotation.transform(position);
        position.add(start);
        position.add(diff);
        staticView.setPosition(position.x, position.y);
        staticView.setRotation(startRotation.rotateRight(rightRotations));
      }
      else
      {
        throw new SimulatorException("Cannot get position for %s.", staticView.toIdentifierString());
      }
    }

    for (Map.Entry<TraceView, Line> traceViewLineEntry : traces.entrySet())
    {
      TraceView traceView = traceViewLineEntry.getKey();
      Line line = traceViewLineEntry.getValue();
      if (line != null)
      {
        Rotation rotation = North.rotateRight(rightRotations);

        Int2D startPosition = line.getStart();
        Int2D position = new Int2D(startPosition);
        position.subtract(start);
        rotation.transform(position);
        position.add(start);
        position.add(diff);
        startPosition = position;

        Int2D endPosition = line.getEnd();
        position = new Int2D(endPosition);
        position.subtract(start);
        rotation.transform(position);
        position.add(start);
        position.add(diff);
        endPosition = position;

        traceView.setLine(startPosition, endPosition);
      }
      else
      {
        throw new SimulatorException("Cannot get position for %s.", traceView.toIdentifierString());
      }
    }
  }
}

