package name.bizna.emu65816.opcode;

import name.bizna.emu65816.AddressingMode;
import name.bizna.emu65816.Cpu65816;

public class OpCode_PHB
    extends OpCode
{
  public OpCode_PHB(String mName, int mCode, InstructionCycles cycles)
  {
    super(mName, mCode, cycles);
  }

  @Override
  public void executeOnFallingEdge(Cpu65816 cpu)
  {
    cpu.push8Bit(cpu.getDataBank());
    cpu.addToProgramAddressAndCycles(1, 3);
  }

  @Override
  public void executeOnRisingEdge(Cpu65816 cpu)
  {
  }
}

