package net.logicim.ui.simulation;

import net.logicim.ui.common.integratedcircuit.IntegratedCircuitView;
import net.logicim.ui.common.integratedcircuit.PassiveView;
import net.logicim.ui.common.integratedcircuit.StaticView;
import net.logicim.ui.common.wire.TunnelView;
import net.logicim.ui.simulation.component.decorative.common.DecorativeView;
import net.logicim.ui.simulation.component.subcircuit.SubcircuitInstanceView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class StaticViewIterator
    implements Iterator<StaticView<?>>
{
  public List<Iterator<? extends StaticView<?>>> iterators;
  public int iteratorIndex;

  public StaticViewIterator(Set<TunnelView> tunnelViews,
                            Set<IntegratedCircuitView<?, ?>> integratedCircuitViews,
                            Set<PassiveView<?, ?>> passiveViews,
                            Set<SubcircuitInstanceView> subcircuitInstanceViews,
                            Set<DecorativeView<?>> decorativeViews)
  {
    iterators = new ArrayList<>();
    addIterator(tunnelViews);
    addIterator(integratedCircuitViews);
    addIterator(passiveViews);
    addIterator(subcircuitInstanceViews);
    addIterator(decorativeViews);

    iteratorIndex = 0;
  }

  protected void addIterator(Set<? extends StaticView<?>> staticViews)
  {
    Iterator<? extends StaticView<?>> iterator = staticViews.iterator();
    if (iterator.hasNext())
    {
      iterators.add(iterator);
    }
  }

  @Override
  public boolean hasNext()
  {
    if (iteratorIndex < iterators.size())
    {
      Iterator<? extends StaticView<?>> iterator = iterators.get(iteratorIndex);
      return iterator.hasNext();
    }
    return false;
  }

  @Override
  public StaticView<?> next()
  {
    if (iteratorIndex < iterators.size())
    {
      Iterator<? extends StaticView<?>> iterator = iterators.get(iteratorIndex);
      StaticView<?> next = iterator.next();
      if (!iterator.hasNext())
      {
        iteratorIndex++;
      }
      return next;
    }
    return null;
  }
}

