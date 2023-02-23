package net.logicim.ui.placement;

import net.logicim.common.type.Float2D;
import net.logicim.common.type.Int2D;
import net.logicim.ui.common.Viewport;
import net.logicim.ui.simulation.CircuitEditor;
import net.logicim.ui.undo.Undo;

import java.awt.*;

public class SimulatorEdit
{
  protected StatefulEdit edit;

  protected Int2D start;
  protected CircuitEditor circuitEditor;
  protected Undo undo;
  protected Int2D end;
  protected Int2D diff;
  protected boolean hadDiff;
  protected boolean previousHadDiff;
  protected int rightRotations;

  public SimulatorEdit(Float2D start,
                       StatefulEdit edit,
                       CircuitEditor circuitEditor,
                       Undo undo)
  {
    this.edit = edit;
    this.circuitEditor = circuitEditor;
    this.undo = undo;

    this.start = new Int2D(start);
    this.end = new Int2D(this.start);
    this.diff = new Int2D();
    this.hadDiff = false;
    this.previousHadDiff = false;

    edit.start(start.x, start.y, this);
  }

  public void start(float x, float y)
  {
    edit.start(x, y, this);
  }

  public void done(float x, float y)
  {
    edit.done(x, y, this);
  }

  public void discard()
  {
    edit.discard(this);
  }

  public void move(float x, float y)
  {
    calculateDiff(Math.round(x), Math.round(y));
    if (hadDiff)
    {
      StatefulEdit newEdit = edit.move(x, y, this);
      if (newEdit != edit)
      {
        newEdit.start(start.x, start.y, this);
        edit = newEdit;
      }
    }
  }

  protected void calculateDiff(int x, int y)
  {
    end.set(x, y);
    diff.set(end);
    diff.subtract(start);

    previousHadDiff = hadDiff;
    if (!diff.isZero() || rightRotations != 0)
    {
      hadDiff = true;
    }
  }

  public boolean hasDiff()
  {
    return !diff.isZero() || rightRotations != 0;
  }

  public void rotate(boolean right)
  {
    previousHadDiff = hadDiff;
    hadDiff = true;
    if (right)
    {
      rightRotations++;
      if (rightRotations > 3)
      {
        rightRotations = 0;
      }
    }
    else
    {
      rightRotations--;
      if (rightRotations < 0)
      {
        rightRotations = 3;
      }
    }

    edit = edit.rotate(right, this);
  }

  public int getRightRotations()
  {
    return rightRotations;
  }

  public Int2D getStart()
  {
    return start;
  }

  public Int2D getDiff()
  {
    return diff;
  }

  public CircuitEditor getCircuitEditor()
  {
    return circuitEditor;
  }

  public void pushUndo()
  {
    undo.pushUndo();
  }

  public void paint(Graphics2D graphics, Viewport viewport)
  {
    edit.paint(graphics, viewport);
  }
}

