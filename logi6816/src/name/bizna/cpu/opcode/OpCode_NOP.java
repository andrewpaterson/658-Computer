package name.bizna.cpu.opcode;

import name.bizna.cpu.addressingmode.InstructionCycles;

public class OpCode_NOP
    extends OpCode
{
  public OpCode_NOP(int mCode, InstructionCycles cycles)
  {
    super("NOP", "No Operation for two cycles.", mCode, cycles);
  }
}

