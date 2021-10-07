package name.bizna.cpu.opcode;

import name.bizna.cpu.addressingmode.InstructionCycles;

public class OpCode_RLA
    extends OpCode
{
  public OpCode_RLA(int mCode, InstructionCycles cycles)
  {
    super("ROL", "Rotate Accumulator One Bit Left.", mCode, cycles);
  }
}

