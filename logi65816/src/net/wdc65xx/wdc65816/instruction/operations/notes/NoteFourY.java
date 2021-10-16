package net.wdc65xx.wdc65816.instruction.operations.notes;

import net.wdc65xx.wdc65816.WDC65C816;
import net.wdc65xx.wdc65816.instruction.operations.Operation;

import static net.util.IntUtil.getLowByte;

public class NoteFourY
    extends Operation
{
  private final boolean nextWillRead;

  public NoteFourY(boolean nextWillRead)
  {
    this.nextWillRead = nextWillRead;
  }

  @Override
  public void execute(WDC65C816 cpu)
  {
  }

  @Override
  public boolean mustExecute(WDC65C816 cpu)
  {
    return (getLowByte(cpu.getAddress().getOffset()) + getLowByte(cpu.getY())) > 0xFF ||
           !nextWillRead ||
           cpu.isIndex16Bit();
  }

  @Override
  public String toString()
  {
    return "Note(4)";
  }
}

