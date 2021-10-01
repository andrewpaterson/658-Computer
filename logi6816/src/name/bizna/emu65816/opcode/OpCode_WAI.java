package name.bizna.emu65816.opcode;

import name.bizna.emu65816.AddressingMode;
import name.bizna.emu65816.Cpu65816;

public class OpCode_WAI
    extends OpCode
{
  public OpCode_WAI(String mName, int mCode, InstructionCycles cycles)
  {
    super(mName, mCode, cycles);
  }

  @Override
  public void executeOnFallingEdge(Cpu65816 cpu)
  {
    //cpu.setRDYPin(false);

    cpu.addToProgramAddress(1);
    cpu.addToCycles(3);
  }

  @Override
  public void executeOnRisingEdge(Cpu65816 cpu)
  {
  }
}

