package name.bizna.cpu.opcode;

import name.bizna.cpu.addressingmode.InstructionCycles;

public class OpCode_LDY
    extends OpCode
{
  public OpCode_LDY(int mCode, InstructionCycles cycles)
  {
    super("LDY", "Load Index Y with Memory", mCode, cycles);
  }
}

