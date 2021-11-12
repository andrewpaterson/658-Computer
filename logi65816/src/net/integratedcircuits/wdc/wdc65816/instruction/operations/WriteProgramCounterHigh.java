package net.integratedcircuits.wdc.wdc65816.instruction.operations;

import net.integratedcircuits.wdc.wdc65816.W65C816;
import net.util.IntUtil;

public class WriteProgramCounterHigh
    extends DataOperation
{
  public WriteProgramCounterHigh()
  {
    super(false, true, true, false, true);
  }

  @Override
  public void execute(W65C816 cpu)
  {
    cpu.writePinData(IntUtil.getHighByte(cpu.getProgramCounter().getOffset()));
  }

  @Override
  public String toString()
  {
    return "Write(PCH)";
  }
}

