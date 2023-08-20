package net.logicim.ui.simulation.component.integratedcircuit.standard.logic.common;

import net.common.type.Int2D;
import net.logicim.data.integratedcircuit.common.StandardIntegratedCircuitProperties;
import net.logicim.domain.common.IntegratedCircuit;
import net.logicim.ui.circuit.SubcircuitView;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.simulation.component.integratedcircuit.standard.common.StandardIntegratedCircuitView;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseGateView<IC extends IntegratedCircuit<?, ?>, PROPERTIES extends StandardIntegratedCircuitProperties>
    extends StandardIntegratedCircuitView<IC, PROPERTIES>
{
  public BaseGateView(SubcircuitView containingSubcircuitView,
                      Int2D position,
                      Rotation rotation,
                      PROPERTIES properties)
  {
    super(containingSubcircuitView, position, rotation, properties);
  }

  protected static List<Integer> calculatePortOffsets(int inputCount)
  {
    int start;
    int end;
    boolean skipZero = false;
    end = inputCount / 2;
    if (inputCount % 2 == 0)
    {
      skipZero = true;
    }
    start = -end;

    ArrayList<Integer> integers = new ArrayList<>();
    for (int i = start; i <= end; i++)
    {
      if (!((i == 0) & skipZero))
      {
        integers.add(i);
      }
    }
    return integers;
  }

  protected float calculateWidth(int inputCount)
  {
    if (inputCount <= 3)
    {
      return 1.5f;
    }
    else
    {
      int i = (inputCount / 2) - 1;
      return 1.5f + i;
    }
  }

}

