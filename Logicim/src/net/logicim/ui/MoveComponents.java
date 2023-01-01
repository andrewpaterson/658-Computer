package net.logicim.ui;

import net.logicim.common.SimulatorException;
import net.logicim.common.type.Int2D;
import net.logicim.ui.common.Viewport;
import net.logicim.ui.common.integratedcircuit.ComponentView;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MoveComponents
{
  protected Int2D start;
  protected Int2D end;
  protected Int2D diff;

  protected Map<ComponentView, Int2D> componentStartPositions;

  public MoveComponents(Viewport viewport, int mouseX, int mouseY, List<ComponentView> selection)
  {
    start = new Int2D(viewport.transformScreenToGridX(mouseX),
                      viewport.transformScreenToGridY(mouseY));
    end = new Int2D(start);
    diff = new Int2D(end);
    diff.subtract(start);

    componentStartPositions = new LinkedHashMap<>(selection.size());
    for (ComponentView componentView : selection)
    {
      componentStartPositions.put(componentView, new Int2D(componentView.getGridPosition()));
      componentView.disable();
    }
  }

  public void calculateDiff(Viewport viewport, int mouseX, int mouseY)
  {
    end.set(viewport.transformScreenToGridX(mouseX),
            viewport.transformScreenToGridY(mouseY));
    diff.set(end);
    diff.subtract(start);
  }

  public Int2D getPosition(ComponentView componentView)
  {
    Int2D start = componentStartPositions.get(componentView);
    if (start != null)
    {
      Int2D position = new Int2D(start);
      position.add(diff);
      return position;
    }
    else
    {
      throw new SimulatorException("Cannot get position for %s [%s].", componentView.getClass().getSimpleName(), componentView.getDescription());
    }
  }

  public List<ComponentView> getComponents()
  {
    return new ArrayList<>(componentStartPositions.keySet());
  }
}

