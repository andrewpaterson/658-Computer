package name.bizna.cpu.opcode;

import name.bizna.cpu.addressingmode.InstructionCycles;

public class OpCode_INX
    extends OpCode
{
  public OpCode_INX(int mCode, InstructionCycles cycles)
  {
    super("INX", "Increment Index X by One", mCode, cycles);
  }
}

