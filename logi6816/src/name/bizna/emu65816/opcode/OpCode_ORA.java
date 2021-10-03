package name.bizna.emu65816.opcode;

import name.bizna.emu65816.AddressingMode;
import name.bizna.emu65816.Cpu65816;
import name.bizna.emu65816.addressingmode.InstructionCycles;

public class OpCode_ORA
    extends OpCode
{
  public OpCode_ORA(int mCode, InstructionCycles cycles)
  {
    super("ORA", "'OR' memory with A; result in A and update NZ.", mCode, cycles);
  }
}

