package name.bizna.emu65816.opcode;

import name.bizna.emu65816.Address;
import name.bizna.emu65816.AddressingMode;
import name.bizna.emu65816.Cpu65816;

public class OpCode_PLD
    extends OpCode
{
  public OpCode_PLD(String mName, byte mCode, AddressingMode mAddressingMode)
  {
    super(mName, mCode, mAddressingMode);
  }

  @Override
  public void execute(Cpu65816 cpu)
  {
    Address opCodeDataAddress = cpu.getAddressOfOpCodeData(getAddressingMode());
    cpu.setD(cpu.getStack().pull16Bit(cpu));
    cpu.getCpuStatus().updateSignAndZeroFlagFrom16BitValue(cpu.getD());
    cpu.addToProgramAddressAndCycles(1, 5);
  }
}

