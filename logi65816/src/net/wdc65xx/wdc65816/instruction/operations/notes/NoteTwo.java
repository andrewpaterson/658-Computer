package net.wdc65xx.wdc65816.instruction.operations.notes;

import net.wdc65xx.wdc65816.Cpu65816;
import net.wdc65xx.wdc65816.instruction.operations.Operation;

import static net.util.IntUtil.toByte;

public class NoteTwo
    extends Operation
{
  @Override
  public boolean mustExecute(Cpu65816 cpu)
  {
    return toByte(cpu.getDirectPage()) == 0;
  }

  @Override
  public void execute(Cpu65816 cpu)
  {
  }

  @Override
  public String toString()
  {
    return "Note(2)";
  }
}
