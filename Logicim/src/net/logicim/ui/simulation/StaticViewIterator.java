package net.logicim.ui.simulation;

import net.logicim.ui.common.integratedcircuit.IntegratedCircuitView;
import net.logicim.ui.common.integratedcircuit.PassiveView;
import net.logicim.ui.common.integratedcircuit.StaticView;
import net.logicim.ui.simulation.component.decorative.common.DecorativeView;

import java.util.*;

public class StaticViewIterator
    implements Iterator<StaticView<?>>
{
  public List<Iterator<? extends StaticView<?>>> iterators;
  public int iteratorIndex;

  public StaticViewIterator(Set<IntegratedCircuitView<?, ?>> integratedCircuitViews, Set<PassiveView<?, ?>> passiveViews, Set<DecorativeView<?>> decorativeViews)
  {
    iterators = new ArrayList<>();
    iterators.add(integratedCircuitViews.iterator());
    iterators.add(passiveViews.iterator());
    iterators.add(decorativeViews.iterator());

    iteratorIndex = 0;
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

