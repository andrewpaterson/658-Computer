package name.bizna.cpu.opcode;

import name.bizna.cpu.addressingmode.InstructionCycles;

public class OpCode_INY
    extends OpCode
{
  public OpCode_INY(int mCode, InstructionCycles cycles)
  {
    super("INY", "Increment Index Y by One", mCode, cycles);
  }
}

