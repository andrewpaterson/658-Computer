package name.bizna.cpu.opcode;

import name.bizna.cpu.addressingmode.InstructionCycles;

public class OpCode_JSL
    extends OpCode
{
  public OpCode_JSL(int mCode, InstructionCycles cycles)
  {
    super("JSL", "Jump long to new location save return address on Stack.", mCode, cycles);
  }
}

