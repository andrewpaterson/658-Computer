package name.bizna.emu65816.addressingmode;

import name.bizna.emu65816.Cpu65816;
import name.bizna.emu65816.opcode.OpCode;

public class Execute2
    extends Operation
{
  public Execute2()
  {
  }

  @Override
  public void execute(Cpu65816 cpu, OpCode opCode)
  {
    opCode.execute2(cpu);
  }
}

