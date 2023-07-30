package net.logicim.ui.placement;

import net.logicim.common.SimulatorException;
import net.logicim.common.geometry.Line;
import net.logicim.common.type.Int2D;
import net.logicim.ui.Edit;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.Viewport;
import net.logicim.ui.common.integratedcircuit.StaticView;
import net.logicim.ui.common.integratedcircuit.View;
import net.logicim.ui.common.wire.TraceView;
import net.logicim.ui.simulation.CircuitEditor;

import java.awt.*;
import java.util.List;
import java.util.*;

import static net.logicim.ui.common.Rotation.North;

public class MoveComponents
    extends StatefulEdit
{
  protected Map<StaticView<?>, Int2D> componentStartPositions;
  protected Map<StaticView<?>, Rotation> componentStartRotations;
  protected Map<TraceView, Line> traceViews;
  protected Set<StaticView<?>> selectedComponents;
  protected boolean newComponents;

  public MoveComponents(StaticView<?> componentView, boolean newComponents)
  {
    ArrayList<View> views = new ArrayList<>();
    views.add(componentView);
    constructor(views, newComponents);
  }

  public MoveComponents(List<View> views, boolean newComponents)
  {
    constructor(views, newComponents);
    for (View view : views)
    {
      if (view instanceof StaticView)
      {
        StaticView<?> staticView = (StaticView<?>) view;
        selectedComponents.add(staticView);
      }
    }
  }

  protected void constructor(List<View> views, boolean deleteOnDiscard)
  {
    this.newComponents = deleteOnDiscard;

    selectedComponents = new HashSet<>();
    componentStartPositions = new LinkedHashMap<>();
    componentStartRotations = new LinkedHashMap<>();
    traceViews = new LinkedHashMap<>();
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
        traceViews.put(traceView, traceView.getLine().clone());
      }
    }
  }

  @Override
  public void start(float x, float y, Edit edit)
  {
    edit.getEditor().startMoveComponents(getStaticViews(), getTraceViews());
  }

  @Override
  public StatefulEdit move(float x, float y, Edit edit)
  {
    moveComponents(edit.getRightRotations(), edit.getStart(), edit.getDiff());
    return this;
  }

  @Override
  public void done(float x, float y, Edit edit)
  {
    edit.getEditor().doneMoveComponents(getStaticViews(),
                                        getTraceViews(),
                                        getSelectedViews(),
                                        newComponents);
    if (edit.hasDiff() || newComponents)
    {
      edit.circuitUpdated();
    }
  }

  @Override
  public void discard(Edit edit)
  {
    CircuitEditor circuitEditor = edit.getEditor();
    if (!newComponents)
    {
      moveComponents(0, edit.getStart(), new Int2D());
      circuitEditor.doneMoveComponents(getStaticViews(),
                                       getTraceViews(),
                                       getSelectedViews(),
                                       !newComponents);
    }
    else
    {
      circuitEditor.deleteTraceViews(traceViews.keySet());
      circuitEditor.deleteComponentViews(getStaticViews());
    }
  }

  @Override
  public void paint(Graphics2D graphics, Viewport viewport)
  {
  }

  @Override
  public boolean canTransformComponents()
  {
    return true;
  }

  public List<StaticView<?>> getStaticViews()
  {
    return new ArrayList<>(componentStartPositions.keySet());
  }

  public Set<StaticView<?>> getSelectedViews()
  {
    return selectedComponents;
  }

  public List<TraceView> getTraceViews()
  {
    return new ArrayList<>(traceViews.keySet());
  }

  @Override
  public StatefulEdit rotate(boolean right, Edit edit)
  {
    moveComponents(edit.getRightRotations(), edit.getStart(), edit.getDiff());
    return this;
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

    for (Map.Entry<TraceView, Line> traceViewLineEntry : traceViews.entrySet())
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

