package name.bizna.emu65816.opcode;

import name.bizna.emu65816.addressingmode.InstructionCycles;

public class OpCode_NOP
    extends OpCode
{
  public OpCode_NOP(int mCode, InstructionCycles cycles)
  {
    super("NOP", "No Operation for two cycles.", mCode, cycles);
  }
}

