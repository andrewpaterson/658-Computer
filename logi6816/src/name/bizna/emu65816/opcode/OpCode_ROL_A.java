package name.bizna.emu65816.opcode;

import name.bizna.emu65816.addressingmode.InstructionCycles;

public class OpCode_ROL_A
    extends OpCode
{
  public OpCode_ROL_A(int mCode, InstructionCycles cycles)
  {
    super("ROL", "Rotate Accumulator One Bit Left.", mCode, cycles);
  }
}

