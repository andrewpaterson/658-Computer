package net.logicim.domain.integratedcircuit.wdc.wdc65816.instruction.address;

import net.common.SimulatorException;
import net.logicim.domain.integratedcircuit.wdc.wdc65816.W65C816;

public class ConstantOffset
    extends AddressOffset
{
  private final int offset;

  public ConstantOffset(int offset)
  {
    if (offset < 0 || offset > 3)
    {
      throw new SimulatorException("Numeric Offset must be in the range 0...3.");
    }
    this.offset = offset;
  }

  @Override
  public int getOffset(W65C816 cpu)
  {
    return offset;
  }

  @Override
  public String toString()
  {
    if (offset >= 0)
    {
      return "+" + offset;
    }
    else
    {
      return "" + offset;
    }
  }
}

