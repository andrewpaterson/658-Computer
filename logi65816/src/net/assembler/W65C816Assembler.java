package net.assembler;

import static net.simulation.common.TraceValue.*;
import net.simulation.common.TraceValue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class W65C816Assembler<X, Y extends List<Map<X, ? extends Integer>>>
{
  private int y;
  private X x;

  public W65C816Assembler()
  {
    TraceValue value = NotConnected;
    List<Map<? extends X, ? extends Integer>> map = new ArrayList<>();
    int x = 5;
  }
}

