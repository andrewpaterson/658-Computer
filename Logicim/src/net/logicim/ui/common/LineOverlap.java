package net.logicim.ui.common;

public enum LineOverlap
{
  Fully,
  Start,  //partially overlapped from the start position but not all the way to the end position
  End,
  Center, //overlapped somewhere in the center including neither the start nor the end position.
  Orthogonal,
  None
}

