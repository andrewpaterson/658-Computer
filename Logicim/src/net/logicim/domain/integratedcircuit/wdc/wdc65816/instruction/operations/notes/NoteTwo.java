package net.logicim.domain.integratedcircuit.wdc.wdc65816.instruction.operations.notes;

import net.logicim.domain.integratedcircuit.wdc.wdc65816.W65C816;
import net.logicim.domain.integratedcircuit.wdc.wdc65816.instruction.operations.Operation;

import static net.common.util.IntUtil.toByte;

public class NoteTwo
    extends Operation
{
  @Override
  public boolean mustExecute(W65C816 cpu)
  {
    return toByte(cpu.getDirectPage()) == 0;
  }

  @Override
  public void execute(W65C816 cpu)
  {
  }

  @Override
  public String toString()
  {
    return "Note(2)";
  }
}
