package name.bizna.emu65816.addressingmode;

import name.bizna.emu65816.Cpu65816;
import name.bizna.emu65816.opcode.OpCode;

public class Execute1
    extends Operation
{
  public Execute1()
  {
  }

  @Override
  public void execute(Cpu65816 cpu, OpCode opCode)
  {
    opCode.execute1(cpu);
  }
}

