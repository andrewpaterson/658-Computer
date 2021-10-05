package name.bizna.emu65816.opcode;

import name.bizna.emu65816.addressingmode.InstructionCycles;

public class OpCode_ADC
    extends OpCode
{
  public OpCode_ADC(int mCode, InstructionCycles cycles)
  {
    super("ADC", "Add memory and carry to A; result in A and update NZC.", mCode, cycles);
  }
}

