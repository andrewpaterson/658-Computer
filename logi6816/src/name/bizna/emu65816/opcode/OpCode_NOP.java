package name.bizna.emu65816.opcode;

import name.bizna.emu65816.Cpu65816;
import name.bizna.emu65816.addressingmode.InstructionCycles;

public class OpCode_NOP
    extends OpCode
{
  public OpCode_NOP(int mCode, InstructionCycles cycles)
  {
    super("NOP", "No Operation for two cycles.", mCode, cycles);
  }

  @Override
  public void executeOnFallingEdge(Cpu65816 cpu)
  {
    cpu.addToProgramAddress(1);
    cpu.addToCycles(2);
  }
}

