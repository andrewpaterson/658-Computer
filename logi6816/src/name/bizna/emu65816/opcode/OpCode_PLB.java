package name.bizna.emu65816.opcode;

import name.bizna.emu65816.AddressingMode;
import name.bizna.emu65816.Cpu65816;

public class OpCode_PLB
    extends OpCode
{
  public OpCode_PLB(String mName, byte mCode, AddressingMode mAddressingMode)
  {
    super(mName, mCode, mAddressingMode);
  }

  @Override
  public void execute(Cpu65816 cpu)
  {
    cpu.setDB(cpu.getStack().pull8Bit(cpu));
    cpu.getCpuStatus().updateSignAndZeroFlagFrom8BitValue(cpu.getDB());
    cpu.addToProgramAddressAndCycles(1, 4);
  }
}

