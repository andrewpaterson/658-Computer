package net.wdc.wdc65816.instruction.operations.notes;

import net.wdc.wdc65816.WDC65816;
import net.wdc.wdc65816.instruction.operations.Operation;

import static net.util.IntUtil.getLowByte;

public class NoteFourX
    extends Operation
{
  private final boolean nextWillRead;

  public NoteFourX(boolean nextWillRead)
  {
    this.nextWillRead = nextWillRead;
  }

  @Override
  public void execute(WDC65816 cpu)
  {
  }

  @Override
  public boolean mustExecute(WDC65816 cpu)
  {
    return (getLowByte(cpu.getAddress().getOffset()) + getLowByte(cpu.getX())) > 0xFF ||
           !nextWillRead ||
           cpu.isIndex16Bit();
  }

  @Override
  public String toString()
  {
    return "Note(4)";
  }
}
