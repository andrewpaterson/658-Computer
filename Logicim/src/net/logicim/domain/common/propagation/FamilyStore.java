package net.logicim.domain.common.propagation;

import net.common.SimulatorException;
import net.logicim.data.family.Family;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class FamilyStore
{
  protected static FamilyStore instance = null;

  protected LinkedHashMap<String, Family> map;

  public FamilyStore()
  {
    map = new LinkedHashMap<>();
  }

  public static FamilyStore getInstance()
  {
    if (instance == null)
    {
      instance = new FamilyStore();
    }
    return instance;
  }

  public Family add(String familyName)
  {
    Family family = map.get(familyName);
    if (family != null)
    {
      throw new SimulatorException("Family [%s] has already been added.", familyName);
    }
    family = new Family(familyName);
    map.put(familyName, family);
    return family;
  }

  public Family get(String familyName)
  {
    return map.get(familyName);
  }

  public List<String> findAllNames()
  {
    return new ArrayList<>(map.keySet());
  }

  public Family get(Family family)
  {
    return get(family.getFamily());
  }
}

