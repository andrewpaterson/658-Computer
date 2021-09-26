package name.bizna.emu65816.opcode;

import name.bizna.emu65816.AddressingMode;
import name.bizna.emu65816.Cpu65816;

public class OpCode_PLD
    extends OpCode
{
  public OpCode_PLD(String mName, int mCode, AddressingMode mAddressingMode)
  {
    super(mName, mCode, mAddressingMode);
  }

  @Override
  public void executeOnFallingEdge(Cpu65816 cpu)
  {
    cpu.setD(cpu.pull16Bit());
    cpu.getCpuStatus().updateSignAndZeroFlagFrom16BitValue(cpu.getD());
    cpu.addToProgramAddressAndCycles(1, 5);
  }

  @Override
  public void executeOnRisingEdge(Cpu65816 cpu)
  {
  }
}

