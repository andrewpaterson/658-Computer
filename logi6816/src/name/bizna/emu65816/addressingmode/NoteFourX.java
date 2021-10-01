package name.bizna.emu65816.addressingmode;

import name.bizna.emu65816.Cpu65816;
import name.bizna.emu65816.opcode.OpCode;

import static name.bizna.emu65816.Binary.getLowByte;

public class NoteFourX
    extends Operation
{
  private final boolean nextWillRead;

  public NoteFourX(boolean nextWillRead)
  {
    this.nextWillRead = nextWillRead;
  }

  @Override
  public void execute(Cpu65816 cpu, OpCode opCode)
  {
  }

  @Override
  public boolean mustExecute(Cpu65816 cpu)
  {
    return (getLowByte(cpu.getAddress().getOffset()) + getLowByte(cpu.getX())) > 0xFF ||
           !nextWillRead ||
           cpu.isIndex16Bit();
  }
}

