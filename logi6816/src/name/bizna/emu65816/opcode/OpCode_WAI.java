package name.bizna.emu65816.opcode;

import name.bizna.emu65816.Cpu65816;
import name.bizna.emu65816.addressingmode.InstructionCycles;

public class OpCode_WAI
    extends OpCode
{
  public OpCode_WAI(int mCode, InstructionCycles cycles)
  {
    super("WAI", "Wait for Interrupt", mCode, cycles);
  }

  @Override
  public void executeOnFallingEdge(Cpu65816 cpu)
  {
    //cpu.setRDYPin(false);

    cpu.addToProgramAddress(1);
    cpu.addToCycles(3);
  }
}

