package net.common.util;

public class IdentifierSource
{
  public long identifier;

  public IdentifierSource()
  {
  }

  public long tick()
  {
    return identifier++;
  }
}

