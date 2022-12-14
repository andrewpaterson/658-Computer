package net.logicim.domain.common.propagation;

import net.logicim.common.SimulatorException;

import java.util.LinkedHashMap;

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
}

