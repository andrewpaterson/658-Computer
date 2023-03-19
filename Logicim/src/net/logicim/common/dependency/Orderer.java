package net.logicim.common.dependency;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Orderer
{
  public static <T> List<T> order(Map<T, List<String>> fulfillmentsMap, Map<T, List<String>> requirementsMap)
  {
    List<T> toProcess = new ArrayList<>(fulfillmentsMap.keySet());
    List<T> satisfied = new ArrayList<>();
    List<T> toRemove = new ArrayList<>();

    boolean complete = toProcess.isEmpty();

    while (!complete)
    {
      for (T relationHolder : toProcess)
      {
        List<String> requirements = requirementsMap.get(relationHolder);

        if (requirementsAreFulfilled(requirements, satisfied, fulfillmentsMap))
        {
          satisfied.add(relationHolder);
          toRemove.add(relationHolder);
        }
      }

      if (toRemove.isEmpty())
      {
        return null;
      }

      toProcess.removeAll(toRemove);
      toRemove.clear();

      complete = toProcess.isEmpty();
    }

    return satisfied;
  }

  private static <T> boolean requirementsAreFulfilled(List<String> requirements, List<T> satisfied, Map<T, List<String>> holderFulfillments)
  {
    List<String> fulfilled = new ArrayList<>();

    for (T relationHolder : satisfied)
    {
      List<String> fulfillments = holderFulfillments.get(relationHolder);
      fulfilled.addAll(fulfillments);
    }

    return fulfilled.containsAll(requirements);
  }
}

