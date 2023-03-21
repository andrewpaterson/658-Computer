package net.logicim.ui.common.defaults;

import net.logicim.data.family.Family;
import net.logicim.domain.common.propagation.FamilyStore;

public class DefaultFamily
{
  public static Family get()
  {
    return FamilyStore.getInstance().get("LVC");
  }
}

